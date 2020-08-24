package it.uniba.ghreposearcher.json;

public class SearchResultBean {
	
	private long total_count;
	private boolean incomplete_results;
	private RepoBean[] items;
	
	public long getTotal_count() {
		return total_count;
	}
	
	public void setTotal_count(long total_count) {
		this.total_count = total_count;
	}
	
	public boolean isIncomplete_results() {
		return incomplete_results;
	}
	
	public void setIncomplete_results(boolean incomplete_results) {
		this.incomplete_results = incomplete_results;
	}
	
	public RepoBean[] getItems() {
		return items;
	}
	
	public void setItems(RepoBean[] items) {
		this.items = items;
	}

}
