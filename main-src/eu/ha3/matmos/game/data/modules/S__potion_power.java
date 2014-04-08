package eu.ha3.matmos.game.data.modules;

import net.minecraft.src.PotionEffect;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.AbstractPotionQualityModule;

/*
--filenotes-placeholder
*/

public class S__potion_power extends AbstractPotionQualityModule
{
	public S__potion_power(Data data)
	{
		super(data, "potion_power");
	}
	
	@Override
	protected String getQuality(PotionEffect effect)
	{
		return Integer.toString(effect.getAmplifier() + 1);
	}
}