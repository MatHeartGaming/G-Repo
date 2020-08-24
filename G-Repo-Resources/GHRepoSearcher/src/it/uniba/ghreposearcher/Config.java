package it.uniba.ghreposearcher;

public class Config {

	private String username;
	private String outputPath;
	private RepoSearchURL repoSearchURL;

	public Config(String username, String outputPath, RepoSearchURL repoSearchURL) {
		super();
		this.username = username;
		this.outputPath = outputPath;
		this.repoSearchURL = repoSearchURL;
	}

	public String getUsername() {
		return username;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public RepoSearchURL getRepoSearchURL() {
		return repoSearchURL;
	}

}
