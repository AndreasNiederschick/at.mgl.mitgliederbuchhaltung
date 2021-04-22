package at.mgl.transaktion;

import java.util.Date;
import java.util.UUID;

import at.mgl.persistenz.MglPersistenzJSON;
import at.mgl.position.Anteilsblock;
import at.mgl.transaktion.inhalt.IMglTransaktionInhalt;

public class MglTransaktionFactory {
	
	public static MglTransaktion erstelleTransaktionGenossenschaft(UUID mglGenossenschaftID,Date mglDatumTransaktion,MglTransaktionsTyp mglTransaktionsTyp,IMglTransaktionInhalt mglInhalt) {
		MglTransaktion ret = new MglTransaktion (mglGenossenschaftID,mglDatumTransaktion,mglTransaktionsTyp,mglInhalt);
		MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		return ret;
	}
	
	public static MglTransaktion erstelleTransaktionMitglied(UUID mglGenossenschaftID,UUID mglMitgliedID,Date mglDatumTransaktion,MglTransaktionsTyp mglTransaktionsTyp,IMglTransaktionInhalt mglInhalt) {
		MglTransaktion ret = new MglTransaktion (mglGenossenschaftID,mglMitgliedID,mglDatumTransaktion,mglTransaktionsTyp,mglInhalt);
		MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		return ret;
	}
	
	public static MglTransaktion erstelleTransaktionAnteilsblock(UUID mglGenossenschaftID,UUID mglMitgliedID,UUID mglAnteilsblockID,Date mglDatumTransaktion,MglTransaktionsTyp mglTransaktionsTyp,IMglTransaktionInhalt mglInhalt) {
		MglTransaktion ret = new MglTransaktion (mglGenossenschaftID,mglMitgliedID,mglAnteilsblockID,mglDatumTransaktion,mglTransaktionsTyp,mglInhalt);
		MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		return ret;
	}
	
	public static MglTransaktion erstelleTransaktion (UUID mglGenossenschaftID,UUID mglMitgliedID,UUID mglAnteilsblockID,Date mglDatumTransaktion,MglTransaktionsTyp mglTransaktionsTyp,IMglTransaktionInhalt mglInhalt) {
		MglTransaktion ret = new MglTransaktion (mglGenossenschaftID,mglMitgliedID,mglAnteilsblockID,mglDatumTransaktion,mglTransaktionsTyp,mglInhalt);
		MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		return ret;
	}

}
