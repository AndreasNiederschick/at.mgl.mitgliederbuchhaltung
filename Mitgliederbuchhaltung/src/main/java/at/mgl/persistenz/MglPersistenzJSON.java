package at.mgl.persistenz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;

import at.mgl.transaktion.JsonInterfaceAdapter;
import at.mgl.transaktion.MglTransaktion;
import at.mgl.transaktion.inhalt.IMglTransaktionInhalt;

public class MglPersistenzJSON {
	
	public static final String jsonDirectory = "/home/andreas/mglJson/";
	public static String jsonFile = "gen3.json";
	
	public static void schreibeTransaktionInJsonDatei(MglTransaktion mglTransaktion) {
		
        try {
        	File file = new File(jsonDirectory + jsonFile);
        	FileWriter writer = new FileWriter(file,true);
        	
    		GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(IMglTransaktionInhalt.class, new JsonInterfaceAdapter());        
            Gson gson = builder.create();

			String json = gson.toJson(mglTransaktion);
			
			writer.append(json);
			writer.append(System.lineSeparator());
			
			writer.flush();
			writer.close();
			
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<MglTransaktion> leseTransaktionenVonJsonDatei() {
		
		String line;
		List<MglTransaktion> ret = new ArrayList<MglTransaktion>();
		MglTransaktion tmpTransaktion;
		
		GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IMglTransaktionInhalt.class, new JsonInterfaceAdapter());        
        Gson gson = builder.create();
		
		try {
			File file = new File(jsonDirectory + jsonFile);
			
			FileReader reader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(reader);
			
			while ((line = bReader.readLine()) != null) {
				tmpTransaktion = gson.fromJson(line, MglTransaktion.class);
				ret.add(tmpTransaktion);
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
		
	}
	

}
