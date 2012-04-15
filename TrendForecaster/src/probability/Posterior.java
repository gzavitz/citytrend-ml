package probability;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;


import dtos.VenueDto;

public class Posterior implements Comparator<Posterior>{
	public VenueDto venue;
	public BigDecimal posterior = new BigDecimal("1");
	NumberFormat formatter = new DecimalFormat("#0.0000000000");
	
	public String toString() {
		return String.format("%s " + formatter.format(posterior).toString(), venue.getVenueName());
	}

	@Override
	public int compare(Posterior o1, Posterior o2) {
		return o1.posterior.compareTo(o2.posterior);
	}

}
