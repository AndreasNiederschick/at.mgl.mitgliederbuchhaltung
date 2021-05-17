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
		
        Genossenschaft genossenschaft = new Genossenschaft();
        genossenschaft.genossenschaftAnlegenTransaktion(LocalDate.now());
        genossenschaft.setBezeichnungTransaktion("Test Genossenschaft szenarioStandard");
        genossenschaft.setAnteilshoeheTransaktion(10.0);
		
        Mitglied mitglied1 = genossenschaft.neuesMitglied();
		mitglied1.setMglNummerTransaktion(1);
		mitglied1.setBeitrittsdatumTransaktion(LocalDate.of(2020,01,01));
		mitglied1.setBeitrittsdatumTransaktion(LocalDate.of(2020,11,11));
		mitglied1.setAustrittsdatum(LocalDate.of(2021,12,12));
		mitglied1.setVornameTransaktion("MGL1_Vorname");
		mitglied1.setNachnameTransaktion("MGL1_Nachname");
		
		Anteilsblock anteilsblock1 = mitglied1.neuerAnteilsblock();
		
		anteilsblock1.zeichnen(LocalDate.of(2021,04,01), new MglTransaktionInhaltInteger(4));
		anteilsblock1.zeichnen(LocalDate.of(2021,04,02), new MglTransaktionInhaltInteger(5));
		
		anteilsblock1.kuendigen(LocalDate.of(2021,04,10));
		anteilsblock1.kuendigen(LocalDate.of(2021,04,11));
		
		MglTransaktion t = anteilsblock1.auszahlungssperrfrist(LocalDate.of(2021,04,20));
		
		anteilsblock1.stornieren(t);
		
		anteilsblock1.auszahlungssperrfrist(LocalDate.of(2021,04,20));
		anteilsblock1.auszahlen(LocalDate.of(2021,04,30));
		anteilsblock1.auszahlen(LocalDate.of(2021,04,29));
		anteilsblock1.zeichnen(LocalDate.of(2021,04,28), new MglTransaktionInhaltInteger(3));
			
		anteilsblock1.aufrollen();
		
		Anteilsblock anteilsblock2 = mitglied1.neuerAnteilsblock();
		anteilsblock2.zeichnen(LocalDate.of(2019,04,01), new MglTransaktionInhaltInteger(8));
		anteilsblock2.kuendigen(LocalDate.of(2019,01,20));
		
		Anteilsblock anteilsblock3 = mitglied1.neuerAnteilsblock();
		anteilsblock2.zeichnen(LocalDate.of(2018,05,05), new MglTransaktionInhaltInteger(20));
		
		return genossenschaft;
	}
	
	/*
	 * Generierung von vielen Migliedern und Anteilen für Lasttests
	 */
	public static Genossenschaft szenarioLasttest(int AnzahlMitglieder, int AnzahlAnteileJeMitglied) {
		
		System.out.println("Lasttest Start um " + LocalDateTime.now());
		System.out.println("Anzahl Mitglieder: " + AnzahlMitglieder);
		System.out.println("Anzahl Anteile je Mitglied: " + AnzahlAnteileJeMitglied);
		
        Genossenschaft genossenschaft = new Genossenschaft();
        genossenschaft.genossenschaftAnlegenTransaktion(LocalDate.now());
        genossenschaft.setBezeichnungTransaktion("Test Genossenschaft szenarioLasttest");
        genossenschaft.setAnteilshoeheTransaktion(10.0);
		
		Mitglied tmpMitglied;
		Anteilsblock tmpAnteilsblock;
		
		
		for (int i = 0; i < AnzahlMitglieder; i++) {
			tmpMitglied = genossenschaft.neuesMitglied();
			
			tmpMitglied.setMglNummerTransaktion(i);
			tmpMitglied.setBeitrittsdatumTransaktion(LocalDate.of(2020,01,01));
			tmpMitglied.setAustrittsdatum(LocalDate.of(2021,12,12));
			tmpMitglied.setVornameTransaktion("Vorname_" + i);
			tmpMitglied.setNachnameTransaktion("Nachname" + i);
			
			for(int j = 0; j < AnzahlAnteileJeMitglied; j++) {
				tmpAnteilsblock = tmpMitglied.neuerAnteilsblock();
				tmpAnteilsblock.zeichnen(LocalDate.now(), new MglTransaktionInhaltInteger(j));
				if (j % 3 == 0) {
					tmpAnteilsblock.kuendigen(LocalDate.now());
				}
				if (j % 6 == 0) {
					tmpAnteilsblock.auszahlungssperrfrist(LocalDate.now());
				}
				if (j % 9 == 0) {
					tmpAnteilsblock.auszahlen(LocalDate.now());
				}
			}
			
		}
		
		System.out.println("Lasttest Ende um " + LocalDateTime.now());
		
		return genossenschaft;
		
	}

}
