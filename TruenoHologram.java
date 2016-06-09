package es.eltrueno.deliveryman.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;

public class TruenoHologram implements Listener{
	
	/**
	 * 
	 * 
	 * @author el_trueno 
	 * 
	 * 
	 **/
	
	private Location location;
	private ArrayList<String> lines;
	private double linesdistance = 0.28;
	
	private ArrayList<ArmorStand> armor_lines = new ArrayList<ArmorStand>();
	private ArrayList<EntityArmorStand> NmsArmorLines = new ArrayList<EntityArmorStand>();
	
	private Player player = null;
	
    /** 
     * Create a new hologram visible to all the players
     * @param loc Hologram location
     * @param lines Lines that will be shown in the hologram. TOP-BUTTOM
     */
	public TruenoHologram(Location loc, ArrayList<String> lines){
		this.location = loc;
		this.lines = lines;
	}
	
    /**
     * Create a new hologram visible only to a specific player
     * @param player Player who will see the hologram
     * @param loc Hologram location
     * @param lines Lines that will be shown in the hologram. TOP-BUTTOM
     */
	public TruenoHologram(Player player, Location loc, ArrayList<String> lines){
		this.player = player;
		this.location = loc;
		this.lines = lines;
	}
		
	private void NmsDestroy(EntityArmorStand hololine){
    	PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(hololine.getId());
    	((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
	}
	
	private void NmsSpawn(EntityArmorStand stand, String line, Location loc){    
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        stand.setCustomName(line);
        stand.setCustomNameVisible(true);
        stand.setGravity(false);
        stand.setSmall(true);
        stand.setInvisible(true);
        stand.setBasePlate(false);
        stand.setArms(false);
        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
	}
	
	private void spawn(){
		int ind = 0;
		for(String line : lines){
			Location finalLoc = location;
			finalLoc.setY(location.getY()+(linesdistance*lines.size()));
			if(ind>0) finalLoc = armor_lines.get(ind-1).getLocation(); finalLoc.setY(finalLoc.getY()-linesdistance);	        
			if(this.player!=null){
		        WorldServer s = ((CraftWorld)this.location.getWorld()).getHandle();
		        EntityArmorStand stand = new EntityArmorStand(s);   
				NmsSpawn(stand, line, finalLoc);
		        NmsArmorLines.add(stand);
			}
			else{
				ArmorStand Armorline = (ArmorStand) location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
				Armorline.setBasePlate(false);
				Armorline.setCustomNameVisible(true);
				Armorline.setGravity(false);
				Armorline.setCanPickupItems(false);
				Armorline.setCustomName(line);
				Armorline.setSmall(true);
				Armorline.setVisible(false);
				armor_lines.add(Armorline);	
			}
			ind++;
		}
	}
	
	private void despawn(){
		if(this.player!=null){
			for(EntityArmorStand nmsStand : NmsArmorLines){
				NmsDestroy(nmsStand);
			}
			NmsArmorLines.clear();
		}
		else{
			for(ArmorStand line : armor_lines){
				line.remove(); 
			}
			armor_lines.clear();
		}
	}
	/**
	 * Set the distance betwen the hologram lines. Default: 0.28
	 * 
	 * @param distance Distance between lines (double)
	 */
	public void setDistanceBetwenLines(Double distance){
		this.linesdistance = distance;
	}
		
	/**
	 * Display the hologram
	 */
	public void Display(){
		spawn();
	}
		
	/**
	 * Update the hologram lines. Only will be updated the lines that have changed.
	 * If the original hologram has more lines, the rest of the lines will be eliminated.
	 * If the original hologram has less lines than the new list, the new lines will be created.
	 * 
	 * @param lines Text lines.
	 */
	public void Update(ArrayList<String> lines){
		if(this.player != null){
			int ind = 0;
			for(String newline : lines){
				if(this.lines.size()>= ind){
					String oldline = this.lines.get(ind);
					if(oldline != newline){
						final EntityArmorStand oldstand = NmsArmorLines.get(ind);
						Location finalLoc = location;
						finalLoc.setY(location.getY()+(linesdistance*lines.size()));
						if(ind>0) finalLoc = armor_lines.get(ind-1).getLocation(); finalLoc.setY(finalLoc.getY()-linesdistance);
						WorldServer s = ((CraftWorld)this.location.getWorld()).getHandle();
				        EntityArmorStand stand = new EntityArmorStand(s);   
						NmsSpawn(stand, newline, finalLoc);
						this.NmsArmorLines.set(ind, stand);
						NmsDestroy(oldstand);
					}
					ind++;
				}
				else{
					Location finalLoc = location;
					finalLoc.setY(location.getY()+(linesdistance*lines.size()));
					if(ind>0) finalLoc = armor_lines.get(ind-1).getLocation(); finalLoc.setY(finalLoc.getY()-linesdistance);
			        WorldServer s = ((CraftWorld)this.location.getWorld()).getHandle();
			        EntityArmorStand stand = new EntityArmorStand(s);   
					NmsSpawn(stand, newline, finalLoc);
			        this.NmsArmorLines.add(stand);
			        this.lines.add(newline);
				}
			}
			if(lines.size() > this.lines.size()){
				int dif = lines.size() - this.lines.size();
				for(int in = 0; in <=dif; in++){
					int arrayind = (this.lines.size()-1)-in;
					this.lines.remove(arrayind);
					NmsDestroy(this.NmsArmorLines.get(arrayind));
					this.NmsArmorLines.remove(arrayind);
				}
			}
		}
		else{
			int ind = 0;
			for(String newline : lines){
				if(this.lines.size()>= ind){
					String oldline = this.lines.get(ind);
					if(oldline != newline){
						this.armor_lines.get(ind).setCustomName(newline);
					}
					ind++;
				}
				else{
					Location finalLoc = location;
					finalLoc.setY(location.getY()+(linesdistance*lines.size()));
					if(ind>0) finalLoc = armor_lines.get(ind-1).getLocation(); finalLoc.setY(finalLoc.getY()-linesdistance);
					ArmorStand Armorline = (ArmorStand) location.getWorld().spawnEntity(finalLoc, EntityType.ARMOR_STAND);
					Armorline.setBasePlate(false);
					Armorline.setCustomNameVisible(true);
					Armorline.setGravity(false);
					Armorline.setCanPickupItems(false);
					Armorline.setCustomName(newline);
					Armorline.setSmall(true);
					Armorline.setVisible(false);
					armor_lines.add(Armorline);	
					this.lines.add(newline);
				}
			}
			if(lines.size() > this.lines.size()){
				int dif = lines.size() - this.lines.size();
				for(int in = 0; in <=dif; in++){
					int arrayind = (this.lines.size()-1)-in;
					this.lines.remove(arrayind);
					this.armor_lines.get(arrayind).remove();
					this.armor_lines.remove(arrayind);
				}
			}
		}
	}
	
	/**
	 * Update a specific hologram line.
	 * 
	 * @param index Line index. (Start on 0)
	 * @param text Text to replace the old line.
	 */
	public void UpdateLine(int index, String text){
		if(this.lines.size() >= index){
			int realindex = (this.lines.size()-1)-index;
			String oldtext = this.lines.get(realindex);
			if(oldtext != text){
				if(this.player != null){
					final EntityArmorStand oldstand = NmsArmorLines.get(realindex);
					Location finalLoc = location;
					finalLoc.setY(location.getY()+(linesdistance*lines.size()));
					if(realindex>0) finalLoc = armor_lines.get(realindex-1).getLocation(); finalLoc.setY(finalLoc.getY()-linesdistance);
					WorldServer s = ((CraftWorld)this.location.getWorld()).getHandle();
			        EntityArmorStand stand = new EntityArmorStand(s);   
					NmsSpawn(stand, text, finalLoc);
					this.NmsArmorLines.set(realindex, stand);
					NmsDestroy(oldstand);
				}
				else{
					this.armor_lines.get(realindex).setCustomName(text);
				}
			 this.lines.set(realindex, text);
			}
		}

	}
	
	/**
	 * Delete a specific hologram line.
	 * 
	 * @param index Line index. (Start on 0)
	 */
	public void RemoveLine(int index){
		if(this.lines.size() >= index){
			int realindex = (this.lines.size()-1)-index;
				if(this.player != null){
					final EntityArmorStand stand = NmsArmorLines.get(realindex);
					this.NmsArmorLines.remove(stand);
					NmsDestroy(stand);
				}
				else{
					this.armor_lines.get(realindex).remove();
				}
			this.lines.remove(realindex);
		}
	}
		
	/**
	 * Delete the hologram
	 */
	public void delete(){
		despawn();
	}
	
}