package me.varmetek.munchymc.backend;


import me.varmetek.core.util.Cleanable;
import me.varmetek.munchymc.Main;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.*;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public class PointManager implements Cleanable
{
	private Main main;
	private final Map<String, Point> POINTS = new HashMap<>();
	private final List<String> WARPS = new LinkedList<>();

	public PointManager(Main plugin){
	  main = plugin;
  }

	public List<String> getWarps(){
	  return new ArrayList<>(WARPS);
  }

  public Map<String, Point> getPoints(){
    return new HashMap<>(POINTS);
  }

	public Point setPoint(String name, Point p){
    isValid(name);

		if(p == null){
			delPoint(name);
		}else{
			POINTS.put(name, p);
		}
		return p;
	}

	public void delPoint(String name){
    isValid(name);

		POINTS.remove(name);
		delWarp(name);
	}
  public boolean pointExist(String name){
	  return POINTS.containsKey(name);
  }
	public Optional<Point> getPoint(String name){
    isValid(name);

		return  Optional.ofNullable(POINTS.get(name));


	}

	public void setWarp(String name){
    isValid(name);
    if(pointExist(name)){
      WARPS.add(name);
    }
	}

  public void delWarp(String name){
    isValid(name);
    WARPS.remove(name);
  }

	public boolean isWarp(String name){
    isValid(name);

		return WARPS.contains(name);
	}

	public Optional<Point> getWarp(String name)
	{
    isValid(name);

		return WARPS.contains(name) ? getPoint(name) : Optional.empty();
	}

	private void isValid(String name) throws IllegalArgumentException{
    Validate.notNull(name, "Warp name cannot be null");
    Validate.notEmpty(name, "Warp name cannot be empty");
    Validate.isTrue(StringUtils.isNotBlank(name), "Warp name cannot be blank");
  }


	@Override
	public void clean (){
		POINTS.clear();
		WARPS.clear();
		main = null;
	}
}
