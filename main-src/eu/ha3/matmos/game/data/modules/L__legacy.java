package eu.ha3.matmos.game.data.modules;

import net.minecraft.src.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityMinecartEmpty;
import net.minecraft.src.EntityHorse;
import net.minecraft.src.ItemStack;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.MODULE_CONSTANTS;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;
import eu.ha3.matmos.game.system.MAtmosUtility;

/*
--filenotes-placeholder
*/

public class L__legacy extends ModuleProcessor implements Module
{
	public L__legacy(Data data)
	{
		super(data, "legacy");
	}
	
	@Override
	protected void doProcess()
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityClientPlayerMP player = mc.thePlayer;
		Entity ride = player.ridingEntity;
		
		setValue("player_health_ceil", (int) Math.ceil(player.getHealth()));
		setValue("world_nether", player.dimension == -1);
		setValue("player_current_item_as_number", number(player.inventory.getCurrentItem()));
		
		setValue("72000_minus_item_use_duration", 72000 - player.getItemInUseDuration());
		setValue("riding_minecart", ride != null && ride.getClass() == EntityMinecartEmpty.class);
		setValue("riding_boat", ride != null && ride.getClass() == EntityBoat.class);
		setValue("armor_0_as_number", number(player.inventory.armorInventory[0]));
		setValue("armor_1_as_number", number(player.inventory.armorInventory[1]));
		setValue("armor_2_as_number", number(player.inventory.armorInventory[2]));
		setValue("armor_3_as_number", number(player.inventory.armorInventory[3]));
		setValue("gui_instanceof_container", mc.currentScreen != null && mc.currentScreen instanceof GuiContainer);
		setValue("riding_horse", ride != null && ride instanceof EntityHorse);
		setValue("seed_higher", (int) (mc.theWorld.getSeed() >> 32));
		setValue("seed_lower", (int) (mc.theWorld.getSeed() & 0xFFFFFFFF));
	}
	
	private int number(ItemStack item)
	{
		return item != null ? MAtmosUtility.legacyOf(item) : MODULE_CONSTANTS.LEGACY_NO_ITEM;
	}
}