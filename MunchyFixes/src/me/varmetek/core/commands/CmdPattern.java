package me.varmetek.core.commands;

import java.util.regex.Pattern;

/**
 * Created by XDMAN500 on 6/10/2017.
 */
public class CmdPattern
{
  static final String paramNameRegex = "([.]+)";

  static final Pattern
    removeWhiteSpace = Pattern.compile("\\s+"),
    paramStrong = Pattern.compile("^\\[" + paramNameRegex + "\\]$"),
    paramWeak = Pattern.compile("^\\(" + paramNameRegex + "\\)$"),
    paramLong = Pattern.compile("^\\{" + paramNameRegex + "\\}$"),
    flag = Pattern.compile("^\\$" + paramNameRegex + "$"),
    alias = Pattern.compile("^/\\*$")
      ;
}
