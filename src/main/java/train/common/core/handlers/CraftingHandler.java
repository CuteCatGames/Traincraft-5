/*******************************************************************************
 * Copyright (c) 2012 Mrbrutal. All rights reserved.
 * 
 * @name TrainCraft
 * @author Mrbrutal
 ******************************************************************************/

package train.common.core.handlers;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import train.common.items.ItemRollingStock;
import train.common.library.AchievementIDs;

public class CraftingHandler
{
  
  @SubscribeEvent
  public void onCrafting(PlayerEvent.ItemCraftedEvent event)
  {
	  for (AchievementIDs ach : AchievementIDs.values())
	  {
		  Item[] items = ach.getItems();
		  if (items != null) for (Item item: items) if (item == event.crafting.getItem()) event.player.addStat(ach.achievement, 1);
      }
		if ((event.crafting.getItem() instanceof ItemRollingStock)) {
			if (!event.player.worldObj.isRemote) {
        if (FMLCommonHandler.instance().getMinecraftServerInstance() != null) {
          //TraincraftSaveHandler.createFile(FMLCommonHandler.instance().getMinecraftServerInstance());
          //int readID = TraincraftSaveHandler.readInt(FMLCommonHandler.instance().getMinecraftServerInstance(), "numberOfTrains:");
          //int newID = stock.setNewUniqueID(event.crafting, event.player, readID);
            event.crafting.setTagCompound(ItemRollingStock.setPersistentData(event.crafting, null, null,event.player).getTagCompound());
          //TraincraftSaveHandler.writeValue(FMLCommonHandler.instance().getMinecraftServerInstance(), "numberOfTrains:", "" + newID);
        }
      }
    }
  }
  
  @SubscribeEvent
	public void onSmelting(PlayerEvent.ItemSmeltedEvent event)
  {
		for (AchievementIDs ach : AchievementIDs.values())
    {
      Item[] items = ach.getItems();
      if (items != null) {
        for (int i = 0; i < items.length; i++) {
					if (items[i] == event.smelting.getItem()) {
						event.player.addStat(ach.achievement, 1);
          }
        }
      }
    }
  }
}