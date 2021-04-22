package at.mgl.transaktion.inhalt;

import java.util.Date;

public class MglTransaktionInhaltInteger implements IMglTransaktionInhalt {

	private int inhalt;

	public MglTransaktionInhaltInteger(int value) {
		super();
		this.inhalt = value;
	}
	
	@Override
	public String inhaltToString() {
		return "" + this.inhalt;
	}

	public int getInhalt() {
		return inhalt;
	}

	public void setInhalt(int value) {
		this.inhalt = value;
	}

}
