package me.varmetek.munchymc.shop;

/**
 * Created by XDMAN500 on 6/11/2017.
 */
public class AlphaCode
{

  private final String code;

  public AlphaCode(int data){

    code = Integer.toString(data,Character.MAX_RADIX);
  }

  @Override
  public String toString(){
    return "@"+code;
  }

  @Override
  public int hashCode(){
    return code.hashCode();

  }

  @Override
  public boolean equals(Object c){
    if(c == null) return false;
    if(c.getClass() !=   this.getClass())return false;
    return c.hashCode() == this.hashCode();
  }
}
