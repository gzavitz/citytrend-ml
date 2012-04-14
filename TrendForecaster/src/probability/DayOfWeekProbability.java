package probability;

import java.util.Calendar;

import dtos.HitDto;

public class DayOfWeekProbability extends Probability {

	private int mDayOfWeek;
	
	public DayOfWeekProbability(int dayOfWeek) {
		mDayOfWeek = dayOfWeek;
	}
	
	@Override
	public boolean matches(HitDto hit) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(hit.createTime);
		return mDayOfWeek == cal.get(Calendar.DAY_OF_WEEK);
	}

}
