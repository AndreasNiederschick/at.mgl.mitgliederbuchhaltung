package at.mgl.transaktion;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import at.mgl.transaktion.inhalt.IMglTransaktionInhalt;

public class MglTransaktion implements IMglTransaktion {

	/* Variablen zum festschreiben von Systemereignissen */
	private UUID mglTransaktionID;
	private Timestamp mglTstAngelegt;
	
	/* Variablen zur Identifikation der Zugehörigkeit zu anderen Entitäten */
	private UUID mglGenossenschaftID;
	private UUID mglMitgliedID;
	private UUID mglAnteilsblockID;
	
	/* Variablen zum Inhalt der Transaktion */
	private Date mglDatumTransaktion;
	private MglTransaktionsTyp mglTransaktionsTyp;
	//private Object mglTransaktionsWert;
	private IMglTransaktionInhalt mglInhalt;
	
	/* Variablen für Stornoinformationen */
	private Timestamp mglTstStorniert;
	private UUID mglTransaktionIDStorno;
	private boolean istStornoTransaktion;
	
	// ---------------------------------------------------------------------------------
	// Konstruktoren 
	// ---------------------------------------------------------------------------------
	/*
	 * Konstruktur zur Erzeugung von Transaktionen zu Genossenschaften
	 * - Mit GenossenschaftID
	 * - Ohne MitgliederID
	 * - Ohne AnteilID
	 */
	public MglTransaktion (UUID mglGenossenschaftID
							,Date mglDatumTransaktion
							,MglTransaktionsTyp mglTransaktionsTyp
							,IMglTransaktionInhalt mglInhalt) {
		super();
		this.mglGenossenschaftID = mglGenossenschaftID;
		this.mglMitgliedID = null;
		this.mglAnteilsblockID = null;
		
		this.initOhneReferenzIDs(mglDatumTransaktion, mglTransaktionsTyp, mglInhalt);
	}
	/*
	 * Konstruktur zur Erzeugung von Transaktionen zu Mitgliedern
	 * - Mit GenossenschaftID
	 * - Mit MitgliederID
	 * - Ohne AnteilID
	 */
	public MglTransaktion (UUID mglGenossenschaftID, UUID mglMitgliedID
							,Date mglDatumTransaktion
							,MglTransaktionsTyp mglTransaktionsTyp
							,IMglTransaktionInhalt mglInhalt) {
		super();
		this.mglGenossenschaftID = mglGenossenschaftID;
		this.mglMitgliedID =mglMitgliedID;
		this.mglAnteilsblockID = null;
		
		this.initOhneReferenzIDs(mglDatumTransaktion, mglTransaktionsTyp, mglInhalt);
	}
	
	/*
	 * Konstruktur zur Erzeugung von Transaktionen zu Anteilsblöcken
	 * - Mit GenossenschaftID
	 * - Mit MitgliederID
	 * - Mit AnteilID
	 */
	public MglTransaktion (UUID mglGenossenschaftID, UUID mglMitgliedID,UUID mglAnteilsblockID
							,Date mglDatumTransaktion
							,MglTransaktionsTyp mglTransaktionsTyp
							,IMglTransaktionInhalt mglInhalt) {
		super();
		
		this.mglGenossenschaftID = mglGenossenschaftID;
		this.mglMitgliedID =mglMitgliedID;
		this.mglAnteilsblockID = mglAnteilsblockID;
		
		this.initOhneReferenzIDs(mglDatumTransaktion, mglTransaktionsTyp, mglInhalt);
	}
	
	private void initOhneReferenzIDs(Date mglDatumTransaktion,MglTransaktionsTyp mglTransaktionsTyp,IMglTransaktionInhalt mglInhalt) {
		this.mglTransaktionID = UUID.randomUUID();
		this.mglTstAngelegt = new Timestamp(new Date().getTime());
		this.mglDatumTransaktion = mglDatumTransaktion;
		this.mglTransaktionsTyp = mglTransaktionsTyp;
		this.mglInhalt = mglInhalt;
		this.mglTstStorniert = null;
		this.mglTransaktionIDStorno = null;
		this.istStornoTransaktion = false;
	}
	

	 // Implementierung der Methoden aus dem Interface

