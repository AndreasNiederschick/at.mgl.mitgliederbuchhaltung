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
	private Genossenschaft gen;

	@Inject
	private MglContextBean mglContext;
	
	public GenossenschaftUebersichtBean() {
		super();
	}

	@PostConstruct
	  public void init() {
		this.gen = mglContext.getGen();
		System.out.println("Genossenschaft injected " + gen.getBezeichnung());
	}
	

	public List<Mitglied> getMitglieder() {
		return this.gen.getMitgliederListe();
	}

	/*
	public void setMitglieder(List<Mitglied> mitglieder) {
		this.mitglieder = mitglieder;
	}*/

	public String getBezeichnung() {
		return this.gen.getBezeichnung();
	}

	public void setBezeichnung(String bezeichnung) {
		this.gen.setBezeichnung(bezeichnung);
	}
	
	

}
