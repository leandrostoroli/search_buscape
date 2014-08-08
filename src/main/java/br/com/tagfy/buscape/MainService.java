package br.com.tagfy.buscape;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import br.com.extra.api.pojo.products.Image;
import br.com.extra.api.pojo.products.Media;
import br.com.extra.api.pojo.products.Product;
import buscape.CategoryType;
import buscape.ProductType;
import buscape.ThumbnailType;

import com.google.gson.Gson;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.util.JSON;

public final class MainService extends UntypedActor {
	
	private final ActorRef categoryRef;
	
	private final ActorRef productRef;
	
	private final DBCollection categoriesColletion;
	
	private final DBCollection productsCollection;
	
	private final DB db;

	private final Gson gson;

	private DBCollection productsCollectionExtra;
	
	public MainService(DB db) {
		this.gson = new Gson();
		this.db = db;
		this.categoriesColletion = db.getCollection("categories");
		this.productsCollection = db.getCollection("products");
		this.productsCollectionExtra = db.getCollection("products_extra");
		this.categoryRef = getContext().actorFor("/user/categories");
		this.productRef = getContext().actorFor("/user/products");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof CategoryResponse) {
			CategoryResponse categoryResponse = (CategoryResponse) message;
			db.requestStart();
			db.requestEnsureConnection();
			for (CategoryType categoryType : categoryResponse.getResult().getSubCategory()) {
				try {
					categoriesColletion.insert((DBObject) JSON.parse(gson.toJson(categoryType)), WriteConcern.SAFE);
					productRef.tell(new ProductRequest(categoryType.getId(), "", -1, -1));
				} finally { }
				categoryRef.tell(new CategoryRequest(categoryType.getId(), ""));
			}
			db.requestDone();
		} else if (message instanceof ProductResponse) {
			ProductResponse productResponse = (ProductResponse) message;
			db.requestStart();
			db.requestEnsureConnection();
			for (ProductType productType : productResponse.getResult().getProduct()) {
				try {
					productsCollection.insert((DBObject) JSON.parse(gson.toJson(productType)), WriteConcern.SAFE);
				} finally { }
			}
			db.requestDone();
			
//			Product product = new Product();
//			List<ProductType> products = productResponse.getResult().getProduct();
//			for (ProductType productType : products) {
//				product.setSkuIdOrigin(String.valueOf(productType.getId()));
//				product.setSellingTitle(productType.getProductName());
//				product.setDescription(productType.getProductName());
//				product.setDefaultPrice(Double.valueOf(productType.getPriceMax()));
//				product.setSalePrice(Double.valueOf(productType.getPriceMin()));
//				Media media = new Media();
//				List<Image> images = new ArrayList<Image>();
//				ThumbnailType thumbnailType = productType.getThumbnail();
//				Image image = new Image();
//				image.setName(thumbnailType.getUrl());
//				image.setUrl(thumbnailType.getUrl());
//				images.add(image);
//				media.setImages(images);
//				product.setMedia(media);
//				product.setHeight(1);
//				product.setWeight(1);
//				product.setLength(1);
//				product.setWidth(1);
//				product.setVariantName(productType.getProductName());
//				db.requestStart();
//				db.requestEnsureConnection();
//				productsCollectionExtra.insert((DBObject) JSON.parse(gson.toJson(product)), WriteConcern.SAFE);
//				db.requestDone();
//			}
			
		} else if (message instanceof StartMessage) {
			StartMessage startMessage = (StartMessage) message;
			CategoryRequest categoryRequest = new CategoryRequest(startMessage.getCategoryId(), startMessage.getKeyword());
			categoryRef.tell(categoryRequest);
		} else {
			unhandled(message);
		}
	}

}
