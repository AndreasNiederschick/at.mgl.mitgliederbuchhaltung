package at.mgl.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.mgl.persistenz.MglPersistenzHelfer;
import at.mgl.persistenz.MglPersistenzJSON;
import at.mgl.position.Anteilsblock;
import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;
import at.mgl.transaktion.*;
import at.mgl.transaktion.inhalt.IMglTransaktionInhalt;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltLocalDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDouble;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //TestSzenario.szenarioStandard();
        //TestSzenario.szenarioLasttest(5, 10);
        

    	Map<UUID,Genossenschaft> genossenschaften = new HashMap<UUID,Genossenschaft>();    	
    	
        List<MglTransaktion> transaktionen = MglPersistenzJSON.leseTransaktionenVonJsonDatei();
        
        genossenschaften = MglPersistenzHelfer.ladeGenossenschaftenAusTransaktionen(transaktionen);
        
        
        System.out.println("Anzahl Genossenschaften: " + genossenschaften.size());
        
        List<Genossenschaft> genList = new ArrayList(genossenschaften.values());
        
        genList.stream().forEach(Genossenschaft::print);
        System.out.println(genList.size());
        genList.stream().forEach(g -> g.getTransaktionen().stream().forEach(MglTransaktion::printMglTransaktion));
        
        genList.stream().forEach(g -> g.getMitgliederListe()
        		.stream()
        		.forEach(m -> { System.out.println("Mitglied " + m.getMitgliedID() + " " + m.getVorname()); }))
        		;
        /*
        genList.stream().forEach(g -> g.getMitgliederListe().stream()
        		.forEach(m -> m.getAnteilsblockListe().stream()
        		.forEach(Anteilsblock::printAnteilsblock)))
        		;
        		*/
    }
    
}
