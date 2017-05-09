package me.varmetek.core.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by XDMAN500 on 5/7/2017.
 */
public interface DataHandler<T>
{
  T save(String name, File file) throws IOException;
  T load(String name, File file) throws IOException;
  void loadAll(String name, File f) throws IOException;
  void savell(String name, File f) throws IOException;

}
