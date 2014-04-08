package eu.ha3.matmos.game.data.modules;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.Minecraft;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.EnumMovingObjectType; // NO
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;

/*
--filenotes-placeholder
*/

public class S__ply_hitscan extends ModuleProcessor implements Module
{
	private final Map<EnumMovingObjectType, String> equiv = new HashMap<EnumMovingObjectType, String>();
	
	public S__ply_hitscan(Data data)
	{
		super(data, "ply_hitscan");
		this.equiv.put(null, "");
		this.equiv.put(EnumMovingObjectType.ENTITY, "entity");
		this.equiv.put(EnumMovingObjectType.TILE, "block");
	}
	
	@Override
	protected void doProcess()
	{
		Minecraft mc = Minecraft.getMinecraft();
		setValue("mouse_over_something", mc.objectMouseOver != null);
		setValue("mouse_over_what_remapped", this.equiv.get(mc.objectMouseOver.typeOfHit));
	}
}