package me.varmetek.core.commands;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XDMAN500 on 3/5/2017.
 */
public interface CmdTab
{

	public static List<String>  send(String... options){
		return Arrays.asList(options);
	}
	List<String> execute (CmdSender sender, String alias, String[] args,int length);
}
