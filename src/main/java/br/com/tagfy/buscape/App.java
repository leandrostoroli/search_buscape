package br.com.tagfy.buscape;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

public class App {

	private static DB db;
	private static Mongo mongo;

	public static void main(String[] args) {
		System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
		try {
			MongoOptions mongoOptions = new MongoOptions();
			mongoOptions.setConnectionsPerHost(100);
			mongoOptions.setThreadsAllowedToBlockForConnectionMultiplier(100);
			mongo = new Mongo("localhost", mongoOptions);
			mongo.getMongoOptions().setConnectionsPerHost(100);
			if (mongo != null) {
				db = mongo.getDB("buscape");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (db != null) {
			try {
				ActorSystem system = ActorSystem.create("BuscapeProducts");
				system.actorOf(new Props(CategoryService.class).withRouter(new RoundRobinRouter(10000)), "categories");
				system.actorOf(new Props(ProductService.class).withRouter(new RoundRobinRouter(10000)), "products");
				final ActorRef mainRef = system.actorOf(new Props(new UntypedActorFactory() {
					private static final long serialVersionUID = 7809029389054780183L;
					public UntypedActor create() {
						return new MainService(db);
					}
				}).withRouter(new RoundRobinRouter(10000)), "master");
				StartMessage startMessage = new StartMessage(0, null);
				mainRef.tell(startMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
