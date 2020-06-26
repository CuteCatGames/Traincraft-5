package train.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import train.common.Traincraft;
import train.common.library.GuiIDs;
import train.common.tile.TileCrafterTierII;

import java.util.Random;

public class BlockAssemblyTableII extends BlockContainer {

	public BlockAssemblyTableII(Material material) {
		super(material);
		setCreativeTab(Traincraft.tcTab);
	}

	public int getRenderType()
	{
		return 3;
	}
	//@Override
	//public int damageDropped(IBlockState i) {
	//	return 0;
	//}
	//todo: i think this is fully redundant

	@Override
	public int quantityDropped(IBlockState meta, int fortune, Random random) {
		return 1;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity te = world.getTileEntity(pos);
		if (player.isSneaking()) {
			return false;
		}
		if (!world.isRemote) {
			if (te instanceof TileCrafterTierII) {
				player.openGui(Traincraft.instance, GuiIDs.CRAFTER_TIER_II, world, pos.getX(),pos.getY(),pos.getZ());
			}
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState par5) {
		Random distilRand = new Random();
		TileCrafterTierII tileentitytierII = (TileCrafterTierII) world.getTileEntity(pos);
		if (tileentitytierII != null) {
			label0: for (int l = 0; l < tileentitytierII.getSizeInventory(); l++) {
				if((l>9 && l<18)){continue;}
				ItemStack itemstack = tileentitytierII.getStackInSlot(l);
				if (itemstack == null) {
					continue;
				}
				float f = distilRand.nextFloat() * 0.8F + 0.1F;
				float f1 = distilRand.nextFloat() * 0.8F + 0.1F;
				float f2 = distilRand.nextFloat() * 0.8F + 0.1F;
				do {
					if (itemstack.stackSize <= 0) {
						continue label0;
					}
					int i1 = distilRand.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, itemstack.splitStack(i1));
					float f3 = 0.05F;
					entityitem.motionX = (float) distilRand.nextGaussian() * f3;
					entityitem.motionY = (float) distilRand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) distilRand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				} while (true);
			}
		}
		super.breakBlock(world, pos, par5);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.markBlockForUpdate(pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityliving, ItemStack stack) {
		super.onBlockPlacedBy(world, pos,state, entityliving, stack);
		TileCrafterTierII te = (TileCrafterTierII) world.getTileEntity(pos);
		if (te != null) {
			int dir = MathHelper.floor_double((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
			te.setFacing(EnumFacing.getHorizontal(dir == 0 ? 2 : dir == 1 ? 5 : dir == 2 ? 3 : 4));
			world.markBlockForUpdate(pos);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int meta) {
		return new TileCrafterTierII();
	}

}