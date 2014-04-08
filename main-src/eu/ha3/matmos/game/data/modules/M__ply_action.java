package eu.ha3.matmos.game.data.modules;

import net.minecraft.src.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;

/*
--filenotes-placeholder
*/

public class M__ply_action extends ModuleProcessor implements Module
{
	public M__ply_action(Data data)
	{
		super(data, "ply_action");
	}
	
	@Override
	protected void doProcess()
	{
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		
		setValue("swing_progress16", (int) Math.floor(player.swingProgress * 16));
		setValue("swinging", player.swingProgress != 0);
		setValue("fall_distance1k", (int) (player.fallDistance * 1000));
		setValue("item_use_duration", player.getItemInUseDuration());
	}
}
