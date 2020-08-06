package it.uniba.ghreposearcher;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class RepoSearchURLTest {


	@Test
	public void testReadableString() throws Exception {
		RepoSearchURL url = new RepoSearchURL();
		url.addQualifier("language:Java");
		url.addQualifier("stars:>=100");
		url.addQualifier("created:2018-10-24..2019-10-24");		
		url.addParameter("sort", "stars");
		url.addParameter("order", "desc");
		String actualUrl = url.toReadableString();

		String expectedUrl = "https://api.github.com/search/repositories?q=language:Java+stars:>=100+created:2018-10-24..2019-10-24&sort=stars&order=desc";
		assertEquals(expectedUrl, actualUrl);
	}
	
	@Test
	public void testGetStartDateOfCreationQualifier() throws Exception {
		String createdQualifier = "created:2018-10-24T00:00:00..2019-10-24T00:00:00";
		Date actualStartDate = RepoSearchURL.getStartDateOfCreationQualifier(createdQualifier);
		Date expectedStartDate = new GregorianCalendar(2018,9,24).getTime();
		assertEquals(expectedStartDate.getTime(), actualStartDate.getTime());
	}
	
	@Test
	public void testGetEndDateOfCreationQualifier() throws Exception {
		String createdQualifier = "created:2018-10-24T00:00:00..2019-10-24T00:00:00";
		Date actualEndDate = RepoSearchURL.getEndDateOfCreationQualifier(createdQualifier);
		Date expectedEndDate = new GregorianCalendar(2019,9,24).getTime();
		assertEquals(expectedEndDate.getTime(), actualEndDate.getTime());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetEndDateOfCreationQualifierWhenBadlyFormatted() throws Exception {
		String createdQualifier = "created:2018-10-24T00:00:00Z..2019-24";
		RepoSearchURL.getEndDateOfCreationQualifier(createdQualifier);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetStartDateOfCreationQualifierWhenBadlyFormatted() throws Exception {
		String createdQualifier = "created:2018-10-..2019-10-24T00:00:00Z";
		RepoSearchURL.getStartDateOfCreationQualifier(createdQualifier);
	}
	
}
