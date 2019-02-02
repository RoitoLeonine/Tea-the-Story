package roito.teastory.item.drink;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import roito.teastory.api.drink.DailyDrink;
import roito.teastory.block.BlockRegister;
import roito.teastory.config.ConfigMain;
import roito.teastory.helper.DrinkingHelper;
import roito.teastory.item.ItemRegister;
import roito.teastory.potion.PotionRegister;

public class PuerTea extends ItemTeaDrink
{
	public PuerTea()
	{
		super("puer_tea");
	}

	@Override
	protected void onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (!world.isRemote)
		{
			int tier = itemstack.getItemDamage() / 2;
			addPotion(tier, world, entityplayer);
		}
	}

	public static void addPotion(int tier, World world, EntityPlayer entityplayer)
	{
		DailyDrink dailyDrink = DrinkingHelper.getLevelAndTimeImprovement(world, entityplayer);
		tier += dailyDrink.getLevel();
		int time = ConfigMain.drink.puerTeaDrink_Time;
		time *= 1.0F + dailyDrink.getTime();

		int addedTimePercent = (int) (dailyDrink.getTime() * 100);
		entityplayer.sendMessage(new TextComponentTranslation("teastory.message.daily_drinking.normal", dailyDrink.getInstantDay(), addedTimePercent + "%", dailyDrink.getLevel()));

		if (ConfigMain.general.useTeaResidueAsBoneMeal)
		{
			ItemHandlerHelper.giveItemToPlayer(entityplayer, new ItemStack(ItemRegister.tea_residue, 1, 5));
		}
		if (Potion.getPotionFromResourceLocation(ConfigMain.drink.puerTeaDrink_Effect) != null)
		{
			entityplayer.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation(ConfigMain.drink.puerTeaDrink_Effect), time / (tier + 1), tier));
		}
		entityplayer.addPotionEffect(new PotionEffect(PotionRegister.PotionExcitement, time / (tier + 1), 0));
	}

	@Override
	public Block getBlock(int meta)
	{
		switch (meta)
		{
			case 2:
				return BlockRegister.puertea_stone_cup;
			case 3:
				return BlockRegister.puertea_glass_cup;
			case 4:
				return BlockRegister.puertea_porcelain_cup;
			case 5:
				return BlockRegister.puertea_zisha_cup;
			default:
				return BlockRegister.puertea_wood_cup;
		}
	}
}