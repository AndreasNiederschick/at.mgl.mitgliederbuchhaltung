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
import javax.inject.Named;

import at.mgl.persistenz.MglPersistenzHelfer;
import at.mgl.persistenz.MglPersistenzJSON;
import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;
import at.mgl.transaktion.MglTransaktion;

@Named
@ApplicationScoped
public class MglContextBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Map<UUID,Genossenschaft> genossenschaften = new HashMap<UUID,Genossenschaft>(); 
	private Genossenschaft genossenschaft;
	
	public MglContextBean() {

	}

	@PostConstruct
	public void init() {
		
		List<MglTransaktion> transaktionen = MglPersistenzJSON.leseTransaktionenVonJsonDatei();
        
        genossenschaften = MglPersistenzHelfer.ladeGenossenschaftenAusTransaktionen(transaktionen);
        
        List<Genossenschaft> genList = new ArrayList(genossenschaften.values());
        
        this.genossenschaft = genList.get(0);
        
	}

	public List<Genossenschaft> getGenossenschaftListe () {
		return new ArrayList(this.genossenschaften.values());
	}
	
	public Map<UUID, Genossenschaft> getGenossenschaften() {
		return genossenschaften;
	}

	public void setGenossenschaften(Map<UUID, Genossenschaft> genossenschaften) {
		this.genossenschaften = genossenschaften;
	}

	public Genossenschaft getGenossenschaft() {
		return genossenschaft;
	}

	public void setGenossenschaft(Genossenschaft genossenschaft) {
		this.genossenschaft = genossenschaft;
	}

}
