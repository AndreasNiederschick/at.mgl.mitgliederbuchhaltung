package at.mgl.Mitgliederbuchhaltung.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.mgl.position.Anteilsblock;
import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;
import at.mgl.position.ZustandAnteilsblock;
import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltLocalDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;

public class AnteilsblockTest {
	
	Genossenschaft genossenschaft;
	Mitglied mitglied;
	Anteilsblock anteilsblock;
	
	@Before
	public void setUp() {
		genossenschaft = new Genossenschaft();
		genossenschaft.setDoPersist(false);
		
		mitglied = genossenschaft.neuesMitglied();
		mitglied.setDoPersist(false);
		
		anteilsblock = mitglied.neuerAnteilsblock();
		anteilsblock.setDoPersist(false);
	}
	@After
	public void cleanUp() {
		
	}
	
	@Test
	public void testAufrollen() {
		
		Anteilsblock antAufroll = mitglied.neuerAnteilsblock();
		antAufroll.setDoPersist(false);
		
		
		MglTransaktion tzeichnung = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(10));
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,antAufroll.getZustand());	
		
		anteilsblock.stornieren(tzeichnung);
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,antAufroll.getZustand());	
		
		tzeichnung = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(10));
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,antAufroll.getZustand());	
		
		MglTransaktion tkuen = anteilsblock.kuendigen(LocalDate.of(2021,02,01));
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,antAufroll.getZustand());	
		
		
		anteilsblock.stornieren(tkuen);
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,antAufroll.getZustand());	
		
		tkuen = anteilsblock.kuendigen(LocalDate.of(2021,02,01));
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,antAufroll.getZustand());	
		
		MglTransaktion tazspf = anteilsblock.auszahlungssperrfrist(LocalDate.of(2021,03,01));
		anteilsblock.stornieren(tazspf);
		
		tazspf = anteilsblock.auszahlungssperrfrist(LocalDate.of(2021,03,01));
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollenPer(LocalDate.of(2021,02,015));
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,antAufroll.getZustand());

		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,antAufroll.getZustand());
		
		MglTransaktion tauszahlen = anteilsblock.auszahlen(LocalDate.of(2021,04,01));
		anteilsblock.stornieren(tauszahlen);
		tauszahlen = anteilsblock.auszahlen(LocalDate.of(2021,04,01));
		antAufroll.setTransaktionen(anteilsblock.getTransaktionen());
		antAufroll.aufrollenPer(LocalDate.of(2021,03,015));
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,antAufroll.getZustand());

		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,antAufroll.getZustand());
		
	}
	
	
	@Test
	public void testSzenarioStandard() {
		int menge = 1;
		
		// Test vor der Zeichnung	
		assertEquals("Zustand zu Beginn muss sein: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,anteilsblock.getZustand());
		
		// Test erste Zeichnung
		
		MglTransaktion t = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(menge));

		assertEquals("Zustand nach Zeichnung muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,anteilsblock.getZustand());
		assertEquals("Transaktion Zeichnen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		assertEquals("Transaktion Zeichnung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Zeichnung
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Zeichnen hat nicht den angegebenen Wert " + menge
				,menge
				,((MglTransaktionInhaltInteger)t.getMglInhalt()).getInhalt());
		
		// Test Zeichnung nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(menge+1));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
		
		t = anteilsblock.auszahlungssperrfrist(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		t = anteilsblock.auszahlen(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,anteilsblock.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());	

		// Test Kündigung im Status Gezeichnet
		
		t = anteilsblock.kuendigen(LocalDate.of(2021,01,01));
		
		assertEquals("Zustand nach Kündigung muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,anteilsblock.getZustand());
		assertEquals("Transaktion Kündigen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,3 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		assertEquals("Transaktion Kündigung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Kuendigung
				,t.getMglTransaktionsTyp());
		
		// Test Kündigung nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(menge));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
		
		t = anteilsblock.kuendigen(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		t = anteilsblock.auszahlen(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,anteilsblock.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,3 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		
		// Test Auszahlungssperrfrist im Status Gekündigt
		
		t = anteilsblock.auszahlungssperrfrist(LocalDate.of(2021,01,01));
				
		assertEquals("Zustand nach Auszahlungssperrfrist muss sein: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,anteilsblock.getZustand());
		assertEquals("Transaktion Auszahlungssperrfristwurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,4 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		assertEquals("Transaktion Auszahlungssperrfrist hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Auszahlungssperrfrist
				,t.getMglTransaktionsTyp());
		
		// Test Auszahlungssperrfrist nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(menge));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
				
		t = anteilsblock.kuendigen(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
				
		t = anteilsblock.auszahlungssperrfrist(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
				
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,anteilsblock.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,4 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		

		// Test Auszahlung im Status Auszahlungssperrfrist
		
		t = anteilsblock.auszahlen(LocalDate.of(2021,01,01));
						
		assertEquals("Zustand nach Auszahlung muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,anteilsblock.getZustand());
		assertEquals("Transaktion Auszahlungssperrfristwurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,5 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		assertEquals("Transaktion Auszahlungssperrfrist hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Auszahlung
				,t.getMglTransaktionsTyp());
		
		// Test Ausgezahlt nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(menge));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
						
		t = anteilsblock.kuendigen(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
					
		t = anteilsblock.auszahlungssperrfrist(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		t = anteilsblock.auszahlen(LocalDate.of(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
						
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,anteilsblock.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,5 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());

	}
	
	@Test
	public void testSzenarioStornierung() {
		int menge = 1;
		
		// Test vor der Zeichnung
		
		assertEquals("Zustand zu Beginn muss sein: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,anteilsblock.getZustand());
		
		// Test erste Zeichnung
		
		MglTransaktion t = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(menge));

		assertEquals("Zustand nach Zeichnung muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,anteilsblock.getZustand());
		assertEquals("Transaktion Zeichnen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		assertEquals("Transaktion Zeichnung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Zeichnung
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Zeichnen hat nicht den angegebenen Wert " + menge
				,menge
				,((MglTransaktionInhaltInteger)t.getMglInhalt()).getInhalt());
		
		//Test Stornierung Zeichnung
		anteilsblock.stornieren(t);
		
		assertEquals("Zustand nach Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,anteilsblock.getZustand());
		assertEquals("Transaktion Storno Zeichnen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,3 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		assertEquals("Transaktion Zeichnung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Zeichnung
				,t.getMglTransaktionsTyp());
		
		//Test zeichnen dann kündigen - dann Storno der Kündigung und dann der Zeichnung
		t = anteilsblock.zeichnen(LocalDate.of(2021,01,01), new MglTransaktionInhaltInteger(menge));
		MglTransaktion tkuen = anteilsblock.kuendigen(LocalDate.of(2021,02,01));
		
		assertEquals("Erwartet werden 4 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung"
				,5 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		
		//Test Stornierung nicht erlaubt - in Zustand Kündigung kann die Zeichnung nicht storniert werden
		anteilsblock.stornieren(t);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,anteilsblock.getZustand());
		
		assertEquals("Erwartet werden 4 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung"
				,5 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		
		//Test bis zu Ausgezahlt - dann zurückstornieren nach Start
		MglTransaktion tazspf = anteilsblock.auszahlungssperrfrist(LocalDate.of(2021,02,01));
		MglTransaktion tauszahlung = anteilsblock.auszahlen(LocalDate.of(2021,02,01));
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,anteilsblock.getZustand());
		
		assertEquals("Erwartet werden 6 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung"
				,7 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		
		anteilsblock.stornieren(t);
		anteilsblock.stornieren(tkuen);
		anteilsblock.stornieren(tazspf);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,anteilsblock.getZustand());
		
		assertEquals("Erwartet werden 6 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung"
				,7 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		
		anteilsblock.stornieren(tauszahlung);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,anteilsblock.getZustand());
		
		assertEquals("Erwartet werden 7 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung"
				,8 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		
		anteilsblock.stornieren(tazspf);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,anteilsblock.getZustand());
		
		assertEquals("Erwartet werden 8 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung, Storno AZSPF"
				,9 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());
		
		anteilsblock.stornieren(tkuen);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,anteilsblock.getZustand());
		
		assertEquals("Erwartet werden 9 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung, Storno AZSPF, Storno Kündigung"
				,10 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());

		anteilsblock.stornieren(t);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,anteilsblock.getZustand());
		
		assertEquals("Erwartet werden 10 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung, Storno AZSPF, Storno Kündigung, Storno Zeichnung"
				,11 //Transaktion AnlageAnteilsblock ist immer die erste Transaktion
				,anteilsblock.getTransaktionen().size());

	}

}
