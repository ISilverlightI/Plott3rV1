package plott3r_V1;

import java.util.Arrays;
import java.util.List;

import lejos.hardware.motor.Motor;

public class Zahnradsatz implements IUebersetzung {

	private List<Zahnrad> zahnraeder;

	public Zahnradsatz(Zahnrad... zahnraeder) {
		this.zahnraeder = Arrays.asList(zahnraeder);
	}

	@Override
	public double getUebersetzungsverhaeltnis() {
		return (((double)(this.zahnraeder.get(0).getZaehne()))/((double)(this.zahnraeder.get(zahnraeder.size()-1).getZaehne())));
	}

	@Override
	public boolean isAntriebsUmkehrung() {
		return (zahnraeder.size()%2==0 ? true : false);
	}

}
