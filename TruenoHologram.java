package es.eltrueno.deliveryman.hologram.truenohologram;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface TruenoHologram {
	
	/**
	 * 
	 * 
	 * @author el_trueno 
	 * 
	 * 
	 **/
	
	
    /** 
     * Set the new hologram visible to all the players
     * @param loc Hologram location
     * @param lines Lines that will be shown in the hologram. TOP-BUTTOM
     */
	public void setupWorldHologram(Location loc, ArrayList<String> lines);
	
    /**
     * Set the new hologram visible only to a specific player
     * @param player Player who will see the hologram
     * @param loc Hologram location
     * @param lines Lines that will be shown in the hologram. TOP-BUTTOM
     */
	public void setupPlayerHologram(Player player, Location loc, ArrayList<String> lines);
	
    /**
     * Return hologram's location
     */
	public Location getLocation();
	
    /**
     * Return hologram's player (only if is a player hologram)
     */
	public Player getPlayer();
	
    /**
     * Set the distance between the hologram lines. Default: 0.30
     * @param distance the distance
     */
	public void setDistanceBetweenLines(Double distance);
	
    /**
     * Spawn the hologram
     */
	public void display();
	
	/**
	 * Update the hologram lines. Only will be updated the lines that have changed.
	 * If the original hologram has more lines, the rest of the lines will be eliminated.
	 * If the original hologram has less lines than the new list, the new lines will be created.
	 * 
	 * @param lines Text lines.
	 */
	public void update(ArrayList<String> lines);
	
    /**
     * Update specific line
     * @param index index of the line to update
     * @param text the new text
     */
	public void updateLine(int index, String text);
	
    /**
     * Remove a specific line
     * @param index the index of the line to remove
     */
	public void removeLine(int index);
	
    /**
     * Delete the hologram
     */
	public void delete();
	
}
