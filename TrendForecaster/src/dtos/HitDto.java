package dtos;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity("Hits")
public class HitDto {
	@Id private ObjectId id;
	public String hitId;
	public double score;
	public String venueId;
	public Integer type;
	public boolean expired = false;
	public long createTime;
	
	public final int FBCHECKIN = 1;
	public final int SQCHECKIN = 2;
	
	public HitDto(String venueId){
		this.venueId = venueId;
		score = 5;
		createTime = System.currentTimeMillis();
	}
	
	public HitDto(){
		score = 5;
	}
	
	public void setScore(double score){
		this.score = score;
	}
	
	public double getScore(){
		return score;
	}
	
	public String getVenueId(){
		return venueId;
	}
	
	public void setType(Integer type){
		this.type = type;
	}
	
	public Integer getType(){
		return type;
	}
	
	public void setHitId(String hitId){
		this.hitId = hitId;
	}
	
	public String getHitId(){
		return hitId;
	}
	
	public void setExpired(boolean isExpired){
		expired = isExpired;
	}
	
	public boolean getExpired(){
		return expired;
	}
	
	public Integer getBinaryExpired(){
		Integer result = 0;
		if(expired){
			result = 1;
		}
		return result;
	}
	
	public long getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(long createTime){
		this.createTime = createTime;
	}
	
	public ObjectId getObjectId(){
		return id;
	}

}