package me.varmetek.munchymc.listeners;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.scoreboard.Sidebar;
import me.varmetek.core.scoreboard.SidebarHandler;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.PlayerSession;
import me.varmetek.munchymc.backend.Rares;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;


/**
 * Created by XDMAN500 on 12/27/2016.
 */
public class MixedListener implements Element
{

	private final Listener listen= new Listener(){
    @EventHandler
    public void ultraCraft(InventoryClickEvent ev){
      Player pl  = (Player) ev.getWhoClicked();

      //pl.spigot().ssendMessage(ChatMessageType.ACTION_BAR , , ClickEvent);

      if( !(pl.getOpenInventory().getTopInventory() instanceof CraftingInventory))return;

      CraftingInventory ci =  (CraftingInventory)pl.getOpenInventory().getTopInventory() ;


      //tc.setText("[]");
      //tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,tc));
      //ComponentBuilder cb =new ComponentBuilder("[]").event(new HoverEvent(HoverEvent.Action.SHOW_ITEM,new ComponentBuilder("LLLLLLLL").create()));
      //pl.spigot().sendMessage(ChatMessageType.CHAT, cb.create());

      int size = ci.getMatrix().length;
      if(ci.getRecipe() != null)return;
      if(ci.getResult() == null)
      {

        MunchyMax.getTaskHandler().run(() ->
        {
          ItemStack[] stuff = tryCraftingRare(ci);

          if (stuff != null)
          {

            //
				/*	for (int i = 0; i < size; i++)
					{
						ci.setItem(i, stuff[i]);
					}*/
            pl.updateInventory();
          }


        });
      }else{
        if(ev.getSlotType() != InventoryType.SlotType.RESULT)return;

        final ItemStack[] items = new ItemStack[size];

        //Messenger.sendGroup(ci.getViewers(), "TryCraftingRare: " +" trying");
        for(int i = 0; i < size;  i++)
        {
          ItemStack item = ci.getMatrix()[i];
          if( MunchyMax.getItemMap().getByItem(item) == null){
            items[i] = null;
            continue;
          }
          if(item.getAmount() > 1)
          {
            items[i] = item.clone();
            items[i].setAmount(item.getAmount()-1);

          }
          else items[i] = null;

        }
        final ItemStack[] e = items;

       MunchyMax.getTaskHandler().run(() ->
        {

          if (e != null)
          {
            for (int i = 0; i < size; i++)
            {
              ci.setItem(i + 1, e[i]);
            }
            Messenger.send(pl, ev.getEventName() + " Cleaning up Matrix");
          }
        });

      }
    }

    private ItemStack[] tryCraftingRare(CraftingInventory ci)
    {
      if(ci == null)return null;
      long key = 0L;
      int amount = 0;
      int size = ci.getMatrix().length;
      ItemStack[] items = new ItemStack[size];
      //Messenger.sendGroup(ci.getViewers(), "TryCraftingRare: " +" trying");
      for(int i = 0; i < size;  i++)
      {

        ItemStack item = ci.getMatrix()[i];
        boolean invalid = MunchyMax.getItemMap().getByItem(item) == null;

        if(invalid) continue;

        if(item.getAmount() > 1)
        {
          items[i] = item.clone();
          items[i].setAmount(item.getAmount()-1);

        }
        else {items[i] = null;}
        //	Messenger.sendGroup(ci.getViewers(), "TryCraftingRare: " +" added "+ item.getType().name());
        amount ++;
        if(amount> minimumRares )return null;
        key = 73* key + item
                          .getItemMeta()
                          .getDisplayName()
                          .hashCode();


      }

      if(amount < minimumRares)return null; // Is matrix full of valid rares?
      //Messenger.sendGroup(ci.getViewers(), "Amount: "+ amount + "/" + size);
      random.setSeed(key);
      ItemStack result  = Rares.getRares().get(random.nextInt(Rares.getRares().size())).getItem();

      //ci.setItem(0,result);
      ci.setResult(result);
      return items;

    }
    /*@EventHandler(priority = EventPriority.LOW)
    public void joinEvent(PlayerLoginEvent ev){
      plugin.getLogger().info( ev.getPlayer().getName() +" exists? "+plugin.getUserHandler().exists(ev.getPlayer()));
      User user = plugin.getUserHandler().renewUser(ev.getPlayer());
      ev.getPlayer().setInvulnerable(false);
      plugin.getLogger().info( ev.getPlayer().getName() +" exists? "+plugin.getUserHandler().exists(ev.getPlayer()));

    }*/
    @EventHandler
    public void login(PlayerJoinEvent ev){
      PlayerSession user = MunchyMax.getPlayerHandler().getSession(ev.getPlayer());
      ev.setJoinMessage(user.compileJoinMessage());
    }

    @EventHandler
    public void logout(PlayerQuitEvent ev){
      PlayerSession user = MunchyMax.getPlayerHandler().getSession(ev.getPlayer());
      ev.setQuitMessage(user.compileLeaveMessage());
    }

    @EventHandler
    public void damageDeath(EntityDamageEvent ev){
      if((ev.getEntity().getType() != EntityType.PLAYER )) return;
      Player player = (Player)ev.getEntity();

      PlayerSession user =  MunchyMax.getPlayerHandler().getSession(player);
      if(user.isDead())ev.setCancelled(true);
    }
    @EventHandler
    public void death(EntityDamageEvent ev){


      if((ev.getEntity().getType() != EntityType.PLAYER )) return;
      Player player = (Player)ev.getEntity();
      PlayerSession user =  MunchyMax.getPlayerHandler().getSession(player);
      if(!user.getScoreBoard().isPresent())return;

      if(player.getHealth() >  ev.getFinalDamage()){return;	}
      ev.setDamage(0.0);
      Messenger.send(player," "," ");
      player.spigot().sendMessage(respawnMsg.create());
      Messenger.send(player," "," ");

      user.kill();
      SidebarHandler sidebar = user.getScoreBoard().get().getSidebarHandler();
      if(!sidebar.has("death")){
        sidebar.add("death");
      }
      final Sidebar sb = sidebar.get("death").get();
      sb.setTitle("&3Stuff and things");
      sb.set("spc1","&7=============",10);
      sb.set("spc2","&5 ",9);
      sb.set("timer","&7Respawn in &e"+ 5,8);
      sb.set("spc3","&5 ",1);
      sb.set("spc4","&7=============",0);
      sb.setVisible(true);


      new BukkitRunnable(){
        Sidebar dd = sb;

        int countdown = 5;


        public void run(){
          if(!user.isDead() || countdown == 0){
            this.cancel();
            user.revive();
            sb.setVisible(false);

            if(sidebar.has("default"))
            {
              sidebar.get("default").get().setVisible(true);
            }
          }
          sb.setName("timer","&7Respawn in &e"+ countdown);
          countdown --;
          sb.getHandle().render();

        }

      }.runTaskTimer(MunchyMax.getInstance(),1,20);


    }

    //@EventHandler
    public void sneak(PlayerToggleSneakEvent ev)
    {
      if(ev.isSneaking())
      {
        ev.getPlayer().hidePlayer(ev.getPlayer());
        ev.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).addModifier(new AttributeModifier("hitspeed",100, AttributeModifier.Operation.ADD_NUMBER));

        ev.getPlayer().setSilent(true);
        ev.getPlayer().setAI(false);
        Messenger.send(ev.getPlayer()," &7Ninja mode activated");
      }
      else
      {
        ev.getPlayer().showPlayer(ev.getPlayer());
        ev.getPlayer().setSilent(false);
        //ev.getPlayer().geta
        ev.getPlayer().setAI(true);
        ev.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).getModifiers().forEach( o ->{
          if(o.getName().equals("hitspeed"))
            ev.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).removeModifier(o);
        });
      }

    }
    //@EventHandler
    public void itRan(PrepareItemCraftEvent ev){
      Messenger.send(ev.getViewers(), "&e&l"+ev.getEventName());
    }


  };



	private  static final ComponentBuilder  respawnMsg = new ComponentBuilder("Revive?");
	static{
		respawnMsg.bold(true).color(ChatColor.GREEN)
			.event(
				new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click Here to revive").create()))
			.event(
				new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/run respawn" ));
	}
	// Used to determine the Rare recieved from crafting.
	private static final Random random= new Random();

	//Used to determine if re-enchanting an items overrides the previous enchantments

	//Number of rares to be used with crafting
	private static final int minimumRares  = 3;



	public MixedListener(){



	}


	@Override
	public void clean (){

	}

	@Override
	public CmdCommand[] supplyCmd (){
		return null;
	}

	@Override
	public Listener supplyListener (){
		return listen;
	}
}
