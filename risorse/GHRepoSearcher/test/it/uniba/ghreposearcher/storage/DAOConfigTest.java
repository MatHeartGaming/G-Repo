package it.uniba.ghreposearcher.storage;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import it.uniba.ghreposearcher.Config;
import it.uniba.ghreposearcher.Constants;
import it.uniba.ghreposearcher.RepoSearchURL;

public class DAOConfigTest {
	
	private String projectPath;
	
	@Before
	public void setUp(){
		projectPath = new File("").getAbsolutePath();
	}
	
	@Test
	public void testReadOutputPath() throws Exception {
		String condingFilePath = projectPath + File.separator + Constants.TEST_RESOURCES + File.separator + "config_with_all_parameters.properties"; 		
		Config config = DAOConfig.readConfig(new File(condingFilePath));
		String actualOutputPath = config.getOutputPath();
		String expectedOutputPath = "output_folder";
		assertEquals(expectedOutputPath, actualOutputPath);
	}
	
	@Test
	public void testReadUsername() throws Exception {
		String condingFilePath = projectPath + File.separator + Constants.TEST_RESOURCES + File.separator + "config_with_all_parameters.properties"; 		
		Config config = DAOConfig.readConfig(new File(condingFilePath));
		String actualUsername = config.getUsername();
		String expectedUsername = "d47700";
		assertEquals(expectedUsername, actualUsername);
	}

	@Test
	public void testReadUrlWithAllParameters() throws Exception {
		String condingFilePath = projectPath + File.separator + Constants.TEST_RESOURCES + File.separator + "config_with_all_parameters.properties"; 		
		Config config = DAOConfig.readConfig(new File(condingFilePath));
		RepoSearchURL repoSearchUrl = config.getRepoSearchURL();
		String actualUrl = repoSearchUrl.toReadableString();
		String expectedUrl = "https://api.github.com/search/repositories?q=created:2018-10-24T00:00:00..2019-10-24T00:00:00+fork:false+language:Java+stars:>=100&sort=stars&order=desc";
		assertEquals(expectedUrl, actualUrl);
	}
	
	@Test
	public void testReadUrlWithQAndOrderParameteres() throws Exception {
		String condingFilePath = projectPath + File.separator + Constants.TEST_RESOURCES + File.separator + "config_with_q_and_order_parameters.properties"; 		
		Config config = DAOConfig.readConfig(new File(condingFilePath));
		RepoSearchURL repoSearchUrl = config.getRepoSearchURL();
		String actualUrl = repoSearchUrl.toReadableString();
		String expectedUrl = "https://api.github.com/search/repositories?q=created:2018-10-24T00:00:00..2019-10-24T00:00:00&order=desc";
		assertEquals(expectedUrl, actualUrl);
	}
	
	@Test
	public void testReadUrlWithQAndSortParameteres() throws Exception {
		String condingFilePath = projectPath + File.separator + Constants.TEST_RESOURCES + File.separator + "config_with_q_and_sort_parameters.properties"; 		
		Config config = DAOConfig.readConfig(new File(condingFilePath));
		RepoSearchURL repoSearchUrl = config.getRepoSearchURL();
		String actualUrl = repoSearchUrl.toReadableString();
		String expectedUrl = "https://api.github.com/search/repositories?q=created:2018-10-24T00:00:00..2019-10-24T00:00:00&sort=stars";
		assertEquals(expectedUrl, actualUrl);
	}

}
