package me.varmetek.munchymc.backend.exceptions;

/**
 * Created by XDMAN500 on 5/14/2017.
 */
public class DeserializeException extends Exception
{
  public DeserializeException() {
    super();
  }

  public DeserializeException(String message) {
    super(message);
  }


  public DeserializeException(String message, Throwable cause) {
    super(message, cause);
  }


  public DeserializeException(Throwable cause) {
    super(cause);
  }

}
