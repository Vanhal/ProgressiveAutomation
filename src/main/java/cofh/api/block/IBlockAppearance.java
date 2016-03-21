package cofh.api.block;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

/**
 * Implement this interface on blocks that can mimic the appearance of other blocks. Note that this is meant to be available server-side, so ensure the code is
 * server-safe and doesn't use client-side code.
 *
 */
public interface IBlockAppearance {

	/**
	 * This function returns the block that is being shown on a given side.
	 * <p>
	 * <b>Do not</b> return <tt>null</tt> from this method.
	 *
	 * @param world
	 *            Reference to the world.
	 * @param x
	 *            X coordinate of the block.
	 * @param y
	 *            Y coordinate of the block.
	 * @param z
	 *            Z coordinate of the block.
	 * @param side
	 *            The side of the block.
	 */
	public Block getVisualBlock(IBlockAccess world, int x, int y, int z, EnumFacing side);

	/**
	 * This function returns metadata of the block that is being shown on a given side.
	 *
	 * @param world
	 *            Reference to the world.
	 * @param x
	 *            X coordinate of the block.
	 * @param y
	 *            Y coordinate of the block.
	 * @param z
	 *            Z coordinate of the block.
	 * @param side
	 *            The side of the block.
	 */
	public int getVisualMeta(IBlockAccess world, int x, int y, int z, EnumFacing side);

	/**
	 * This function returns whether the block's renderer will visually connect to other blocks implementing IBlockAppearance.
	 */
	public boolean supportsVisualConnections();

}
