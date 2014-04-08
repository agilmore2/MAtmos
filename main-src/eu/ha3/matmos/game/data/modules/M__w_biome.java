package eu.ha3.matmos.game.data.modules;

import net.minecraft.src.Minecraft;
import net.minecraft.src.MathHelper;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Chunk;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.Module;
import eu.ha3.matmos.game.data.abstractions.module.ModuleProcessor;
import eu.ha3.matmos.game.system.MAtMod;

/*
--filenotes-placeholder
*/

public class M__w_biome extends ModuleProcessor implements Module
{
	private final MAtMod mod;
	
	public M__w_biome(Data data, MAtMod mod)
	{
		super(data, "w_biome");
		this.mod = mod;
	}
	
	@Override
	protected void doProcess()
	{
		int biomej = this.mod.getConfig().getInteger("useroptions.biome.override");
		if (biomej <= -1)
		{
			setValue("id", calculateBiome().biomeID);
			setValue("_name_debugonly", calculateBiome().biomeName);
		}
		else
		{
			setValue("id", biomej);
			setValue("_name_debugonly", "");
		}
	}
	
	private BiomeGenBase calculateBiome()
	{
		Minecraft mc = Minecraft.getMinecraft();
		int x = MathHelper.floor_double(mc.thePlayer.posX);
		int z = MathHelper.floor_double(mc.thePlayer.posZ);
		
		Chunk chunk = mc.theWorld.getChunkFromBlockCoords(x, z);
		return chunk.getBiomeGenForWorldCoords(x & 15, z & 15, mc.theWorld.getWorldChunkManager());
	}
}
