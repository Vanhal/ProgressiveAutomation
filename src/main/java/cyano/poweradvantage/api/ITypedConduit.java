package cyano.poweradvantage.api;

import net.minecraft.util.EnumFacing;

/**
 * Implement this method in every TileEntity and Block that interacts with energy. 
 * This ensures that all relevant Blocks and TileEntities can check the energy 
 * type of a Blocks or TileEntity. 
 * @author DrCyano
 *
 */
public interface ITypedConduit {
	/**
	 * Determines whether this conduit is compatible with an adjacent one
	 * @param type The type of energy in the conduit
	 * @param blockFace The side through-which the energy is flowing
	 * @return true if this conduit can flow the given energy type through the given face, false 
	 * otherwise
	 */
	public abstract boolean canAcceptType(ConduitType type, EnumFacing blockFace);
	/**
	 * Determines whether this conduit is compatible with a type of energy through any side
	 * @param type The type of energy in the conduit
	 * @return true if this conduit can flow the given energy type through one or more of its block 
	 * faces, false otherwise
	 */
	public abstract boolean canAcceptType(ConduitType type);
	
	/**
	 * Gets the energy type of this conduit. Most conduits can only interact 
	 * with other conduits of the same type. 
	 * @return The energy type of this conduit
	 */
	public abstract ConduitType getType();
	/**
	 * Determines whether this block/entity should receive energy. If this is not a sink, then it 
	 * will never be given power by a power source. 
	 * @return true if this block/entity should receive energy
	 */
	public abstract boolean isPowerSink();
	/**
	 * Determines whether this block/entity can provide energy. 
	 * @return true if this block/entity can provide energy
	 */
	public abstract boolean isPowerSource();
	

}
