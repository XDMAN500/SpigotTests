package me.varmetek.core.commands;

import org.apache.commons.lang.Validate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XDMAN500 on 3/5/2017.
 */
public interface CmdLogic
{



	///void execute(CmdSender sender, String alias, String[] args, int length);
	void execute(CmdData cmd);


	static String getUsage(String alias, String[] args, int len,  String extra){
		Validate.notNull(alias);
		Validate.notNull(args);
		Validate.notNull(extra);
		Validate.isTrue(len>=0);

		String[] steps = len!= 0 ? Arrays.copyOf(args,len): null;

		len = Math.min(args.length,len);
		StringBuilder text = new StringBuilder("/");
		String separator = " ";
		text.append(alias).append(separator);

		if(steps!=null){
			for (String elem : steps) {
				text.append(elem).append(separator);
			}
		}
		text.append(extra);

		return text.toString();
	}

	static String getUsage(String alias, List<String> args, int len,  String extra){
		Validate.notNull(alias);
		Validate.notNull(args);
		Validate.notNull(extra);
		Validate.isTrue(len>=0);

		String[] steps = len!= 0 ? Arrays.copyOf(args.toArray(new String[0]),len): null;

		len = Math.min(args.size(),len);
		StringBuilder text = new StringBuilder("/");
		String separator = " ";
		text.append(alias).append(separator);

		if(steps!=null){
			for (String elem : steps) {
				text.append(elem).append(separator);
			}
		}
		text.append(extra);

		return text.toString();
	}




}
