package probability;

import java.util.Calendar;

import dtos.HitDto;

public class TimeOfDayProbability extends Probability {

	private int mStartHour, mStopHour;
	
	public TimeOfDayProbability(int startHour, int stopHour) {
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
