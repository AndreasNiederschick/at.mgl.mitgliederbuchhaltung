package at.mgl.transaktion.inhalt;

import java.util.Date;

public class MglTransaktionInhaltDouble implements IMglTransaktionInhalt {
	
	private double inhalt;

	public MglTransaktionInhaltDouble(double value) {
		super();
		this.inhalt = value;
	}
	
	@Override
	public String inhaltToString() {
		return "" + this.inhalt;
	}

	public double getInhalt() {
		return inhalt;
	}

	public void setInhalt(int value) {
		this.inhalt = value;
	}
	
}
