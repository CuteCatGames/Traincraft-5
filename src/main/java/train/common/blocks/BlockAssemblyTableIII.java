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
import train.common.tile.TileCrafterTierIII;

import java.util.Random;

//import net.minecraft.client.renderer.texture.IIconRegister;
//import net.minecraft.util.IIcon;

public class BlockAssemblyTableIII extends BlockContainer {

	//private IIcon textureTop;
	//private IIcon textureBottom;
	//private IIcon textureFront;
	//private IIcon textureSide;

	public BlockAssemblyTableIII(Material material) {
		super(material);
		setCreativeTab(Traincraft.tcTab);
	}

	public int getRenderType()
	{
		return 3;
	}
	@Override
	public int damageDropped(IBlockState state) {
		return state.getBlock().getMetaFromState(state);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 1;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity te = world.getTileEntity(pos);
		if (player.isSneaking()) {
			return false;
		}
		if (!world.isRemote) {
			if (te instanceof TileCrafterTierIII) {
				player.openGui(Traincraft.instance, GuiIDs.CRAFTER_TIER_III, world, pos.getX(), pos.getY(),pos.getZ());
			}
		}
		return true;
	}

	/*@Override
	public IIcon getIcon(int i, int j) {
		if (i == 1) {
			return textureTop;
		}
		if (i == 0) {
			return textureBottom;
		}
		if (i == 3) {
			return textureFront;
		}
		else {
			return textureSide;
		}
	}

	@Override
	public IIcon getIcon(IBlockAccess worldAccess, int i, int j, int k, int side) {
		if (((TileCrafterTierIII) worldAccess.getTileEntity(i, j, k)).getFacing() != null) {
			side = TileHelper.getOrientationFromSide(((TileCrafterTierIII) worldAccess.getTileEntity(i, j, k)).getFacing(), ForgeDirection.getOrientation(side)).ordinal();
		}
		return side == 1 ? textureTop : side == 0 ? textureBottom : side == 3 ? textureFront : textureSide;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		textureTop = iconRegister.registerIcon(Info.modID.toLowerCase() + ":assembly_3_top");
		textureBottom = iconRegister.registerIcon(Info.modID.toLowerCase() + ":assembly_3_bottom");
		textureFront = iconRegister.registerIcon(Info.modID.toLowerCase() + ":assembly_3_front");
		textureSide = iconRegister.registerIcon(Info.modID.toLowerCase() + ":assembly_3_side");
	}

	*/

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		Random distilRand = new Random();
		TileCrafterTierIII tileentitytierIII = (TileCrafterTierIII) world.getTileEntity(pos);
		if (tileentitytierIII != null) {
			label0: for (int l = 0; l < tileentitytierIII.getSizeInventory(); l++) {
				if((l>9 && l<18)){continue;}
				ItemStack itemstack = tileentitytierIII.getStackInSlot(l);
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
					EntityItem entityitem = new EntityItem(world, (float) pos.getX() + f, (float) pos.getY() + f1, (float) pos.getZ() + f2, itemstack.splitStack(i1));
					float f3 = 0.05F;
					entityitem.motionX = (float) distilRand.nextGaussian() * f3;
					entityitem.motionY = (float) distilRand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) distilRand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				} while (true);
			}
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.markBlockForUpdate(pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityliving, ItemStack stack) {
		TileCrafterTierIII te = (TileCrafterTierIII) world.getTileEntity(pos);
		if (te != null) {
			int dir = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
			te.setFacing(EnumFacing.getHorizontal(dir == 0 ? 2 : dir == 1 ? 5 : dir == 2 ? 3 : 4));
			world.markBlockForUpdate(pos);
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int meta) {
		return new TileCrafterTierIII();
	}

}