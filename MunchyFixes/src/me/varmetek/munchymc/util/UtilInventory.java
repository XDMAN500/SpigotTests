package me.varmetek.munchymc.util;

import me.varmetek.munchymc.MunchyMax;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by XDMAN500 on 12/27/2016.
 */
public final class UtilInventory
{
	private UtilInventory(){}


	public static final Set<Recipe> PakedRs = new HashSet<Recipe>();


	public static String getAlphaCode(ItemStack i){
		if(i == null ||Material.AIR == i.getType()){
			return "AIR";
		}
		if(i.getAmount() != 1){

			i = i.clone();
			i.setAmount(1);

		}
		return Integer.toString(i.hashCode(),Character.MAX_RADIX);

	}



	public static void cancelClickEvent(final InventoryClickEvent ev){
		Validate.notNull(ev);
		if(ev.getClick() != ClickType.NUMBER_KEY)return;
		ev.setCancelled(true);
		MunchyMax.getTaskHandler().run(()->{

			ev.getWhoClicked().getInventory().setItem(ev.getHotbarButton(),ev.getClickedInventory().getItem(ev.getSlot()));
			ev.getClickedInventory().setItem(ev.getSlot(),null);

		});
	}




	private static final List<EnchantmentTarget> whiteList = Arrays.asList(
			new EnchantmentTarget[] { EnchantmentTarget.ALL, EnchantmentTarget.TOOL});





	public static void combineEnchantments(ItemStack base, Map<Enchantment,Integer> enchs, boolean useWhiteList)
	{
		if(base  == null | enchs== null)return;
		if(base.getType() == Material.AIR || enchs.isEmpty())return;

			base.hashCode();
		for(Enchantment ench : enchs.keySet() )
		{
			int lvl = 	enchs.get(ench);

			int maxLvl = useWhiteList ? ( whiteList.contains(ench.getItemTarget()) ? 256 : ench.getMaxLevel() ) : 256;

			boolean isSameLevel = base.getEnchantmentLevel(ench) == lvl ;


			base.addUnsafeEnchantment(ench,
					base.containsEnchantment(ench)?
							( isSameLevel ? Math.min(lvl+1,maxLvl) :
									  Math.max(base.getEnchantmentLevel(ench) ,lvl) )
							: Math.min(lvl,maxLvl));

		}

	}




	public static ItemStack combineEnchantments(ItemStack base, ItemStack modifier, boolean useWhiteList,boolean ignoreItem)
	{
		if(base  == null | modifier== null)return base;
		if(base.getType() == Material.AIR ||modifier.getType() == Material.AIR)return base;
		if( !(ignoreItem ||  base.getType() == modifier.getType()))return base;

		if(base.getEnchantments().isEmpty() )return base;

		ItemStack result  = base.clone();


		for(Enchantment ench : modifier.getEnchantments().keySet() )
		{
			int lvl = 	modifier.getEnchantmentLevel(ench);

			int maxLvl = useWhiteList ? ( whiteList.contains(ench.getItemTarget()) ? 256 : ench.getMaxLevel() ) : 256;

			boolean isSameLevel = result.getEnchantmentLevel(ench) == lvl ;


			result.addUnsafeEnchantment(ench,
					result.containsEnchantment(ench)?
							( isSameLevel ? Math.min(lvl+1,maxLvl) :
									  Math.max(result.getEnchantmentLevel(ench) ,lvl) )
							: Math.min(lvl,maxLvl));

		}
		return result;
	}





	public static void  giveItem(ItemStack toGive, ClickType ct, Player pl, int button)
	{

		switch (ct)
		{

			case LEFT:
				pl.setItemOnCursor(toGive);
				break;
			case SHIFT_LEFT:
				pl.getInventory().addItem(toGive);
				break;
			case RIGHT:
				pl.setItemOnCursor(toGive);
				break;
			case SHIFT_RIGHT:
				pl.getInventory().addItem(toGive);
				break;
			case WINDOW_BORDER_LEFT:
				break;
			case WINDOW_BORDER_RIGHT:
				break;
			case MIDDLE:
				pl.setItemOnCursor(toGive);
				break;
			case NUMBER_KEY:
				if(pl.getInventory().getItem(button) == null)
					pl.getInventory().setItem(button,toGive);
				else
					pl.getInventory().addItem(toGive);
				break;
			case DOUBLE_CLICK:
				pl.setItemOnCursor(toGive);
				break;
			case DROP:
				break;
			case CONTROL_DROP:
				break;
			case CREATIVE:
				break;
			case UNKNOWN:
				break;
		}
	}




	public static boolean isSimular(@Nullable ItemStack prim, @Nullable ItemStack second){
		if(prim == null){
			return  second == prim;
		}else{
			return prim.isSimilar(second);
		}
	}




	public static int numberOfItem(Inventory inv, @Nullable  ItemStack item){
		if(inv == null) throw new IllegalArgumentException("Inventory Cannot be null");
		int amount = 0;
		for(ItemStack index: inv){
			if(isSimular(item,index)){
				if(item == null){
					amount++;
				}else{
					amount += index.getAmount();
				}
			}
		}
		return amount;
	}




	public static boolean verifyItem(ItemStack im , MaterialData d){

		return (!im.hasItemMeta() && im.getData().equals(d));
	}




	public static boolean verifyItem(ItemStack im , String d){

		return (im.hasItemMeta() && !d.isEmpty()) && getAlphaCode(im).equals(d);
	}





