package cateam.teastory.item;

import java.util.List;

import cateam.teastory.achievement.AchievementLoader;
import cateam.teastory.block.BlockLoader;
import cateam.teastory.common.ConfigLoader;
import cateam.teastory.creativetab.CreativeTabsLoader;
import cateam.teastory.tileentity.TileEntityTeaDrink;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class HotWater extends ItemFood
{
    public HotWater()
    {
        super(0, false);
        this.setCreativeTab(CreativeTabsLoader.tabteastory);
        this.setAlwaysEdible();
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setUnlocalizedName("hot_water");
    }
    
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List list, boolean b)
    {
        list.add(StatCollector.translateToLocal("teastory.tooltip.hot_water"));
    }
    
    @Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
	{
	    subItems.add(new ItemStack(itemIn, 1, 0));
	    subItems.add(new ItemStack(itemIn, 1, 1));
	    subItems.add(new ItemStack(itemIn, 1, 2));
	    subItems.add(new ItemStack(itemIn, 1, 3));
	}
    
    @Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		String name;
		switch(stack.getItemDamage())
		{
		    case 1:
		    	name = "stone";
		    	break;
		    case 2:
		    	name = "glass";
		    	break;
		    case 3:
		    	name = "porcelain";
		    	break;
		    default:
		    	name = "wood";
		}
	    return super.getUnlocalizedName() + "." + name;
	}

    @Override
    public void onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        entityplayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 0));
    }
  
    @Override
    public EnumAction getItemUseAction(ItemStack itemStackIn)
    {
        return EnumAction.DRINK;
    }
    
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        super.onItemUseFinish(stack, worldIn, playerIn);
        playerIn.triggerAchievement(AchievementLoader.hotWater);
        return new ItemStack(ItemLoader.cup, 1, stack.getItemDamage());
    }
    
    public Block getBlock(int meta)
	{
		switch(meta)
		{
		    case 1:
		    	return BlockLoader.hotwater_stone_cup;
		    case 2:
		    	return BlockLoader.hotwater_glass_cup;
		    case 3:
		    	return BlockLoader.hotwater_porcelain_cup;
		    default:
		    	return BlockLoader.hotwater_wood_cup;
		}
	}
    
    @Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (playerIn.isSneaking())
		{
			Block drinkblock = getBlock(stack.getItemDamage());
			IBlockState iblockstate = worldIn.getBlockState(pos);
	        Block block = iblockstate.getBlock();

	        if (!block.isReplaceable(worldIn, pos))
	        {
	            pos = pos.offset(side);
	        }

	        if (stack.stackSize == 0)
	        {
	            return false;
	        }
	        else if (!playerIn.canPlayerEdit(pos, side, stack))
	        {
	            return false;
	        }
	        else if (worldIn.canBlockBePlaced(drinkblock, pos, false, side, (Entity)null, stack))
	        {
	            int i = this.getMetadata(stack.getMetadata());
	            IBlockState iblockstate1 = drinkblock.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);

	            if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, iblockstate1))
	            {
	                worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), drinkblock.stepSound.getPlaceSound(), (drinkblock.stepSound.getVolume() + 1.0F) / 2.0F, drinkblock.stepSound.getFrequency() * 0.8F);
	                --stack.stackSize;
	            }

	            return true;
	        }
	        else
	        {
	            return false;
	        }
		}
		else return false;
	}
	
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
    {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == getBlock(stack.getItemDamage()))
        {
            ItemBlock.setTileEntityNBT(world, player, pos, stack);
            getBlock(stack.getItemDamage()).onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }
}