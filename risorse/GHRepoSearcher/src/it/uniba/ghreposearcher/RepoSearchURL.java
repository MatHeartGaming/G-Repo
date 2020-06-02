package it.uniba.ghreposearcher;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RepoSearchURL extends URL {

	private final static String CONNECTION_TYPE = "https";
	private final static String GITHUB_HOST = "api.github.com";

	private List<String> qualifiers;
	
	public RepoSearchURL() {
		super(CONNECTION_TYPE, GITHUB_HOST);
		addSubfolder("search");
		addSubfolder("repositories");
		qualifiers = new ArrayList<>();
	}
	
	public RepoSearchURL(RepoSearchURL url) {
		this();
		for (Entry<String, String> entry : url.getParams().entrySet()) {
			this.addParameter(entry.getKey(), entry.getValue());
		}
		for (String qualifier : url.getQualifiers()) {
			this.addQualifier(qualifier);
		}
	}
	
	private List<String> getQualifiers() {
		return qualifiers;
	}

	public void addQualifier(String qualifier) {
		qualifiers.add(qualifier);
	}
	
	@Override
	public void addParameter(String parameter, String value) {
		if(parameter.equals("q")) {
			throw new IllegalArgumentException("The q parameter is automatically built by usign the qualifiers");
		}
		params.put(parameter, value);
	}
	
	public String getParameterValue(String parameter) {
		return params.get(parameter);
	}
	
	public String getCreatedQualifier() {
		return qualifiers.get(indexOfCreatedQualifier());
	}
	
	public int indexOfCreatedQualifier() {
		for (int i = 0; i < qualifiers.size(); i++) {
			String qualifier = qualifiers.get(i);
			if(qualifier.startsWith("created:")){
				return i;
			}
		}
		throw new IllegalArgumentException("Created qualifier not specified.");
	}
	
	public void setCreatedQualifier(String newCreatedQualifier) {
		int i = indexOfCreatedQualifier();
		qualifiers.set(i, newCreatedQualifier);
	}
	
	public static Date getStartDateOfCreationQualifier(String createdQualifier) {
		String value = createdQualifier.replace("created:", "");
		String[] datesAsString = value.split("\\.\\.");  
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			return formatter.parse(datesAsString[0]);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The qualifier \"" + createdQualifier + "\" do not follow the format YYYY-MM-DD..YYYY-MM-DD");
		}
	}
	
	public static Date getEndDateOfCreationQualifier(String createdQualifier) {
		String value = createdQualifier.replace("created:", "");
		String[] datesAsString = value.split("\\.\\.");  
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			return formatter.parse(datesAsString[1]);
		} catch (ParseException e) {
			throw new IllegalArgumentException("The qualifier \"" + createdQualifier + "\" do not follow the format YYYY-MM-DD..YYYY-MM-DD");
		}
	}
	
	@Override
	protected String paramsToString() {
		StringBuilder buffer = new StringBuilder();
		if(!qualifiers.isEmpty()) {
			buffer.append("q=");
			for (Iterator<String> iterator = qualifiers.iterator(); iterator.hasNext();) {
				buffer.append(iterator.next());
				if(iterator.hasNext()) {
					buffer.append(" ");
				}
			}
		}
		for (Map.Entry<String,String> entry : params.entrySet()) {
			if (!buffer.toString().isEmpty()) {
				buffer.append("&");
			}
			buffer.append(entry.getKey());
			buffer.append("=");
			buffer.append(entry.getValue());
		}
		return buffer.toString();
	}
	
	@Override
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
            buffer.append(query.replace(' ', '+'));
        }
        String fragment = uri.getFragment();
        if (fragment != null) {
            buffer.append('#');
            buffer.append(fragment);
        }
        return buffer.toString();
    }
	
}
