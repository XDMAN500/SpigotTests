package me.varmetek.munchymc.backend;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import me.varmetek.core.placeholder.FormatHandleGeneric;
import me.varmetek.core.scoreboard.Scoreboardx;
import me.varmetek.core.scoreboard.Sidebar;
import me.varmetek.core.scoreboard.SidebarHandler;
import me.varmetek.core.user.BasePlayerSession;
import me.varmetek.core.util.Messenger;
import me.varmetek.core.util.PluginCore;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.time.LocalTime;
import java.util.*;


public class PlayerSession extends BasePlayerSession<PlayerData>
{

  ;
  protected Optional<Scoreboardx> board ;
  protected PluginCore plugin;
  public PlayerSession (UUID name, final PlayerHandler handler){
    super(name, handler);

    plugin  = handler.getPlugin();
    dead = !profile.isOnline();
    if (board.isPresent() && !board.get().getSidebarHandler().has("default")){
      board.get().getSidebarHandler().add("default");
    }

    board = Optional.empty();

    if(player.isPresent())
    {
      handler.getPlugin().getLogger().info( "Creating Profile ("+ this.profile.getName()+") "+  this.profile.getUniqueId() );
      board = Optional.of(new Scoreboardx());
      board.get().apply(player.get());

    }

    //main.addTickListener(this);
  }

  public PlayerSession (PlayerSession old){
    super(old);

   plugin = this.handler.getPlugin();
    dead = !profile.isOnline();

    board = old.board;
    if(player.isPresent())
    {
      board.get().apply(player.get());
    }
    if (board.isPresent() && !board.get().getSidebarHandler().has("default")){
      board.get().getSidebarHandler().add("default");
    }


    //main.addTickListener(this);
  }
  public Optional<Scoreboardx> getScoreBoard(){
    return board;

  }

  @Override
  public void clean (){

    if (getScoreBoard().isPresent()){
      //board.get().getSidebarHandler().clear();
    }
    super.clean();
    board = null;

  }

  /////////////////////////////////////////////////////////////////
  private boolean testMode = false;
  private boolean dead = false;
  protected InventoryMap invMap = new InventoryMap(InventoryType.ANVIL,
                                                    InventoryType.BREWING,
                                                    InventoryType.ENCHANTING,
                                                    InventoryType.ENDER_CHEST,
                                                    InventoryType.CHEST,
                                                    InventoryType.WORKBENCH);


  protected String msgReply = "";
  protected long lastMsgReply = 0l;



  private static final FormatHandleGeneric<PlayerSession> formater = new FormatHandleGeneric<>();

  static{
    formater.register("{PLAYER}", PlayerSession::getName);
  }



  public String compileJoinMessage (){
    return Messenger.color(formater.apply(playerData.joinMessage, this));
  }

  public String compileLeaveMessage (){
    return Messenger.color(formater.apply(playerData.leaveMessage, this));
  }


  public boolean isTesting (){
    return testMode;
  }

  public PlayerSession setTestMode (boolean val){
    testMode = val;
    return this;
  }

  public boolean isDead (){
    return dead;
  }

