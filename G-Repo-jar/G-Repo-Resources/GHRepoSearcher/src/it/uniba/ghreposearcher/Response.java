package it.uniba.ghreposearcher;

import com.google.gson.Gson;

import it.uniba.ghreposearcher.json.SearchResultBean;

public class Response {
	
	private String[] headers;
	private String rawBody;
	
	public Response(String textualResponse) {
		String headersString = textualResponse.substring(0, textualResponse.indexOf('{')).trim();
		this.headers = headersString.split("\\r?\\n");
		this.rawBody = textualResponse.substring(textualResponse.indexOf('{')).trim();
	}

	public String[] getHeaderItems() {
		return headers;
	}
	
	/*public String getStatus(){
		for (String header : headers) {
			if(header.startsWith("Status: ")) {
				return header.substring(header.indexOf(' ')+1); 
			}
		}
		return null;
	}*/
	
	public String getStatus(){
		/*for (String header : headers) {
			if(header.startsWith("Status: ")) {
				return header.substring(header.indexOf(' ')+1); 
			}
		}*/
		String firstHeader = headers[0];
		return firstHeader.substring(firstHeader.indexOf(' ')+1);
		//return null;
}
	
	public String getRawBody() {
		return rawBody;
	}
	
	public SearchResultBean getBody() {
		Gson gson = new Gson();
		return gson.fromJson(rawBody, SearchResultBean.class);
	}
	
	public String getHeaders() {
		StringBuilder buffer = new StringBuilder();
		for (String headerItem : headers) {
			buffer.append(headerItem);
			buffer.append("\n");
		}
		return buffer.toString().trim();
	}

}
