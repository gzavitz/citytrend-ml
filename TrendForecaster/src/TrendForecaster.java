import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import probability.DayOfWeekProbability;
import probability.Posterior;
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
		System.out.println("Establishing Database connection...");
		
		
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
		
		System.out.println("Datastore obtained");
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
	
	
	public static void main(String[] args){
		BigDecimal dayWeight = new BigDecimal("0.7");
		TrendForecaster forecast = new TrendForecaster();
		
		Long beginTime = System.currentTimeMillis();
		Hits hits = forecast.getHitCollection();
		Venues venues = forecast.getVenueCollection();
		Long endTime = System.currentTimeMillis();
		System.out.println("Pulled " + hits.size() + " hits and " + venues.size() + " venues in " + (endTime - beginTime) + " ms");
		
		ArrayList<Posterior> posteriors = new ArrayList<Posterior>();
		Hits dayOfWeekSubset = hits.findVenueHits(new DayOfWeekProbability(Calendar.MONDAY));
		Hits timeOfDaySubset = hits.findVenueHits(new TimeOfDayProbability(9, 10));
		Hits timeOfDayDayOfWeekSubset = hits.findVenueHits(new TimeOfDayDayOfWeekProbability(Calendar.TUESDAY, 9, 10));
		
		Long beginCalcTime = System.currentTimeMillis();
		for(VenueDto venue : venues.asList()) {
			Posterior posterior = new Posterior();
			posterior.venue = venue;
			
			//prior which is overall probability of a venue checkin being of this venue
//			posterior.posterior *= 0.7f * Probability.getProbability(hits, venue);
			posterior.posterior = posterior.posterior.multiply(dayWeight.multiply(Probability.getProbability(hits, venue)));
			
			posterior.posterior = posterior.posterior.multiply(dayWeight.multiply(Probability.getProbability(dayOfWeekSubset, venue)));
			posterior.posterior = posterior.posterior.multiply(Probability.getProbability(timeOfDaySubset, venue));
			posterior.posterior = posterior.posterior.multiply(Probability.getProbability(timeOfDayDayOfWeekSubset, venue));
			
//			posterior.posterior *= 0.7f * Probability.getProbability(dayOfWeekSubset, venue);
//			posterior.posterior *= Probability.getProbability(timeOfDaySubset, venue);
//			posterior.posterior *= Probability.getProbability(timeOfDayDayOfWeekSubset, venue);
			
			posteriors.add(posterior);
		}
		Long endCalcTime = System.currentTimeMillis();
		System.out.println("Calculated Posteriors in " + (endCalcTime - beginCalcTime) + " ms");

		beginCalcTime = System.currentTimeMillis();
		Collections.sort(posteriors, new Comparator<Posterior>() {
			@Override
			public int compare(Posterior o1, Posterior o2) {
				return o1.posterior.compareTo(o2.posterior);
			}
		});
		
		
		endCalcTime = System.currentTimeMillis();
		System.out.println("Sorted Venues in " + (endCalcTime - beginCalcTime));
		
		System.out.println("Total Hits: " + hits.asList().size());
		System.out.println("Total Venues: " + venues.asList().size());
		
		for(Posterior posterior : posteriors)
			System.out.println(posterior);
	}
}
