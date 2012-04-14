import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import probability.DayOfWeekProbability;
import probability.Probability;
import probability.TimeOfDayDayOfWeekProbability;
import probability.TimeOfDayProbability;
import collections.Hits;
import collections.Venues;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
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
	
	public Hits getHitCollection(){
		Query<HitDto> hitQuery = datastore.find(HitDto.class, "expired", true);
		Hits allHits = new Hits();
		for(HitDto hitDto: hitQuery){
			allHits.add(hitDto);
		}
		
		return allHits;
	}
	
	public Venues getVenueCollection(){
		Query<VenueDto> venueQuery = datastore.find(VenueDto.class);
		Venues venues = new Venues();
		for(VenueDto venueDto: venueQuery){
			venues.add(venueDto);
		}
		
		return venues;
	}
	
	public static class Posterior {
		
		public VenueDto venue;
		public double posterior = 1f;
		
		public String toString() {
			return String.format("%s " + posterior, venue.getVenueName());
		}
		
	}
	
	public static void main(String[] args){
		TrendForecaster forecast = new TrendForecaster();
		Hits hits = forecast.getHitCollection();
		Venues venues = forecast.getVenueCollection();
		
		ArrayList<Posterior> posteriors = new ArrayList<Posterior>();
		Hits dayOfWeekSubset = hits.findVenueHits(new DayOfWeekProbability(Calendar.MONDAY));
		Hits timeOfDaySubset = hits.findVenueHits(new TimeOfDayProbability(12, 15));
		Hits timeOfDayDayOfWeekSubset = hits.findVenueHits(new TimeOfDayDayOfWeekProbability(Calendar.MONDAY, 12, 15));
		for(VenueDto venue : venues.asList()) {
			Posterior posterior = new Posterior();
			posterior.venue = venue;
			
			//prior which is overall probability of a venue checkin being of this venue
			posterior.posterior *= 0.7f * Probability.getProbability(hits, venue);
			
			posterior.posterior *= 0.7f * Probability.getProbability(dayOfWeekSubset, venue);
			posterior.posterior *= Probability.getProbability(timeOfDaySubset, venue);
			posterior.posterior *= Probability.getProbability(timeOfDayDayOfWeekSubset, venue);
			
			posteriors.add(posterior);
		}
		Collections.sort(posteriors, new Comparator<Posterior>() {
			@Override
			public int compare(Posterior o1, Posterior o2) {
				if(o1.posterior < o2.posterior) return -1;
				else if(o1.posterior > o2.posterior) return 1;
				return 0;
			}
		});
		
		System.out.println("Total Hits: " + hits.asList().size());
		System.out.println("Total Venues: " + venues.asList().size());
		
		for(Posterior posterior : posteriors)
			System.out.println(posterior);
	}
}
