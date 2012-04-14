package probability;

import java.util.Calendar;

import collections.Hits;
import dtos.HitDto;
import dtos.VenueDto;

public class DayOfWeekProbability extends Probability {

	private int dayOfWeek;
	
	public DayOfWeekProbability(VenueDto venue, int dayOfWeek) {
		super(venue);
		this.dayOfWeek = dayOfWeek;
	}
	
	@Override
	public float getProbability(Hits hits) {
		return getStandardRatio(hits);
	}
	
	@Override
	public boolean matches(HitDto hit) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(hit.createTime);
		return dayOfWeek == cal.get(Calendar.DAY_OF_WEEK);
	}

}
