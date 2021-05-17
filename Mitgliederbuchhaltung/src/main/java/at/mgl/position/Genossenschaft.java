package at.mgl.position;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionFactory;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDouble;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class Genossenschaft implements Position{
	
	/* Variablen zum festschreiben von Systemereignissen */
	protected UUID genossenschaftID = null;
	
	/* Variablen mit Genossenschaftsstammdaten*/
	private String bezeichnung = "";
	private double anteilshoehe = 0;
	
	/* Liste der Mitglieder zur Genossenschaft */
	//private List<Mitglied> mitglieder;
	private Map<UUID,Mitglied> mitglieder = new HashMap<UUID,Mitglied>();
	
	/* Liste der Transaktionen zur Genossenschaft */
	private List<MglTransaktion> transaktionen = new ArrayList<MglTransaktion>();
	
	/* Im Standardfall soll die erstellten Transaktionen persistiert werden.
	 * Für Tests aber zB nicht. Dann kann auf false gestellt werden.
	 */
	private boolean doPersist = true;
	
	// ---------------------------------------------------------------------------------
	// Konstruktoren 
	// ---------------------------------------------------------------------------------
	public Genossenschaft () {
		
		this.genossenschaftID = UUID.randomUUID();
	
	}
	
	public Genossenschaft (UUID uuid) {
		
		this.genossenschaftID = uuid;
	
	}

	/*
	 * Methoden
	 */
	
	public Mitglied neuesMitglied() {
		
		Mitglied ret = new Mitglied(this,UUID.randomUUID());
		
		this.mitglieder.put(ret.getMitgliedID(),ret);
		
		return ret;
	}
	
	public Mitglied neuesMitglied(UUID mglID) {
		
		Mitglied ret = new Mitglied(this,mglID);
		
		this.mitglieder.put(ret.getMitgliedID(),ret);
		
		return ret;
	}
	
	public void addMitglied (Mitglied mgl) {
		
		this.mitglieder.put(mgl.getMitgliedID(),mgl);
		
	}
	
	// Methoden aus dem Interface IPosition
	
	@Override
	public void aufrollen() {
		
		this.aufrollenPer(LocalDate.of(9999,12,31));
		
	}

	@Override
	public void aufrollenPer(LocalDate datumPer) {
		
		for (MglTransaktion mglTransaktion : this.transaktionen) {
			
			if (mglTransaktion.getMglDatumTransaktion().isBefore(datumPer)) {
				
				switch(mglTransaktion.getMglTransaktionsTyp()) {
				
				case GenossenschaftBezeichnung:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.bezeichnung = ((MglTransaktionInhaltString)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case GenossenschaftAnteilshoehe:
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
		return new ArrayList<Mitglied>(this.mitglieder.values());
	}
	
	// GETTER & SETTER die Transaktionen erstellen
	public MglTransaktion  setBezeichnungTransaktionPer(String bezeichnung,LocalDate datum)  {
		
		this.bezeichnung = bezeichnung;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(
				this.genossenschaftID
				,datum
				,MglTransaktionsTyp.GenossenschaftBezeichnung
				,new MglTransaktionInhaltString(this.bezeichnung)
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion  setBezeichnungTransaktion(String bezeichnung)  {
		return this.setBezeichnungTransaktionPer(bezeichnung, LocalDate.now());
	}
	
	public MglTransaktion  setAnteilshoeheTransaktionPer(double anteilshoehe, LocalDate datum)  {
		
		this.anteilshoehe = anteilshoehe;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(
				this.genossenschaftID
				,datum
				,MglTransaktionsTyp.GenossenschaftAnteilshoehe
				,new MglTransaktionInhaltDouble(this.anteilshoehe)
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion  setAnteilshoeheTransaktion(double anteilshoehe)  {
		return this.setAnteilshoeheTransaktionPer(anteilshoehe, LocalDate.now());
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
		this.bezeichnung = bezeichnung;
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
	
	public boolean isDoPersist() {
		return doPersist;
	}

	public void setDoPersist(boolean doPersist) {
		this.doPersist = doPersist;
	}

}
