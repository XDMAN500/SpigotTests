package me.varmetek.munchymc.backend.test;

import me.varmetek.core.item.CustomItem;
import me.varmetek.munchymc.backend.RareType;
import me.varmetek.munchymc.backend.Rares;
import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public abstract class CustomItemRare extends CustomItem
{
	protected RareType type;



	public CustomItemRare (String name, RareType type){

		super(name);
		this.type = type;


		if(type != RareType.SERVER)
		{
			if (!Rares.rares.containsKey(type))
				Rares.rares.put(type, new ArrayList<>(10));
			Rares.rares.get(type).add(this);
		}



	}
	public boolean isRare()
	{
		return type!=  RareType.SERVER;
	}
	public RareType getRareType()
	{
		return type;
	}

	public static ItemStack buildCustomLeather(Color col, String title, EquipmentSlot at)
	{
		Validate.notNull(col); Validate.notNull(title); Validate.notNull(at);

		ItemStack item = null;
		switch (at)
		{
			case HEAD: item = new ItemStack(Material.LEATHER_HELMET); break;
			case FEET: item = new ItemStack(Material.LEATHER_BOOTS);break;
			case CHEST:  item =new ItemStack(Material.LEATHER_CHESTPLATE);break;
			case LEGS:  item = new ItemStack(Material.LEATHER_LEGGINGS);break;
			default: return item;
		}
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 7);
		LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();

		lam.setUnbreakable(true);
		lam.setDisplayName(Utils.colorCode(title));
		lam.setColor(col);
		item.setItemMeta(lam);

		return item;

	}
}
