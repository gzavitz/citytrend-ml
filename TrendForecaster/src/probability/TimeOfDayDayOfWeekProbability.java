package probability;

import java.util.Calendar;

import collections.Hits;
import dtos.HitDto;
import dtos.VenueDto;

public class TimeOfDayDayOfWeekProbability extends Probability {

	private int mDayOfWeek, mStartHour, mStopHour;
	
	public TimeOfDayDayOfWeekProbability(VenueDto venue, int dayOfWeek, int startHour, int stopHour) {
		super(venue);
		mDayOfWeek = dayOfWeek;
		mStartHour = startHour;
		mStopHour = stopHour;
	}
	
	@Override
	public boolean matches(HitDto hit) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(hit.createTime);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour >= mStartHour && hour < mStopHour && mDayOfWeek == cal.get(Calendar.DAY_OF_WEEK);
	}

}
