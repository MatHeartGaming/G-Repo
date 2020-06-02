package it.uniba.ghreposearcher.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import it.uniba.ghreposearcher.Config;
import it.uniba.ghreposearcher.RepoSearchURL;

public class DAOConfig {
	
	private DAOConfig() {
	}

	public static Config readConfig(File file) throws StorageException {
		try (InputStream input = new FileInputStream(file)) {
			
			Properties properties = new Properties();
			properties.load(input);
			
			String username = properties.getProperty("username").trim(); 
			
			String outputPath = properties.getProperty("output_path").trim();
					
			RepoSearchURL url = new RepoSearchURL();
			
			addQParameter(properties, url);
			addSortParameter(properties, url);
			addOrderParameter(properties, url);
			addPerPageParameter(properties, url);
					
			return new Config(username, outputPath, url);
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}	

	private static void addQParameter(Properties properties, RepoSearchURL url) {		
		int i = 1;
		//boolean found = false;
		while (properties.containsKey("q" + i)) {
			String qualifier = properties.getProperty("q" + i);
			if(isCreatedQualifier(qualifier)) {
				//found = true;
				
			}
			url.addQualifier(qualifier.trim());
			i++;
		}
		
//		if(!found) {
//			throw new IllegalArgumentException("");
//		}
	}
	
	private static boolean isCreatedQualifier(String qualifier) {
		return qualifier.startsWith("created:");
	}

	private static void addSortParameter(Properties properties, RepoSearchURL url) {
		if(!properties.containsKey("sort")) {
			return;
		}
		url.addParameter("sort", properties.getProperty("sort").trim());
	}
	
	private static void addOrderParameter(Properties properties, RepoSearchURL url) {
		if(!properties.containsKey("order")) {
			return;
		}
		url.addParameter("order", properties.getProperty("order").trim());
	}
	
	private static void addPerPageParameter(Properties properties, RepoSearchURL url) {
		if(!properties.containsKey("per_page")) {
			return;
		}
		url.addParameter("per_page", properties.getProperty("per_page").trim());
	}

}
