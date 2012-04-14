package probability;

import collections.Hits;
import dtos.HitDto;
import dtos.VenueDto;

public abstract class Probability {
	
	private VenueDto mVenue;
	private String mVenueId;
	
	public Probability(VenueDto venue) {
		mVenue = venue;
		mVenueId = venue.getVenueId();
	}
	
	public final boolean matchesId(HitDto hit) {
		return hit.venueId.equals(mVenueId);
	}
	
	public float getProbability(Hits hits) {
		int overallCount = 0, venueCount = 0;
		for(HitDto hit : hits.asList()) {
			if(matchesId(hit))
				venueCount++;
			else
				overallCount++;
		}
		return (float) venueCount / (float) overallCount;
	}
	
	public abstract boolean matches(HitDto hit);
	
}
