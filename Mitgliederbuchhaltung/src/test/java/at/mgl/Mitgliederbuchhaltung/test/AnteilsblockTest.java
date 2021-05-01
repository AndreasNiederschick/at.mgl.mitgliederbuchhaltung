package at.mgl.Mitgliederbuchhaltung.test;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.mgl.position.Anteilsblock;
import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;
import at.mgl.position.ZustandAnteilsblock;
import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionsTyp;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;

public class AnteilsblockTest {
	
	Genossenschaft gen;
	Mitglied mgl;
	Anteilsblock ant;
	
	@Before
	public void setUp() {
		gen = new Genossenschaft();
		mgl = gen.neuesMitglied();
		ant = mgl.neuerAnteilsblock();
	}
	@After
	public void cleanUp() {
		
	}
	
	@Test
	public void testAufrollen() {
		
		Anteilsblock antAufroll = mgl.neuerAnteilsblock();
		
		
		MglTransaktion tzeichnung = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(10));
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,antAufroll.getZustand());	
		
		ant.stornieren(tzeichnung);
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,antAufroll.getZustand());	
		
		tzeichnung = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(10));
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,antAufroll.getZustand());	
		
		MglTransaktion tkuen = ant.kuendigen(new Date(2021,02,01));
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,antAufroll.getZustand());	
		
		
		ant.stornieren(tkuen);
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,antAufroll.getZustand());	
		
		tkuen = ant.kuendigen(new Date(2021,02,01));
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,antAufroll.getZustand());	
		
		MglTransaktion tazspf = ant.auszahlungssperrfrist(new Date(2021,03,01));
		ant.stornieren(tazspf);
		
		tazspf = ant.auszahlungssperrfrist(new Date(2021,03,01));
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollenPer(new Date(2021,02,015));
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,antAufroll.getZustand());

		antAufroll.aufrollen();
		assertEquals("Erwarteter Zustand: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,antAufroll.getZustand());
		
		MglTransaktion tauszahlen = ant.auszahlen(new Date(2021,04,01));
		ant.stornieren(tauszahlen);
		tauszahlen = ant.auszahlen(new Date(2021,04,01));
		antAufroll.setTransaktionen(ant.getTransaktionen());
		antAufroll.aufrollenPer(new Date(2021,03,015));
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
				,ant.getZustand());
		
		// Test erste Zeichnung
		
		MglTransaktion t = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(menge));

		assertEquals("Zustand nach Zeichnung muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,ant.getZustand());
		assertEquals("Transaktion Zeichnen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,1
				,ant.getTransaktionen().size());
		assertEquals("Transaktion Zeichnung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Zeichnung
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Zeichnen hat nicht den angegebenen Wert " + menge
				,menge
				,((MglTransaktionInhaltInteger)t.getMglInhalt()).getInhalt());
		
		// Test Zeichnung nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(menge+1));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
		
		t = ant.auszahlungssperrfrist(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		t = ant.auszahlen(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,ant.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,1
				,ant.getTransaktionen().size());	

		// Test Kündigung im Status Gezeichnet
		
		t = ant.kuendigen(new Date(2021,01,01));
		
		assertEquals("Zustand nach Kündigung muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,ant.getZustand());
		assertEquals("Transaktion Kündigen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2
				,ant.getTransaktionen().size());
		assertEquals("Transaktion Kündigung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Kuendigung
				,t.getMglTransaktionsTyp());
		
		// Test Kündigung nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(menge));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
		
		t = ant.kuendigen(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		t = ant.auszahlen(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,ant.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,2
				,ant.getTransaktionen().size());
		
		// Test Auszahlungssperrfrist im Status Gekündigt
		
		t = ant.auszahlungssperrfrist(new Date(2021,01,01));
				
		assertEquals("Zustand nach Auszahlungssperrfrist muss sein: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,ant.getZustand());
		assertEquals("Transaktion Auszahlungssperrfristwurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,3
				,ant.getTransaktionen().size());
		assertEquals("Transaktion Auszahlungssperrfrist hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Auszahlungssperrfrist
				,t.getMglTransaktionsTyp());
		
		// Test Auszahlungssperrfrist nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(menge));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
				
		t = ant.kuendigen(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
				
		t = ant.auszahlungssperrfrist(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
				
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,ant.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,3
				,ant.getTransaktionen().size());
		

		// Test Auszahlung im Status Auszahlungssperrfrist
		
		t = ant.auszahlen(new Date(2021,01,01));
						
		assertEquals("Zustand nach Auszahlung muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,ant.getZustand());
		assertEquals("Transaktion Auszahlungssperrfristwurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,4
				,ant.getTransaktionen().size());
		assertEquals("Transaktion Auszahlungssperrfrist hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Auszahlung
				,t.getMglTransaktionsTyp());
		
		// Test Ausgezahlt nicht erlaubte Transaktionen wegen nicht erlaubter Zustandsänderung
		
		t = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(menge));	
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);	
						
		t = ant.kuendigen(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
					
		t = ant.auszahlungssperrfrist(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
		
		t = ant.auszahlen(new Date(2021,01,01));
		assertEquals("Transaktion der nicht erlauben Geschäftsfälle muss null sein",null,t);
						
		assertEquals("Zustand nach nicht erlaubten Geschäftsfällen muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,ant.getZustand());
		assertEquals("Nach nicht erlaubten Geschäftsfällen dürfen keine weiteren Transaktionen in der Liste sein"
				,4
				,ant.getTransaktionen().size());

	}
	
	@Test
	public void testSzenarioStornierung() {
		int menge = 1;
		
		// Test vor der Zeichnung
		
		assertEquals("Zustand zu Beginn muss sein: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,ant.getZustand());
		
		// Test erste Zeichnung
		
		MglTransaktion t = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(menge));

		assertEquals("Zustand nach Zeichnung muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,ant.getZustand());
		assertEquals("Transaktion Zeichnen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,1
				,ant.getTransaktionen().size());
		assertEquals("Transaktion Zeichnung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Zeichnung
				,t.getMglTransaktionsTyp());
		assertEquals("Transaktion Zeichnen hat nicht den angegebenen Wert " + menge
				,menge
				,((MglTransaktionInhaltInteger)t.getMglInhalt()).getInhalt());
		
		//Test Stornierung Zeichnung
		ant.stornieren(t);
		
		assertEquals("Zustand nach Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,ant.getZustand());
		assertEquals("Transaktion Storno Zeichnen wurde nicht erzeugt oder in die Transaktionsliste eingefügt"
				,2
				,ant.getTransaktionen().size());
		assertEquals("Transaktion Zeichnung hat den falschen MglTransaktionstyp " + t.getMglTransaktionsTyp()
				,MglTransaktionsTyp.Zeichnung
				,t.getMglTransaktionsTyp());
		
		//Test zeichnen dann kündigen - dann Storno der Kündigung und dann der Zeichnung
		t = ant.zeichnen(new Date(2021,01,01), new MglTransaktionInhaltInteger(menge));
		MglTransaktion tkuen = ant.kuendigen(new Date(2021,02,01));
		
		assertEquals("Erwartet werden 4 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung"
				,4
				,ant.getTransaktionen().size());
		
		//Test Stornierung nicht erlaubt - in Zustand Kündigung kann die Zeichnung nicht storniert werden
		ant.stornieren(t);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,ant.getZustand());
		
		assertEquals("Erwartet werden 4 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung"
				,4
				,ant.getTransaktionen().size());
		
		//Test bis zu Ausgezahlt - dann zurückstornieren nach Start
		MglTransaktion tazspf = ant.auszahlungssperrfrist(new Date(2021,02,01));
		MglTransaktion tauszahlung = ant.auszahlen(new Date(2021,02,01));
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,ant.getZustand());
		
		assertEquals("Erwartet werden 6 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung"
				,6
				,ant.getTransaktionen().size());
		
		ant.stornieren(t);
		ant.stornieren(tkuen);
		ant.stornieren(tazspf);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Ausgezahlt
				,ZustandAnteilsblock.Ausgezahlt
				,ant.getZustand());
		
		assertEquals("Erwartet werden 6 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung"
				,6
				,ant.getTransaktionen().size());
		
		ant.stornieren(tauszahlung);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Auszahlungssperrfrist
				,ZustandAnteilsblock.Auszahlungssperrfrist
				,ant.getZustand());
		
		assertEquals("Erwartet werden 7 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung"
				,7
				,ant.getTransaktionen().size());
		
		ant.stornieren(tazspf);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Gekuendigt
				,ZustandAnteilsblock.Gekuendigt
				,ant.getZustand());
		
		assertEquals("Erwartet werden 8 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung, Storno AZSPF"
				,8
				,ant.getTransaktionen().size());
		
		ant.stornieren(tkuen);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Gezeichnet
				,ZustandAnteilsblock.Gezeichnet
				,ant.getZustand());
		
		assertEquals("Erwartet werden 9 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung, Storno AZSPF, Storno Kündigung"
				,9
				,ant.getTransaktionen().size());

		ant.stornieren(t);
		
		assertEquals("Zustand nach nicht möglicher Stornierung Zeichnung muss sein: " + ZustandAnteilsblock.Start
				,ZustandAnteilsblock.Start
				,ant.getZustand());
		
		assertEquals("Erwartet werden 10 Transaktionen: Zeichnung, Storno Zeichung, Zeichnung, Kündigung, Auszahlungssperrfrist, Auszahlung, Storno Auszahlung, Storno AZSPF, Storno Kündigung, Storno Zeichnung"
				,10
				,ant.getTransaktionen().size());

	}

}
