package es.eltrueno.deliveryman.hologram.truenohologram;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import es.eltrueno.deliveryman.hologram.truenohologram.version.TruenoHologram_v1_10_r1;
import es.eltrueno.deliveryman.hologram.truenohologram.version.TruenoHologram_v1_11_r1;
import es.eltrueno.deliveryman.hologram.truenohologram.version.TruenoHologram_v1_8_r3;
import es.eltrueno.deliveryman.hologram.truenohologram.version.TruenoHologram_v1_9_r1;
import es.eltrueno.deliveryman.hologram.truenohologram.version.TruenoHologram_v1_9_r2;
import net.md_5.bungee.api.ChatColor;

public class TruenoHologramAPI {

	
	private static String version;
	
	private static void setupVersion(){
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
	}
	
	public static TruenoHologram getNewHologram(){
		if(version==null){
			setupVersion();
		}
        if (version.equals("v1_8_R3")) {
            return new TruenoHologram_v1_8_r3();
        }else if(version.equals("v1_9_R1")){
        	return new TruenoHologram_v1_9_r1();
        }else if(version.equals("v1_9_R2")){
        	return new TruenoHologram_v1_9_r2();
        }else if(version.equals("v1_10_R1")){
        	return new TruenoHologram_v1_10_r1();
        }else if(version.equals("v1_11_R1")){
        	return new TruenoHologram_v1_11_r1();
        }else{
        	Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED+"Unsopported server version.");
        	return null;
        }

	}
		
}
