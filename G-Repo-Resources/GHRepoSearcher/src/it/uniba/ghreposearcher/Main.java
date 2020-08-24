package it.uniba.ghreposearcher;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.uniba.ghreposearcher.storage.DAOConfig;
import it.uniba.ghreposearcher.storage.StorageException;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		String configFilePath = args[0];
		Config config = null;
		try {
			config = DAOConfig.readConfig(new File(configFilePath));

			checkAuthentication(config.getUsername());
			
			logger.info("Original query: " + config.getRepoSearchURL());
			SearchRepo searchRepo = new SearchRepo(config);
			searchRepo.execute();

		} catch (StorageException | GHRepoSearcherException e) {
			logger.error(e.toString());
		}

	}

	private static void checkAuthentication(String username) throws GHRepoSearcherException {
		if (username == null) {
			throw new GHRepoSearcherException(
					"Username not specified. You must provide your identity by providing a value (i.e., a valid GitHub token) to the configuration property \"username\"");
		}
		String curlCommand = "curl -i -u username:" + username + " https://api.github.com";
		RequestSender sender = new RequestSender(curlCommand);
		sender.sendRequest();		
	}

}
