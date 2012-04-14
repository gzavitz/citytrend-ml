package dtos;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class FacebookObj {
	public Integer currentCheckins;
	public String facebookId;
	public String venueId;
	
	public FacebookObj(String facebookId, String venueId){
		this.venueId = venueId;
		this.facebookId = facebookId;
	}
	
	public FacebookObj(){
		
	}
	
	public void setCurrentCheckins(Integer checkins){
		currentCheckins = checkins;
	}
	
	public Integer getCurrentCheckins(){
		return currentCheckins;
	}
	
	public String getFacebookId(){
		return facebookId;
	}
	
	public String getVenueId(){
		return venueId;
	}
}
