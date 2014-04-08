package eu.ha3.matmos.game.data.modules;

import net.minecraft.src.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.MODULE_CONSTANTS;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;
import eu.ha3.matmos.game.system.MAtmosUtility;

/*
--filenotes-placeholder
*/

public class M__ply_inventory extends ModuleProcessor implements Module
{
	public M__ply_inventory(Data data)
	{
		super(data, "ply_inventory");
	}
	
	@Override
	protected void doProcess()
	{
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		
		setValue("held_slot", player.inventory.currentItem);
		setValue(
			"current_item",
			player.inventory.getCurrentItem() != null
				? MAtmosUtility.nameOf(player.inventory.getCurrentItem()) : MODULE_CONSTANTS.NO_ITEM); // NEW
		setValue(
			"current_damage",
			player.inventory.getCurrentItem() != null ? Integer.toString(player.inventory
				.getCurrentItem().getItemDamage()) : MODULE_CONSTANTS.NO_ITEM); // NEW
	}
}
