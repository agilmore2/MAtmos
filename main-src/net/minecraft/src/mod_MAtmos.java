package net.minecraft.src;

import eu.ha3.matmos.engine0.game.system.MAtMod;

/*
            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE 
                    Version 2, December 2004 

 Copyright (C) 2004 Sam Hocevar <sam@hocevar.net> 

 Everyone is permitted to copy and distribute verbatim or modified 
 copies of this license document, and changing it is allowed as long 
 as the name is changed. 

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE 
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION 

  0. You just DO WHAT THE FUCK YOU WANT TO. 
*/

public class mod_MAtmos extends HaddonBridgeModLoader
{
	public mod_MAtmos()
	{
		super(new MAtMod());
		
	}
	
	@Override
	public String getVersion()
	{
		return "r" + MAtMod.VERSION + " for " + MAtMod.FOR; // Remember to change the thing on MAtMod
		
	}
	
}