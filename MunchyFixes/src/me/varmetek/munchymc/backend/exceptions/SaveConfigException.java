package me.varmetek.munchymc.backend.exceptions;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
@Deprecated
public class SaveConfigException extends Exception
{
  public SaveConfigException () {
    super();
  }

  public SaveConfigException (String message) {
    super(message);
  }


  public SaveConfigException (String message, Throwable cause) {
    super(message, cause);
  }


  public SaveConfigException (Throwable cause) {
    super(cause);
  }

}
