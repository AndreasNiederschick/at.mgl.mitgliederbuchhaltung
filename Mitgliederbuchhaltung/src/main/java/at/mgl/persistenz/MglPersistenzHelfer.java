package at.mgl.persistenz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.mgl.position.Anteilsblock;
import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;
import at.mgl.transaktion.MglTransaktion;

public class MglPersistenzHelfer {
	
	public static Map<UUID,Genossenschaft> ladeGenossenschaftenAusTransaktionen(List<MglTransaktion> transaktionen) {
		
		Map<UUID,Genossenschaft> ret = new HashMap<UUID,Genossenschaft>();
		
		Genossenschaft tmpGen = null;
		Mitglied tmpMgl = null;
		Anteilsblock tmpAnt = null;
		
        for(MglTransaktion t : transaktionen) {
        	switch (t.getMglTransaktionsTyp().positionsTyp()) {
        	case "Genossenschaft":
        		tmpGen = ladeGenossenschaft(t,ret);
        		break;
        	case "Mitglied":
        		tmpGen = ladeGenossenschaft(t,ret);
        		tmpMgl = ladeMitglied(t, tmpGen);
        		break;
        	case "Anteilsblock":
        		tmpGen = ladeGenossenschaft(t,ret);
        		tmpMgl = ladeMitglied(t, tmpGen);
        		tmpAnt = ladeAnteilsblock(t, tmpMgl);
        		break;
        	default: System.out.println("Transaktionstyp nicht korrekt");
        	} 	
        }
        
        List<Genossenschaft> genList = new ArrayList(ret.values());
        for (Genossenschaft gen : genList) {
        	gen.aufrollen();
        	for (Mitglied mgl : gen.getMitgliederListe()) {
        		mgl.aufrollen();
        		for(Anteilsblock ant : mgl.getAnteilsblockListe()) {
        			ant.aufrollen();
        		}
        	}
        }
        
    	if (tmpGen != null) { tmpGen.aufrollen(); }
    	if (tmpMgl != null) { tmpMgl.aufrollen(); }
    	if (tmpAnt != null) { tmpAnt.aufrollen(); }  
        
		return ret;
	}
	
	private static Genossenschaft ladeGenossenschaft(MglTransaktion t, Map<UUID,Genossenschaft> genossenschaften) {	
		
		Genossenschaft ret;
		
		if (genossenschaften.containsKey(t.getMglGenossenschaftID())) {
			ret = genossenschaften.get(t.getMglGenossenschaftID());
			ret.getTransaktionen().add(t);
		} else {
			ret = new Genossenschaft(t.getMglGenossenschaftID());
			ret.getTransaktionen().add(t);
			genossenschaften.put(ret.getGenossenschaftID(), ret);
		}
		return ret;
	}
	private static Mitglied ladeMitglied(MglTransaktion t, Genossenschaft gen) {
		Mitglied ret;
		if (gen.getMitglieder().containsKey(t.getMglMitgliedID())) {
			ret = gen.getMitglieder().get(t.getMglMitgliedID());
			ret.getTransaktionen().add(t);
		} else {
			ret = gen.neuesMitglied(t.getMglMitgliedID());
			ret.getTransaktionen().add(t);
		}
		return ret;
	}
	private static Anteilsblock ladeAnteilsblock(MglTransaktion t, Mitglied mgl) {
		Anteilsblock ret;
		if (mgl.getAnteile().containsKey(t.getMglAnteilsblockID())) {
			ret = mgl.getAnteile().get(t.getMglAnteilsblockID());
			ret.getTransaktionen().add(t);
		} else {
			ret = mgl.neuerAnteilsblock(t.getMglAnteilsblockID());
			ret.getTransaktionen().add(t);
		}
		
		return ret;
	}

}
