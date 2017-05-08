package me.varmetek.core.placeholder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by XDMAN500 on 2/4/2017.
 */
public class FormatHandleGeneric<D> implements FormatHandle<D>
{
	protected Map<String,PHFunction<D>> placeHolders = new HashMap<>();

	/**
	 * Registers a placholder with a given function to replace the string with
	 */

	public void register(String placeholder,PHFunction<D> function ){
		Validate.isTrue(StringUtils.isNotBlank(placeholder));
		Validate.notNull(function);
		placeHolders.put(placeholder,function);
	}


	/**
	 * Removes a placeholder from the handle. They placeholder will no longer be replaced by the function
	 */
	public void unregister(String placeholder ){
		Validate.isTrue(StringUtils.isNotBlank(placeholder));

		placeHolders.remove(placeholder);
	}


	/**
	 *
	 * Formats the text inputed and applies all placeholder functions to the original text
	 *
	 */

	public String apply (String toFormat, D data){
		Validate.isTrue(StringUtils.isNotBlank(toFormat));
		String output = toFormat;

		for(String placeholder :placeHolders.keySet()){
			output = output.replace(placeholder, placeHolders.get(placeholder).apply(data));
		}

		return output;
	}
}
