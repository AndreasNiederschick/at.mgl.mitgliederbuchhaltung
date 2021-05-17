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
import at.mgl.transaktion.inhalt.MglTransaktionInhaltLocalDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class Mitglied implements Position{
	
	/* Variablen zum festschreiben von Systemereignissen */
	private Genossenschaft genossenschaft = null;
	private UUID mitgliedID = null;
	
	/* Variablen mit Mitliederstammdaten*/
	private int mitgliedsNummer = 0;
	private String vorname = "";
	private String nachname = "";
	private LocalDate beitrittsdatum = LocalDate.of(1000,1,1);
	private LocalDate austrittsdatum = LocalDate.of(1000,1,1);

	
	/* Liste der Anteile zum Miglied */
	//private List<Anteilsblock> anteile;
	private Map<UUID,Anteilsblock> anteilsbloecke = new HashMap<UUID,Anteilsblock>();
	
	/* Liste der Transaktionen zum Mitglied */
	private List<MglTransaktion> transaktionen = new ArrayList<MglTransaktion>();
	
	/* Im Standardfall soll die erstellten Transaktionen persistiert werden.
	 * Für Tests aber zB nicht. Dann kann auf false gestellt werden.
	 */
	private boolean doPersist = true;
	
	// ---------------------------------------------------------------------------------
	// Konstruktoren 
	// ---------------------------------------------------------------------------------
	
	public Mitglied(Genossenschaft genossenschaft) {
		this.genossenschaft = genossenschaft;
		this.mitgliedID = UUID.randomUUID();
	}
	
	public Mitglied(UUID mitgliedID) {
		this.genossenschaft = null;
		this.mitgliedID = mitgliedID;
	}
	
	public Mitglied(Genossenschaft gen,UUID mitgliedID) {
		this.genossenschaft = gen;
		this.mitgliedID = mitgliedID;
	}
	
	// Methoden aus Interface IPosition
	
	@Override
	public void aufrollen() {
		this.aufrollenPer(LocalDate.of(9999,12,31));
	}

	@Override
	public void aufrollenPer(LocalDate datumPer) {
		
		for (MglTransaktion mglTransaktion : this.transaktionen) {
			
			if (mglTransaktion.getMglDatumTransaktion().isBefore(datumPer)) {
				
				switch(mglTransaktion.getMglTransaktionsTyp()) {
				
				case MitgliedMitgliedsnummer:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.mitgliedsNummer = ((MglTransaktionInhaltInteger)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MitgliedBeitritt:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.beitrittsdatum = ((MglTransaktionInhaltLocalDate)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MitgliedAustritt:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.austrittsdatum = ((MglTransaktionInhaltLocalDate)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MitgliedVorname:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.vorname = ((MglTransaktionInhaltString)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MitgliedNachname:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.nachname = ((MglTransaktionInhaltString)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				default: break;
				}	
			}		
		}
	}
	
	public List<Anteilsblock> getAnteilsblockListe () {
		return new ArrayList<Anteilsblock>(this.anteilsbloecke.values());
	}
	
	public MglTransaktion  mitgliedAnlegenTransaktion(LocalDate datum)  {
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied (
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MitgliedAnlage
				,new MglTransaktionInhaltString(this.mitgliedID.toString())
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	
	// GETTER & SETTER die Transaktionen erstellen	
	public MglTransaktion setMitgliedsNummerTransaktionPer(int mitgliedsNummer, LocalDate datum) {
		
		this.mitgliedsNummer = mitgliedsNummer;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
						this.genossenschaft.getGenossenschaftID()
						,this.mitgliedID
						,datum
						,MglTransaktionsTyp.MitgliedMitgliedsnummer
						,new MglTransaktionInhaltInteger(mitgliedsNummer)
						,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion setMglNummerTransaktion(int mitgliedsNummer) {
		
		return this.setMitgliedsNummerTransaktionPer(mitgliedsNummer, LocalDate.now());
		
	}
	
	public MglTransaktion setVornameTransaktionPer(String vorname,LocalDate datum) {
		
		this.vorname = vorname;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MitgliedVorname
				,new MglTransaktionInhaltString(vorname)
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion setVornameTransaktion(String vorname) {
		
		return this.setVornameTransaktionPer(vorname, LocalDate.now());
		
	}
	
	public MglTransaktion setNachnameTransaktionPer(String nachname,LocalDate datum) {
		
		this.nachname = nachname;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MitgliedNachname
				,new MglTransaktionInhaltString(nachname)
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion setNachnameTransaktion(String nachname) {
		
		return this.setNachnameTransaktionPer(nachname, LocalDate.now());
		
	}
	
	public MglTransaktion  setBeitrittsdatumTransaktionPer(LocalDate beitrittsdatum,LocalDate datum) {
		
		this.beitrittsdatum = beitrittsdatum;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MitgliedBeitritt
				,new MglTransaktionInhaltLocalDate(beitrittsdatum)
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion  setBeitrittsdatumTransaktion(LocalDate beitrittsdatum) {
		
		return this.setBeitrittsdatumTransaktionPer(beitrittsdatum, LocalDate.now());
		
	}
	
	public MglTransaktion  setAustrittsdatumTransaktionPer(LocalDate austrittsdatum, LocalDate datum) {
		
		this.austrittsdatum = austrittsdatum;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MitgliedAustritt
				,new MglTransaktionInhaltLocalDate(austrittsdatum)
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion  setAustrittsdatumTransaktion(LocalDate austrittsdatum) {
		
		return this.setAustrittsdatumTransaktionPer(austrittsdatum, LocalDate.now());
		
	}
	
	public Anteilsblock neuerAnteilsblock () {
		
		return this.neuerAnteilsblock(UUID.randomUUID());
	}
	public Anteilsblock neuerAnteilsblock (UUID anteilID) {
		
		Anteilsblock ret = new Anteilsblock(genossenschaft, this, anteilID);
		
		this.anteilsbloecke.put(ret.getMglAnteilsblockID(),ret);
		
		if(!this.doPersist) {
			ret.setDoPersist(this.doPersist);
			ret.anteilsblockAnlegenTransaktion(LocalDate.now());
		}
		
		return ret;
	}

	public UUID getMitgliedID() {
		return mitgliedID;
	}

	public void setMitgliedID(UUID mitgliedID) {
		this.mitgliedID = mitgliedID;
	}

	public int getMitgliedsNummer() {
		return mitgliedsNummer;
	}

	public void setMitgliedsNummer(int mglNummer) {
		this.mitgliedsNummer = mglNummer;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public LocalDate getBeitrittsdatum() {
		return this.beitrittsdatum;
	}

	public void setBeitrittsdatum(LocalDate beitrittsdatum) {
		this.beitrittsdatum = beitrittsdatum;
	}

	public LocalDate getAustrittsdatum() {
		return this.austrittsdatum;
	}

	public void setAustrittsdatum(LocalDate austrittsdatum) {
		this.austrittsdatum = austrittsdatum;
	}

	public List<MglTransaktion> getTransaktionen() {
		return transaktionen;
	}

	public void setTransaktionen(List<MglTransaktion> transaktionen) {
		this.transaktionen = transaktionen;
	}

	public Genossenschaft getGen() {
		return genossenschaft;
	}

	public void setGen(Genossenschaft gen) {
		this.genossenschaft = gen;
	}

	public Map<UUID, Anteilsblock> getAnteile() {
		return anteilsbloecke;
	}

	public void setAnteile(Map<UUID, Anteilsblock> anteile) {
		this.anteilsbloecke = anteile;
	}
	
	public boolean isDoPersist() {
		return doPersist;
	}

	public void setDoPersist(boolean doPersist) {
		this.doPersist = doPersist;
	}

}
