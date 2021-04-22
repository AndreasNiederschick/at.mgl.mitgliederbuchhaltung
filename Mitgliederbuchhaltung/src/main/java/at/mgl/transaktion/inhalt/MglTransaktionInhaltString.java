package at.mgl.transaktion.inhalt;

import java.util.Date;

public class MglTransaktionInhaltString implements IMglTransaktionInhalt  {

	private String inhalt;
	
	public MglTransaktionInhaltString(String inhalt) {
		super();
		this.inhalt = inhalt;
	}
	
	@Override
	public String inhaltToString() {
		return this.inhalt;
	}

	public String getInhalt() {
		return inhalt;
	}

	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}	
}
