package collections;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dtos.VenueDto;

public class Venues extends ArrayList<Object>{

	private static final long serialVersionUID = 1L;
	private ArrayList<VenueDto> venues = new ArrayList<VenueDto>();

	
	public Venues(){
		super();
	}
	
	public Venues(Collection<VenueDto> dtos) {
		super(dtos);
	}
	
	public VenueDto findVenue(String venueId){
		VenueDto foundVenue = null;
		for(VenueDto venueDto: venues){
			if(venueId.equals(venueDto.getVenueId())){
				foundVenue = venueDto;
				break;
			}
		}
		return foundVenue;
	}
	
	public Venues getSubset(List<String> venueIds) {
		Venues subset = new Venues();
		for(VenueDto venue : venues) {
			if(venueIds.contains(venue.getVenueId()))
				subset.add(venue);
		}
		return subset;
	}
	
	public boolean containsVenue(String venueId) {
		for(VenueDto venueDto : venues) {
			if(venueId.equals(venueDto.getVenueId()))
				return true;
		}
		return false;
	}
	
	public void set(int index, VenueDto venueDto){
		venues.set(index, venueDto);
	}
	
	public ArrayList<VenueDto> asList(){
		return venues;
	}
	
	public void add(VenueDto hitDto){
		venues.add(hitDto);
	}
	
	public int size(){
		return venues.size();
	}
	
	public VenueDto get(int index){
		return venues.get(index);
	}
	
	public void addAll(ArrayList<VenueDto> arraylist){
		for(VenueDto venueDto: arraylist){
			venues.add(venueDto);
		}
	}
	
	public boolean isEmpty(){
		return venues.isEmpty();
	}
}
