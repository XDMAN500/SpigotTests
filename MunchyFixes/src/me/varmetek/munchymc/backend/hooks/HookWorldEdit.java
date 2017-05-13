package me.varmetek.munchymc.backend.hooks;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import me.varmetek.core.hook.BaseHook;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Created by XDMAN500 on 5/12/2017.
 */
public class HookWorldEdit extends BaseHook<WorldEditPlugin>
{
  public HookWorldEdit (WorldEditPlugin plugin, boolean needed){
    super(plugin, needed);
  }

  public WorldEdit get(){
    return plugin.getWorldEdit();

  }


  public Optional<Selection> getSelection(Player player){
      return  Optional.of(plugin.getSelection(player));
  }

}
