package probability;

import java.util.Calendar;

import collections.Hits;
import dtos.HitDto;
import dtos.VenueDto;

public class DayOfWeekProbability extends Probability {

	private int mDayOfWeek;
	
	public DayOfWeekProbability(VenueDto venue, int dayOfWeek) {
		super(venue);
		mDayOfWeek = dayOfWeek;
	}
	
	@Override
	public boolean matches(HitDto hit) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(hit.createTime);
		return mDayOfWeek == cal.get(Calendar.DAY_OF_WEEK);
	}

}
