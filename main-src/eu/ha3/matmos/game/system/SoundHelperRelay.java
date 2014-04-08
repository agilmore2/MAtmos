package eu.ha3.matmos.game.system;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.Minecraft;
import net.minecraft.src.Entity;
import eu.ha3.matmos.engine.core.interfaces.SoundRelay;

/*
--filenotes-placeholder
*/

public class SoundHelperRelay extends SoundHelper implements SoundRelay
{
	private static int streamingToken;
	private Map<String, String> paths;
	
	public SoundHelperRelay(SoundAccessor accessor)
	{
		super(accessor);
		
		this.paths = new HashMap<String, String>();
	}
	
	@Override
	public void routine()
	{
	}
	
	@Override
	public void cacheSound(String path)
	{
		String dotted = path.replace(".ogg", "").replace('/', '.').replaceAll("[0-9]", "");
		this.paths.put(path, dotted);
	}
	
	@Override
	public void playSound(String path, float volume, float pitch, int meta)
	{
		// XXX 2014-01-12 TEMPORARY: USE MONO-STEREO
		//playStereo(this.paths.get(path), volume, pitch);
		Entity e = Minecraft.getMinecraft().thePlayer;
		if (meta <= 0)
		{
			playStereo(this.paths.get(path), volume, pitch);
		}
		else
		{
			playMono(this.paths.get(path), e.posX, e.posY, e.posZ, volume, pitch);
		}
	}
	
	@Override
	public int getNewStreamingToken()
	{
		return SoundHelperRelay.streamingToken++;
	}
	
	@Override
	public boolean setupStreamingToken(
		int token, String path, float volume, float pitch, boolean isLooping, boolean usesPause)
	{
		//registerStreaming(tokenToString(token), path, volume, pitch, isLooping, usesPause);
		return true;
	}
	
	public String tokenToString(int token)
	{
		return token + "_";
	}
	
	@Override
	public void startStreaming(int token, float fadeDuration)
	{
		//playStreaming(tokenToString(token), (int) (fadeDuration * 1000));
	}
	
	@Override
	public void stopStreaming(int token, float fadeDuration)
	{
		//stopStreaming(tokenToString(token), (int) (fadeDuration * 1000));
	}
	
	@Override
	public void eraseStreamingToken(int token)
	{
	}
}
