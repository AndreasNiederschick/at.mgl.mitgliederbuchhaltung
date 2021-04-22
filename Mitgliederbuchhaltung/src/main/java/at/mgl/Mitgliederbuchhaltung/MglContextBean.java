package at.mgl.Mitgliederbuchhaltung;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import at.mgl.persistenz.MglPersistenzHelfer;
import at.mgl.persistenz.MglPersistenzJSON;
import at.mgl.position.Genossenschaft;
import at.mgl.transaktion.MglTransaktion;


@ApplicationScoped
public class MglContextBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Genossenschaft gen;
	
	public MglContextBean() {
		super();
	}

	@PostConstruct
	  public void init() {
		Map<UUID,Genossenschaft> genossenschaften = new HashMap<UUID,Genossenschaft>();    	
    	
        List<MglTransaktion> transaktionen = MglPersistenzJSON.leseTransaktionenVonJsonDatei();
        
        genossenschaften = MglPersistenzHelfer.ladeGenossenschaftenAusTransaktionen(transaktionen);
        
        
        System.out.println("Anzahl Genossenschaften: " + genossenschaften.size());
        
        List<Genossenschaft> genList = new ArrayList(genossenschaften.values());
        
        this.gen = genList.get(0);
	  }

	public Genossenschaft getGen() {
		return gen;
	}

	public void setGen(Genossenschaft gen) {
		this.gen = gen;
	}
	
	

}
