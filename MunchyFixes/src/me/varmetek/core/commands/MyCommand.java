package me.varmetek.core.commands;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by XDMAN500 on 5/11/2017.
 */
public final class MyCommand extends Command
{
 private final CmdLogic logic;
 private final CmdTab tab;

 private MyCommand (Builder builder){
    super(builder.name);
    logic = builder.cmd;
    tab = builder.tab;
    this.setAliases(builder.aliases);
    this.setDescription(builder.desc);
    this.setPermission(builder.perm);
  }

  @Override
  public boolean execute (CommandSender sender, String commandLabel, String[] args){
   logic.execute( new CmdSender(sender),commandLabel,args,args.length);
    return false;
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String commandLabel, String[] args){
    return tab.execute( new CmdSender(sender),commandLabel,args,args.length);
  }

  public static class Builder
  {
    public static final ImmutableList<String> emptyAlias = ImmutableList.copyOf(Collections.EMPTY_LIST);
    public static final CmdLogic defaultLogic =  (sender,alias,args,legnth)->{ sender.asSender().sendMessage("Empty command");};
    public static final CmdTab defaultTab =  (sender,alias,args,legnth)->{ return Collections.EMPTY_LIST;};

    private final String name;
    private CmdLogic cmd = defaultLogic;
    private CmdTab tab = defaultTab;

    private List<String> aliases = null;
    private String desc = "";
    private String perm = null;

    public Builder (String name, CmdLogic cmd)
    {
      this(name);
      setLogic(cmd);
    }
    public Builder (String name)
    {
      Validate.notNull(name, "Command name cannot be null");
      Validate.isTrue(StringUtils.isNotBlank(name), "Command name cannot be empty");

      this.name = name;

    }

    public Builder setLogic(CmdLogic cmd){
      this.cmd = cmd == null ?  defaultLogic : cmd;
      return this;
    }
    public Builder setTab (CmdTab tab)
    {
      this.tab = tab == null ?  defaultTab : tab;
      return this;
    }

    public Builder addAlias (String name)
    {
      Validate.notNull(name, "Command alias cannot be null");
      Validate.isTrue(StringUtils.isNotBlank(name), "Command alias cannot be empty");
      aliases().add(name);
      return this;
    }


    public Builder addAliases (String... names)
    {

      for (String e : names)
      {
        try
        {
          addAlias(name);
        } catch (Exception ex)
        {
          continue;
        }
      }
      return this;
    }


    private List<String> aliases ()
    {
      return aliases == null ? new ArrayList<>() : aliases;
    }

    public Builder setDescription (String description)
    {
      desc = description;
      return this;
    }
    public Builder setPermission(String permission){
      perm = permission;
      return this;
    }

    public MyCommand build(){
      Validate.notNull(cmd, "Command logic cannot be null");
      return new MyCommand(this);

    }

  }
}
