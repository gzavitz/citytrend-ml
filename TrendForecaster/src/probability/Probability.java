package probability;

import collections.Hits;
import dtos.HitDto;
import dtos.VenueDto;

public abstract class Probability {
	
	public static float getProbability(Hits matchSubset, VenueDto search) {
		int overallCount = 0, venueCount = 0;
		for(HitDto hit : matchSubset.asList()) {
			if(hit.venueId.equals(search.getVenueId()))
				venueCount++;
			else
				overallCount++;
		}
		return (float) venueCount / (float) overallCount;
	}
	
	public abstract boolean matches(HitDto hit);
	
}
