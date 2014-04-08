package eu.ha3.matmos.game.data.modules;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.Minecraft;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.EnumMovingObjectType;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.MODULE_CONSTANTS;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;
import eu.ha3.matmos.game.system.MAtmosUtility;

/*
--filenotes-placeholder
*/

public class L__legacy_hitscan extends ModuleProcessor implements Module
{
	private final Map<EnumMovingObjectType, String> equiv = new HashMap<EnumMovingObjectType, String>();
	
	public L__legacy_hitscan(Data data)
	{
		super(data, "legacy_hitscan");
		
		// The ordinal values was different back then, "0" was the block.
		this.equiv.put(null, "-1");
		this.equiv.put(EnumMovingObjectType.ENTITY, "1");
		this.equiv.put(EnumMovingObjectType.TILE, "0");
	}
	
	@Override
	protected void doProcess()
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit == null)
		{
			setValue("mouse_over_something", false);
			setValue("mouse_over_what_remapped", -1);
			setValue("block_as_number", MODULE_CONSTANTS.LEGACY_NO_BLOCK_IN_THIS_CONTEXT);
			setValue("meta_as_number", MODULE_CONSTANTS.LEGACY_NO_BLOCK_IN_THIS_CONTEXT);
			
			return;
		}
		
		setValue("mouse_over_something", mc.objectMouseOver.typeOfHit !=null);
		setValue("mouse_over_what_remapped", this.equiv.get(mc.objectMouseOver.typeOfHit));
		setValue(
			"block_as_number",
			mc.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE
				? MAtmosUtility.legacyOf(MAtmosUtility.getBlockAt(
					mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ))
				: MODULE_CONSTANTS.LEGACY_NO_BLOCK_IN_THIS_CONTEXT);
		setValue(
			"meta_as_number",
			mc.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE ? MAtmosUtility.getMetaAt(
				mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ,
				MODULE_CONSTANTS.LEGACY_NO_BLOCK_OUT_OF_BOUNDS) : MODULE_CONSTANTS.LEGACY_NO_BLOCK_IN_THIS_CONTEXT);
	}
}