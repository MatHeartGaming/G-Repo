package it.uniba.ghreposearcher.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.uniba.ghreposearcher.json.SearchResultBean;

public class DAOSearchResultBean {
	
	private DAOSearchResultBean() {
	}
	
	public static void write (SearchResultBean searchResultBean, File file) throws StorageException {
		try (Writer writer = new FileWriter(file)) {
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    gson.toJson(searchResultBean, writer);
		} catch (IOException e) {
			throw new StorageException(e);
		} 
		
	}

}
