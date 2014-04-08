package eu.ha3.matmos.game.user;

import net.minecraft.src.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.KeyBinding;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import eu.ha3.easy.TimeStatistic;
import eu.ha3.matmos.game.gui.MAtGuiMenu;
import eu.ha3.matmos.game.system.MAtMod;
import eu.ha3.matmos.game.system.MAtmosUtility;
import eu.ha3.mc.convenience.Ha3HoldActions;
import eu.ha3.mc.convenience.Ha3KeyHolding;
import eu.ha3.mc.convenience.Ha3KeyManager;
import eu.ha3.mc.haddon.supporting.SupportsFrameEvents;
import eu.ha3.mc.haddon.supporting.SupportsKeyEvents;
import eu.ha3.mc.haddon.supporting.SupportsTickEvents;
import eu.ha3.mc.quick.chat.ChatColorsSimple;
import eu.ha3.mc.quick.keys.KeyWatcher;

/* x-placeholder */

public class UserControl implements Ha3HoldActions, SupportsTickEvents, SupportsFrameEvents, SupportsKeyEvents
{
	private final MAtMod mod;
	private final KeyWatcher watcher = new KeyWatcher(this);
	private final Ha3KeyManager keyManager = new Ha3KeyManager();
	
	private KeyBinding keyBindingMain;
	private VolumeScroller scroller;
	
	private int loadingCount;
	
	public UserControl(MAtMod mod)
	{
		this.mod = mod;
	}
	
	public void load()
	{
		// new KeyBinding registers it rightaway to the list of keys
		this.keyBindingMain = new KeyBinding("key.matmos", 65);
		Minecraft.getMinecraft().gameSettings.keyBindings =
			ArrayUtils.addAll(Minecraft.getMinecraft().gameSettings.keyBindings, this.keyBindingMain);
		this.watcher.add(this.keyBindingMain);
		this.keyBindingMain.keyCode = this.mod.getConfig().getInteger("key.code");
		KeyBinding.resetKeyBindingArrayAndHash();
		
		this.scroller = new VolumeScroller(this.mod);
		this.keyManager.addKeyBinding(this.keyBindingMain, new Ha3KeyHolding(this, 7));
	}
	
	private String getKeyBindingMainFriendlyName()
	{
		if (this.keyBindingMain == null)
			return "undefined";
		
		return Keyboard.getKeyName(this.keyBindingMain.keyCode); // OBF getKeyCode(), or .keyCode
	}
	
	@Override
	public void onKey(KeyBinding event)
	{
		this.keyManager.handleKeyDown(event);
	}
	
	@Override
	public void onTick()
	{
		this.watcher.onTick();
		this.keyManager.handleRuntime();
		
		this.scroller.routine();
		if (this.scroller.isRunning())
		{
			this.mod.getGlobalVolumeControl().setVolumeAndUpdate(this.scroller.getValue());
		}
	}
	
	@Override
	public void onFrame(float fspan)
	{
		this.scroller.draw(fspan);
	}
	
	private void printUnusualMessages()
	{
		if (!this.mod.isInitialized())
		{
			this.mod.getChatter().printChat(ChatColorsSimple.COLOR_RED, "Unknown error: MAtmos isn't initialized");
		}
		else
		{
			if (!MAtmosUtility.isSoundMasterEnabled())
			{
				this.mod.getChatter().printChat(
					ChatColorsSimple.COLOR_RED, "Warning: ", ChatColorsSimple.COLOR_WHITE,
					"Sounds are turned off in your game settings!");
			}
			if (!MAtmosUtility.isSoundAmbientEnabled())
			{
				this.mod.getChatter().printChat(
					ChatColorsSimple.COLOR_RED, "Warning: ", ChatColorsSimple.COLOR_WHITE,
					"Ambient sounds are at 0% volume in the advanced MAtmos options menu!");
			}
		}
	}
	
	@Override
	public void beginHold()
	{
		if (this.mod.getConfig().getBoolean("reversed.controls"))
		{
			displayMenu();
		}
		else if (this.mod.isActivated())
		{
			this.scroller.start();
		}
	}
	
	@Override
	public void shortPress()
	{
		if (this.mod.getConfig().getBoolean("reversed.controls"))
		{
			whenWantsToggle();
		}
		else
		{
			if (!this.mod.isActivated())
			{
				whenWantsToggle();
			}
			else
			{
				displayMenu();
			}
		}
		
		printUnusualMessages();
	}
	
	@Override
	public void endHold()
	{
		if (this.scroller.isRunning())
		{
			this.scroller.stop();
			this.mod.getConfig().setProperty("globalvolume.scale", this.mod.getGlobalVolumeControl().getVolume());
			this.mod.saveConfig();
		}
		
		whenWantsForcing();
		printUnusualMessages();
		
	}
	
	private void whenWantsToggle()
	{
		if (this.mod.isActivated())
		{
			this.mod.deactivate();
			this.mod.getChatter().printChat(
				ChatColorsSimple.COLOR_YELLOW, "Stopped. Press ", ChatColorsSimple.COLOR_WHITE,
				getKeyBindingMainFriendlyName(), ChatColorsSimple.COLOR_YELLOW, " to re-enable.");
			
		}
		else if (this.mod.isInitialized())
		{
			if (this.loadingCount != 0)
			{
				this.mod.getChatter().printChat(ChatColorsSimple.COLOR_BRIGHTGREEN, "Loading...");
			}
			else
			{
				this.mod.getChatter().printChat(
					ChatColorsSimple.COLOR_BRIGHTGREEN, "Loading...", ChatColorsSimple.COLOR_YELLOW, " (Hold ",
					ChatColorsSimple.COLOR_WHITE, getKeyBindingMainFriendlyName() + " down",
					ChatColorsSimple.COLOR_YELLOW, " to tweak the volume)");
			}
			
			this.loadingCount++;
			this.mod.activate();
			
		}
		else if (!this.mod.isInitialized())
		{
			whenUninitializedAction();
		}
		
	}
	
	private void whenUninitializedAction()
	{
		if (this.mod.isInitialized())
			return;
		
		TimeStatistic stat = new TimeStatistic();
		this.mod.initializeAndEnable();
		this.mod.getChatter().printChat(
			ChatColorsSimple.COLOR_BRIGHTGREEN, "Loading for the first time (" + stat.getSecondsAsString(2) + "s)");
	}
	
	private void whenWantsForcing()
	{
		if (!this.mod.isActivated() && this.mod.isInitialized())
		{
			TimeStatistic stat = new TimeStatistic();
			this.mod.reloadEverything();
			this.mod.activate();
			this.mod.getChatter().printChat(
				ChatColorsSimple.COLOR_BRIGHTGREEN, "Reloading expansions (" + stat.getSecondsAsString(2) + "s)");
		}
		else if (!this.mod.isInitialized())
		{
			whenUninitializedAction();
		}
	}
	
	private void displayMenu()
	{
		if (this.mod.isActivated() && this.mod.util().isCurrentScreen(null))
		{
			// OBF displayGuiScreen
			Minecraft.getMinecraft().displayGuiScreen(
				new MAtGuiMenu((GuiScreen) this.mod.util().getCurrentScreen(), this.mod));
		}
	}
	
	@Override
	public void beginPress()
	{
	}
	
	@Override
	public void endPress()
	{
	}
	
}
