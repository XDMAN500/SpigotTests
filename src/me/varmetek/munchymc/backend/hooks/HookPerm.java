package me.varmetek.munchymc.backend.hooks;

import me.varmetek.core.hook.BaseHook;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by XDMAN500 on 4/22/2017.
 */
public class HookPerm extends BaseHook<PermissionsEx>
{
	public HookPerm (PermissionsEx plugin, boolean needed)
	{
		super(plugin, needed);
	}


	public PermissionManager getManager(){

		return PermissionsEx.getPermissionManager();
	}

	private void test(){
		PermissionUser user =getManager().getUser("e");
		user.
	}
}

