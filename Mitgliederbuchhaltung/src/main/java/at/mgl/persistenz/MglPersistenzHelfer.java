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
		
		transaktionen.stream().filter(t -> t.getMglTransaktionsTyp().positionsTyp() == "Genossenschaft")
							.forEach(t -> ladeGenossenschaft(t,ret));
		
		
		transaktionen.stream().filter(t -> t.getMglTransaktionsTyp().positionsTyp() == "Mitglied")
							.forEach(t -> ladeMitglied(t,ret));
		
		transaktionen.stream().filter(t -> t.getMglTransaktionsTyp().positionsTyp() == "Anteilsblock")
		.forEach(t -> ladeAnteilsblock(t,ret));
        
        
		List<Genossenschaft> genList = new ArrayList(ret.values());
        
        genList.stream().forEach(Genossenschaft::aufrollen);
        
        genList.stream().forEach(g -> g.getMitgliederListe().stream()
        				.forEach(Mitglied::aufrollen));
        
        genList.stream().forEach(g -> g.getMitgliederListe().stream()
        				.forEach(m -> m.getAnteilsblockListe().stream()
        				.forEach(Anteilsblock::aufrollen)));
        
		return ret;
	}
	
	private static Genossenschaft ladeGenossenschaft(MglTransaktion t, Map<UUID,Genossenschaft> genossenschaften) {	
		
		Genossenschaft ret;
		
		ret = genossenschaften.computeIfAbsent(t.getMglGenossenschaftID(),Genossenschaft::new);
		ret.getTransaktionen().add(t);
		genossenschaften.put(ret.getGenossenschaftID(), ret);
		
		return ret;
	}
	private static Mitglied ladeMitglied(MglTransaktion t, Map<UUID,Genossenschaft> genossenschaften) {
		
		Mitglied ret;
		Genossenschaft gen = genossenschaften.get(t.getMglGenossenschaftID());
		
		ret = gen.getMitglieder().computeIfAbsent(t.getMglMitgliedID(),Mitglied::new);
		ret.setGen(gen);
		ret.getTransaktionen().add(t);
		
		return ret;
		
	}
	
	private static Anteilsblock ladeAnteilsblock(MglTransaktion t, Map<UUID,Genossenschaft> genossenschaften) {
		
		Anteilsblock ret;
		Genossenschaft gen = genossenschaften.get(t.getMglGenossenschaftID());
		Mitglied mgl = gen.getMitglieder().get(t.getMglMitgliedID());
		
		ret = mgl.getAnteile().computeIfAbsent(t.getMglAnteilID(),Anteilsblock::new);
		ret.setGenossenschaft(gen);
		ret.setMitglied(mgl);
		ret.getTransaktionen().add(t);
		
		return ret;
	}

}
