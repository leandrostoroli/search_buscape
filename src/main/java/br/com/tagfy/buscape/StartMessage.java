package br.com.tagfy.buscape;


public final class StartMessage {
	
	private int categoryId;
	
	private String keyword;
	
	public StartMessage(final int categoryId, final String keyworkd) {
		this.categoryId = categoryId;
		this.keyword = keyworkd;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getKeyword() {
		return keyword;
	}

}
