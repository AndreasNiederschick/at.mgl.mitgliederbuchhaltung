package at.mgl.position;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionFactory;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.IMglTransaktionInhalt;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class Anteilsblock implements Position {
	
	private Genossenschaft genossenschaft = null;
	private Mitglied mitglied = null;
	private UUID anteilsblockID = null;
	
	private LocalDateTime tstZeichnung = null;
	private LocalDateTime tstKuendigung = null;
	private LocalDateTime tstAuszahlungssperrfrist = null;
	private LocalDateTime tstAuszahlung = null;
	
	private ZustandAnteilsblock zustand = ZustandAnteilsblock.Start;
	
	private int menge = 0;

	private long anteilshoehe = 0;
	private long eingezahlt = 0;
	private boolean hatHaftung = false;
	
	/* Im Standardfall soll die erstellten Transaktionen persistiert werden.
	 * Für Tests aber zB nicht. Dann kann auf false gestellt werden.
	 */
	private boolean doPersist = true;
	
	/* Liste der Transaktionen zum Anteil */
	private List<MglTransaktion> transaktionen = new ArrayList<MglTransaktion>();
	
	public Anteilsblock(Genossenschaft genossenschaft, Mitglied mitglied) {
		this.genossenschaft = genossenschaft;
		this.mitglied = mitglied;
		this.anteilsblockID = UUID.randomUUID();
	}
	
	public Anteilsblock(UUID anteilsblockID) {
		this.genossenschaft = null;
		this.mitglied = null;
		this.anteilsblockID = anteilsblockID;
	}
	
	public Anteilsblock(Genossenschaft genossenschaft, Mitglied mitglied, UUID anteilsblockID) {
		super();
		this.genossenschaft = genossenschaft;
		this.mitglied = mitglied;
		this.anteilsblockID = anteilsblockID;
	}
	
	public MglTransaktion  anteilsblockAnlegenTransaktion(LocalDate datum)  {
		
		MglTransaktion ret = MglTransaktionFactory.erstelleTransaktionAnteilsblock (
				this.genossenschaft.getGenossenschaftID()
				,this.mitglied.getMitgliedID()
				,this.anteilsblockID
				,datum
				,MglTransaktionsTyp.MitgliedAnlage
				,new MglTransaktionInhaltString(this.anteilsblockID.toString())
				,this.doPersist);
		
		this.transaktionen.add(ret);
		
		return ret;
	}
	
	public MglTransaktion zeichnen (LocalDate mglDatumTransaktion,IMglTransaktionInhalt mglInhalt) {
		
		MglTransaktion ret = null;
		
		if (this.zustand != this.zustand.naechsterStatus(MglTransaktionsTyp.Zeichnung, false)) {
			
			//Transaktion erzeugen und in die Liste einhängen
			ret = MglTransaktionFactory.erstelleTransaktion(
					this.genossenschaft.getGenossenschaftID()
					,this.mitglied.getMitgliedID()
					,this.getMglAnteilsblockID()
					,mglDatumTransaktion
					,MglTransaktionsTyp.Zeichnung
					,mglInhalt
					,this.doPersist);
			
			//Zustand und Werte am Anteilsblock verändern
			this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Zeichnung, false);
			this.menge += ((MglTransaktionInhaltInteger)mglInhalt).getInhalt();
			this.tstZeichnung = ret.getMglTstAngelegt();
			
			this.transaktionen.add(ret);
			} else {
			System.out.println ("In Zustand " + this.zustand + " ist eine Zeichnung NICHT möglich!");
		}
		return ret;
	}
	
	public MglTransaktion kuendigen (LocalDate mglDatumTransaktion) {
		
		MglTransaktion ret = null;
		
		if (this.zustand != this.zustand.naechsterStatus(MglTransaktionsTyp.Kuendigung, false)) {
			
			//Transaktion erzeugen und in die Liste einhängen
			ret = MglTransaktionFactory.erstelleTransaktion(
					this.genossenschaft.getGenossenschaftID()
					,this.mitglied.getMitgliedID()
					,this.getMglAnteilsblockID()
					,mglDatumTransaktion
					,MglTransaktionsTyp.Kuendigung
					,new MglTransaktionInhaltInteger(this.menge)
					,this.doPersist);
					
			//Zustand und Werte am Anteilsblock verändern
			this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Kuendigung, false);
			this.tstKuendigung = ret.getMglTstAngelegt();
			
			this.transaktionen.add(ret);
		} else {
			//System.out.println ("In Zustand " + this.zustand + " ist eine Kündigung NICHT möglich!");
		}
		return ret;
	}
	
	public MglTransaktion auszahlungssperrfrist (LocalDate mglDatumTransaktion) {
		
		MglTransaktion ret = null;
		
		if (this.zustand != this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlungssperrfrist, false)) {
			
			//Transaktion erzeugen und in die Liste einhängen
			ret = MglTransaktionFactory.erstelleTransaktion(
					this.genossenschaft.getGenossenschaftID(),
					this.mitglied.getMitgliedID()
					,this.getMglAnteilsblockID()
					,mglDatumTransaktion
					,MglTransaktionsTyp.Auszahlungssperrfrist
					,new MglTransaktionInhaltInteger(this.menge)
					,this.doPersist);
					
			//Zustand und Werte am Anteilsblock verändern
			this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlungssperrfrist, false);
			this.tstAuszahlungssperrfrist = ret.getMglTstAngelegt();
			
			this.transaktionen.add(ret);
			
		} else {
			//System.out.println ("In Zustand " + this.zustand + " ist eine Auszahlungssperrfrist NICHT möglich!");
		}
		return ret;
	}

	public MglTransaktion auszahlen(LocalDate mglDatumTransaktion) {
		
		MglTransaktion ret = null;
		
		if (this.zustand != this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlung, false)) {
			
			//Transaktion erzeugen und in die Liste einhängen
			ret = MglTransaktionFactory.erstelleTransaktion(
					this.genossenschaft.getGenossenschaftID()
					,this.mitglied.getMitgliedID()
					,this.getMglAnteilsblockID()
					,mglDatumTransaktion
					,MglTransaktionsTyp.Auszahlung
					,new MglTransaktionInhaltInteger(this.menge)
					,this.doPersist);
							
			//Zustand und Werte am Anteilsblock verändern
			this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlung, false);
			this.tstAuszahlung = ret.getMglTstAngelegt();
			
			this.transaktionen.add(ret);
			
		} else {
			//System.out.println ("In Zustand " + this.zustand + " ist eine Auszahlung NICHT möglich!");
		}
		return ret;
	}
	
	public MglTransaktion stornieren (MglTransaktion mglTransaktion) {
		
		MglTransaktion ret = null;
		
		if (mglTransaktion != null
				&& !mglTransaktion.isIstStornoTransaktion()
				&& this.zustand != this.zustand.naechsterStatus(mglTransaktion.getMglTransaktionsTyp(), true)
				) {
			
			//Transaktion erzeugen und in die Liste einhängen
			ret = mglTransaktion.stornieren(this.doPersist);
			this.transaktionen.add(ret);
			
			//Zustand und Werte am Anteilsblock verändern
			this.zustand = this.zustand.naechsterStatus(mglTransaktion.getMglTransaktionsTyp(), true);
			
			//System.out.println("Transaktion wurde storniert " + mglTransaktion.getMglTransaktionID());
			
		} else {
			//System.out.println ("In Zustand " + this.zustand + " ist eine Auszahlung NICHT möglich!");
		}
		return ret;
	}
	
	public void aufrollen() {
		this.aufrollenPer(LocalDate.of(9999,12,31));
	}
	
	@Override
	public void aufrollenPer(LocalDate datumPer) {
		
		for (MglTransaktion mglTransaktion : this.transaktionen) {
			
			if (mglTransaktion.getMglDatumTransaktion().isBefore(datumPer)) {
				
				switch(mglTransaktion.getMglTransaktionsTyp()) {
				
				case Zeichnung:
					if (mglTransaktion.isIstStornoTransaktion()) {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Zeichnung, true);
						this.menge -= ((MglTransaktionInhaltInteger)mglTransaktion.getMglInhalt()).getInhalt();
						this.tstZeichnung = null;
					} else {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Zeichnung, false);
						this.menge += ((MglTransaktionInhaltInteger)mglTransaktion.getMglInhalt()).getInhalt();
						this.tstZeichnung = mglTransaktion.getMglTstAngelegt();
					}
					break;
				
				case Kuendigung:
					if (mglTransaktion.isIstStornoTransaktion()) {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Kuendigung, true);
						this.tstKuendigung = null;
					} else {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Kuendigung, false);
						this.tstKuendigung = mglTransaktion.getMglTstAngelegt();
					}
					break;
				
				case Auszahlungssperrfrist:
					if (mglTransaktion.isIstStornoTransaktion()) {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlungssperrfrist, true);
						this.tstAuszahlungssperrfrist = null;
					} else {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlungssperrfrist, false);
						this.tstAuszahlungssperrfrist = mglTransaktion.getMglTstAngelegt();
					}
					break;
				
				case Auszahlung:
					if (mglTransaktion.isIstStornoTransaktion()) {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlung, true);
						this.tstAuszahlung = null;
					} else {
						this.zustand = this.zustand.naechsterStatus(MglTransaktionsTyp.Auszahlung, false);
						this.tstAuszahlung = mglTransaktion.getMglTstAngelegt();
					}
					break;
				default: break;
				}	
			}		
		}
	}
	
	public void printAnteilsblock () {
		System.out.println("###Anteil: " + this.getMglAnteilsblockID() + " #Zustand " + this.zustand + " #Menge " + this.menge + " #Eingezahlt " + this.eingezahlt + " #Anteilshöhe " + this.anteilshoehe + " #HatHaftung " + this.hatHaftung);
	}

	public UUID getMglAnteilsblockID() {
		return anteilsblockID;
	}

	public void setMglAnteilsblockID(UUID mglAnteilID) {
		this.anteilsblockID = mglAnteilID;
	}

	public LocalDateTime getTstZeichnung() {
		return tstZeichnung;
	}

	public void setTstZeichnung(LocalDateTime tstZeichnung) {
		this.tstZeichnung = tstZeichnung;
	}

	public LocalDateTime getTstKuendigung() {
		return tstKuendigung;
	}

	public void setTstKuendigung(LocalDateTime tstKuendigung) {
		this.tstKuendigung = tstKuendigung;
	}

	public LocalDateTime getTstAuszahlungssperrfrist() {
		return tstAuszahlungssperrfrist;
	}

	public void setTstAuszahlungssperrfrist(LocalDateTime tstAuszahlungssperrfrist) {
		this.tstAuszahlungssperrfrist = tstAuszahlungssperrfrist;
	}

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public long getAnteilshoehe() {
		return anteilshoehe;
	}

	public void setAnteilshoehe(long anteilshoehe) {
		this.anteilshoehe = anteilshoehe;
	}

	public long getEingezahlt() {
		return eingezahlt;
	}

	public void setEingezahlt(long eingezahlt) {
		this.eingezahlt = eingezahlt;
	}

	public boolean isHatHaftung() {
		return hatHaftung;
	}

	public void setHatHaftung(boolean hatHaftung) {
		this.hatHaftung = hatHaftung;
	}

	public Genossenschaft getGenossenschaft() {
		return genossenschaft;
	}

	public void setGenossenschaft(Genossenschaft genossenschaft) {
		this.genossenschaft = genossenschaft;
	}

	public Mitglied getMitglied() {
		return mitglied;
	}

	public void setMitglied(Mitglied mitglied) {
		this.mitglied = mitglied;
	}

	public LocalDateTime getTstAuszahlung() {
		return tstAuszahlung;
	}

	public void setTstAuszahlung(LocalDateTime tstAuszahlung) {
		this.tstAuszahlung = tstAuszahlung;
	}

	public ZustandAnteilsblock getZustand() {
		return zustand;
	}

	public void setZustand(ZustandAnteilsblock zustand) {
		this.zustand = zustand;
	}

	public List<MglTransaktion> getTransaktionen() {
		return transaktionen;
	}

	public void setTransaktionen(List<MglTransaktion> transaktionen) {
		this.transaktionen = transaktionen;
	}

	public boolean isDoPersist() {
		return doPersist;
	}

	public void setDoPersist(boolean doPersist) {
		this.doPersist = doPersist;
	}
	
}
