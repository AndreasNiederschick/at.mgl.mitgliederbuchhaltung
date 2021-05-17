package at.mgl.Mitgliederbuchhaltung;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import at.mgl.position.Genossenschaft;
import at.mgl.position.Mitglied;

@Named
@ViewScoped
public class GenossenschaftUebersichtBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Genossenschaft genossenschaft;

	@Inject
	private MglContextBean mglContext;
	
	public GenossenschaftUebersichtBean() {

	}

	@PostConstruct
	  public void init() {
		this.genossenschaft = mglContext.getGenossenschaft();
		System.out.println("Genossenschaft injected " + genossenschaft.getBezeichnung());
	}
	

	public List<Mitglied> getMitglieder() {
		return this.genossenschaft.getMitgliederListe();
	}

	public String getBezeichnung() {
		return this.genossenschaft.getBezeichnung();
	}

	public void setBezeichnung(String bezeichnung) {
		this.genossenschaft.setBezeichnung(bezeichnung);
	}
	
	

}
