package eu.ha3.matmos.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import javax.swing.UIManager;

import com.google.gson.stream.MalformedJsonException;

import eu.ha3.matmos.editor.interfaces.Editor;
import eu.ha3.matmos.editor.interfaces.Window;
import eu.ha3.matmos.editor.tree.Selector;
import eu.ha3.matmos.engine.core.implem.ProviderCollection;
import eu.ha3.matmos.expansions.debugunit.ReadOnlyJasonStringEDU;
import eu.ha3.matmos.jsonformat.serializable.expansion.SerialRoot;
import eu.ha3.matmos.pluggable.PluggableIntoMinecraft;
import eu.ha3.matmos.pluggable.UnpluggedListener;
import eu.ha3.matmos.tools.Jason;
import eu.ha3.matmos.tools.JasonExpansions_Engine1Deserializer2000;

/*
--filenotes-placeholder
*/

public class EditorMaster implements Runnable, Editor, UnpluggedListener
{
	//private IEditorWindow __WINDOW;
	private Window window__EventQueue;
	
	private final PluggableIntoMinecraft minecraft;
	private boolean isUnplugged;
	
	private File file;
	private File workingDirectory = new File(System.getProperty("user.dir"));
	private SerialRoot root = new SerialRoot();
	private boolean hasModifiedContents;
	
	public EditorMaster()
	{
		this(null);
	}
	
	public EditorMaster(PluggableIntoMinecraft minecraft)
	{
		this(minecraft, null);
	}
	
	public EditorMaster(PluggableIntoMinecraft minecraft, File fileIn)
	{
		File potentialFile = fileIn;
		
		this.minecraft = minecraft;
		if (minecraft != null)
		{
			minecraft.addUnpluggedListener(this);
			
			File fileIF = minecraft.getFileIfAvailable();
			File workingDirectoryIF = minecraft.getWorkingDirectoryIfAvailable();
			if (fileIF != null && workingDirectoryIF != null)
			{
				potentialFile = fileIF;
				this.workingDirectory = workingDirectoryIF;
			}
		}
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		this.root = new SerialRoot();
		this.hasModifiedContents = false;
		this.file = potentialFile;
	}
	