  public void revive (){
    Validate.isTrue(player.isPresent(), "Cannot killed a non existing player");
    if (!dead) return;
    dead = false;
    Player pl = player.get();
    pl.setAllowFlight(false);
    pl.setFlying(false);
    pl.setInvulnerable(false);
    pl.setFoodLevel(20);
    pl.setSaturation(20);
    pl.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 1, 9, true, false, Color.GREEN));
    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
  }

  public void kill (){
    Validate.isTrue(player.isPresent(), "Cannot killed a non existing player");
    if (dead) return;
    dead = true;
    Player pl = player.get();
    pl.setAllowFlight(true);
    pl.setFlying(true);
    pl.setInvulnerable(false);
    pl.setHealth(pl.getMaxHealth());
    pl.getActivePotionEffects().forEach((pot) -> {
      pl.removePotionEffect(pot.getType());
    });
    pl.playSound(pl.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);


    Vector vec = pl.getVelocity();
    vec.setY(Math.min(vec.getY(), 3));
    vec.add(pl.getLocation().getDirection().multiply(-0.7));

  }

  public InventoryMap getInventoryMap (){
    return invMap;
  }

  public void setMsgReply (String name){
    Validate.notNull(name);
    Validate.notEmpty(name);

    msgReply = name;
  }

  public String getMsgReply (){
    return msgReply;
  }

  public void setLastMsgReply(long time){
    lastMsgReply = time;
  }

  public long getLastMsgReply (){
    return lastMsgReply;
  }

  @Override
  public void tick (){

    if (!board.isPresent()){
      return;
    }
    SidebarHandler sidebars = board.get().getSidebarHandler();
    Sidebar sb;
    LocalTime time = LocalTime.now();
    if (!sidebars.has("default")){

      sb = sidebars.add("default");

      sb.set("spc1", "&7=============", 10);
      sb.set("spc2", "&5 ", 9);
      String timeS = (time.getHour() > 12 ? time.getHour() - 12 : time.getHour()) + ":" + time.getMinute() + ":" + time.getSecond() + " " + (time.isAfter(LocalTime.NOON) ? "PM" : "AM");
      sb.set("timer", "&4 Time:&b " + timeS, 5);
      sb.set("spc4", "&7=============", 0);
      sb.set("spc3", "&5 ", 1);
    } else {
      sb = sidebars.get("default").get();
    }


    if (sidebars.hasDisplay() && !sb.isVisible()) return;
    try {
      sb.setTitle("&b&lMunchy Max");

      sb.set("spc1", "&7=============", 10);
      sb.set("spc2", "&5 ", 9);
      String timeS = (time.getHour() > 12 ? time.getHour() - 12 : time.getHour()) + ":" + time.getMinute() + ":" + time.getSecond() + " " + (time.isAfter(LocalTime.NOON) ? "PM" : "AM");
      sb.set("timer", "&4 Time:&b " + timeS, 5);
      sb.set("spc4", "&7=============", 0);
      sb.set("spc3", "&5 ", 1);
      if (!sidebars.hasDisplay()){
        sb.setVisible(true);
      }

      if (player.isPresent()){
        player.get().setLevel(sb.size());

      }

    } catch (Exception e) {
      e.printStackTrace();
      plugin.getLogger().warning("Failed to edit sb: " + sb.ID());
    } finally {
      sb.getHandle().render();
    }
  }




  public class InventoryMap
  {
    private final Map<InventoryType,Inventory> inventories = new HashMap<>();
    private final ImmutableSet<InventoryType> types;

    public InventoryMap (Collection<InventoryType> input){
      types = ImmutableSet.copyOf(input);
      setup();
    }

    public InventoryMap (InventoryType... input){
      types = ImmutableSet.copyOf(input);
      setup();
    }

    //public  final InventoryType[] types = { InventoryType.ANVIL, InventoryType.BREWING, InventoryType.ENCHANTING, InventoryType.ENDER_CHEST, InventoryType.CHEST, InventoryType.WORKBENCH};
    public ImmutableSet<InventoryType> getAllowedTypes (){
      return types;
    }

    private void add (Inventory inv){

      inventories.put(inv.getType(), inv);

    }

    private void add (InventoryType type){
      if (player.isPresent()){
        inventories.put(type, Bukkit.createInventory(player.get(), type, "Personal " + type.getDefaultTitle()));
      }

    }

    private void remove (InventoryType type){
      inventories.remove(type);
    }

    public Optional<Inventory> get (InventoryType type){
      return Optional.ofNullable(inventories.get(type));
    }

    public Optional<InventoryType> getType (String name){
      InventoryType output = null;

      UnmodifiableIterator<InventoryType> iter = types.iterator();

      while (iter.hasNext()) {
        output = iter.next();
        if (output.name().equals(name.toUpperCase())){
          return Optional.of(output);
        }
      }

      return Optional.empty();
    }

    private InventoryMap setup (){
      types.forEach((type) -> add(type));

      return this;
    }
  }


}
