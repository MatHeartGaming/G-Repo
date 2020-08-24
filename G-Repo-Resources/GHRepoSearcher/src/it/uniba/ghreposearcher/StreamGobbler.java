package it.uniba.ghreposearcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamGobbler extends Thread{
	
	private Logger logger = LoggerFactory.getLogger(StreamGobbler.class);
	
	private InputStream inputStream;
	private StringBuilder outputString;

	public StreamGobbler(InputStream is) {
		this.inputStream = is;
		this.outputString = new StringBuilder();
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null){
				outputString.append(line);
				outputString.append("\n");
			}
		} catch (IOException ioe) {
			logger.error(ioe.toString());
		}
	}
	
	public String getOutputString() {
		return outputString.toString().trim();
	}

}
