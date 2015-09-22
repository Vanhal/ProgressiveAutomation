package cyano.poweradvantage.api.modsupport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import cyano.poweradvantage.api.ConduitType;
import cyano.poweradvantage.api.ITypedConduit;

/**
 * <p>This class provides a place to register blocks from other mods as power consumers. Such mods are 
 * considered "external" power systems because their power implementation is not controlled by Power 
 * Advantage. </p>
 * <p>For example, you can add Power Advantage compatibility to an existing mod like this:</p><pre>
	if(Loader.isModLoaded("poweradvantage")){
		LightWeightPowerRegistry.registerLightWeightPowerAcceptor(Blocks.myMachine, 
				new ILightWeightPowerAcceptor(){

					public boolean canAcceptEnergyType(ConduitType powerType) {
						return ConduitType.areSameType(powerType, "steam") || ConduitType.areSameType(powerType, "electricity");
					}

					public float getEnergyDemand(TileEntity yourMachine,
							ConduitType powerType) {
						TileEntityMyMachine m = (TileEntityMyMachine)yourMachine;
						return m.getMaxEnergyStored() - m.getEnergyStored();
					}

					public float addEnergy(TileEntity yourMachine,
							float amountAdded, ConduitType powerType) {
						TileEntityMyMachine m = (TileEntityMyMachine)yourMachine;
						return m.receiveEnergy(amountAdded,true);
					}
			
		});
	}
</pre>
 * @author DrCyano
 *
 */

///// MOCK CLASS! EXCLUDE FROM JAR /////
public class LightWeightPowerRegistry {

	/**
	 * Registers a block (and its associated TileEntity) as being able to receive power from the 
	 * Power Advantage power sources in spite of not extending the Power Advantage classes.
	 * @param machineBlock The block that has a machine that you want to receive power
	 * @param powerAcceptorImplementation Implemetnation of ILightWeightPowerAcceptor that allows 
	 * Power Advantage to ask the TileEntity associated with the given block how much power it wants 
	 * and how to add power to that TileEntity
	 */
	public static void registerLightWeightPowerAcceptor(Block machineBlock, ILightWeightPowerAcceptor powerAcceptorImplementation){
		// MOCK
	}
	
	
}
