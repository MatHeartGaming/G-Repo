package it.uniba.ghreposearcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class URL {

	protected String connectionType;
	protected String host;

	protected List<String> folders;

	protected Map<String, String> params;
	
	public URL(URL url) {
		this(url.getConnectionType(), url.getHost());
		for (String folder : url.getFolders()) {
			this.addSubfolder(folder);
		}
		for (Entry<String, String> entry : url.getParams().entrySet()) {
			this.addParameter(entry.getKey(), entry.getValue());
		}		
	}
	
	public URL(String connectionType, String host) {
		this.connectionType = connectionType;
		this.host = host;
		this.folders = new ArrayList<>();
		this.params = new HashMap<>();
	}

	private List<String> getFolders() {
		return folders;
	}

	protected Map<String, String> getParams() {
		return params;
	}

	public void addSubfolder(String folder) {
		folders.add(folder);
	}

	public void addParameter(String parameter, String value) {
		params.put(parameter, value);
	}
		

	public String getConnectionType() {
		return connectionType;
	}

	public String getHost() {
		return host;
	}

	public String toReadableString() throws URISyntaxException {
		URI uri = new URI(connectionType, host, foldersToString(), paramsToString(), null);
		return toString(uri);
	}

	protected String foldersToString() {
		StringBuilder buffer = new StringBuilder();
		for (String folder : folders) {
			buffer.append("/");
			buffer.append(folder);
		}
		return buffer.toString();
	}

	protected String paramsToString() {
		StringBuilder buffer = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (!buffer.toString().isEmpty()) {
				buffer.append("&");
			}
			buffer.append(entry.getKey());
			buffer.append("=");
			buffer.append(entry.getValue());
		}
		return buffer.toString();
	}

	protected String toString(URI uri) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(uri.getScheme());
		buffer.append(':');
		buffer.append("//");
		buffer.append(uri.getHost());
		String path = uri.getPath();
		if (path != null) {
			buffer.append(path);
		}
		String query = uri.getQuery();
		if (query != null) {
			buffer.append('?');
			buffer.append(query);
		}
		String fragment = uri.getFragment();
		if (fragment != null) {
			buffer.append('#');
			buffer.append(fragment);
		}
		return buffer.toString();
	}


	

}
