package at.mgl.transaktion;

import at.mgl.position.ZustandAnteilsblock;

public enum MglTransaktionsTyp {
	
	/* Transaktionstypen für Genossenschaften */
	GenBezeichnung { @Override public String positionsTyp() {return "Genossenschaft";}},
	GenAnteilshoehe { @Override public String positionsTyp() {return "Genossenschaft";}},
	
	/* Transaktionstypen für Mitglieder */
	MglMitgliedsnummer { @Override public String positionsTyp() {return "Mitglied";}},
	MglBeitritt { @Override public String positionsTyp() {return "Mitglied";}},
	MglAustritt { @Override public String positionsTyp() {return "Mitglied";}},
	MglVorname { @Override public String positionsTyp() {return "Mitglied";}},
	MglNachname { @Override public String positionsTyp() {return "Mitglied";}},
	
	/* Transaktionstypen für Anteilsblöcke */
	Zeichnung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Kuendigung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Auszahlungssperrfrist { @Override public String positionsTyp() {return "Anteilsblock";}},
	Einzahlung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Auszahlung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Uebertrag { @Override public String positionsTyp() {return "Anteilsblock";}};
	
	public abstract String positionsTyp();
	
}
