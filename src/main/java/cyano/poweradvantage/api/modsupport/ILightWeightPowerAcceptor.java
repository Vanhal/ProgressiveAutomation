package cyano.poweradvantage.api.modsupport;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import cyano.poweradvantage.api.ConduitType;

/**
 * This interface is used in the LightWeightPowerRegistry to allow established mods to optionally 
 * support receiving power from Power Advantage power sources. This interface is usually 
 * instantiated as an anonymous inner class rather than making a class implement it directly.
 * @author DrCyano
 *
 */
public interface ILightWeightPowerAcceptor {

	/**
	 * Used to determine which types of energy are acceptable
	 * @param powerType A type of energy that you may or may not want to use as power
	 * @return True if you accept this type, false if you reject it.
	 */
	public abstract boolean canAcceptEnergyType(ConduitType powerType);
	/**
	 * This method will be called whenever your machine block is polled for power requests. The 
	 * TileEntity of the block being polled will be passed in (so you can safely cast it to your 
	 * native TileEntity class type) along with the type of power that is being offered. Your 
	 * implementation should cast the TileEntity to your specific class type, then decide how much 
	 * energy of the offered type that it wants, and then return that amount (or 0 if you don't want 
	 * to accept the offered energy type). Note that if energy is given, then the 
	 * addEnergy(TileEntity, float, ConduitType) method will be invoked.
	 * @param yourMachine The TileEntity instance being polled for power
	 * @param powerType The type of power being offered
	 * @return How much energy of the provided type your TileEntity is willing to accept.
	 */
	public abstract float getEnergyDemand(TileEntity yourMachine, ConduitType powerType);
	/**
	 * This method is called when energy it being provided to your TileEntity. The implementation of 
	 * this method should figure out what to do with the energy and return the amount of energy that 
	 * was actually used.
	 * @param yourMachine The TileEntity receiving power
	 * @param amountAdded The amount of power provided
	 * @param powerType The type of power provided
	 * @return The actual amount of power taken
	 */
	public abstract float addEnergy(TileEntity yourMachine, float amountAdded, ConduitType powerType);
}
