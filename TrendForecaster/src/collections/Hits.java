package collections;

import java.util.ArrayList;
import java.util.Collection;

import dtos.HitDto;

public class Hits extends ArrayList<Object>{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<HitDto> hits = new ArrayList<HitDto>();
	
	public Hits(){
		super();
	}
	
	public Hits(Collection<HitDto> dtos) {
		super(dtos);
	}
	
	public Hits findVenueHits(String venueId){
		Hits venuesHits = new Hits();
		for(HitDto hitDto: hits){
			if(venueId.equals(hitDto.getVenueId())){
				venuesHits.add(hitDto);
			}
		}
		return venuesHits;
	}
	
	public ArrayList<HitDto> asList(){
		return hits;
	}
	
	public void add(HitDto hitDto){
		hits.add(hitDto);
	}
	
	public int size(){
		return hits.size();
	}
	
	public void addAll(ArrayList<HitDto> arraylist){
		for(HitDto venueDto: arraylist){
			hits.add(venueDto);
		}
	}
	
	public boolean isEmpty(){
		return hits.isEmpty();
	}
	
	

}
