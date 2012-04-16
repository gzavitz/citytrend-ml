import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

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
	
	public Hits getExpiredHitCollection(){
		Query<HitDto> hitQuery = datastore.find(HitDto.class, "expired", true);
		Hits allHits = new Hits();
		for(HitDto hitDto: hitQuery){
			allHits.add(hitDto);
		}
		
		return allHits;
	}
	
	public Hits getExpiredHitCollection(Venues venues){
		Query<HitDto> hitQuery = datastore.find(HitDto.class, "expired", true);
		Hits allHits = new Hits();
		for(HitDto hitDto: hitQuery){
			if(venues.containsVenue(hitDto.venueId))
				allHits.add(hitDto);
		}
		
		return allHits;
	}
	
	public Hits getActiveHitCollection(){
		Query<HitDto> hitQuery = datastore.find(HitDto.class, "expired", false);
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
		String[] venueIds = new String[] {
			// bars: "1", "5", "8", "9", "10", "12", "14"
			"7", "13", // hotels
			"17", "32", "55", "60"
		};
		
		BigDecimal dayWeight = new BigDecimal("0.7");
		TrendForecaster forecast = new TrendForecaster();
		
		Long beginTime = System.currentTimeMillis();
		Venues venues = forecast.getVenueCollection();//.getSubset(Arrays.asList(venueIds));
		Hits hits = forecast.getExpiredHitCollection(venues);
		Long endTime = System.currentTimeMillis();
		System.out.println("Pulled " + hits.size() + " hits and " + venues.size() + " venues in " + (endTime - beginTime) + " ms");
		
		ArrayList<Posterior> posteriors = new ArrayList<Posterior>();
		int dayOfWeek = Calendar.MONDAY;
		int hourOfDay = 9;
		Hits dayOfWeekSubset = hits.findVenueHits(new DayOfWeekProbability(dayOfWeek));
		Hits timeOfDaySubset = hits.findVenueHits(new TimeOfDayProbability(hourOfDay, hourOfDay + 1));
		Hits timeOfDayDayOfWeekSubset = hits.findVenueHits(new TimeOfDayDayOfWeekProbability(dayOfWeek, hourOfDay, hourOfDay + 1));
//		Hits currentSubset = forecast.getActiveHitCollection();
		
		Long beginCalcTime = System.currentTimeMillis();
		for(VenueDto venue : venues.asList()) {
			Posterior p = new Posterior();
			p.venue = venue;
			
			//prior which is overall probability of a venue checkin being of this venue
			p.posterior = p.posterior.multiply(dayWeight.multiply(Probability.getProbability(hits, venue)));
			
			p.posterior = p.posterior.multiply(dayWeight.multiply(Probability.getProbability(dayOfWeekSubset, venue)));
			p.posterior = p.posterior.multiply(Probability.getProbability(timeOfDaySubset, venue));
			p.posterior = p.posterior.multiply(Probability.getProbability(timeOfDayDayOfWeekSubset, venue));
//			p.posterior = p.posterior.multiply(Probability.getProbability(currentSubset, venue));
			
			posteriors.add(p);
		}
		long endCalcTime = System.currentTimeMillis();
		System.out.println("Calculated Posteriors in " + (endCalcTime - beginCalcTime) + " ms");
		Collections.sort(posteriors);
		System.out.println("Total Hits: " + hits.asList().size());
		System.out.println("Total Venues: " + venues.asList().size());
		
		for(Posterior posterior : posteriors)
			System.out.println(posterior);
		
		HashMap<Integer, BigDecimal> categories = new HashMap<Integer, BigDecimal>();
		for(Posterior posterior : posteriors) {
			int key = posterior.venue.getCategory();
			if(categories.containsKey(key)) {
				categories.put(key, categories.get(key).add(posterior.posterior));
			} else {
				BigDecimal decimal = new BigDecimal(posterior.posterior.toString());
				categories.put(key, decimal);
			}
		}
		String[] categoryNames = new String[] {
			"UNCATEGORIZED",
			"FASTFOOD",
		    "RESTAURANT",
		    "BAR ",
		    "CLUB",
		    "ENTERTAINMENT",
		    "STORE",
		    "MUSEUM",
		    "CAFE"
		};
		NumberFormat formatter = new DecimalFormat("#0.0000000000");
		for(Integer key : categories.keySet()) {
			System.out.printf("%s: " + formatter.format(categories.get(key)).toString() + "\n", categoryNames[key.intValue()]);
		}
	}
}
