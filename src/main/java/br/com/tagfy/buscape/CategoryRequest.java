package br.com.tagfy.buscape;

import java.util.HashMap;
import java.util.Map;

public final class CategoryRequest {
	
	private final Map<String, String> params = new HashMap<String, String>();
	
	public CategoryRequest(final int categoryId, final String keyworkd) {
		params.put("categoryId", String.valueOf(categoryId));
		if (keyworkd != null && !keyworkd.isEmpty()) {
			params.put("keyworkd", keyworkd);
		}
	}

	public Map<String, String> getParams() {
		return params;
	}

}
