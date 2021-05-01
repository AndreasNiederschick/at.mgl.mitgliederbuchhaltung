package at.mgl.position;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionFactory;
import at.mgl.transaktion.MglTransaktionInfo;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class Mitglied extends Position implements IPosition{
	
	/* Variablen zum festschreiben von Systemereignissen */
	private Genossenschaft gen;
	private UUID mglMitgliedID;
	
	/* Variablen mit Mitliederstammdaten*/
	@MglTransaktionInfo(mglTransaktionstyp = MglTransaktionsTyp.MglMitgliedsnummer)
	private int mglNummer;
	@MglTransaktionInfo(mglTransaktionstyp = MglTransaktionsTyp.MglVorname)
	private String vorname;
	@MglTransaktionInfo(mglTransaktionstyp = MglTransaktionsTyp.MglNachname)
	private String nachname;
	@MglTransaktionInfo(mglTransaktionstyp = MglTransaktionsTyp.MglBeitritt)
	private Date beitrittsdatum;
	@MglTransaktionInfo(mglTransaktionstyp = MglTransaktionsTyp.MglAustritt)
	private Date austrittsdatum;

	
	/* Liste der Anteile zum Miglied */
	//private List<Anteilsblock> anteile;
	private Map<UUID,Anteilsblock> anteile;
	
	/* Liste der Transaktionen zum Mitglied */
	private List<MglTransaktion> transaktionen;
	
	// ---------------------------------------------------------------------------------
	// Konstruktoren 
	// ---------------------------------------------------------------------------------
	
	public Mitglied(Genossenschaft gen) {
		super();
		this.gen = gen;
		this.mglMitgliedID = UUID.randomUUID();
		this.anteile = new HashMap<UUID,Anteilsblock>();
		this.transaktionen = new ArrayList<MglTransaktion>();
		this.mglNummer = 0;
		this.vorname = "";
		this.nachname = "";
		this.beitrittsdatum = new Date(1800,1,1);
		this.austrittsdatum = new Date(1800,1,1);
	}
	
	public Mitglied(UUID mglMitgliedID) {
		super();
		this.gen = null;
		this.mglMitgliedID = UUID.randomUUID();
		this.anteile = new HashMap<UUID,Anteilsblock>();
		this.transaktionen = new ArrayList<MglTransaktion>();
		this.mglNummer = 0;
		this.vorname = "";
		this.nachname = "";
		this.beitrittsdatum = new Date(1800,1,1);
		this.austrittsdatum = new Date(1800,1,1);
	}
	
	public Mitglied(Genossenschaft gen,UUID mglMitgliedID) {
		super();
		this.gen = gen;
		this.mglMitgliedID = mglMitgliedID;
		this.anteile = new HashMap<UUID,Anteilsblock>();
		this.transaktionen = new ArrayList<MglTransaktion>();
		this.mglNummer = 0;
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
						this.mglNummer = ((MglTransaktionInhaltInteger)mglTransaktion.getMglInhalt()).getInhalt();
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
		return new ArrayList(this.anteile.values());
	}
	
	// GETTER & SETTER die Transaktionen erstellen	
	public MglTransaktion setMglNummerTransaktionPer(int mglNummer, Date datum) {
		this.mglNummer = mglNummer;
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(this.gen.getGenossenschaftID(), this.mglMitgliedID, datum, MglTransaktionsTyp.MglMitgliedsnummer, new MglTransaktionInhaltInteger(mglNummer));
		this.transaktionen.add(ret);
		return ret;
	}
	public MglTransaktion setMglNummerTransaktion(int mglNummer) {
		return this.setMglNummerTransaktionPer(mglNummer, new Date());
	}
	
	public MglTransaktion setVornameTransaktionPer(String vorname,Date datum) {
		this.vorname = vorname;
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(this.gen.getGenossenschaftID(), this.mglMitgliedID, datum, MglTransaktionsTyp.MglVorname, new MglTransaktionInhaltString(vorname));
		this.transaktionen.add(ret);
		return ret;
	}
	public MglTransaktion setVornameTransaktion(String vorname) {
		return this.setVornameTransaktionPer(vorname, new Date());
	}
	
	public MglTransaktion setNachnameTransaktionPer(String nachname,Date datum) {
		this.nachname = nachname;
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(this.gen.getGenossenschaftID(), this.mglMitgliedID, datum, MglTransaktionsTyp.MglNachname, new MglTransaktionInhaltString(nachname));
		this.transaktionen.add(ret);
		return ret;
	}
	public MglTransaktion setNachnameTransaktion(String nachname) {
		return this.setNachnameTransaktionPer(nachname, new Date());
	}
	
	public MglTransaktion  setBeitrittsdatumTransaktionPer(Date beitrittsdatum,Date datum) {
		this.beitrittsdatum = beitrittsdatum;
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(this.gen.getGenossenschaftID(),this.mglMitgliedID, datum, MglTransaktionsTyp.MglBeitritt, new MglTransaktionInhaltDate(beitrittsdatum));
		this.transaktionen.add(ret);
		return ret;
	}
	public MglTransaktion  setBeitrittsdatumTransaktion(Date beitrittsdatum) {
		return this.setBeitrittsdatumTransaktionPer(beitrittsdatum, new Date());
	}
	
	public MglTransaktion  setAustrittsdatumTransaktionPer(Date austrittsdatum, Date datum) {
		this.austrittsdatum = austrittsdatum;
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionMitglied(this.gen.getGenossenschaftID(), this.mglMitgliedID, datum, MglTransaktionsTyp.MglAustritt, new MglTransaktionInhaltDate(austrittsdatum));
		this.transaktionen.add(ret);
		return ret;
	}
	public MglTransaktion  setAustrittsdatumTransaktion(Date austrittsdatum) {
		return this.setAustrittsdatumTransaktionPer(austrittsdatum, new Date());
	}
	
	public Anteilsblock neuerAnteilsblock () {
		Anteilsblock ret = new Anteilsblock(gen, this);
		this.anteile.put(ret.getMglAnteilID(),ret);
		return ret;
	}
	public Anteilsblock neuerAnteilsblock (UUID anteilID) {
		Anteilsblock ret = new Anteilsblock(gen, this, anteilID);
		this.anteile.put(ret.getMglAnteilID(),ret);
		return ret;
	}

	public UUID getMglMitgliedID() {
		return mglMitgliedID;
	}

	public void setMglMitgliedID(UUID mglMitgliedID) {
		this.mglMitgliedID = mglMitgliedID;
	}

	public int getMglNummer() {
		return mglNummer;
	}

	public void setMglNummer(int mglNummer) {
		this.mglNummer = mglNummer;
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
		return gen;
	}

	public void setGen(Genossenschaft gen) {
		this.gen = gen;
	}

	public Map<UUID, Anteilsblock> getAnteile() {
		return anteile;
	}

	public void setAnteile(Map<UUID, Anteilsblock> anteile) {
		this.anteile = anteile;
	}
	
	

}
