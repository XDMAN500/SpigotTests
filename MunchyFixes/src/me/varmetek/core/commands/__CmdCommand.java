package me.varmetek.core.commands;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by XDMAN500 on 3/17/2017.
 */
public class __CmdCommand
{

	private  String name;
	private  CmdLogic cmd;
	private  CmdTab tab = null;
	private ImmutableList<String> aliases = null;
	private String desc = null;
	private String perm = null;
	private __CmdCommand (){ }

	public String getName(){
		return name;
	}

	public CmdLogic getCmdLogic(){
		return cmd;
	}

	public CmdTab getTabLogic(){
		return tab;
	}

	public ImmutableList<String> getAliases(){
		return aliases;
	}

	public String getDescription(){
		return desc;
	}

	public boolean  hasTabCompleter(){
		return tab != null;
	}

	public boolean hasAliases(){
		return aliases != null;
	}

	public String getPermission(){
		return perm;
	}

	public static __CmdCommand[] PackCmd(__CmdCommand... containers)
	{
		return containers;
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
		private String desc = null;
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
		public  Builder setPermission(String permission){
			perm = permission;
			return this;
		}

		public __CmdCommand build(){
      Validate.notNull(cmd, "Command logic cannot be null");
			__CmdCommand out = new __CmdCommand();
			out.cmd = this.cmd;
			out.name = this.name;
			out.tab = this.tab;

			out.aliases = aliases != null ? ImmutableList.copyOf(this.aliases ) : emptyAlias;
			out.desc = this.desc;
			out.perm = this.perm;
			return out;

		}

	}
}
