package me.varmetek.munchymc.backend;

import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public final class  Rares
{
	private Rares(){}
	//static final Set<CustomItem> rares = new HashSet<>();
	public	static final Map<RareType, List<CustomItemRare>>  rares = new LinkedHashMap<>();
	static final List<CustomItemRare> allRares = new LinkedList<>();

	public static List<CustomItemRare> getRares()
	{
		if(allRares.isEmpty())
		{
			for(List<CustomItemRare> ee : rares.values())
			{
				allRares.addAll(ee);
			}
		}
		return new LinkedList<>(allRares);
	}

	public  static List<CustomItemRare>  getRares(RareType type)
{
	return new LinkedList<>(rares.get(type));
}

	public static boolean isRare(RareType type, CustomItemRare ci)
	{
		return ci.getRareType().equals(type);
	}


	public static boolean checkItem(final ItemStack item, CustomItemRare rare, boolean testMode)
	{

		if(rare == null || !rare.isRare()) return false;
		if(item == null) return false;
		ItemStack clone = item.clone();
		ItemStack rarer = rare.getItem();
		clone.setAmount(1);
		if(testMode){
			return clone.getType().equals(rarer.getType()) && clone.getItemMeta().getDisplayName().equals(rarer.getItemMeta().getDisplayName());
		}else
			return clone.isSimilar(rarer);
	}
}
