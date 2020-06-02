package it.uniba.ghreposearcher;
import static org.junit.Assert.*;

import org.junit.Test;

public class URLTest {	
	
	@Test
	public void testToReadableString() throws Exception {
		URL url = new URL("https", "api.github.com");
		url.addSubfolder("search");
		url.addSubfolder("repositories");
		url.addParameter("q", "language:Java stars:>=100 created:2018-10-24..2019-10-24");
		url.addParameter("sort", "stars");
		url.addParameter("order", "desc");
		String actualUrl = url.toReadableString();
		
		String expectedUrl = "https://api.github.com/search/repositories?q=language:Java stars:>=100 created:2018-10-24..2019-10-24&sort=stars&order=desc";
		assertEquals(expectedUrl, actualUrl); 
	}
	
}
