package eu.ha3.matmos.game.data.abstractions.module;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.src.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import eu.ha3.matmos.engine.core.interfaces.Data;

/* x-placeholder */

/**
 * An abstract module that extracts all enchantments associated to an item
 * defined by the implementing class.
 * 
 * @author Hurry
 */
public abstract class AbstractEnchantmentModule extends ModuleProcessor implements Module
{
	private Set<String> oldThings = new LinkedHashSet<String>();
	
	public AbstractEnchantmentModule(Data dataIn, String name)
	{
		super(dataIn, name);
		dataIn.getSheet(name).setDefaultValue("0");
		dataIn.getSheet(name + ModuleProcessor.DELTA_SUFFIX).setDefaultValue("0");
	}
	
	@Override
	protected void doProcess()
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack item = getItem(player);
		
		for (String i : this.oldThings)
		{
			setValue(i, 0);
		}
		this.oldThings.clear();
		
		if (item != null && item.getEnchantmentTagList() != null && item.getEnchantmentTagList().tagCount() > 0)
		{
			int total = item.getEnchantmentTagList().tagCount();
			
			NBTTagList enchantments = item.getEnchantmentTagList();
			for (int i = 0; i < total; i++)
			{
				int id = ((NBTTagCompound) enchantments.tagAt(i)).getShort("id");
				
				short lvl = ((NBTTagCompound) enchantments.tagAt(i)).getShort("lvl");
				setValue(Integer.toString(id), Short.toString(lvl));
				this.oldThings.add(Integer.toString(id));
			}
		}
	}
	
	protected abstract ItemStack getItem(EntityPlayer player);
	
}
