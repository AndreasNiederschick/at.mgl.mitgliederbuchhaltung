package at.mgl.transaktion.inhalt;

import java.util.Date;

public class MglTransaktionInhaltDate implements IMglTransaktionInhalt {

	private Date inhalt;
	
	public MglTransaktionInhaltDate(Date inhalt) {
		super();
		this.inhalt = inhalt;
	}
	
	@Override
	public String inhaltToString() {
		return this.inhalt.toString();
	}

	public Date getInhalt() {
		return inhalt;
	}

	public void setInhalt(Date inhalt) {
		this.inhalt = inhalt;
	}
}
