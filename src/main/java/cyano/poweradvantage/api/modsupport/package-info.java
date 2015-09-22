/**
This package contains the light-weight API for people interested in adding Power Advantage support 
to existing mods (instead of making a Power Advantage add-on mod). Typically, you achieve this be 
first checking whether Power Advantage exists with <code>if(Loader.isModLoaded("poweradvantage")){ ... }</code>. 
Then if you detect that Power Advantage is installed, you can add an ad-hoc power handler for each 
of your machines like this:<br><pre>
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
</pre>
The above example is for registerring a machine that should accept steam and electricity power from 
Power Advantage add-on mods.
*/
package cyano.poweradvantage.api.modsupport;