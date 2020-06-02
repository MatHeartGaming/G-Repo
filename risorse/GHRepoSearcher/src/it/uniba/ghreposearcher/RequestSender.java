package it.uniba.ghreposearcher;

import java.io.IOException;

public class RequestSender {
	
	private String curlCommand;
	private Response response;
		
	public RequestSender(String curlCommand) {
		this.curlCommand = curlCommand;
	}
	
	public void sendRequest() throws GHRepoSearcherException {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(curlCommand);
			StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
			errorGobbler.start();
			StreamGobbler inputGobbler = new StreamGobbler(process.getInputStream());
			inputGobbler.start();
			int processStatus = process.waitFor();
			if(processStatus!=0) {
				throw new GHRepoSearcherException(errorGobbler.getOutputString());			
			}
			String outputString = inputGobbler.getOutputString();
			response = new Response(outputString);
			String responseStatus = response.getStatus();
			if(isErrorStatus(responseStatus)) {
				throw new GHRepoSearcherException(responseStatus + ":\n" + response.getRawBody());
			}
		} catch (IOException | InterruptedException e) {
			throw new GHRepoSearcherException(e);
		}
		
	}

	private boolean isErrorStatus(String status) {
		return status.startsWith("4") || status.startsWith("5");
	}

	public Response getResponse() {
		return response;
	}

}
