package me.varmetek.munchymc.mines;

/**
 * Created by XDMAN500 on 5/13/2017.
 */
public abstract class  MineGenerator
{
  Mine mine;

  public MineGenerator (Mine mine){
    this.mine = mine;
  }

  public abstract  void generate();
}
