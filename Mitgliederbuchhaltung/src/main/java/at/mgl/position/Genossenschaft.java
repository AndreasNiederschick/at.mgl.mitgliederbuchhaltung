package at.mgl.position;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.mgl.transaktion.IMglTransaktion;
import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionFactory;
import at.mgl.transaktion.MglTransaktionInfo;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDouble;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class Genossenschaft extends Position implements IPosition{
	
	/* Variablen zum festschreiben von Systemereignissen */
	protected UUID genossenschaftID;
	
	/* Variablen mit Genossenschaftsstammdaten*/
	@MglTransaktionInfo(mglTransaktionstyp = MglTransaktionsTyp.GenBezeichnung)
	private String bezeichnung;
	@MglTransaktionInfo(mglTransaktionstyp = MglTransaktionsTyp.GenAnteilshoehe)
	private double anteilshoehe;
	
	/* Liste der Mitglieder zur Genossenschaft */
	//private List<Mitglied> mitglieder;
	private Map<UUID,Mitglied> mitglieder;
	
	/* Liste der Transaktionen zur Genossenschaft */
	private List<MglTransaktion> transaktionen;
	
	// ---------------------------------------------------------------------------------
	// Konstruktoren 
	// ---------------------------------------------------------------------------------
	public Genossenschaft () {
		super();
		this.genossenschaftID = UUID.randomUUID();
		this.bezeichnung = "";
		this.anteilshoehe = 0;
		this.mitglieder = new HashMap<UUID,Mitglied>();
		this.transaktionen = new ArrayList<MglTransaktion>();
	}
	
	public Genossenschaft (UUID uuid) {
		super();
		this.genossenschaftID = uuid;
		this.bezeichnung = "";
		this.anteilshoehe = 0;
		this.mitglieder = new HashMap<UUID,Mitglied>();
		this.transaktionen = new ArrayList<MglTransaktion>();
	}

	/*
	 * Methoden
	 */
	
	public Mitglied neuesMitglied() {
		Mitglied ret = new Mitglied(this,UUID.randomUUID());
		this.mitglieder.put(ret.getMglMitgliedID(),ret);
		return ret;
	}
	
	public Mitglied neuesMitglied(UUID mglID) {
		Mitglied ret = new Mitglied(this,mglID);
		this.mitglieder.put(ret.getMglMitgliedID(),ret);
		return ret;
	}
	
	public boolean addMitglied (Mitglied mgl) {
		this.mitglieder.put(mgl.getMglMitgliedID(),mgl);
		return true;
	}
	
	// Methoden aus dem Interface IPosition
	
	@Override
	public void aufrollen() {
		this.aufrollenPer(new Date(9999,31,12));
		
	}

	@Override
	public void aufrollenPer(Date datumPer) {
		for (MglTransaktion mglTransaktion : this.transaktionen) {
			if (mglTransaktion.getMglDatumTransaktion().before(datumPer)) {
				switch(mglTransaktion.getMglTransaktionsTyp()) {
				case GenBezeichnung:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.bezeichnung = ((MglTransaktionInhaltString)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				case GenAnteilshoehe:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.anteilshoehe = ((MglTransaktionInhaltDouble)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				default: break;
				}	
			}
				
		}
		
	}
	
	public List<Mitglied> getMitgliederListe () {
		return new ArrayList(this.mitglieder.values());
	}
	
	// GETTER & SETTER die Transaktionen erstellen
	public MglTransaktion  setBezeichnungTransaktionPer(String bezeichnung,Date datum)  {
		this.bezeichnung = bezeichnung;
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(this.genossenschaftID,datum, MglTransaktionsTyp.GenBezeichnung, new MglTransaktionInhaltString(this.bezeichnung));
		this.transaktionen.add(ret);
		return ret;
	}
	public MglTransaktion  setBezeichnungTransaktion(String bezeichnung)  {
		return this.setBezeichnungTransaktionPer(bezeichnung, new Date());
	}
	
	public MglTransaktion  setAnteilshoeheTransaktionPer(double anteilshoehe, Date datum)  {
		this.anteilshoehe = anteilshoehe;
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(this.genossenschaftID,datum, MglTransaktionsTyp.GenAnteilshoehe, new MglTransaktionInhaltDouble(this.anteilshoehe));
		this.transaktionen.add(ret);
		return ret;
	}
	public MglTransaktion  setAnteilshoeheTransaktion(double anteilshoehe)  {
		return this.setAnteilshoeheTransaktionPer(anteilshoehe, new Date());
	}	
	
	public void print () {
		System.out.println("###Genossenschaft: " + this.getGenossenschaftID() + " #Bezeichnung " + this.getBezeichnung() + " #Anteilshöhe " + this.getAnteilshoehe()+ " #Anzahl Mitglieder " + this.getMitglieder().size());
	}
	
	/*
	 * Getter & Setter
	 */
	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		bezeichnung = bezeichnung;
	}

	public UUID getGenossenschaftID() {
		return genossenschaftID;
	}

	public void setGenossenschaftID(UUID mglGenossenschaftID) {
		this.genossenschaftID = mglGenossenschaftID;
	}

	public double getAnteilshoehe() {
		return anteilshoehe;
	}

	public void setAnteilshoehe(long anteilshoehe) {
		this.anteilshoehe = anteilshoehe;
	}

	public List<MglTransaktion> getTransaktionen() {
		return transaktionen;
	}

	public void setTransaktionen(List<MglTransaktion> transaktionen) {
		this.transaktionen = transaktionen;
	}

	public Map<UUID, Mitglied> getMitglieder() {
		return mitglieder;
	}

	public void setMitglieder(Map<UUID, Mitglied> mitglieder) {
		this.mitglieder = mitglieder;
	}

	public void setAnteilshoehe(double anteilshoehe) {
		this.anteilshoehe = anteilshoehe;
	}
	

}
