package eu.ha3.matmos.game.system;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.src.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.ResourceLocation;

/*
--filenotes-placeholder
*/

public class SoundHelper implements SoundCapabilities
{
	protected SoundAccessor accessor;
	protected Map<String, StreamingSound> streaming;
	
	private float volumeModulator;
	
	private boolean isInterrupt;
	
	public SoundHelper(SoundAccessor accessor)
	{
		this.accessor = accessor;
		this.streaming = new LinkedHashMap<String, StreamingSound>();
	}
	
	@Override
	public void playMono(String event, double xx, double yy, double zz, float volume, float pitch)
	{
		if (this.isInterrupt)
			return;
		
		// XXX 2014-01-10 what is the last boolean?
		playUnattenuatedSound(xx, yy, zz, event, volume * this.volumeModulator, pitch);
	}
	
	@Override
	public void playStereo(String event, float volume, float pitch)
	{
		if (this.isInterrupt)
			return;
		
		// Play the sound 2048 blocks above the player to keep support of mono sounds
		Entity e = Minecraft.getMinecraft().thePlayer;
		playUnattenuatedSound(e.posX, e.posY + 2048, e.posZ, event, volume * this.volumeModulator, pitch);
	}
	
	private void playUnattenuatedSound(double xx, double yy, double zz, String loc, float a, float b)
	{
		NoAttenuationSound nas =
			new NoAttenuationSound(new ResourceLocation(loc), a, b, (float) xx, (float) yy, (float) zz);
		
		Minecraft.getMinecraft().getSoundHandler().playSound(nas);
	}
	
	@Override
	public void registerStreaming(
		String customName, String path, float volume, float pitch, boolean isLooping, boolean usesPause)
	{
		if (this.isInterrupt)
			return;
		
		StreamingSound sound =
			new StreamingSoundUsingAccessor(
				this.accessor, path, volume * this.volumeModulator, pitch, isLooping, usesPause);
		this.streaming.put(customName, sound);
	}
	
	@Override
	public void playStreaming(String customName, int fadeIn)
	{
		if (this.isInterrupt)
			return;
		
		if (!this.streaming.containsKey(customName))
		{
			IDontKnowHowToCode.warnOnce("Tried to play missing stream " + customName);
			return;
		}
		
		this.streaming.get(customName).play(fadeIn);
	}
	
	@Override
	public void stopStreaming(String customName, int fadeOut)
	{
		if (this.isInterrupt)
			return;
		
		if (!this.streaming.containsKey(customName))
		{
			IDontKnowHowToCode.warnOnce("Tried to stop missing stream " + customName);
			return;
		}
		
		this.streaming.get(customName).stop(fadeOut);
	}
	
	@Override
	public void stop()
	{
		if (this.isInterrupt)
			return;
		
		for (StreamingSound sound : this.streaming.values())
		{
			sound.stop(0f);
		}
	}
	
	@Override
	public void applyVolume(float volumeMod)
	{
		this.volumeModulator = volumeMod;
		for (StreamingSound sound : this.streaming.values())
		{
			sound.applyVolume(volumeMod);
		}
	}
	
	@Override
	public void interrupt()
	{
		this.isInterrupt = true;
	}
	
	@Override
	public void cleanUp()
	{
		if (this.isInterrupt)
			return;
		
		for (StreamingSound sound : this.streaming.values())
		{
			sound.dispose();
		}
		this.streaming.clear();
	}
	
}
