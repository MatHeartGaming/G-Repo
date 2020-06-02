package it.uniba.ghreposearcher;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import it.uniba.ghreposearcher.json.SearchResultBean;
import it.uniba.ghreposearcher.storage.DAOSearchResultBean;
import it.uniba.ghreposearcher.storage.StorageException;

public class SearchRepo {

	private Config config;

	public SearchRepo(Config config) {
		super();
		this.config = config;
	}
	
	public void execute() throws GHRepoSearcherException {
		try {
			String username = config.getUsername();
			String outputPath = config.getOutputPath();
			
			RepoSearchURL url = config.getRepoSearchURL();
			StringBuilder curlCommand = createCurlCommand(username, url);
			
			RequestSender sender = new RequestSender(curlCommand.toString());
			sender.sendRequest();
			Response response = sender.getResponse();
			sleepForAboutTwoSeconds();
						
			SearchResultBean searchResultBean = response.getBody();
			long totalCount = searchResultBean.getTotal_count();
			if(exceedSearchLimit(totalCount)) {
					
				long minNumOfRequests = totalCount / Constants.SEARCH_LIMIT; 
				if((totalCount % Constants.SEARCH_LIMIT) > 0) {
					minNumOfRequests++;
				}
				
				String createdQualifier = url.getCreatedQualifier();
				Date startDate = RepoSearchURL.getStartDateOfCreationQualifier(createdQualifier);
				Date endDate = RepoSearchURL.getEndDateOfCreationQualifier(createdQualifier);
				long totalInterval = endDate.getTime()-startDate.getTime();
				
				long increment = totalInterval/minNumOfRequests;			
				Date newStartDate = new Date(startDate.getTime()); 
				Date newEndDate = new Date(newStartDate.getTime() + increment);
				SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				for (int i = 0; i < minNumOfRequests; i++) {
					String newCreatedQualifier = "created:" + formatter.format(newStartDate) + ".." + formatter.format(newEndDate); 
					RepoSearchURL newUrl = new RepoSearchURL(url);
					newUrl.setCreatedQualifier(newCreatedQualifier);
					newUrl.addParameter("page", Integer.toString(1));
					SearchRepo searchRepo = new SearchRepo(new Config(username, outputPath, newUrl));
					searchRepo.execute();
					if(i<minNumOfRequests-1) {
						newStartDate = new Date(newEndDate.getTime() + 1000);
						newEndDate = new Date(newStartDate.getTime() + increment);
					}
				}
			} else {
				
				int currentPage = getCurrentPage(url);
				writeSearchResultBean(outputPath, url, searchResultBean, currentPage);
				
				int perPage = Integer.parseInt(url.getParameterValue("per_page"));				
				int nextPage = currentPage + 1;
				long maxNumOfPages = totalCount / perPage; 
				if((totalCount % perPage) > 0) {
					maxNumOfPages++;
				}
				
				if(nextPage <= maxNumOfPages) {
					RepoSearchURL newUrl = new RepoSearchURL(url);
					newUrl.addParameter("page", Integer.toString(nextPage));
					SearchRepo searchRepo = new SearchRepo(new Config(username, outputPath, newUrl));
					searchRepo.execute();
				}
			}
		} catch (URISyntaxException | StorageException | GHRepoSearcherException e) {
			throw new GHRepoSearcherException(e);
		}
	}

	private void sleepForAboutTwoSeconds() throws GHRepoSearcherException{
		try {
			TimeUnit.MILLISECONDS.sleep(Constants.SLEEP_TIME);
		} catch (InterruptedException e) {
			throw new GHRepoSearcherException(e);
		}
	}

	protected void writeSearchResultBean(String outputPath, RepoSearchURL url, SearchResultBean searchResultBean,
			int currentPage) throws StorageException {
		File outputFolder = new File(outputPath);				
		outputFolder.mkdir();
		String fileName = url.getCreatedQualifier().replace("created:", "").replace("..", "_").replace(":", "-") + "_" + currentPage;
		File outputFile = new File (outputFolder.getAbsolutePath() + File.separator + fileName + ".json");				
		DAOSearchResultBean.write(searchResultBean, outputFile);
	}

	protected int getCurrentPage(RepoSearchURL url) {
		int page = 1;
		String pageParameter = url.getParameterValue("page");
		if(pageParameter!=null) {
			page = Integer.parseInt(pageParameter);
		}
		return page;
	}

	protected boolean exceedSearchLimit(long totalCount) {
		return totalCount > Constants.SEARCH_LIMIT;
	}

	protected StringBuilder createCurlCommand(String username, RepoSearchURL repoSearchUrl) throws URISyntaxException {

		StringBuilder command = new StringBuilder("curl -i ");
		if (username != null) {
			command.append("-u username:");
			command.append(username);
			command.append(" ");
		}
		command.append(repoSearchUrl.toReadableString());
		return command;
	}

}
