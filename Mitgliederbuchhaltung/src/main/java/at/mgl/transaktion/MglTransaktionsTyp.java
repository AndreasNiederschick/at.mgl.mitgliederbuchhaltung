package at.mgl.transaktion;

import at.mgl.position.ZustandAnteilsblock;

public enum MglTransaktionsTyp {
	
	/* Transaktionstypen für Genossenschaften */
	GenossenschaftAnlage { @Override public String positionsTyp() {return "Genossenschaft";}},
	GenossenschaftBezeichnung { @Override public String positionsTyp() {return "Genossenschaft";}},
	GenossenschaftAnteilshoehe { @Override public String positionsTyp() {return "Genossenschaft";}},
	
	/* Transaktionstypen für Mitglieder */
	MitgliedAnlage { @Override public String positionsTyp() {return "Mitglied";}},
	MitgliedMitgliedsnummer { @Override public String positionsTyp() {return "Mitglied";}},
	MitgliedBeitritt { @Override public String positionsTyp() {return "Mitglied";}},
	MitgliedAustritt { @Override public String positionsTyp() {return "Mitglied";}},
	MitgliedVorname { @Override public String positionsTyp() {return "Mitglied";}},
	MitgliedNachname { @Override public String positionsTyp() {return "Mitglied";}},
	
	/* Transaktionstypen für Anteilsblöcke */
	AnteilsblockAnlage { @Override public String positionsTyp() {return "Anteilsblock";}},
	Zeichnung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Kuendigung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Auszahlungssperrfrist { @Override public String positionsTyp() {return "Anteilsblock";}},
	Einzahlung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Auszahlung { @Override public String positionsTyp() {return "Anteilsblock";}},
	Uebertrag { @Override public String positionsTyp() {return "Anteilsblock";}};
	
	public abstract String positionsTyp();
	
}
