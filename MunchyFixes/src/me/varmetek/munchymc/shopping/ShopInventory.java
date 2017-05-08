package me.varmetek.munchymc.shopping;

import com.google.common.collect.Maps;
import org.apache.commons.lang.Validate;

import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 3/4/2017.
 */
public class ShopInventory
{
	Map<ItemIcon,ShopEntry> storage = Maps.newHashMap();

	public  void addEntry(ShopEntry entry){
		Validate.isTrue(!isPosted(entry), "An identical item has already been posted");
			storage.put(entry.getIcon(),entry);

	}


	public  void removeEntry(ShopEntry entry){
		storage.remove(entry.getIcon());
	}

	public  void removeEntry(ItemIcon entry){
		storage.remove(entry);
	}

	public Optional<ShopEntry> getEntry(ItemIcon icon){

			return Optional.ofNullable(storage.get(icon));


	}

	public boolean isPosted(ShopEntry entry)
	{
		return storage.containsKey(entry);
	}

	public boolean isPosted(ItemIcon entry)
	{
		return storage.containsKey(entry);
	}
}
