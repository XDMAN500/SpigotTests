package me.varmetek.core.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by XDMAN500 on 3/5/2017.
 */
public interface CmdTab
{

	static List<String>  send(String... options){
		if(options == null || options.length == 0){
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(options);
	}
	List<String> execute (CmdData cmd);
}
