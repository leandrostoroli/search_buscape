package br.com.tagfy.buscape;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXB;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class BuscapeRestService {
	
	public static final String FIND_CATEGORY_LIST_URL = "http://sandbox.buscape.com/service/findCategoryList/77666639426f6f787676453d/";
	public static final String FIND_PRODUCT_LIST_URL = "http://sandbox.buscape.com/service/findProductList/77666639426f6f787676453d/";

	private static final String APPLICATION_XML = "application/xml";

	private ClientRequest request;
	
	public BuscapeRestService(String url, Map<String, String> params) {
		request = new ClientRequest(url);
		if (params == null) {
			params = new HashMap<String, String>();
		}
		Set<Entry<String, String>> entrySet = params.entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (entry.getValue() != null && !entry.getValue().isEmpty()) {
				request.queryParameter(entry.getKey(), entry.getValue());
			}
		}
		request.accept(APPLICATION_XML);
	}
	
	public <T> T get(Class<T> clazz) throws Exception {
		ClientResponse<String> response;
		response = request.get(String.class);
		return getResponse(response, clazz);
	}
	
	public <T> T getResponse(ClientResponse<String> response, Class<T> clazz) {
		String xml = response.getEntity(String.class);
		if (response.getStatus() == 200 && !xml.isEmpty()) {
			return JAXB.unmarshal(new StringReader(xml), clazz);
		}
		return null;
	}

}
