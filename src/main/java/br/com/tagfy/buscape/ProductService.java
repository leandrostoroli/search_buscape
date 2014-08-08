package br.com.tagfy.buscape;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import buscape.Result;

public class ProductService extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ProductRequest) {
			final ActorRef productRef = getContext().actorFor("/user/products");
			final ActorRef mainRef = getContext().actorFor("/user/master");
			ProductRequest productRequest = (ProductRequest) message;
			BuscapeRestService restService = new BuscapeRestService(BuscapeRestService.FIND_PRODUCT_LIST_URL,
					productRequest.getParams());
			Result result;
			try {
				result = restService.get(Result.class);
				if (result != null) {
					productRequest.setPage(productRequest.getPage() + 1);
					if (result.getTotalResultsReturned() > 0 && result.getPage() < result.getTotalPages()) {
						productRef.tell(productRequest);
					} 
					mainRef.tell(new ProductResponse(result));
				} else {
					System.out.println("There is no Result");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
