package at.mgl.transaktion.inhalt;

import java.time.LocalDate;
import java.util.Date;

public class MglTransaktionInhaltLocalDate implements IMglTransaktionInhalt {

	private LocalDate inhalt;
	
	public MglTransaktionInhaltLocalDate(LocalDate inhalt) {
		this.inhalt = inhalt;
	}
	
	@Override
	public String inhaltToString() {
		return this.inhalt.toString();
	}

	public LocalDate getInhalt() {
		return inhalt;
	}

	public void setInhalt(LocalDate inhalt) {
		this.inhalt = inhalt;
	}
}
