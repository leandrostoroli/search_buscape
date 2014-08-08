package br.com.tagfy.buscape;

import java.util.HashMap;
import java.util.Map;

public class ProductRequest {
	
	private final Map<String, String> params = new HashMap<String, String>();
	
	private int categoryId;
	
	private String keyword;
	
	private int page;
	
	private int results;
	
	public ProductRequest(final int categoryId, final String keyworkd, final int page, final int results) {
		params.put("categoryId", String.valueOf(categoryId));
		this.categoryId = categoryId;
		if (keyworkd != null && !keyworkd.isEmpty()) {
			params.put("keyworkd", keyworkd);
			this.keyword = keyworkd;
		}
		if (page <= 0) {
			params.put("page", "1");
			this.page = 1;
		} else {
			params.put("page", String.valueOf(page));
			this.page = page;
		}
		if (results <= 0) {
			params.put("results", "100");
			this.results = 100;
		} else {
			params.put("results", String.valueOf(results));
			this.results = results;
		}
	}

	public Map<String, String> getParams() {
		return params;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getResults() {
		return results;
	}

	public void setResults(int results) {
		this.results = results;
	}
	

}
