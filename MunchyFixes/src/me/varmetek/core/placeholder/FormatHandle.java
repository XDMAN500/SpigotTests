package me.varmetek.core.placeholder;

/**
 * Created by XDMAN500 on 2/4/2017.
 */
public interface FormatHandle<D>
{



	/**
	 * Registers a placholder with a given function to replace the string with
	 */

	void register(String toReplace, PHFunction<D> function );


	/**
	 * Removes a placeholder from the handle. They placeholder will no longer be replaced by the function
	 */
	void unregister(String toReplace );


	/**
	 *
	 * Formats the text inputed and applies all placeholder functions to the original text
	 *
	 */

	String apply (String toFormat, D data);
}
