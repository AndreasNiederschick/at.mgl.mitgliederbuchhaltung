package at.mgl.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import at.mgl.position.Anteilsblock;
import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;
import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionFactory;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;

public class TestSzenario {
	
	/*
	 * Das Standardszenario beinhaltet:
	 * - Einen Beitritt
	 * - Eine erste Zeichnung
	 * - Eine Nachzeichnung
	 * - Eine Kündigung
	 * - Eine Verschiebung auf Auszahlungssperrfrist
	 * - Ein Storno der Nachzeichnung
	 * - Einen Austritt
	 */
	public static Genossenschaft szenarioStandard() {
		
        Genossenschaft gen = new Genossenschaft();
        gen.setBezeichnungTransaktion("Test Genossenschaft szenarioStandard");
        gen.setAnteilshoeheTransaktion(10.0);
		
		Mitglied mgl1 = new Mitglied (gen,UUID.randomUUID());
		gen.addMitglied(mgl1);
		mgl1.setMglNummerTransaktion(1);
		mgl1.setBeitrittsdatumTransaktion(LocalDate.of(2020,01,01));
		mgl1.setBeitrittsdatumTransaktion(LocalDate.of(2020,11,11));
		mgl1.setAustrittsdatum(LocalDate.of(2021,12,12));
		mgl1.setVornameTransaktion("MGL1_Vorname");
		mgl1.setNachnameTransaktion("MGL1_Nachname");
		
		Anteilsblock ant1 = mgl1.neuerAnteilsblock();
		
		ant1.zeichnen(LocalDate.of(2021,04,01), new MglTransaktionInhaltInteger(4));
		ant1.zeichnen(LocalDate.of(2021,04,02), new MglTransaktionInhaltInteger(5));
		
		ant1.kuendigen(LocalDate.of(2021,04,10));
		ant1.kuendigen(LocalDate.of(2021,04,11));
		
		MglTransaktion t = ant1.auszahlungssperrfrist(LocalDate.of(2021,04,20));
		
		ant1.stornieren(t);
		
		ant1.auszahlungssperrfrist(LocalDate.of(2021,04,20));
		ant1.auszahlen(LocalDate.of(2021,04,30));
		ant1.auszahlen(LocalDate.of(2021,04,29));
		ant1.zeichnen(LocalDate.of(2021,04,28), new MglTransaktionInhaltInteger(3));
			
		ant1.aufrollen();
		
		Anteilsblock ant2 = mgl1.neuerAnteilsblock();
		ant2.zeichnen(LocalDate.of(2019,04,01), new MglTransaktionInhaltInteger(8));
		ant2.kuendigen(LocalDate.of(2019,01,20));
		
		Anteilsblock ant3 = mgl1.neuerAnteilsblock();
		ant2.zeichnen(LocalDate.of(2018,05,05), new MglTransaktionInhaltInteger(20));
		
		return gen;
	}
	
	/*
	 * Generierung von vielen Migliedern und Anteilen für Lasttests
	 */
	public static Genossenschaft szenarioLasttest(int AnzahlMitglieder, int AnzahlAnteileJeMitglied) {
		
		System.out.println("Lasttest Start um " + LocalDateTime.now());
		System.out.println("Anzahl Mitglieder: " + AnzahlMitglieder);
		System.out.println("Anzahl Anteile je Mitglied: " + AnzahlAnteileJeMitglied);
		
        Genossenschaft gen = new Genossenschaft();
        gen.setBezeichnungTransaktion("Test Genossenschaft szenarioLasttest");
        gen.setAnteilshoeheTransaktion(10.0);
		
		Mitglied tmpMgl;
		Anteilsblock tmpAnt;
		
		
		for (int i = 0; i < AnzahlMitglieder; i++) {
			tmpMgl = gen.neuesMitglied();
			
			tmpMgl.setMglNummerTransaktion(i);
			tmpMgl.setBeitrittsdatumTransaktion(LocalDate.of(2020,01,01));
			tmpMgl.setAustrittsdatum(LocalDate.of(2021,12,12));
			tmpMgl.setVornameTransaktion("Vorname_" + i);
			tmpMgl.setNachnameTransaktion("Nachname" + i);
			
			for(int j = 0; j < AnzahlAnteileJeMitglied; j++) {
				tmpAnt = tmpMgl.neuerAnteilsblock();
				tmpAnt.zeichnen(LocalDate.now(), new MglTransaktionInhaltInteger(j));
				if (j % 3 == 0) {
					tmpAnt.kuendigen(LocalDate.now());
				}
				if (j % 6 == 0) {
					tmpAnt.auszahlungssperrfrist(LocalDate.now());
				}
				if (j % 9 == 0) {
					tmpAnt.auszahlen(LocalDate.now());
				}
			}
			
		}
		
		System.out.println("Lasttest Ende um " + LocalDateTime.now());
		
		return gen;
		
	}

}
