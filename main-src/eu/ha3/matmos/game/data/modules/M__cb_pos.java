package eu.ha3.matmos.game.data.modules;

import net.minecraft.src.Minecraft;
import net.minecraft.src.Entity;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;

/*
--filenotes-placeholder
*/

public class M__cb_pos extends ModuleProcessor implements Module
{
	public M__cb_pos(Data data)
	{
		super(data, "cb_pos");
	}
	
	@Override
	protected void doProcess()
	{
		Entity e = Minecraft.getMinecraft().thePlayer;
		setValue("x", (long) Math.floor(e.posX));
		setValue("y", (long) Math.floor(e.posY));
		setValue("z", (long) Math.floor(e.posZ));
	}
}
