package at.mgl.position;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.MglTransaktionFactory;
import at.mgl.transaktion.MglTransaktionInfo;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDate;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltDouble;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltInteger;
import at.mgl.transaktion.inhalt.MglTransaktionInhaltString;

public class Position implements IPosition {

	
	public static String getFieldName(Object fieldObject, Object parent) {
		System.out.println("fieldObject " + fieldObject.toString());

	    java.lang.reflect.Field[] allFields = parent.getClass().getDeclaredFields();
	    for (java.lang.reflect.Field field : allFields) {
	        Object currentFieldObject;
	        try {
	        	field.setAccessible(true);
	            currentFieldObject = field.get(parent);
	        } catch (Exception e) {
	            return null;
	        }
	        boolean isWantedField = fieldObject.equals(currentFieldObject);
	        if (isWantedField) {
	            String fieldName = field.getName();
	            System.out.println(fieldName);
	            return fieldName;
	        }
	    }
	    return null;
	}
	
	public MglTransaktion setMitTransaktion(Object fieldObject, Object wert) {
		MglTransaktion ret = null;
		Class cls = this.getClass();
		Field f;
		
		try {
			//Feld aus dem Übergabeobjekt ermitteln
			f = cls.getDeclaredField(this.getFieldName(fieldObject,this));
			
			//Neuen Wert für das Feld setzen. Kann auch verwendet werden, wenn kein Transakton erfolgen soll
			//Also auch ohne Annotation @MglTransaktionInfo auszuführen.
			// ----------------------------------------------------------
			// ACHTUNG - hier wäre jedenfalls eine Exception nötig, die den korrekten Datentyp wert prüft - muss gleich dem Datentyp fon fieldObject sein
			// ----------------------------------------------------------
			f.setAccessible(true);
			f.set(this, wert);
			
			System.out.println("fieldName: " + f.getName());
			System.out.println("fieldType: " + f.getType());
			//Wenn das Feld die Annotation @MglTransaktionInfo zugwiesen hat, dann wird eine Transaktion erzeugt
			MglTransaktionInfo ab = f.getAnnotation(MglTransaktionInfo.class);
	        if (ab != null) {
	            
	            switch (f.getType().getName()) {
	            case "java.lang.String":
	            	if (this instanceof Genossenschaft) {
	            		ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(((Genossenschaft)this).genossenschaftID,new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltString(wert.toString()));            	
	            		((Genossenschaft)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Mitglied) {
	            		ret = MglTransaktionFactory.erstelleTransaktionMitglied(((Mitglied)this).getGen().genossenschaftID, ((Mitglied)this).getMglMitgliedID(), new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltString(wert.toString())); 
	            		((Mitglied)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Anteilsblock) {
		            	ret = MglTransaktionFactory.erstelleTransaktionAnteilsblock(((Anteilsblock)this).getGen().genossenschaftID, ((Anteilsblock)this).getMgl().getMglMitgliedID(),((Anteilsblock)this).getMglAnteilID(),  new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltString(wert.toString()));
		            	((Anteilsblock)this).getTransaktionen().add(ret);
		            }
	            	break;
	            case "int":
	            	if (this instanceof Genossenschaft) {
	            		ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(((Genossenschaft)this).genossenschaftID,new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltInteger(((Integer)wert)));            	
	            		((Genossenschaft)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Mitglied) {
	            		ret = MglTransaktionFactory.erstelleTransaktionMitglied(((Mitglied)this).getGen().genossenschaftID, ((Mitglied)this).getMglMitgliedID(), new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltInteger(((Integer)wert))); 
	            		((Mitglied)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Anteilsblock) {
		            	ret = MglTransaktionFactory.erstelleTransaktionAnteilsblock(((Anteilsblock)this).getGen().genossenschaftID, ((Anteilsblock)this).getMgl().getMglMitgliedID(),((Anteilsblock)this).getMglAnteilID(),  new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltInteger(((Integer)wert))); 
		            	((Anteilsblock)this).getTransaktionen().add(ret);
		            }
	            	break;
	            case "double":
	            	if (this instanceof Genossenschaft) {
	            		ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(((Genossenschaft)this).genossenschaftID,new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltDouble(((Double)wert)));          	
	            		((Genossenschaft)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Mitglied) {
	            		ret = MglTransaktionFactory.erstelleTransaktionMitglied(((Mitglied)this).getGen().genossenschaftID, ((Mitglied)this).getMglMitgliedID(), new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltDouble(((Double)wert)));
	            		((Mitglied)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Anteilsblock) {
		            	ret = MglTransaktionFactory.erstelleTransaktionAnteilsblock(((Anteilsblock)this).getGen().genossenschaftID, ((Anteilsblock)this).getMgl().getMglMitgliedID(),((Anteilsblock)this).getMglAnteilID(),  new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltDouble(((Double)wert))); 
		            	((Anteilsblock)this).getTransaktionen().add(ret);
		            }
	            	break;
	            case "java.util.Date":
	            	if (this instanceof Genossenschaft) {
	            		ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(((Genossenschaft)this).genossenschaftID,new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltDate(((Date)wert)));            	
	            		((Genossenschaft)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Mitglied) {
	            		ret = MglTransaktionFactory.erstelleTransaktionMitglied(((Mitglied)this).getGen().genossenschaftID, ((Mitglied)this).getMglMitgliedID(), new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltDate(((Date)wert))); 
	            		((Mitglied)this).getTransaktionen().add(ret);
	            	} else if (this instanceof Anteilsblock) {
		            	ret = MglTransaktionFactory.erstelleTransaktionAnteilsblock(((Anteilsblock)this).getGen().genossenschaftID, ((Anteilsblock)this).getMgl().getMglMitgliedID(),((Anteilsblock)this).getMglAnteilID(),  new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltDate(((Date)wert)));  
		            	((Anteilsblock)this).getTransaktionen().add(ret);
		            }
	            	break;
	            }
	            
	            //ret = MglTransaktionFactory.erstelleTransaktionGenossenschaft(this.genossenschaftID,new Date(), ab.mglTransaktionstyp(), new MglTransaktionInhaltString(wert));
	    		
	        }	
	        
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void aufrollen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aufrollenPer(Date datumPer) {
		// TODO Auto-generated method stub
		
	}

}
