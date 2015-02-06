package cofh.api.tileentity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

/**
 * Implement this interface on Tile Entities which can provide information about themselves.
 * 
 * @author King Lemming
 * 
 */
public interface ITileInfo {

	/**
	 * This function appends information to a list provided to it.
	 * 
	 * @param info
	 *            The list that the information should be appended to.
	 * @param facing
	 *            The side of the block that is being queried.
	 * @param player
	 *            Player doing the querying - this can be NULL.
	 * @param debug
	 *            If true, the tile should return "debug" information.
	 */
	void getTileInfo(List<IChatComponent> info, EnumFacing facing, EntityPlayer player, boolean debug);

}
