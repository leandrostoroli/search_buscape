package br.com.tagfy.buscape;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import buscape.Result;

public final class CategoryService extends UntypedActor {
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof CategoryRequest) {
			final ActorRef mainRef = getContext().actorFor("/user/master");
			CategoryRequest categoryRequest = (CategoryRequest) message;
			BuscapeRestService restService = new BuscapeRestService(BuscapeRestService.FIND_CATEGORY_LIST_URL,
					categoryRequest.getParams());
			Result result;
			try {
				result = restService.get(Result.class);
				if (result != null && result.getCategory() != null) {
					mainRef.tell(new CategoryResponse(result));
				} else {
					System.out.println("There is no Result");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
