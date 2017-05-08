package me.varmetek.munchymc.shopping;

import org.apache.commons.lang3.Validate;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

/**
 * Created by XDMAN500 on 3/4/2017.
 */
public class ShopEntry
{
	private final BigDecimal price;
	private final int perItem;
	private final ItemIcon icon;
	private int stock = 0;


	private ShopEntry(ItemIcon icon, BigDecimal price, int perItem){
		Validate.notNull(icon);
		Validate.notNull(price);

		this.icon = icon;
		this.price = price;
		this.perItem = perItem;
	}

	private ShopEntry(ItemStack icon, BigDecimal price){

		this( new ItemIcon(icon),price,icon.getAmount());

	}

	public int getStock(){
		return stock;
	}

	public void setStock(int aam){
		Validate.isTrue(aam  >=0 );
		stock = aam;
	}

	public int addStock(int aam){
		Validate.isTrue(aam  >=0 );
		int diff = stock + aam;
		Validate.isTrue(diff  >=0 );

		stock = diff;
		return diff;
	}

	public int subStock(int aam){
		Validate.isTrue(aam  >=0 );
		int diff = stock - aam;

		Validate.isTrue(diff >= 0);
		stock = diff;
		return diff;
	}

	public boolean isEmpty(){
		return stock < 1;
	}

	public BigDecimal items2price(int offer){
		return BigDecimal.valueOf(offer).multiply( price.divide(BigDecimal.valueOf(perItem)) );
	}
	public int price2item(BigDecimal offer){
		return offer.multiply( BigDecimal.valueOf(perItem).divide(price) ).intValue();
	}

	public boolean equals(ItemStack input){
		return equals(new ItemIcon(input));

	}
	public boolean equals(ItemIcon input){
		return this.hashCode() == input.hashCode();

	}

	public boolean equals(ShopEntry input){
		return this.hashCode() == input.hashCode();

	}

	public int hashCode(){
		return this.icon.hashCode();
	}

	public ItemIcon getIcon(){
		return icon;
	}




	static class Builder{
		private BigDecimal price = BigDecimal.ZERO;
		private int perItem = 1;
		private ItemIcon icon = null;
		private int stock = 0;

		public Builder(ItemIcon icon){
			icon = icon;
		}

		public Builder(ItemStack i){
			this(new ItemIcon(i));
			perItem = i.getAmount();
		}

		public Builder setPrice(BigDecimal price){
			this.price = price;
			return this;
		}

		public Builder setPrice(BigDecimal price, int perItem){
			Validate.isTrue(perItem > 0);
			Validate.notNull (price);
			Validate.isTrue(price.compareTo(BigDecimal.ZERO) != -1);

			this.price = price;
			this.perItem = perItem;

			return this;
		}

		public Builder setStock(int stock){
			Validate.isTrue(stock > -1);
			this.stock = stock;
			return this;
		}

		public ShopEntry build(){
			ShopEntry shop = new ShopEntry(icon,price,perItem);

			shop.stock = stock;
			return shop;

		}

	}


}