	@Override
	public MglTransaktion stornieren() {
		// Stornotransaktion anlegen
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktion(this.mglGenossenschaftID, this.mglMitgliedID,this.mglAnteilsblockID,this.mglDatumTransaktion, this.mglTransaktionsTyp, this.mglInhalt);
		ret.setIstStornoTransaktion(true);
		ret.mglTstStorniert = this.mglTstAngelegt;
		ret.mglTransaktionIDStorno = this.mglTransaktionID;
		
		// Zu stornierende Transaktion mit den Werten der Stornotransaktion versorgen
		this.mglTstStorniert = ret.getMglTstAngelegt();
		this.mglTransaktionIDStorno = ret.getMglTransaktionID();
		
		return ret;
	}
	@Override
	public List<MglTransaktion> split() {
		// TODO Auto-generated method stubs
		return null;
	}
	@Override
	public String toHash() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void printMglTransaktion() {
		
		System.out.println("#mglTransaktionID: " + this.mglTransaktionID.toString()
					+ " #TransaktionsDatum: " + this.mglDatumTransaktion
					+ " #TransaktionsTyp : " + this.mglTransaktionsTyp
					+ " #istStorno : " + this.istStornoTransaktion
					+ " #inhalt: " + this.getMglInhalt().inhaltToString());
		/*
		if (this.mglTransaktionID != null) {
			System.out.println("mglTransaktionID: " + this.mglTransaktionID.toString());
		}
		else { System.out.println("mglTransaktionID: null"); }
		
		if (this.mglTstAngelegt != null) {
		System.out.println("mglTstAngelegt: " + this.mglTstAngelegt.toString());
		}
		else { System.out.println("mglTstAngelegt: null"); }
		
		if (this.mglMitgliedID != null) {
		System.out.println("mglMitgliedID: " + this.mglMitgliedID.toString());
		}
		else { System.out.println("mglMitgliedID: null"); }
		
		if (this.mglAnteilsblockID != null) {
			System.out.println("mglAnteilID: " + this.mglAnteilsblockID.toString());
		}
		else { System.out.println("mglAnteilID: null"); }
		
		if (this.mglTransaktionsTyp != null) {
			System.out.println("mglTransaktionsTyp: " + this.mglTransaktionsTyp);
		}
		else { System.out.println("mglTransaktionsTyp: null"); }
		
		if (this.mglTransaktionsWert != null) {
			System.out.println("mglTransaktionsWert: " + this.mglTransaktionsWert.toString());
		}
		else { System.out.println("mglTransaktionsWert: null"); }
		
		if (this.mglTstStorniert != null) {
			System.out.println("mglTstStorniert: " + this.mglTstStorniert.toString());
		}
		else { System.out.println("mglTstStorniert: null"); }
		
		if (this.mglTransaktionIDStorno != null) {
			System.out.println("mglTransaktionIDStorno: " + this.mglTransaktionIDStorno.toString());
		}
		else { System.out.println("mglTransaktionIDStorno: null"); }
		
		System.out.println("istStornoTransaktion: " + istStornoTransaktion);
		
		*/
		
	}
	
	// GETTER & SETTER

	public UUID getMglTransaktionID() {
		return mglTransaktionID;
	}

	public void setMglTransaktionID(UUID mglTransaktionID) {
		this.mglTransaktionID = mglTransaktionID;
	}

	public Timestamp getMglTstAngelegt() {
		return mglTstAngelegt;
	}

	public void setMglTstAngelegt(Timestamp mglTstAngelegt) {
		this.mglTstAngelegt = mglTstAngelegt;
	}

	public UUID getMglMitgliedID() {
		return mglMitgliedID;
	}

	public void setMglMitgliedID(UUID mglMitgliedID) {
		this.mglMitgliedID = mglMitgliedID;
	}

	public UUID getMglAnteilID() {
		return mglAnteilsblockID;
	}

	public void setMglAnteilID(UUID mglAnteilID) {
		this.mglAnteilsblockID = mglAnteilID;
	}

	public MglTransaktionsTyp getMglTransaktionsTyp() {
		return mglTransaktionsTyp;
	}

	public void setMglTransaktionsTyp(MglTransaktionsTyp mglTransaktionsTyp) {
		this.mglTransaktionsTyp = mglTransaktionsTyp;
	}

	public Timestamp getMglTstStorniert() {
		return mglTstStorniert;
	}

	public void setMglTstStorniert(Timestamp mglTstStorniert) {
		this.mglTstStorniert = mglTstStorniert;
	}

	public UUID getMglTransaktionIDStorno() {
		return mglTransaktionIDStorno;
	}

	public void setMglTransaktionIDStorno(UUID mglTransaktionIDStorno) {
		this.mglTransaktionIDStorno = mglTransaktionIDStorno;
	}

	public boolean isIstStornoTransaktion() {
		return istStornoTransaktion;
	}

	public void setIstStornoTransaktion(boolean istStornoTransaktion) {
		this.istStornoTransaktion = istStornoTransaktion;
	}

	public UUID getMglGenossenschaftID() {
		return mglGenossenschaftID;
	}

	public void setMglGenossenschaftID(UUID mglGenossenschaftID) {
		this.mglGenossenschaftID = mglGenossenschaftID;
	}

	public Date getMglDatumTransaktion() {
		return mglDatumTransaktion;
	}

	public void setMglDatumTransaktion(Date mglDatumTransaktion) {
		this.mglDatumTransaktion = mglDatumTransaktion;
	}
	public UUID getMglAnteilsblockID() {
		return mglAnteilsblockID;
	}
	public void setMglAnteilsblockID(UUID mglAnteilsblockID) {
		this.mglAnteilsblockID = mglAnteilsblockID;
	}
	public IMglTransaktionInhalt getMglInhalt() {
		return mglInhalt;
	}
	public void setMglInhalt(IMglTransaktionInhalt mglInhalt) {
		this.mglInhalt = mglInhalt;
	}
	
	
	
}
