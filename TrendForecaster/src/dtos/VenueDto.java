package dtos;


import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.mongodb.BasicDBObject;

@Entity("Venues")
public class VenueDto implements Comparable<VenueDto>{
	@Id private ObjectId id;
	private String venueId;
	private String venueName;
	private String streetAddress;
	private String city;
	private String zipcode;
	private Integer numCurrentHits;
	private double totalHitScore;
	private Integer category;
	private Integer currentRank;
	private double latitude;
	private double longitude;
	
	@Embedded
	private FoursquareObj foursquareObj;
	
	@Embedded
	private FacebookObj facebookObj;
	
	public VenueDto(String venueId){
		this.venueId = venueId;

	}
	
	public VenueDto(){

	}
	
	public String getVenueId() {
		return venueId;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Integer getNumCurrentHits() {
		return numCurrentHits;
	}

	public void setNumCurrentHits(Integer numCurrentHits) {
		this.numCurrentHits = numCurrentHits;
	}

	public double getTotalHitScore(){
		return totalHitScore;
	}
	
	public void setTotalHitScore(double totalHitScore) {
		this.totalHitScore = totalHitScore;
	}
	
	public Integer getCategory(){
		return category;
	}
	
	public void setCategory(Integer category){
		this.category = category;
	}
	
	public Integer getRank(){
		return currentRank;
	}
	
	public void setRank(Integer currentRank){
		this.currentRank = currentRank;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public ObjectId getObjectId(){
		return id;
	}
	
	public FoursquareObj getFoursquareObj(){
		return foursquareObj;
	}
	
	public void setFoursquareObj(FoursquareObj foursquareObj){
		this.foursquareObj = foursquareObj;
	}
	
	public FacebookObj getFacebookObj(){
		return facebookObj;
	}
	
	public void setFacebookObj(FacebookObj facebookObj){
		this.facebookObj = facebookObj;
	}

	public int compareTo(VenueDto otherVenue) {
		if(this.totalHitScore < otherVenue.totalHitScore){
			return -1;
		}
		else if(this.totalHitScore > otherVenue.totalHitScore){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public BasicDBObject getBasicDBObject()
	{
		BasicDBObject venue = new BasicDBObject();
		venue.put("venueId", this.venueId );
		venue.put("venueName", this.venueName); 
		venue.put("streetAddress", this.streetAddress); 
		venue.put("city", this.city);
		venue.put("zipcode", this.zipcode);
		venue.put("numCurrentHits", this.numCurrentHits);
		venue.put("totalHitScore", this.totalHitScore);
		venue.put("category", this.category);
		venue.put("rank", this.currentRank);
		venue.put("latitude", this.latitude);
		venue.put("longitude", this.longitude);
		venue.put("foursquareObj", this.foursquareObj);
		return venue;
	}	

}

