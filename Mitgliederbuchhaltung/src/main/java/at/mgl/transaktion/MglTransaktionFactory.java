package at.mgl.transaktion;

import java.time.LocalDate;
import java.util.UUID;

import at.mgl.persistenz.MglPersistenzJSON;
import at.mgl.transaktion.inhalt.IMglTransaktionInhalt;

public class MglTransaktionFactory {
	
	public static MglTransaktion erstelleTransaktionGenossenschaft(
			UUID mglGenossenschaftID
			,LocalDate mglDatumTransaktion
			,MglTransaktionsTyp mglTransaktionsTyp
			,IMglTransaktionInhalt mglInhalt
			,boolean doPersist) {
		
		MglTransaktion ret = new MglTransaktion (
				mglGenossenschaftID
				,mglDatumTransaktion
				,mglTransaktionsTyp,mglInhalt);
		
		if (doPersist)  {
			MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		}	
		
		return ret;
	}
	
	public static MglTransaktion erstelleTransaktionMitglied(
			UUID mglGenossenschaftID
			,UUID mglMitgliedID
			,LocalDate mglDatumTransaktion
			,MglTransaktionsTyp mglTransaktionsTyp
			,IMglTransaktionInhalt mglInhalt
			,boolean doPersist) {
		
		MglTransaktion ret = new MglTransaktion (
				mglGenossenschaftID
				,mglMitgliedID
				,mglDatumTransaktion
				,mglTransaktionsTyp
				,mglInhalt);
		
		if (doPersist)  {
			MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		}
		
		return ret;
	}
	
	public static MglTransaktion erstelleTransaktionAnteilsblock(
			UUID mglGenossenschaftID
			,UUID mglMitgliedID
			,UUID mglAnteilsblockID
			,LocalDate mglDatumTransaktion
			,MglTransaktionsTyp mglTransaktionsTyp
			,IMglTransaktionInhalt mglInhalt
			,boolean doPersist) {
		
		MglTransaktion ret = new MglTransaktion (
				mglGenossenschaftID
				,mglMitgliedID
				,mglAnteilsblockID
				,mglDatumTransaktion
				,mglTransaktionsTyp
				,mglInhalt);
		
		if (doPersist)  {
			MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		}
		
		return ret;
	}
	
	public static MglTransaktion erstelleTransaktion (
			UUID mglGenossenschaftID
			,UUID mglMitgliedID
			,UUID mglAnteilsblockID
			,LocalDate mglDatumTransaktion
			,MglTransaktionsTyp mglTransaktionsTyp
			,IMglTransaktionInhalt mglInhalt
			,boolean doPersist) {
		
		MglTransaktion ret = new MglTransaktion (
				mglGenossenschaftID
				,mglMitgliedID
				,mglAnteilsblockID
				,mglDatumTransaktion
				,mglTransaktionsTyp
				,mglInhalt);
		
		if (doPersist)  {
			MglPersistenzJSON.schreibeTransaktionInJsonDatei(ret);
		}
		
		return ret;
	}

}
