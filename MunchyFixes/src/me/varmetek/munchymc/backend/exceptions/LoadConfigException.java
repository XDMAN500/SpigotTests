package me.varmetek.munchymc.backend.exceptions;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
@Deprecated
public class LoadConfigException extends Exception
{
  public LoadConfigException() {
    super();
  }

  public LoadConfigException(String message) {
    super(message);
  }


  public LoadConfigException(String message, Throwable cause) {
    super(message, cause);
  }


  public LoadConfigException(Throwable cause) {
    super(cause);
  }

}
