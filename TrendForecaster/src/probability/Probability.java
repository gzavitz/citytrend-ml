package probability;

import java.math.BigDecimal;
import java.math.RoundingMode;

import collections.Hits;
import dtos.HitDto;
import dtos.VenueDto;

public abstract class Probability {
	
	public static BigDecimal getProbability(Hits matchSubset, VenueDto search) {
		int overallCount = 0, venueCount = 0;
		for(HitDto hit : matchSubset.asList()) {
			if(hit.venueId.equals(search.getVenueId()))
				venueCount++;
			else
				overallCount++;
		}
		BigDecimal resultingCount = new BigDecimal(venueCount);
		BigDecimal totalCount = new BigDecimal(overallCount);
		return resultingCount.divide(totalCount, 8, RoundingMode.HALF_UP);
	}
	
	public abstract boolean matches(HitDto hit);
	
}

