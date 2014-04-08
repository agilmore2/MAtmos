package eu.ha3.matmos.game.system;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.src.ResourceLocation;

/*
--filenotes-placeholder
*/

public class NoAttenuationSound extends PositionedSoundRecord
{
	public NoAttenuationSound(ResourceLocation r, float a, float b, float c, float d, float e)
	{
		super(r, a, b, c, d, e);
		this.field_147666_i = ISound.AttenuationType.NONE;
	}
}