	public static boolean isItemEmpty(ItemStack i){
		return i ==null || i.getType() == Material.AIR || i.getAmount() <1;
	}




	public static Map<Integer, ItemStack> toMap(Inventory inv){
		HashMap<Integer, ItemStack>  map = new HashMap<>();
		for(int i = 0; i< inv.getSize(); i++){
			if(!UtilInventory.isItemEmpty(inv.getItem(i))){
				map.put(i,inv.getItem(i));
			}
		}
		return map;
	}

	public static boolean isEmpty(Collection<ItemStack> inv){
		if(inv.isEmpty()) return true;


		for(ItemStack i : inv){
			if(!isItemEmpty(i)){
				return false;
			}
		}
		return true;
	}
	public static boolean isEmpty(Inventory inv){



		for(ItemStack i : inv){
			if(!isItemEmpty(i)){
				return false;
			}
		}
		return true;
	}
  public static boolean isFull(Inventory inv){
	  return inv.firstEmpty() == -1;
  }

	public static boolean isFull(Collection<ItemStack> inv){
		if(inv.isEmpty()) return false;


		for(ItemStack i : inv){
			if(isItemEmpty(i)){
				return false;
			}
		}
		return true;
	}

	public static Inventory fromMap(Map<Integer, ItemStack>  map, Inventory inv){
		map.forEach((slot,item) -> inv.setItem(slot,item));
		return inv;
	}

	public static Inventory fromMap(MemorySection memSec, Inventory inv)
	{

		Map<Integer,ItemStack> map = new HashMap<>();

		for (String s : memSec.getKeys(false))
		{
			Integer in = Utils.getInt(s);
			if (in != null && memSec.isItemStack(s))
			{
				inv.setItem(in,memSec.getItemStack(s));
			}
		}
		return inv;
	}

	public static Map<Integer, ItemStack>  toInvMap(MemorySection memSec)
	{

		Map<Integer,ItemStack> map = new HashMap<>();

		for (String s : memSec.getKeys(false))
		{
			Integer in = Utils.getInt(s);
			if (in != null && memSec.isItemStack(s))
			{
				map.put(in.intValue(),memSec.getItemStack(s));
			}
		}
		return map;
	}

	public static int firstPartial(int materialId, Inventory inv) {
		ItemStack[] inventory = inv.getStorageContents();

		for(int i = 0; i < inventory.length; ++i) {
			ItemStack item = inventory[i];
			if(item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
				return i;
			}
		}

		return -1;
	}

	public static int firstPartial(Material material,Inventory inv) {
		Validate.notNull(material, "Material cannot be null");
		return firstPartial(material.getId(),inv);
	}

	public static int firstPartial(ItemStack item, Inventory  inv) {
		ItemStack[] inventory = inv.getStorageContents();

		if(item == null) {
			return -1;
		} else {
			for(int i = 0; i < inventory.length; ++i) {
				ItemStack cItem = inventory[i];
				if(cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item)) {
					return i;
				}
			}

			return -1;
		}
	}
	public static int firstPartial(Material material,Collection<ItemStack> inv) {
		Validate.notNull(material, "Material cannot be null");
		return firstPartial(material.getId(),inv);
	}


	public static int firstPartial(int materialId,  Collection<ItemStack> inv) {
		Iterator<ItemStack> inventory = inv.iterator();

		for(int i = 0; inventory.hasNext(); ++i) {
			ItemStack item = inventory.next();
			if(item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
				return i;
			}
		}

		return -1;
	}
	public static int firstPartial(ItemStack item, Collection<ItemStack> inv) {


		if(item == null) {
			return -1;
		} else {
			Iterator<ItemStack> inventory = inv.iterator();

			for(int i = 0; inventory.hasNext(); ++i) {
				ItemStack cItem = inventory.next();
				if(cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(item)) {
					return i;
				}
			}

			return -1;
		}
	}



	public static boolean canAccept(Inventory inv, ItemStack item){
		return inv.firstEmpty() != -1 || firstPartial(item,inv) != -1;
	}
  public static boolean canAccept(Inventory inv, Material item){
    return inv.firstEmpty() != -1 || firstPartial(item,inv) != -1;
  }


	public static  int getAmount(Inventory inv, Material item){
		Validate.notNull(inv);
		Validate.notNull(item);
		int amount = 0;

		for(ItemStack i: inv){
			if(isItemEmpty(i) || i.getType() != item)continue;
			amount += i.getAmount();
		}
		return amount;

	}


	public static List<String> getLore(ItemMeta im){
		Validate.notNull(im);
		List<String> lore = im.getLore();
		if(lore == null){
			lore = new ArrayList<String>();
		}
		return lore;
	}

	public static  int getAmount(Inventory inv, ItemStack item){
		int amount = 0;

		for(ItemStack i: inv){
			if(isItemEmpty(i) || !item.isSimilar(i)) continue;;
			amount += i.getAmount();
		}
		return amount;

	}



	public static Inventory convert2Inv(Collection<ItemStack> items){

		return convert2Inv(items,54);
	}
	public static Inventory convert2Inv(Collection<ItemStack> items , int minSize){
		int size = items.size();
		if(size > minSize){
			if (size % 9 != 0){
				size += 9 - size % 9;
			}
		}else{
			size = minSize;
		}
		Inventory inv = Bukkit.getServer().createInventory(null, size);

		items.forEach(
			item -> inv.addItem(item)
		);
		return inv;
	}
}
