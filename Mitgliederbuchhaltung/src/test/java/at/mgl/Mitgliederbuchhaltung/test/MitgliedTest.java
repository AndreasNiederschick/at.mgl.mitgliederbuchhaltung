package at.mgl.Mitgliederbuchhaltung.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;
import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltLocalDate;

public class MitgliedTest {
	
	Genossenschaft genossenschaft;
	Mitglied mitglied;
	LocalDate datumTest;
	
	@Before
	public void setUp() {
		genossenschaft = new Genossenschaft();
		genossenschaft.setDoPersist(false);
		
		mitglied = genossenschaft.neuesMitglied();
		mitglied.setDoPersist(false);
		
		datumTest = LocalDate.of(1900,1,1);
	}
	@After
	public void cleanUp() {

	}
		
	@Test
	public void testAufrollen() {
		
		//Mitglied mglAufroll = gen.neuesMitglied();
	}
		
	@Test
	public void shouldSetMglNummerAndTransaktion() {
		
		int mitgliedsNummer = 1000;
		MglTransaktion t = mitglied.setMglNummerTransaktion(1000);

		assertEquals("Mitgliedsnummer wurde nicht gesetzt"
				,mitgliedsNummer
				, mitglied.getMitgliedsNummer());
		assertEquals("Transaktion Mitgliedsnummer wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitgliedsnummer hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedMitgliedsnummer
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitgliedsnummer hat nicht den angegebenen Wert " + mitgliedsNummer
				,mitgliedsNummer
				,((MglTransaktionInhaltInteger)t.getMglInhalt()).getInhalt());
	}
	@Test
	public void shouldSetMglNummerAndTransaktionPer() {
		
		int mglNummer = 1000;
		MglTransaktion t = mitglied.setMitgliedsNummerTransaktionPer(1000,datumTest);

		assertEquals("Mitgliedsnummer wurde nicht gesetzt"
				,mglNummer
				, mitglied.getMitgliedsNummer());
		assertEquals("Transaktion Mitgliedsnummer wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitgliedsnummer hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedMitgliedsnummer
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitgliedsnummer hat nicht den angegebenen Wert " + mglNummer
				,mglNummer
				,((MglTransaktionInhaltInteger)t.getMglInhalt()).getInhalt());
		assertEquals("Transaktion Mitgliedsnummer hat nicht das korrekte Datum" + datumTest
				,datumTest
				,t.getMglDatumTransaktion());
	}
	@Test
	public void shouldSetVornameAndTransaktionPer() {
		
		String name = "Vorname_1";
		MglTransaktion t = mitglied.setVornameTransaktionPer(name,datumTest);

		assertEquals("Mitglied Vorname wurde nicht gesetzt"
				,name
				, mitglied.getVorname());
		assertEquals("Transaktion Mitglied Vorname wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Vorname hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedVorname
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Vorname hat nicht den angegebenen Wert " + name
				,name
				,((MglTransaktionInhaltString)t.getMglInhalt()).getInhalt());
		assertEquals("Transaktion Vorname hat nicht das korrekte Datum" + datumTest
				,datumTest
				,t.getMglDatumTransaktion());
	}
	@Test
	public void shouldSetVornameAndTransaktion() {
		
		String name = "Vorname_1";	
		MglTransaktion t = mitglied.setVornameTransaktion(name);

		assertEquals("Mitglied Vorname wurde nicht gesetzt"
				,name
				, mitglied.getVorname());
		assertEquals("Transaktion Mitglied Vorname wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Vorname hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedVorname
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Vorname hat nicht den angegebenen Wert " + name
				,name
				,((MglTransaktionInhaltString)t.getMglInhalt()).getInhalt());
	}
	@Test
	public void shouldSetNachnameAndTransaktionPer() {
		
		String nachname = "Nachname_1";
		MglTransaktion t = mitglied.setNachnameTransaktionPer(nachname,datumTest);

		assertEquals("Transaktion Mitglied Nachname wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Nachname hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedNachname
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Nachname hat nicht den angegebenen Wert " + nachname
				,nachname
				,((MglTransaktionInhaltString)t.getMglInhalt()).getInhalt());
		assertEquals("Transaktion Nachname hat nicht das korrekte Datum" + datumTest
				,datumTest
				,t.getMglDatumTransaktion());
		
	}
	@Test
	public void shouldSetNachnameAndTransaktion() {
		
		String nachname = "Nachname_1";
		MglTransaktion t = mitglied.setNachnameTransaktion(nachname);

		assertEquals("Transaktion Mitglied Nachname wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Nachname hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedNachname
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Nachname hat nicht den angegebenen Wert " + nachname
				,nachname
				,((MglTransaktionInhaltString)t.getMglInhalt()).getInhalt());
		
	}
	@Test
	public void shouldSetBeitrittsdatumAndTransaktionPer() {
		
		LocalDate beitritt = LocalDate.of(2021,04,01);
		MglTransaktion t = mitglied.setBeitrittsdatumTransaktionPer(beitritt,datumTest);

		assertEquals("Mitglied Beitrittsdatum wurde nicht gesetzt"
				,beitritt
				, mitglied.getBeitrittsdatum());
		assertEquals("Transaktion Mitglied Beitrittsdatum wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Beitrittsdatum hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedBeitritt
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Beitrittsdatum hat nicht den angegebenen Wert " + beitritt
				,beitritt
				,((MglTransaktionInhaltLocalDate)t.getMglInhalt()).getInhalt());
		assertEquals("Transaktion Beitrittsdatum hat nicht das korrekte Datum" + datumTest
				,datumTest
				,t.getMglDatumTransaktion());
	}
	@Test
	public void shouldSetBeitrittsdatumAndTransaktion() {
		
		LocalDate beitritt = LocalDate.of(2021,04,01);
		MglTransaktion t = mitglied.setBeitrittsdatumTransaktion(beitritt);

		assertEquals("Mitglied Beitrittsdatum wurde nicht gesetzt"
				,beitritt
				, mitglied.getBeitrittsdatum());
		assertEquals("Transaktion Mitglied Beitrittsdatum wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Beitrittsdatum hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedBeitritt
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Beitrittsdatum hat nicht den angegebenen Wert " + beitritt
				,beitritt
				,((MglTransaktionInhaltLocalDate)t.getMglInhalt()).getInhalt());
	}
	@Test
	public void shouldSetAustrittsdatumAndTransaktionPer() {
		
		LocalDate austritt = LocalDate.of(2025,05,01);
		MglTransaktion t = mitglied.setAustrittsdatumTransaktionPer(austritt,datumTest);

		assertEquals("Mitglied Austrittsdatum wurde nicht gesetzt"
				,austritt
				, mitglied.getAustrittsdatum());
		assertEquals("Transaktion Mitglied Austrittsdatum wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Austrittsdatum hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedAustritt
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Austrittsdatum hat nicht den angegebenen Wert " + austritt
				,austritt
				,((MglTransaktionInhaltLocalDate)t.getMglInhalt()).getInhalt());
		assertEquals("Transaktion Austrittsdatum hat nicht das korrekte Datum" + datumTest
				,datumTest
				,t.getMglDatumTransaktion());
	}
	@Test
	public void shouldSetAustrittsdatumAndTransaktion() {
		
		LocalDate austritt = LocalDate.of(2025,05,01);
		MglTransaktion t = mitglied.setAustrittsdatumTransaktion(austritt);

		assertEquals("Mitglied Austrittsdatum wurde nicht gesetzt"
				,austritt
				, mitglied.getAustrittsdatum());
		assertEquals("Transaktion Mitglied Austrittsdatum wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,mitglied.getTransaktionen().size());
		assertEquals("Transaktion Mitglied Austrittsdatum hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.MitgliedAustritt
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Mitglied Austrittsdatum hat nicht den angegebenen Wert " + austritt
				,austritt
				,((MglTransaktionInhaltLocalDate)t.getMglInhalt()).getInhalt());
	}

}
