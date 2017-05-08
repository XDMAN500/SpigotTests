package me.varmetek.munchymc.backend;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XDMAN500 on 12/29/2016.
 */
public class CustomRecipe extends ShapedRecipe
{
	private static final ItemStack NULL = new ItemStack(Material.AIR);
	private Map<Character, ItemStack> ingredients = new HashMap<Character, ItemStack>();
	private List<Character> chars = Arrays.asList('1','2','3','4','5','6','7','8','9','0');
	private ItemStack output;
	public CustomRecipe (ItemStack result)
	{
		super(result);
		output= result;
	}

	@Override
	public ItemStack getResult ()
	{
	//	new ShapedRecipe();
		return output;
	}
	public CustomRecipe setIngredients( ItemStack[] array)
	{

		int index=0;
		for(ItemStack i :array)
		{
			ingredients.put(chars.get(index),i);
			index = index+1 % 9;
		}
		return this;
	}
	@Override
	public Map<Character, ItemStack> getIngredientMap() {
		HashMap<Character, ItemStack> result = new HashMap<>();
		for (Map.Entry<Character, ItemStack> ingredient : ingredients.entrySet()) {
			if (ingredient.getValue() == null) {
				result.put(ingredient.getKey(), null);
			} else {
				result.put(ingredient.getKey(), ingredient.getValue().clone());
			}
		}
		return result;
	}
}
