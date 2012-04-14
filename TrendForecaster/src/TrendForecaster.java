import java.net.UnknownHostException;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

import dtos.FacebookObj;
import dtos.FoursquareObj;
import dtos.HitDto;
import dtos.VenueDto;


public class TrendForecaster {
	private Datastore datastore;
    private final static String uriString = "mongodb://ctytrend:11526HHTx105@ds029807.mongolab.com:29807/citytrend_prototype";


	public TrendForecaster(){
		// use the MongoURI to access MongoDB connection methods.
    	MongoURI uri = new MongoURI(uriString);
    	DB database = null;
    	Mongo mongo = null;
    	DBCollection locations = null;	

    	try {
    		mongo = uri.connect();
    	    database = uri.connectDB();
    	    database.authenticate(uri.getUsername(), uri.getPassword());

		} catch (UnknownHostException uhe) {
			System.out.println("UnknownHostException: " + uhe);
		} catch (MongoException me) {
			System.out.println("MongoException: " + me);
		}
		if(mongo != null){
			char[] pass = new String("11526HHTx105").toCharArray();
			Morphia morphia = new Morphia();
	    	morphia.map(VenueDto.class).map(FoursquareObj.class).map(FacebookObj.class).map(HitDto.class);
	    	
			datastore = morphia.createDatastore(mongo, "citytrend_prototype", "ctytrend", pass);



		}
	}
	
	public static void main(String[] args){
	}
}
