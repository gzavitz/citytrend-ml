package dtos;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class FoursquareObj {
	private String foursquareId;
	private Integer totalCheckins;
	private Integer uniqueUsers;
	private Long currentCheckins;
	
	public FoursquareObj(String foursquareId){
		this.foursquareId = foursquareId;
	}
	
	public FoursquareObj(){
		
	}
	
	public String getFoursquareId() {
		return foursquareId;
	}
	public void setFoursquareId(String foursquareId) {
		this.foursquareId = foursquareId;
	}
	public Integer getTotalCheckins() {
		return totalCheckins;
	}
	public void setTotalCheckins(Integer totalCheckins) {
		this.totalCheckins = totalCheckins;
	}
	public Integer getUniqueUsers() {
		return uniqueUsers;
	}
	public void setUniqueUsers(Integer uniqueUsers) {
		this.uniqueUsers = uniqueUsers;
	}
	public Long getCurrentCheckins() {
		return currentCheckins;
	}
	public void setCurrentCheckins(Long currentCheckins) {
		this.currentCheckins = currentCheckins;
	}

}
