package eu.ha3.matmos.game.data.modules;

import net.minecraft.src.Minecraft;
import net.minecraft.src.World;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.MODULE_CONSTANTS;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;
import eu.ha3.matmos.game.system.MAtmosUtility;

/*
--filenotes-placeholder
*/

public class M__cb_column extends ModuleProcessor implements Module
{
	public M__cb_column(Data data)
	{
		super(data, "cb_column");
	}
	
	@Override
	protected void doProcess()
	{
		World w = Minecraft.getMinecraft().theWorld;
		
		int x = MAtmosUtility.getPlayerX();
		int y = MAtmosUtility.getPlayerY();
		int z = MAtmosUtility.getPlayerZ();
		
		setValue("y-1", MAtmosUtility.getNameAt(x, y - 1, z, MODULE_CONSTANTS.NO_BLOCK_OUT_OF_BOUNDS));
		setValue("y-2", MAtmosUtility.getNameAt(x, y - 2, z, MODULE_CONSTANTS.NO_BLOCK_OUT_OF_BOUNDS));
		setValue("y0", MAtmosUtility.getNameAt(x, y + 0, z, MODULE_CONSTANTS.NO_BLOCK_OUT_OF_BOUNDS));
		setValue("y1", MAtmosUtility.getNameAt(x, y + 1, z, MODULE_CONSTANTS.NO_BLOCK_OUT_OF_BOUNDS));
		setValue("topmost_block", w.getTopSolidOrLiquidBlock(x, z));
		setValue("thickness_overhead", w.getTopSolidOrLiquidBlock(x, z) - y);
		setValue("can_rain_reach", w.canBlockSeeTheSky(x, y, z) && !(w.getTopSolidOrLiquidBlock(x, z) > y));
		// FIXME: 26 is unresolved
	}
}