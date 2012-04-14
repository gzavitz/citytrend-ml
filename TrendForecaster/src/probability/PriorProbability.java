package probability;

import dtos.HitDto;
import dtos.VenueDto;

public class PriorProbability extends Probability {

	public PriorProbability(VenueDto venue) {
		super(venue);
	}

	@Override
	public boolean matches(HitDto hit) {
		return matchesId(hit);
	}

}
