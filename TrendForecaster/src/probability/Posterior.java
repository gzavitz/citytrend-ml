package probability;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;


import dtos.VenueDto;

public class Posterior implements Comparable<Posterior>{
	public VenueDto venue;
	public BigDecimal posterior = new BigDecimal("1");
	NumberFormat formatter = new DecimalFormat("#0.0000000000");
	
	public String toString() {
		return String.format("%s " + formatter.format(posterior).toString(), venue.getVenueName());
	}
	
	@Override
	public int compareTo(Posterior o) {
		return posterior.compareTo(o.posterior);
	}

}
