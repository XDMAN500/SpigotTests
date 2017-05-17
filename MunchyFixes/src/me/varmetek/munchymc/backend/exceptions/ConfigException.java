package me.varmetek.munchymc.backend.exceptions;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public class ConfigException extends Exception
{
  public ConfigException () {
    super();
  }

  public ConfigException (String message) {
    super(message);
  }


  public ConfigException (String message, Throwable cause) {
    super(message, cause);
  }


  public ConfigException (Throwable cause) {
    super(cause);
  }

}
