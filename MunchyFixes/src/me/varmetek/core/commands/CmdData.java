package me.varmetek.core.commands;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;

/**
 * Created by XDMAN500 on 6/8/2017.
 */
public class CmdData
{
  private final CmdSender sender;
  private final String alias;
  private final ImmutableList<String> args;
  private ImmutableList<String[]> flags;
  private final Command command;

  CmdData(CmdSender sender, String alias, String[] args, Command command){
    Validate.notNull(sender);
    Validate.notNull(alias);
    Validate.notNull(args);
    Validate.notNull(command);

    this.sender = sender;
    this.alias = alias;
    this.args = ImmutableList.copyOf(args);

    this.command = command;
  }


  public Command getCommand(){
    return command;
  }



  public String getAlias(){
    return alias;
  }




  public  ImmutableList<String> getArguments(){
    return args;
  }




  public CmdSender getSender(){
    return sender;
  }

  public String[] getArgArray(){
    return args.toArray(new String[0]);
  }


}