	@Override
	public void run()
	{
		System.out.println("Loading designer...");
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				initializedWindow(new EditorWindow(EditorMaster.this));
			}
		});
	}
	
	private void initializedWindow(EditorWindow editorWindow)
	{
		//this.__WINDOW = editorWindow;
		this.window__EventQueue = new WindowEventQueue(editorWindow);
		this.window__EventQueue.display();
		
		System.out.println("Loaded.");
		
		if (this.file != null)
		{
			trySetAndLoadFile(this.file);
		}
		
		if (this.minecraft instanceof ReadOnlyJasonStringEDU)
		{
			flushFileAndSerial();
			this.root =
				new JasonExpansions_Engine1Deserializer2000().jsonToSerial(((ReadOnlyJasonStringEDU) this.minecraft)
					.obtainJasonString());
			updateFileAndContentsState();
		}
	}
	
	public static void main(String args[])
	{
		new EditorMaster().run();
	}
	
	@Override
	public boolean isMinecraftControlled()
	{
		return this.minecraft != null;
	}
	
	@Override
	public void trySetAndLoadFile(File potentialFile)
	{
		if (potentialFile == null)
		{
			showErrorPopup("Missing file pointer.");
			return;
		}
		
		if (potentialFile.isDirectory())
		{
			showErrorPopup("The selected file is actually a directory.");
			return;
		}
		
		if (!potentialFile.exists())
		{
			showErrorPopup("The selected file is inaccessible.");
			return;
		}
		
		try
		{
			loadFile(potentialFile);
			this.file = potentialFile;
		}
		catch (Exception e)
		{
			this.file = null;
			
			showErrorPopup("File could not be loaded:\n" + e.getLocalizedMessage());
			reset();
			updateFileAndContentsState();
		}
	}
	
	private void reset()
	{
		flushFileAndSerial();
		modelize();
	}
	
	private void flushFileAndSerial()
	{
		this.file = null;
		this.root = new SerialRoot();
		this.hasModifiedContents = false;
		this.window__EventQueue.setEditFocus(null, null, false);
	}
	
	private void modelize()
	{
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
			}
		});
	}
	
	private void loadFile(File potentialFile) throws IOException, MalformedJsonException
	{
		flushFileAndSerial();
		String jasonString = new Scanner(new FileInputStream(potentialFile)).useDelimiter("\\Z").next();
		System.out.println(jasonString);
		this.root = new JasonExpansions_Engine1Deserializer2000().jsonToSerial(jasonString);
		this.hasModifiedContents = false;
		updateFileAndContentsState();
	}
	
	private void updateFileAndContentsState()
	{
		this.window__EventQueue.refreshFileState();
		this.window__EventQueue.updateSerial(EditorMaster.this.root);
	}
	
	private void updateFileState()
	{
		this.window__EventQueue.refreshFileState();
	}
	
	private void showErrorPopup(String error)
	{
		this.window__EventQueue.showErrorPopup(error);
	}
	
	@Override
	public void minecraftReloadFromDisk()
	{
		if (!isPlugged())
			return;
		
		this.minecraft.reloadFromDisk();
	}
	
	@Override
	public boolean hasValidFile()
	{
		return this.file != null;
	}
	
	@Override
	public ProviderCollection getProviderCollectionIfAvailable()
	{
		if (!isPlugged())
			return null;
		
		return this.minecraft.getProviders();
	}
	
	@Override
	public File getWorkingDirectory()
	{
		return this.workingDirectory;
	}
	
	@Override
	public boolean hasUnsavedChanges()
	{
		return this.hasModifiedContents;
	}
	
	@Override
	public File getFile()
	{
		return this.file;
	}
	
	@Override
	public String generateJson(boolean pretty)
	{
		return pretty ? Jason.toJsonPretty(this.root) : Jason.toJson(this.root);
	}
	
	@Override
	public void minecraftPushCurrentState()
	{
		if (!isPlugged())
			return;
		
		this.minecraft.pushJason(Jason.toJson(this.root));
	}
	
	@Override
	public boolean longSave(File location, boolean setAsNewPointer)
	{
		boolean success = writeToFile(location);
		
		if (success && setAsNewPointer)
		{
			this.file = location;
			this.hasModifiedContents = false;
			updateFileState();
		}
		
		return success;
	}
	
	@Override
	public boolean quickSave()
	{
		if (!hasValidFile())
			return false;
		
		boolean success = writeToFile(this.file);
		if (success)
		{
			this.hasModifiedContents = false;
			updateFileState();
		}
		return success;
	}
	
	private boolean writeToFile(File fileToWrite)
	{
		try
		{
			if (!fileToWrite.exists())
			{
				fileToWrite.createNewFile();
			}
			
			FileWriter write = new FileWriter(fileToWrite);
			write.append(Jason.toJsonPretty(this.root));
			write.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			this.window__EventQueue.showErrorPopup("Writing to disk resulted in an error: " + e.getLocalizedMessage());
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onUnpluggedEvent(PluggableIntoMinecraft pluggable)
	{
		this.isUnplugged = true;
		this.window__EventQueue.disableMinecraftCapabilitites();
	}
	
	@Override
	public boolean isPlugged()
	{
		return isMinecraftControlled() && !this.isUnplugged;
	}
	
	@Override
	public File getExpansionDirectory()
	{
		return new File(this.workingDirectory, "assets/matmos/expansions").exists() ? new File(
			this.workingDirectory, "assets/matmos/expansions") : this.workingDirectory;
	}
	
	@Override
	public File getSoundDirectory()
	{
		return new File(this.workingDirectory, "assets/minecraft/sounds").exists() ? new File(
			this.workingDirectory, "assets/minecraft/sounds") : this.workingDirectory;
	}
	
	@Override
	public void switchEditItem(Selector selector, String itemName)
	{
		Map<String, ? extends Object> map = null;
		
		switch (selector)
		{
		case CONDITION:
			map = this.root.condition;
			break;
		case SET:
			map = this.root.set;
			break;
		case MACHINE:
			map = this.root.machine;
			break;
		case LIST:
			map = this.root.list;
			break;
		case DYNAMIC:
			map = this.root.dynamic;
			break;
		case EVENT:
			map = this.root.event;
			break;
		case LOGIC:
			break;
		case SUPPORT:
			break;
		default:
			break;
		}
		
		if (map != null && map.containsKey(itemName))
		{
			this.window__EventQueue.setEditFocus(itemName, map.get(itemName), false);
		}
	}
	
	@Override
	public void renameItem(String oldName, Object editFocus, String newName)
	{
		if (oldName.equals(newName))
			return;
		
		if (newName == null || newName.equals("") || newName.contains("\"") || newName.contains("\\"))
		{
			showErrorPopup("Name must not be empty or include the characters \" and \\");
			return;
		}
		
		try
		{
			SerialManipulator.rename(this.root, editFocus, oldName, newName);
			flagChange(true);
			this.window__EventQueue.setEditFocus(newName, editFocus, true);
		}
		catch (ItemNamingException e)
		{
			showErrorPopup(e.getMessage());
		}
	}
	
	@Override
	public void deleteItem(String nameOfItem, Object editFocus)
	{
		try
		{
			SerialManipulator.delete(this.root, editFocus, nameOfItem);
			flagChange(true);
			this.window__EventQueue.setEditFocus(null, null, false);
		}
		catch (ItemNamingException e)
		{
			showErrorPopup(e.getMessage());
		}
	}
	
	private void flagChange(boolean treeWasDeeplyModified)
	{
		boolean previousStateIsFalse = !this.hasModifiedContents;
		this.hasModifiedContents = true;
		
		if (treeWasDeeplyModified)
		{
			updateFileAndContentsState();
		}
		else
		{
			if (previousStateIsFalse)
			{
				updateFileState();
			}
		}
	}
	
	@Override
	public boolean createItem(KnowledgeItemType choice, String name)
	{
		try
		{
			Object o = SerialManipulator.createNew(this.root, choice, name);
			flagChange(true);
			this.window__EventQueue.setEditFocus(name, o, true);
			return true;
		}
		catch (ItemNamingException e)
		{
			showErrorPopup(e.getMessage());
		}
		
		return false;
	}
	
	@Override
	public void informInnerChange()
	{
		flagChange(false);
	}
	
	@Override
	public SerialRoot getRootForCopyPurposes()
	{
		return this.root;
	}
}
