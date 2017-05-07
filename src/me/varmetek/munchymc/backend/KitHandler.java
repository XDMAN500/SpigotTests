package me.varmetek.munchymc.backend;

import com.google.common.collect.Maps;
import me.varmetek.core.util.Cleanable;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 1/15/2017.
 */
public class KitHandler implements Cleanable
{
	private Map<String, Kit>  kits = Maps.newHashMap();

  private static Kit  EMPTY = new Kit.Builder().build();
	public KitHandler(){


	}

	public Optional<Kit> getKit(String name){
	  return Optional.ofNullable(kits.get(name));
  }

	public void setKit(String name, Kit kit){
    Validate.notNull(name);
    Validate.notEmpty(name);

    if(kit == null){
	    delKit(name);
    }else{
	    kits.put(name,kit);
    }
  }

  public void delKit(String name){
    Validate.notNull(name);
    Validate.notEmpty(name);
	  kits.remove(name);
  }
  public boolean isKit(String name){
    return kits.keySet().contains(name);
  }


  public Map<String, Kit> getKits(){

    return new HashMap<>(kits);
  }

	public void applyEmpty(Player pl){
		EMPTY.apply(pl);
	}


  public void clear(){
	  kits.clear();
  }

  @Override
  public void clean (){
    kits.clear();
    kits = null;
  }
}
