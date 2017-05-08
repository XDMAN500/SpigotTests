package me.varmetek.core.commands;

import java.util.List;

/**
 * Created by XDMAN500 on 3/5/2017.
 */
public interface CmdTab
{


	List<String> execute (CmdSender sender, String alias, String[] args,int length);
}
