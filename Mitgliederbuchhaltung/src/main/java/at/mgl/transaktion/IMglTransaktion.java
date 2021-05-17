package at.mgl.transaktion;

import java.util.List;
import java.util.UUID;

public interface IMglTransaktion {
	
	public MglTransaktion stornieren (boolean doPersist);
	
	public List<MglTransaktion> split ();
	
	public void printMglTransaktion ();
	
	public String toHash ();
	
}
