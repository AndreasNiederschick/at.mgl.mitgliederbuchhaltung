package at.mgl.position;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionFactory;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class Mitglied implements Position{
	
	/* Variablen zum festschreiben von Systemereignissen */
	private Genossenschaft genossenschaft;
	private UUID mitgliedID;
	
	/* Variablen mit Mitliederstammdaten*/
	private int mitgliedsNummer;
	private String vorname;
	private String nachname;
	private Date beitrittsdatum;
	private Date austrittsdatum;

	
	/* Liste der Anteile zum Miglied */
	//private List<Anteilsblock> anteile;
	private Map<UUID,Anteilsblock> anteilsbloecke;
	
	/* Liste der Transaktionen zum Mitglied */
	private List<MglTransaktion> transaktionen;
	
	// ---------------------------------------------------------------------------------
	// Konstruktoren 
	// ---------------------------------------------------------------------------------
	
	public Mitglied(Genossenschaft genossenschaft) {
		super();
		this.genossenschaft = genossenschaft;
		this.mitgliedID = UUID.randomUUID();
		this.anteilsbloecke = new HashMap<UUID,Anteilsblock>();
		this.transaktionen = new ArrayList<MglTransaktion>();
		this.mitgliedsNummer = 0;
		this.vorname = "";
		this.nachname = "";
		this.beitrittsdatum = new Date(1800,1,1);
		this.austrittsdatum = new Date(1800,1,1);
	}
	
	public Mitglied(UUID mitgliedID) {
		super();
		this.genossenschaft = null;
		this.mitgliedID = mitgliedID;
		this.anteilsbloecke = new HashMap<UUID,Anteilsblock>();
		this.transaktionen = new ArrayList<MglTransaktion>();
		this.mitgliedsNummer = 0;
		this.vorname = "";
		this.nachname = "";
		this.beitrittsdatum = new Date(1800,1,1);
		this.austrittsdatum = new Date(1800,1,1);
	}
	
	public Mitglied(Genossenschaft gen,UUID mitgliedID) {
		super();
		this.genossenschaft = gen;
		this.mitgliedID = mitgliedID;
		this.anteilsbloecke = new HashMap<UUID,Anteilsblock>();
		this.transaktionen = new ArrayList<MglTransaktion>();
		this.mitgliedsNummer = 0;
		this.vorname = "";
		this.nachname = "";
		this.beitrittsdatum = new Date(1800,1,1);
		this.austrittsdatum = new Date(1800,1,1);
	}
	
	// Methoden aus Interface IPosition
	
	@Override
	public void aufrollen() {
		this.aufrollenPer(new Date(9999,31,12));
	}

	@Override
	public void aufrollenPer(Date datumPer) {
		
		for (MglTransaktion mglTransaktion : this.transaktionen) {
			
			if (mglTransaktion.getMglDatumTransaktion().before(datumPer)) {
				
				switch(mglTransaktion.getMglTransaktionsTyp()) {
				
				case MglMitgliedsnummer:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.mitgliedsNummer = ((MglTransaktionInhaltInteger)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MglBeitritt:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.beitrittsdatum = ((MglTransaktionInhaltDate)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MglAustritt:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.austrittsdatum = ((MglTransaktionInhaltDate)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MglVorname:
					if (mglTransaktion.isIstStornoTransaktion()) {
						System.out.println("Keine Stammdatenänderung bei Storno zu Transaktionstyp " + mglTransaktion.getMglTransaktionsTyp());
					} else {
						this.vorname = ((MglTransaktionInhaltString)mglTransaktion.getMglInhalt()).getInhalt();
					}
					break;
				
				case MglNachname:
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
		return new ArrayList(this.anteilsbloecke.values());
	}
	
	// GETTER & SETTER die Transaktionen erstellen	
	public MglTransaktion setMitgliedsNummerTransaktionPer(int mitgliedsNummer, Date datum) {
		
		this.mitgliedsNummer = mitgliedsNummer;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
						this.genossenschaft.getGenossenschaftID()
						,this.mitgliedID
						,datum
						,MglTransaktionsTyp.MglMitgliedsnummer
						,new MglTransaktionInhaltInteger(mitgliedsNummer));
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion setMglNummerTransaktion(int mitgliedsNummer) {
		
		return this.setMitgliedsNummerTransaktionPer(mitgliedsNummer, new Date());
		
	}
	
	public MglTransaktion setVornameTransaktionPer(String vorname,Date datum) {
		
		this.vorname = vorname;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MglVorname
				,new MglTransaktionInhaltString(vorname));
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion setVornameTransaktion(String vorname) {
		
		return this.setVornameTransaktionPer(vorname, new Date());
		
	}
	
	public MglTransaktion setNachnameTransaktionPer(String nachname,Date datum) {
		
		this.nachname = nachname;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MglNachname
				,new MglTransaktionInhaltString(nachname));
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion setNachnameTransaktion(String nachname) {
		
		return this.setNachnameTransaktionPer(nachname, new Date());
		
	}
	
	public MglTransaktion  setBeitrittsdatumTransaktionPer(Date beitrittsdatum,Date datum) {
		
		this.beitrittsdatum = beitrittsdatum;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MglBeitritt
				,new MglTransaktionInhaltDate(beitrittsdatum));
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion  setBeitrittsdatumTransaktion(Date beitrittsdatum) {
		
		return this.setBeitrittsdatumTransaktionPer(beitrittsdatum, new Date());
		
	}
	
	public MglTransaktion  setAustrittsdatumTransaktionPer(Date austrittsdatum, Date datum) {
		
		this.austrittsdatum = austrittsdatum;
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(
				this.genossenschaft.getGenossenschaftID()
				,this.mitgliedID
				,datum
				,MglTransaktionsTyp.MglAustritt
				,new MglTransaktionInhaltDate(austrittsdatum));
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	public MglTransaktion  setAustrittsdatumTransaktion(Date austrittsdatum) {
		
		return this.setAustrittsdatumTransaktionPer(austrittsdatum, new Date());
		
	}
	
	public Anteilsblock neuerAnteilsblock () {
		
		Anteilsblock ret = new Anteilsblock(genossenschaft, this);
		
		this.anteilsbloecke.put(ret.getMglAnteilsblockID(),ret);
		
		return ret;
	}
	public Anteilsblock neuerAnteilsblock (UUID anteilID) {
		
		Anteilsblock ret = new Anteilsblock(genossenschaft, this, anteilID);
		
		this.anteilsbloecke.put(ret.getMglAnteilsblockID(),ret);
		
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

	public Date getBeitrittsdatum() {
		return this.beitrittsdatum;
	}

	public void setBeitrittsdatum(Date beitrittsdatum) {
		this.beitrittsdatum = beitrittsdatum;
	}

	public Date getAustrittsdatum() {
		return this.austrittsdatum;
	}

	public void setAustrittsdatum(Date austrittsdatum) {
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
	
	

}
