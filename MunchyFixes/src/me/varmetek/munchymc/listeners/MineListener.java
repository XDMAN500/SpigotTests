package me.varmetek.munchymc.listeners;

import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.user.AutoAction;
import me.varmetek.munchymc.backend.user.PlayerHandler;
import me.varmetek.munchymc.backend.user.PlayerSession;
import me.varmetek.munchymc.mines.Mine;
import me.varmetek.munchymc.mines.MineManager;
import me.varmetek.munchymc.util.UtilInventory;
import me.varmetek.munchymc.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Created by XDMAN500 on 6/4/2017.
 */
public class MineListener implements Listener
{
  private static final Pattern validNumber =   Pattern.compile("((((\\d+)(\\.\\d+)?)(([eE](\\d+))|(mil|th|bil))?)(,)?)+");
  private static final TextComponent txtFullInv = new TextComponent("INVENTORY IS FULL");

  static{
    txtFullInv.setBold(true);
    txtFullInv.setColor(ChatColor.RED);
  }

  private PlayerHandler pHandle;
  private MineManager mineMan;
  public MineListener(PlayerHandler playerhandler, MineManager mineManager){
    this.pHandle = playerhandler;
    this.mineMan = mineManager;
  }



  @EventHandler(priority = EventPriority.HIGH)
  public void handeBreaking(final BlockBreakEvent ev){

    final Player player = ev.getPlayer();
    if(player == null) return;
    final PlayerSession user = pHandle.getSession(player);

    final Block bl = ev.getBlock();
    if(bl == null)return;
    if(bl.getType() == Material.AIR)return;
    //final ItemStack hand = player.getInventory().getItemInMainHand();



   /* if(GlobalShop.PRICES.containsKey(bl.getState().getData())){
      expBonus = (int) Math.floor(GlobalShop.PRICES.get(bl.getState().getData())/10);
    }*/

    boolean isIgnored = false;// Main.ignoredWorlds.contains(bl.getWorld().getName());

    if(mineMan.getMineNames().isEmpty()){return;}

    Optional<Mine> mine = mineMan.getMineAt(bl.getLocation());
    if(!mine.isPresent()) return;

    Mine selected = mine.get();
    ev.setCancelled(true);

    MineBreakBlockEvent breakEvent = new MineBreakBlockEvent(bl,player,selected);
    Bukkit.getServer().getPluginManager().callEvent(breakEvent);

    if(breakEvent.isCancelled()){
      return;
    }


  /*  if(selected.isRanked()){

      if(user.getMinerRank() < selected.getRankedPos()){
        return;
      }
    }*/

    DropData  drops = getDrops(player,ev.getBlock());

    MinePreAutoActionEvent preEvent = new MinePreAutoActionEvent(bl,player,selected,drops.getDrops());
    Bukkit.getServer().getPluginManager().callEvent(preEvent);
    if(drops.isGiveExp()){
      player.giveExp(ev.getExpToDrop());
    }



    checkAutoActions(new AutoActionTask(preEvent,ev.getExpToDrop()));


    bl.setType(Material.AIR);
   // Messenger.send(player," Mined in mine \""+ selected.getName()+" \".");



  }

  @EventHandler
  public void handleAura(PlayerInteractEvent ev){

    Player player = ev.getPlayer();
   PlayerSession user = pHandle.getSession(player);
    if(!Utils.isRightClicked(ev.getAction()))return;

    Block cblock = ev.getClickedBlock();
    if(cblock == null)return;
    if(!user.hasAutoAction(AutoAction.AURA) )return;


    Optional<Mine> sel = mineMan.getMineAt(cblock.getLocation());
    if(!sel.isPresent())return;
    Mine mine = sel.get();




    if(UtilInventory.isFull(player.getInventory())){
      player.spigot().sendMessage(ChatMessageType.ACTION_BAR,txtFullInv);
      return;
    }



    int radius  = 1;

    Location center = cblock.getRelative(ev.getBlockFace().getOppositeFace()	).getLocation();

    int minX = center.getBlockX()-radius;
    int minY = center.getBlockY()-radius;
    int minZ = center.getBlockZ()-radius;
    int maxX = center.getBlockX()+radius;
    int maxY = center.getBlockY()+radius;
    int maxZ = center.getBlockZ()+radius;

    for (int x = minX; x <= maxX; x++) {
      if(UtilInventory.isFull(player.getInventory()))break;
      for (int z = minZ; z <= maxZ; z++) {
        if(UtilInventory.isFull(player.getInventory()))break;
        for (int y = minY; y <= maxY; y++) {

          if(UtilInventory.isFull(player.getInventory()))break;

          if(!mine.contains(x, y, z))continue;
          Block bl = player.getWorld().getBlockAt(x, y, z);
          if(bl.getType()==  Material.AIR)continue;

          Bukkit.getPluginManager().callEvent( new BlockBreakEvent( bl,player));
         // Bukkit.getPluginManager().callEvent( new PlayerInteractEvent(player, Action.LEFT_CLICK_BLOCK,ev.getItem(),bl,ev.getBlockFace()));
       //   bl.getWorld().playEffect(bl.getLocation(), Effect.MOBSPAWNER_FLAMES, 2);


        }
      }
    }







  }





