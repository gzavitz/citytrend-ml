package probability;

import java.util.Calendar;

import collections.Hits;
import dtos.HitDto;
import dtos.VenueDto;

public class TimeOfDayProbability extends Probability {

	private int mStartHour, mStopHour;
	
	public TimeOfDayProbability(VenueDto venue, int startHour, int stopHour) {
		super(venue);
		mStartHour = startHour;
		mStopHour = stopHour;
	}

	@Override
	public boolean matches(HitDto hit) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(hit.createTime);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour >= mStartHour && hour < mStopHour;
	}
	
}
