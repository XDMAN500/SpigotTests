package me.varmetek.munchymc.backend.hell;

public enum CutCustomItem
{
	/*PARKOUR_BOOTS(RareType.ULTRA, () -> { return new RareParkourBoots(Main.get());},"pk_boots"){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.TEAL, "&3&lParkour Boots", EquipmentSlot.FEET);
		}
	},

	HERMES_HELM(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHermes Helm",EquipmentSlot.HEAD);
		}

	},
	HERMES_CHEST(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHermes Chest",EquipmentSlot.CHEST);
		}
	},
	HERMES_LEGS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHermes Legs",EquipmentSlot.LEGS);
		}
	},
	HERMES_SANDALS(RareType.ULTRA,() -> {return new RareHermesSandals(Main.get());}){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHermes Sandals",EquipmentSlot.FEET);
		}
	},


	HAWK_EYE(RareType.ULTRA,() -> {return new RareHawkEye(Main.get());}){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLACK, "&0&lHawk Eye", EquipmentSlot.HEAD);
		}
	},
	SUPER_CHEST(RareType.ULTRA,() -> {return new RareSuperChest(Main.get());}){
		@Override
		protected ItemStack  build(){
			ItemStack item = new ItemStack(Material.ENDER_CHEST);
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
			ItemMeta im =  item.getItemMeta();

			im.spigot().setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&7&lSuper Chest"));

			item.setItemMeta(im);
			return item;
		}
	},
	SLOT_PLACEHOLDER(){
		@Override
		protected ItemStack  build(){
			ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);



			ItemMeta im =  item.getItemMeta();

			im.addItemFlags(ItemFlag.values());
			im.setDisplayName(" ");

			item.setItemMeta(im);
			return item;
		}
	},

	ARTEMIS_HELM(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.FUCHSIA, "&d&lArtemis Helm", EquipmentSlot.HEAD);
		}
	},
	ARTEMIS_PLATE(RareType.ULTRA, () -> {return new RareArtemisPlate(Main.get());}){
		@Override
		protected ItemStack  build(){
			return  buildCustomLeather(Color.FUCHSIA, "&d&lArtemis Plate", EquipmentSlot.CHEST);
		}
	},
	ARTEMIS_LEGS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.FUCHSIA, "&d&lArtemis Legs", EquipmentSlot.LEGS);
		}
	},
	ARTEMIS_BOOTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.FUCHSIA, "&d&lArtemis Boots", EquipmentSlot.FEET);
		}
	},

	VADER_CAPE(RareType.ULTRA, () -> {return new RareVaderCape(Main.get());}){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLACK, "&0&lVader Cape", EquipmentSlot.LEGS);
		}
	},
	VADER_BOOTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLACK, "&0&lVader Boots", EquipmentSlot.FEET);
		}
	},
	VADER_MASK(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return  buildCustomLeather(Color.BLACK, "&0&lVader Mask", EquipmentSlot.HEAD);
		}
	},
	VADER_PLATE(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLACK, "&0&lVader Plate", EquipmentSlot.CHEST);
		}
	},

	OPTIMIZER(RareType.LEGENDARY,() -> {return new RareOptimizer(Main.get());}){
		@Override
		protected ItemStack  build(){
			ItemStack item = new ItemStack(Material.STONE_AXE);
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
			ItemMeta im =  item.getItemMeta();

			im.setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&7&lOptimizer"));

			item.setItemMeta(im);
			return item;
		}
	},
	MUSCLE_SWORD(RareType.LEGENDARY){
		@Override
		protected ItemStack  build(){
			ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
			ItemMeta im =  item.getItemMeta();

			im.setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&c&lMUSCLE SWORD"));

			item.setItemMeta(im);
			item.getItemMeta();
			return item;
		}
	},
	PEARLS(RareType.SEMI){
		@Override
		protected ItemStack  build(){
			ItemStack item = new ItemStack(Material.ENDER_PEARL);
			item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
			ItemMeta im =  item.getItemMeta();

			im.setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&A&lPearls"));

			item.setItemMeta(im);
			item.getItemMeta();
			return item;
		}
	},

	TROOPER_HAT(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.WHITE, "&f&lTrooper Helm", EquipmentSlot.HEAD);
		}
	},
	TROOPER_CHEST(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.WHITE, "&f&lTrooper Plate", EquipmentSlot.CHEST);
		}
	},
	TROOPER_PANTS(RareType.LEGENDARY){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.WHITE, "&f&lTROOPER LEGS", EquipmentSlot.LEGS);
		}
	},
	TROOPER_BOOTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.WHITE, "&f&lTrooper Boots", EquipmentSlot.FEET);
		}
	},

	HANSOLO_HAT(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHanSolo Hat", EquipmentSlot.HEAD);
		}
	},
	HANSOLO_CHEST(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHanSolo Jacket", EquipmentSlot.CHEST);
		}
	},
	HANSOLO_PANTS(RareType.ULTRA,() -> {return new RareHanSoloPants(Main.get());}){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHanSolo Pants", EquipmentSlot.LEGS);
		}
	},
	HANSOLO_BOOTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.AQUA, "&b&lHanSolo Boots", EquipmentSlot.FEET);
		}
	},

	ZUES_HAT(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLUE, "&1&lZues Crown", EquipmentSlot.HEAD);
		}
	},
	ZUES_CHEST(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLUE, "&1&lZues Plate", EquipmentSlot.CHEST);
		}
	},
	ZUES_PANTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLUE, "&1&lZues Legs", EquipmentSlot.LEGS);
		}
	},
	ZUES_BOOTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.BLUE, "&1&lZues Feet", EquipmentSlot.FEET);
		}
	},

	HERA_HAT(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.TEAL, "&3&lHera Hat", EquipmentSlot.HEAD);
		}
	},
	HERA_CHEST(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.TEAL, "&3&lHera Chest", EquipmentSlot.CHEST);
		}
	},
	HERA_PANTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.TEAL, "&3&lHera Legs", EquipmentSlot.LEGS);
		}
	},
	HERA_BOOTS(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			return buildCustomLeather(Color.TEAL, "&3&lHera Boots", EquipmentSlot.FEET);
		}
	},

	DARK_PICK(RareType.ULTRA){
		@Override
		protected ItemStack  build(){
			ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
			item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 9);
			ItemMeta im =  item.getItemMeta();

			im.setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&8&lDark Pick"));

			item.setItemMeta(im);
			item.getItemMeta();
			return item;
		}
	},
	BOOMER_BOW(RareType.LEGENDARY){
		@Override
		protected ItemStack  build(){

			ItemStack item = new ItemStack(Material.BOW);
			item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 39);
			item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE,1);
		ItemMeta im =  item.getItemMeta();

			im.setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&e&lBOOMER BOW"));

			item.setItemMeta(im);
			item.getItemMeta();
			return item;
		}
	},
	PURIFIER(RareType.LEGENDARY){
		@Override
		protected ItemStack  build(){
            ItemStack item = new ItemStack(Material.YELLOW_FLOWER);

			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,7);
			ItemMeta im =  item.getItemMeta();

			im.setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&4&lPURIFIER"));

			item.setItemMeta(im);
			item.getItemMeta();
			return item;
		}
	},
	ZUES_BOLT(RareType.ULTRA, (() -> {return new RareZuesBolt(Main.get());})){
		@Override
		protected ItemStack  build(){
			ItemStack item = new ItemStack(Material.BLAZE_ROD);

			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,7);
			ItemMeta im =  item.getItemMeta();

			im.setUnbreakable(true);
			im.setDisplayName(Utils.colorCode("&1&lZues Bolt"));

			item.setItemMeta(im);
			item.getItemMeta();
			return item;
		}
	},


	;



	private String[] names;
	private RareType type;
	private ItemStack item;
	private Supplier<Listener> itemEvents;

	CutCustomItem (){
		this(RareType.SERVER,null, null);
	}

	CutCustomItem (RareType type){
		this(type,null ,null);
	}
	CutCustomItem (RareType type, Supplier<Listener> itemEvents){
		this(type,itemEvents ,null);
	}
	CutCustomItem (Supplier<Listener> itemEvents, String... alias){
		this(RareType.SERVER,itemEvents,alias);
	}

	CutCustomItem (String... alias)
	{
		this(RareType.SERVER,null,alias);
	}

	CutCustomItem (RareType type, Supplier<Listener> itemEvents, String... alias){

		names = alias == null ? new String[]{} : alias;
		this.type = type;
		item = this.build();
		this.itemEvents = itemEvents;
		if(type != RareType.SERVER)
		{
			//if (!Rares.rares.containsKey(type))
			//	Rares.rares.put(type, new ArrayList<CustomItem>(10));
			//Rares.rares.get(type).add(this);
		}



	}

	public boolean isRare()
	{
		return type!=  RareType.SERVER;
	}
	public ItemStack getItem ()
	{
		return item.clone();
	}
	public String ID()
	{
		return this.name().toLowerCase();
	}
	public RareType getRareType()
	{
		return type;
	}


	protected ItemStack  build(){
		throw new IllegalStateException("Custom item has no item builder");
	}

	public static CutCustomItem getByItem(ItemStack t)
	{

		boolean invalid = (t== null || t.getType() == Material.AIR) ;

		if(invalid  ||  !t.getItemMeta().hasDisplayName() ) return null;
		for(CutCustomItem ci : values())
		{
			if (ci.getItem().isSimilar(t))
				return ci;
		}

		return  null;
	}
	public static CutCustomItem get (String name)
	{
		for (CutCustomItem temp : CutCustomItem.values())
			if (temp.name().toLowerCase().equalsIgnoreCase(name.toLowerCase()))

				return temp;
			else if (temp.names.length > 0)
				if (Arrays.asList(temp.names).contains(name.toLowerCase()))

					return temp;


		return null;
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


	public static void registerItemEvent()
	{
		Main.get().getLogger().info("[CustomItems] Loading Item events");
		for(CutCustomItem ci : CutCustomItem.values())
		{
			if(ci.itemEvents != null)
			{
				Main.get().getElementService().registerListener(ci.itemEvents.get());
				Main.get().getLogger().info("[CustomItems] Loading events for "+ ci.name());
			}
		}
	}

}
/*
* Code Grave
*
*
	*
	*
	*
	* //private static final Map<CustomItem,ItemBuilder> registry = new HashMap<>();
	* public static void registerItems()
	{
		for(CustomItem ci : CustomItem.values())
		{
			registry.put(ci,getBuilder(ci));
		}
	}


	private static abstract class ItemBuilder
	{
		 ItemStack item = null;


		protected abstract void build ();

		public ItemStack getItem ()
		{
			if (item == null)
				build();

			return item.clone();
		}


	}
*
* 	public static ItemBuilder getCustomLeather(Color col, String title, EquipmentSlot at)
	{
		Validate.notNull(col); Validate.notNull(title); Validate.notNull(at);
		 return new ItemBuilder()
		 {
			 @Override
			 protected void build ()
			 {
				 switch (at)
				 {
					 case HEAD: item = new ItemStack(Material.LEATHER_HELMET); break;
					 case FEET: item = new ItemStack(Material.LEATHER_BOOTS);break;
					 case CHEST:  item =new ItemStack(Material.LEATHER_CHESTPLATE);break;
					 case LEGS:  item = new ItemStack(Material.LEATHER_LEGGINGS);break;
					 default: return;
				 }
				 item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 7);
				 LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();

				 lam.setUnbreakable(true);
				 lam.setDisplayName(Utils.colorCode(title));
				 lam.setColor(col);
				 item.setItemMeta(lam);

			 }
		 };
	}
* 	private static ItemBuilder getBuilder(CustomItem ci)
	{

		switch (ci)
		{
			case ARTEMIS_PLATE: return  getCustomLeather(Color.FUCHSIA, "&d&lArtemis Plate", EquipmentSlot.CHEST);
			case HAWK_EYE: return getCustomLeather(Color.BLACK, "&0&lHawk Eye", EquipmentSlot.HEAD);
			case HERMES_HELM:return getCustomLeather(Color.AQUA, "&b&lHermes Helm",EquipmentSlot.HEAD);
			case HERMES_CHEST:return getCustomLeather(Color.AQUA, "&b&lHermes Chest",EquipmentSlot.CHEST);
			case HERMES_LEGS:return getCustomLeather(Color.AQUA, "&b&lHermes Legs",EquipmentSlot.LEGS);

			case HERMES_SANDALS: return getCustomLeather(Color.AQUA, "&b&lHermes Sandals",EquipmentSlot.FEET);
			case PARKOUR_BOOTS: return getCustomLeather(Color.TEAL, "&3&lParkour Boots", EquipmentSlot.FEET);
			case SUPER_CHEST: return new ItemBuilder()
			{
				@Override
				protected void build ()
				{
					item = new ItemStack(Material.ENDER_CHEST);
					item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
					ItemMeta im =  item.getItemMeta();

					im.spigot().setUnbreakable(true);
					im.setDisplayName(Utils.colorCode("&7&lSuper Chest"));

					item.setItemMeta(im);
				}
			};

			case SLOT_PLACEHOLDER: return new ItemBuilder()
			{

				@Override
				protected void build ()
				{
					item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0, (byte)8);



					ItemMeta im =  item.getItemMeta();

					im.addItemFlags(ItemFlag.values());
					im.setDisplayName(" ");

					item.setItemMeta(im);
				}
			};

			case VADER_CAPE: return getCustomLeather(Color.BLACK, "&0&lVader Cape", EquipmentSlot.LEGS);
			case HANSOLO_PANTS: return getCustomLeather(Color.AQUA, "&b&lHanSolo Pants", EquipmentSlot.LEGS);
			case OPTIMIZER: return new ItemBuilder()
			{
				@Override
				protected void build ()
				{
					item = new ItemStack(Material.STONE_AXE);
					item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
					ItemMeta im =  item.getItemMeta();

					im.spigot().setUnbreakable(true);
					im.setDisplayName(Utils.colorCode("&7&lOptimizer"));

					item.setItemMeta(im);
				}
			};
			case ARTEMIS_HELM: return getCustomLeather(Color.FUCHSIA, "&d&lArtemis Helm", EquipmentSlot.HEAD);



			case ARTEMIS_LEGS:return getCustomLeather(Color.FUCHSIA, "&d&lArtemis Legs", EquipmentSlot.LEGS);
			case ARTEMIS_BOOTS:return getCustomLeather(Color.FUCHSIA, "&d&lArtemis Boots", EquipmentSlot.FEET);
			case VADER_BOOTS:return getCustomLeather(Color.BLACK, "&0&lVader Boots", EquipmentSlot.FEET);
			case VADER_MASK:return getCustomLeather(Color.BLACK, "&0&lVader Mask", EquipmentSlot.HEAD);
			case VADER_PLATE:return getCustomLeather(Color.BLACK, "&0&lVader Plate", EquipmentSlot.CHEST);
			case MUSCLE_SWORD: return  new ItemBuilder()
			{
				@Override
				protected void build ()
				{
					item = new ItemStack(Material.DIAMOND_SWORD);
					item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
					ItemMeta im =  item.getItemMeta();

					im.setUnbreakable(true);
					im.setDisplayName(Utils.colorCode("&c&lMUSCLE SWORD"));

					item.setItemMeta(im);
					item.getItemMeta();
				}
			};
			case PEARLS:return  new ItemBuilder()
			{
				@Override
				protected void build ()
				{
					item = new ItemStack(Material.ENDER_PEARL);
					item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
					ItemMeta im =  item.getItemMeta();

					im.setUnbreakable(true);
					im.setDisplayName(Utils.colorCode("&A&lPearls"));

					item.setItemMeta(im);
					item.getItemMeta();
				}
			};
			case TROOPER_HAT:return getCustomLeather(Color.WHITE, "&f&lTrooper Helm", EquipmentSlot.HEAD);
			case TROOPER_CHEST:return getCustomLeather(Color.WHITE, "&f&lTrooper Plate", EquipmentSlot.CHEST);
			case TROOPER_PANTS:return getCustomLeather(Color.WHITE, "&f&lTROOPER LEGS", EquipmentSlot.LEGS);
			case TROOPER_BOOTS:return getCustomLeather(Color.WHITE, "&f&lTrooper Boots", EquipmentSlot.FEET);
			case HANSOLO_HAT: return getCustomLeather(Color.AQUA, "&b&lHanSolo Hat", EquipmentSlot.HEAD);

			case HANSOLO_CHEST:return getCustomLeather(Color.AQUA, "&b&lHanSolo Jacket", EquipmentSlot.CHEST);
			case HANSOLO_BOOTS:return getCustomLeather(Color.AQUA, "&b&lHanSolo Boots", EquipmentSlot.FEET);
			case ZUES_HAT:return getCustomLeather(Color.BLUE, "&1&lZues Crown", EquipmentSlot.HEAD);
			case ZUES_CHEST:return getCustomLeather(Color.BLUE, "&1&lZues Plate", EquipmentSlot.CHEST);
			case ZUES_PANTS:return getCustomLeather(Color.BLUE, "&1&lZues Legs", EquipmentSlot.LEGS);
			case ZUES_BOOTS:return getCustomLeather(Color.BLUE, "&1&lZues Feet", EquipmentSlot.FEET);
			case HERA_HAT:return getCustomLeather(Color.TEAL, "&3&lHera Hat", EquipmentSlot.HEAD);
			case HERA_CHEST:return getCustomLeather(Color.TEAL, "&3&lHera Chest", EquipmentSlot.CHEST);
			case HERA_PANTS:return getCustomLeather(Color.TEAL, "&3&lHera Legs", EquipmentSlot.LEGS);
			case HERA_BOOTS:return getCustomLeather(Color.TEAL, "&3&lHera Boots", EquipmentSlot.FEET);
			case DARK_PICK: return  new ItemBuilder()
		{
			@Override
			protected void build ()
			{
				item = new ItemStack(Material.DIAMOND_PICKAXE);
				item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 9);
				ItemMeta im =  item.getItemMeta();

				im.setUnbreakable(true);
				im.setDisplayName(Utils.colorCode("&8&lDark Pick"));

				item.setItemMeta(im);
				item.getItemMeta();
			}
		};
			case BOOMER_BOW:return  new ItemBuilder()
			{
				@Override
				protected void build ()
				{
					item = new ItemStack(Material.BOW);
					item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 39);
					item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE,1);
					ItemMeta im =  item.getItemMeta();

					im.setUnbreakable(true);
					im.setDisplayName(Utils.colorCode("&e&lBOOMER BOW"));

					item.setItemMeta(im);
					item.getItemMeta();
				}
			};
			case PURIFIER:return  new ItemBuilder()
			{
				@Override
				protected void build ()
				{
					item = new ItemStack(Material.YELLOW_FLOWER);

					item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,7);
					ItemMeta im =  item.getItemMeta();

					im.setUnbreakable(true);
					im.setDisplayName(Utils.colorCode("&4&lPURIFIER"));

					item.setItemMeta(im);
					item.getItemMeta();
				}
			};
			case ZUES_BOLT: return new ItemBuilder()
			{
				@Override
				protected void build ()
				{
					item = new ItemStack(Material.BLAZE_ROD);

					item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,7);
					ItemMeta im =  item.getItemMeta();

					im.setUnbreakable(true);
					im.setDisplayName(Utils.colorCode("&1&lZues Bolt"));

					item.setItemMeta(im);
					item.getItemMeta();
				}
			};
		}
		return null;

*
	}*/
}