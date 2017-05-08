package me.varmetek.munchymc.shopping;

import me.varmetek.munchymc.util.UtilInventory;
import org.apache.commons.lang3.Validate;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

/**
 * Created by XDMAN500 on 3/4/2017.
 */
public class ItemIcon implements  Cloneable
{
	private final ItemStack icon;
	//	private final int lore;

	public ItemIcon(ItemStack item){
		icon = toIcon(item);
			/*if(icon.getItemMeta().hasLore()){
				lore =	icon.getItemMeta().getLore().size();
			}else{
				lore =0;
				icon.getItemMeta().setLore( new ArrayList<>(10));
			}*/

	}

	private ItemStack toIcon(ItemStack input){
		Validate.isTrue(UtilInventory.isItemEmpty(input));
		input = input.clone();
		input.setAmount(1);
		return input;
	}

		/*public void  setText(String s){
			Validate.isTrue(StringUtils.isNotEmpty(s));
			List<String> lines  =icon.getItemMeta().getLore();

			if(lines.size() > lore){
				for(int i = lore -1; i< lines.size(); i++){
					lines.remove(i);
				}
			}

			lines.addAll(Arrays.asList(s.split("\\n")));
			icon.getItemMeta().setLore(lines);

		}*/
	/***
	 * Returns a copy of the icon
	 * **/
	public ItemStack toItem(){
		return icon.clone();
	}


	/***
	 *
	 * returns an altered copy of the icon based on the function provided
	 *
	 */

	public ItemStack toItem(Function<ItemStack,ItemStack> func){
		return func.apply(icon.clone());
	}
	public int hashCode(){
		return  icon.hashCode();
	}

	public boolean equals(ItemStack input){
		return equals(new ItemIcon(input));

	}
	public boolean equals(ItemIcon input){
		return this.hashCode() == input.hashCode();

	}

	public ItemIcon clone(){
		try
		{
			return (ItemIcon)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return null;
	}
}
