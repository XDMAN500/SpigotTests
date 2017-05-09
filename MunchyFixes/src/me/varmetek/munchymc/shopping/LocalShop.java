package me.varmetek.munchymc.shopping;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by XDMAN500 on 3/1/2017.
 */
public class LocalShop
{
	ShopInventory sellShop = new ShopInventory();
	ShopInventory buyShop = new ShopInventory();
	final UUID player;

	public  LocalShop(UUID player){
		this.player = player;
	}

	private ShopInventory getShop(boolean selling){
		return (selling ? sellShop : buyShop);
	}
	public void postEntry(boolean selling, ShopEntry entry){
		getShop(selling).addEntry(entry);

	}

	public Optional<ShopEntry> getEntry(boolean selling, ItemIcon icon){
		return getShop(selling).getEntry(icon);
	}
	public Optional<ShopEntry> getEntry(boolean selling, ItemStack icon){
		return getShop(selling).getEntry(new ItemIcon(icon));
	}

	public int addItem(boolean selling, ItemIcon  icon, int stock){
		ShopEntry option = getEntry(selling,icon).orElseThrow(
				()->{ return new IllegalArgumentException("Item was not previously posted");});
		return option.addStock(stock);
	}
	public int addItem(boolean selling, ItemIcon  icon){
		return addItem( selling, icon, 1);
	}
	public int addItem(boolean selling, ItemStack item ){
		return addItem( selling, new ItemIcon(item), item.getAmount());
	}

	public int removeItem(boolean selling, ItemIcon  icon, int stock){
		ShopEntry option = getEntry(selling,icon).orElseThrow(
				()->{ return new IllegalArgumentException("Item was not previously posted");});
		int i =option.subStock(stock);
		if(option.isEmpty() &&  selling){ unPostEntry(selling, option);}
		return i;
	}

	public int removeItem(boolean selling, ItemIcon  icon){
		return removeItem( selling, icon, 1);
	}
	public int removeItem(boolean selling, ItemStack item ){
		return removeItem( selling, new ItemIcon(item), item.getAmount());
	}

	public void  unPostEntry(boolean selling, ItemIcon  icon){
		 getShop(selling).removeEntry(icon);
	}
	public void  unPostEntry(boolean selling, ItemStack icon){
		unPostEntry( selling, new ItemIcon(icon));

	}

	public void  unPostEntry(boolean selling, ShopEntry entry){
		unPostEntry( selling, entry.getIcon());

	}





















/*	public void addItem(boolean selling , ItemStack i, BigDecimal price){
		addItem(selling,i,price,i.getAmount());
	}

	public void addItem(boolean selling,ItemStack i, BigDecimal price, int perItem){
		Icon icon = new Icon(i);
		Map<Icon,Entry> current =  selling ? sellShop : buyShop;

		if(current.containsKey(icon)){
			current.get(icon).addAmount(perItem);
		}else{
			current.put( icon, new Entry(price,perItem,i.getAmount()));
			i.setAmount(perItem);
		}

	}*/
/*	public void removeItem(boolean selling , ItemStack i){
		removeItem(selling,i,i.getAmount());
	}

	public void removeItem(boolean selling,ItemStack i, int amount){
		Icon icon = new Icon(i);
		Map<Icon,Entry> current =  selling ? sellShop : buyShop;

		if(current.containsKey(icon)){
			current.get(icon).subAmount(amount);
			i.setAmount( i.getAmount() -amount);
			if(current.get(icon).isEmpty()){
				current.remove(icon);
			}
		}

	}*/

	/*class Entry{

		private final BigDecimal price;
		private final int perItem;
		private int stock = 0;



		public Entry(BigDecimal price, int perItem, int stock){
			this.stock =stock;
			this.perItem = perItem;
			this.price = price;


		}

		public int getAmount(){
			return stock;
		}

		public void setAmount(int aam){
			stock = aam;
		}

		public int addAmount(int aam){
			stock += aam;
			return stock;
		}

		public int subAmount(int aam){
			stock = Math.max(aam-stock,0);
			return stock;
		}



		public BigDecimal items4price(int offer){
				return BigDecimal.valueOf(offer).multiply( price.divide(BigDecimal.valueOf(perItem)) );
		}
		public int price4item(BigDecimal offer){
			return offer.multiply( BigDecimal.valueOf(perItem).divide(price) ).intValue();
		}

		public boolean isEmpty(){
			return stock < 1;
		}



	}
	class Icon{
		private final ItemStack icon;
		private final int lore;

		public Icon(ItemStack item){
				icon = toIcon(item);
				if(icon.getItemMeta().hasLore()){
					lore =	icon.getItemMeta().getLore().size();
				}else{
					lore =0;
					icon.getItemMeta().setLore( new ArrayList<>(10));
				}

		}

		private ItemStack toIcon(ItemStack input){
			Validate.isTrue(UtilInventory.isItemEmpty(input));
			input = input.clone();
			input.setAmount(1);
			return input;
		}

		public void  setText(String s){
			Validate.isTrue(StringUtils.isNotEmpty(s));
			List<String> lines  =icon.getItemMeta().getLore();

			if(lines.size() > lore){
				for(int i = lore -1; i< lines.size(); i++){
					lines.remove(i);
				}
			}

			lines.addAll(Arrays.asList(s.split("\\n")));
			icon.getItemMeta().setLore(lines);

		}

		public ItemStack toItem(){
			return icon.clone();
		}
		public int hashCode(){
			return  icon.hashCode();
		}

		public boolean equals(ItemStack input){
			return equals(new Icon(input));

		}
		public boolean equals(Icon  input){
			return this.hashCode() == input.hashCode();

		}
	}*/
}
