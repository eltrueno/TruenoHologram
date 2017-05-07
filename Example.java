package es.eltrueno.deliveryman.hologram.truenohologram;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Example {
    
    public static void createWorldHologram(Location loc, ArrayList<String> lines){
        //Create the hologram instance
        TruenoHologram hologram = TruenoHologramAPI.getNewHologram();
        //Setup the world hologram
        hologram.setupWorldHologram(loc, lines);
        //Display hologram to everyone
        hologram.display();
    }

    public static void createPlayerHologram(Player p, Location loc, ArrayList<String> lines){
        //Create the hologram instance
        TruenoHologram hologram = TruenoHologramAPI.getNewHologram();
        //Setup the player hologram
        hologram.setupPlayerHologram(p, loc, lines);
        //Display hologram only to the player
        hologram.display();
    }
    
}