  @EventHandler
  public void onDamageBlock(final BlockDamageEvent ev){
    Player player = ev.getPlayer();
    PlayerSession user = pHandle.getSession(player);
    Block cblock = ev.getBlock();

    if(cblock == null)return;
    Optional<Mine> mine = mineMan.getMineAt(cblock.getLocation());

    if(!mine.isPresent()) return;
    ev.setInstaBreak(true);


    if(!UtilInventory.canAccept(player.getInventory(),cblock.getType())){

      player.spigot().sendMessage(ChatMessageType.ACTION_BAR,txtFullInv);

    }



  }

  public static void checkAutoActions(AutoActionTask task){
    Validate.notNull(task);
   task.runTaskLater(task.getPlugin(),1L);
  }

  private static class DropData{
    private final Inventory drops;
    private final boolean giveExp;

    public DropData(Inventory inv, boolean giveExp){
      drops = inv;
      this.giveExp = giveExp;

    }

    public Inventory getDrops(){
      return drops;

    }
    public boolean isGiveExp(){
      return giveExp;
    }

  }

  private static DropData  getDrops(Player pl, Block bl){


    ItemStack ewhand = new ItemStack(pl.getItemInHand() == null? Material.AIR: pl.getItemInHand().getType());
    ItemStack hand = pl.getItemInHand() == null ? new ItemStack(Material.AIR): pl.getItemInHand();
    Inventory drops = provideInventory(bl.getDrops(ewhand));

    int lootBonus = 1;
    boolean giveExp = true;

    if(hand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)){
      lootBonus += new Random().nextInt(hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)+1);
      //MunchyMax.logger().info( "Loot Bonus: "+ lootBonus );
    }



    if(hand.containsEnchantment(Enchantment.SILK_TOUCH)){
      giveExp = false;
      drops.clear();
      //MunchyMax.logger().info( "Using silk touch" );
      ItemStack item = new ItemStack(bl.getType(),lootBonus,bl.getData());


      drops.addItem(item);
    }else{
      for(ItemStack i: drops){

        if(UtilInventory.isItemEmpty(i))continue;

        i.setAmount(i.getAmount()*(lootBonus));

      }

    }
    return new DropData(drops,giveExp);



  }

  private static Inventory provideInventory(){
    return provideInventory(null);
  }

  private static Inventory provideInventory(Collection<ItemStack> items){
    Inventory inv;
    if (items == null){

      inv = Bukkit.getServer().createInventory(null,54);
    } else {
      inv = UtilInventory.convert2Inv(items, 54);

    }

    return inv;
  }


  private static class AutoActionTask extends BukkitRunnable{
    private final Player player;
    private final PlayerSession user;

    private final MinePreAutoActionEvent ev;

    private final Random random;
    private int exp = 0;
    public AutoActionTask(MinePreAutoActionEvent event, int exp){
      Validate.notNull(event);

      ev = event;
      player = ev.getPlayer();
      user  = MunchyMax.getPlayerHandler().getSession(player);

      this.exp = exp;
      random =new Random();

    }
    public AutoActionTask(MinePreAutoActionEvent event){
     this(event,0);

    }

    public Plugin getPlugin(){
      return user.getHandler().getPlugin();
    }

    public void run(){




      if(user.hasAutoAction(AutoAction.SMELT)){

        SmeltResult res =  smeltInv(ev.getDrops());
      ///  Messenger.send(player, "Smelted " + res.getExp() + " xp ");
        player.giveExp(res.getExp());

      }
      if(user.hasAutoAction(AutoAction.PACK)){

        packItems(ev.getDrops());
      //  packItems(player.getInventory());

      }
      if(user.hasAutoAction(AutoAction.SELL)){
        // sellInventory(player.getInventory(),user);
      }


      MinePostAutoActionEvent breakEvent = new MinePostAutoActionEvent(ev.getBlock(),player,ev.getMine(),ev.getDrops());
      Bukkit.getServer().getPluginManager().callEvent(breakEvent);


      if(user.hasAutoAction(AutoAction.PACK)){
        packItems(player.getInventory());
      }
      for(ItemStack item :ev.getDrops()){
        if(UtilInventory.isItemEmpty(item)) continue;
        player.getInventory().addItem(item);
      }



    }
  }


  public static final Map<Material,SmeltData> FurnaceRs = new EnumMap(Material.class);

  public static final PackedItemRegistry piReg = new PackedItemRegistry();


  public static void setupRegistry(){
    piReg.register(Material.EMERALD_BLOCK);
    piReg.register(Material.DIAMOND_BLOCK);
    piReg.register(Material.IRON_BLOCK);
    piReg.register(Material.LAPIS_BLOCK);
    piReg.register(Material.REDSTONE_BLOCK);
    piReg.register(Material.COAL_BLOCK);
    piReg.register(Material.SNOW_BLOCK);
    piReg.register(Material.QUARTZ_BLOCK);
    piReg.register(Material.BRICK);
    piReg.register(Material.GLOWSTONE);
    piReg.register(Material.GOLD_BLOCK);
    piReg.register(Material.NETHER_BRICK);



    Iterator<Recipe> iter = Bukkit.getServer().recipeIterator();
    while (iter.hasNext()) {
      Recipe recipe = (Recipe) iter.next();
      if(recipe instanceof FurnaceRecipe){
        FurnaceRecipe rep = (FurnaceRecipe) recipe;
        Material type = rep.getInput().getType();
        float exp = 0;

        switch (type){
          default:break;
          case COBBLESTONE: exp = 0.1f;break;
          case COAL_ORE: exp = 0.1f;break;
          case LAPIS_ORE: exp = 0.1f;break;
          case IRON_ORE: exp = 0.7f;break;
          case GOLD_ORE: exp = 1f;break;
          case REDSTONE_ORE: exp = 0.7f;break;
          case DIAMOND_ORE: exp = 1f;break;
          case EMERALD_ORE: exp = 1f;break;

        }


        FurnaceRs.put(type, new SmeltData(rep.getResult(), exp));
        if(rep.getExperience() > 0){
          MunchyMax.logger().info("SmeltData for "+ rep.getInput().getType().name() + "has been registered");

        }
      }
    }



  }

  public static class CraftData{
    final Map<Material,AtomicInteger> data = new HashMap<>();
    final ItemStack product;
    private static String condence(String[] a){
      StringBuilder b = new StringBuilder();
      for(String i :a)
      {
        b.append(a);
      }
      return b.toString();
    }
    public CraftData (ShapedRecipe recipe){
      //Bukkit.getLogger().info("Craft data for "+ recipe.getResult().getType().name() +" =: "+condence(recipe.getShape()));
      product = recipe.getResult();

      for(String ch: recipe.getShape()){
        for(char  c: ch.toCharArray()) {
          Material item = recipe.getIngredientMap().get(c).getType();
          if (data.containsKey(item)){
            data.get(item).incrementAndGet();
          } else {
            data.put(item, new AtomicInteger(1));
          }
        }
      }

    }

    public Map<Material, AtomicInteger> getData(){
      return data;
    }

    public int getNeeded(Material mat){
      Validate.isTrue(data.containsKey(mat));
      return data.get(mat).intValue();
    }
  }

  public static class PackedItemRegistry{
    Map<Material,CraftData> map = new EnumMap(Material.class);



    public Optional<CraftData> getData(Material mat){
      return map.containsKey(mat) ? Optional.ofNullable(map.get(mat)) : Optional.empty();
    }

    public void register(Material mat){
      Validate.notNull(mat);
      Validate.isTrue(mat.isBlock());


      ItemStack item = new ItemStack(mat);
      List<Recipe> recipes = Bukkit.getRecipesFor(item);
      Validate.isTrue(!recipes.isEmpty());
      ShapedRecipe rec = null;
      for(Recipe r: recipes){
        if(r instanceof ShapedRecipe){
          rec = (ShapedRecipe) r;
          break;
        }
      }

      Validate.notNull(rec);

      map.put(mat,new CraftData(rec));
    }

    public Map<Material,CraftData> getRoster(){
      return map;
    }
  }

  public static class SmeltData{
    final ItemStack  item;
    final float experience;

    public SmeltData( ItemStack item, float experience){
      Validate.notNull(item);
      Validate.isTrue(experience >= 0.0);

      this.item = item;
      this.experience = experience;
    }
  }

  public static SmeltData getSmeltData(ItemStack input){


    if(input == null) return null;
    SmeltData rep = FurnaceRs.get(input.getType());

    return rep == null ? null : rep;

  }

  public static class SmeltResult{
    private final Inventory inventory;
    private final int exp;

    public SmeltResult (Inventory inv,int exp){
      Validate.notNull(inv);
      inventory = inv;
      this.exp = exp;
    }

    public Inventory getInventory(){
      return inventory;
    }

    public int getExp(){
      return exp;
    }
  }

  public static SmeltResult smeltInv(Inventory inv){
  Validate.notNull(inv);
  int exp = 0;

  if(UtilInventory.isEmpty(inv))  return new SmeltResult(inv,exp);

  for (ItemStack i : inv)
  {
    if(UtilInventory.isItemEmpty(i)) continue;

    SmeltData result = getSmeltData(i);
    if (result != null){
      int amount = i.getAmount();

      inv.removeItem(i);
      ItemStack toAdd = new ItemStack(result.item);
      toAdd.setAmount(amount);
      inv.addItem(toAdd);
      exp += (int)(result.experience* amount);


    }
  }
  return new SmeltResult(inv,exp);
}





  public static void packItems(Inventory inv){
    ////("Running AP");

    Validate.notNull(inv);

    for(CraftData data: piReg.getRoster().values()){
      Map<Material,Integer> amounts = new HashMap<>();

      boolean sufficient = true;
      for(Material mat: data.getData().keySet()){

        int amount = UtilInventory.getAmount(inv,mat);
        if(amount < data.getNeeded(mat)){
          sufficient = false;
          break;
        }
        amounts.put(mat,amount);
      }

      if(!sufficient){
        continue;
      }

      int canMake =-1;
      for(Material mat: data.getData().keySet()){


        canMake = canMake == -1? amounts.get(mat)/ data.getNeeded(mat) :
                    Math.min(canMake,amounts.get(mat)/ data.getNeeded(mat));
      }



      for(Material mat: data.getData().keySet()){

        HashMap<Integer, ? extends ItemStack>  items = inv.all(mat);
        int toGO  = canMake*data.getNeeded(mat);

        for(Map.Entry<Integer,? extends ItemStack> en : items.entrySet()){
          if(toGO < 1) break;
          if(UtilInventory.isItemEmpty(en.getValue()))continue;
          ItemStack stack = en.getValue();
          int toRem = Math.min(stack.getAmount(),toGO);

          stack.setAmount( stack.getAmount()-toRem);
          toGO -= toRem;

          if(stack.getAmount() == 0){
            inv.clear(en.getKey());
          }


        }

      }
      ItemStack result = new ItemStack(data.product);
      result.setAmount( canMake * data.product.getAmount());
      inv.addItem(result);

     /* ItemStack result = new ItemStack(data.product);
      ItemStack resultStack= new ItemStack(result);

      int stacks = canMake/ result.getMaxStackSize()*result.getAmount();
      int extra = canMake % result.getMaxStackSize()* result.getAmount();

      result.setAmount(extra);
      resultStack.setAmount(resultStack.getMaxStackSize());

      if(stacks > 0){
        for (; stacks > 0; stacks--) {
          inv.addItem(resultStack);
        }
      }
      if(extra > 0){
        inv.addItem(result);
      }*/




    }

  }
}
