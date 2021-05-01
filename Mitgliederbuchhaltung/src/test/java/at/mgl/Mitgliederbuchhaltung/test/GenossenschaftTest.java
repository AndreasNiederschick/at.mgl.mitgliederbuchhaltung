package at.mgl.Mitgliederbuchhaltung.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.mgl.position.Genossenschaft;
import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDouble;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class GenossenschaftTest {
	
	Genossenschaft gen;
	Date datumTest;
	
	@Before
	public void setUp() {
		gen = new Genossenschaft();
		datumTest = new Date(1900,1,1);
	}
	@After
	public void cleanUp() {
		
	}
	
	@Test
	public void shouldSetBezeichnungAndTransaktionPer() {
		String bezeichnung = "Testbezeichung";
		//MglTransaktion t = gen.setMitTransaktion(gen.getBezeichnung(), "Test Genossenschaft 1");
		MglTransaktion t = gen.setBezeichnungTransaktionPer(bezeichnung,datumTest);
		
		assertEquals("Genossenschaft Bezeichnung wurde nicht gesetzt"
				,bezeichnung
				, gen.getBezeichnung());
		assertEquals("Transaktion Genossenschaft Bezeichnung wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,1
				,gen.getTransaktionen().size());
		assertEquals("Transaktion Genossenschaft Bezeichnung hat nicht MglTransaktionsTyp GenBezeichnung"
				,MglTransaktionsTyp.GenBezeichnung
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Genossenschaft Bezeichnung hat nicht den angegebenen Wert " + bezeichnung
				,bezeichnung
				,((MglTransaktionInhaltString)t.getMglInhalt()).getInhalt());
		assertEquals("Transaktion Genossenschaft Bezeichnung hat nicht das korrekte Datum" + datumTest
				,datumTest
				,t.getMglDatumTransaktion());
	}
	@Test
	public void shouldSetBezeichnungAndTransaktion() {
		String bezeichnung = "Testbezeichung";
		//MglTransaktion t = gen.setMitTransaktion(gen.getBezeichnung(), "Test Genossenschaft 1");
		MglTransaktion t = gen.setBezeichnungTransaktion(bezeichnung);
		
		assertEquals("Genossenschaft Bezeichnung wurde nicht gesetzt"
				,bezeichnung
				, gen.getBezeichnung());
		assertEquals("Transaktion Genossenschaft Bezeichnung wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,1
				,gen.getTransaktionen().size());
		assertEquals("Transaktion Genossenschaft Bezeichnung hat nicht MglTransaktionsTyp GenBezeichnung"
				,MglTransaktionsTyp.GenBezeichnung
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Genossenschaft Bezeichnung hat nicht den angegebenen Wert " + bezeichnung
				,bezeichnung
				,((MglTransaktionInhaltString)t.getMglInhalt()).getInhalt());
	}
	
	@Test
	public void shouldSetAnteilshoeheAndTransaktionPer() {
		double anteilshoehe = 10.1;
		//MglTransaktion t = gen.setMitTransaktion(gen.getAnteilshoehe(), anteilshoehe);
		MglTransaktion t = gen.setAnteilshoeheTransaktionPer(anteilshoehe,datumTest);

		assertEquals("Genossenschaft Anteilshöhe wurde nicht gesetzt"
				,anteilshoehe
				, gen.getAnteilshoehe()
				,0.001);
		assertEquals("Transaktion Genossenschaft Anteilshöhe wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,1
				,gen.getTransaktionen().size());
		assertEquals("Transaktion Genossenschaft Anteilshöhe hat nicht MglTransaktionsTyp Gen1anteilshoehe"
				,MglTransaktionsTyp.GenAnteilshoehe
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Genossenschaft Anteilshöhe hat nicht den angegebenen Wert"
				,anteilshoehe
				,((MglTransaktionInhaltDouble)t.getMglInhalt()).getInhalt()
				,0.001);
		assertEquals("Transaktion Genossenschaft Anteilshöhe hat nicht das korrekte Datum" + datumTest
				,datumTest
				,t.getMglDatumTransaktion());
	}
	@Test
	public void shouldSetAnteilshoeheAndTransaktion() {
		double anteilshoehe = 10.1;
		//MglTransaktion t = gen.setMitTransaktion(gen.getAnteilshoehe(), anteilshoehe);
		MglTransaktion t = gen.setAnteilshoeheTransaktion(anteilshoehe);

		assertEquals("Genossenschaft Anteilshöhe wurde nicht gesetzt"
				,anteilshoehe
				, gen.getAnteilshoehe()
				,0.001);
		assertEquals("Transaktion Genossenschaft Anteilshöhe wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,1
				,gen.getTransaktionen().size());
		assertEquals("Transaktion Genossenschaft Anteilshöhe hat nicht MglTransaktionsTyp Gen1anteilshoehe"
				,MglTransaktionsTyp.GenAnteilshoehe
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Genossenschaft Anteilshöhe hat nicht den angegebenen Wert"
				,anteilshoehe
				,((MglTransaktionInhaltDouble)t.getMglInhalt()).getInhalt()
				,0.001);
	}

}
