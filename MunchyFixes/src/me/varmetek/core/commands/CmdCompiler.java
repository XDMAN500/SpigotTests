package me.varmetek.core.commands;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.Validate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XDMAN500 on 6/10/2017.
 */
public class CmdCompiler
{
  static final String
    R_word = "([.]+)",
    R_prefix = "([\\$\\{\\{])",
    R_suffix = "([\\$\\}\\]])",
    R_paramS  = "\\["+R_word +"\\]",
    R_paramW = "\\(" + R_word + "\\)",
    R_paramL = "\\{" + R_word+ "\\}",
    R_flag = "\\$" + R_word + "\\$",
    R_const = "<" + R_word +">",
    R_alias = "/\\*",
    R_validate = R_alias +
                   "("+
                     "(" +
                        "\\s+"
                       //"(\\s+"+ R_prefix+")"+ "|" + "("+ R_suffix+"\\s+)" + "|" + "("+R_suffix+ "\\s+" + R_prefix+")"
                     +")"+
                     "("+R_paramL +"|"+ R_paramS + "|" + R_paramW + "|" + R_flag + ")"
                   +")*"

      ;

  // example: /* <run> [on] (secret) $HI$ {Hololol}
  static Optional<String> getArgument(List<String> args, int arg){
    return arg < args.size() ? Optional.ofNullable(args.get(arg)) : Optional.empty();
  }
  static Optional<String> getArgument(String[] args, int arg){
    return arg < args.length ? Optional.ofNullable(args[arg]) : Optional.empty();
  }

  static Optional<String[]> getFlags(List<String[]> args, int arg){
    return arg < args.size() ? Optional.ofNullable(args.get(arg)) : Optional.empty();
  }
  static Optional<String[]> getFlags(String[][] args, int arg){
    return arg < args.length ? Optional.ofNullable(args[arg]) : Optional.empty();
  }

  static final Pattern
    validate = Pattern.compile("^"+R_validate+"$"),
    removeWhiteSpace = Pattern.compile("\\s+"),
    paramStrong = Pattern.compile("^"+R_paramS+"$"),
    paramWeak = Pattern.compile("^"+R_paramW+"$"),
    paramLong = Pattern.compile("^"+R_paramL+"$"),
    paramFlag = Pattern.compile("^"+R_flag+"$"),
    paramConst= Pattern.compile("^"+R_const+"$"),
    alias = Pattern.compile("^"+R_paramS+"$")
      ;






  private final String[] format;


  public CmdCompiler(String format){
    this.format =  removeWhiteSpace.split(format);

  }


  public Argument compile(CmdData data){
    return new Argument(data);
  }



  public static abstract class Param<T>{
    protected final String name;
    protected final T value;

    public Param(String name, T value){
      this.name = name;
      this.value = value;
    }

;

    public String getName(){
      return name;
    }

    public T getValue(){
      return value;
    }

    public abstract Double getNumber() throws NumberFormatException;

    @Override
    public int hashCode(){
      return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o){
      if(! (o instanceof Param)) return false;

      return this.hashCode() == o.hashCode();

    }

    public abstract int getLevel();


  }

  public static class ParamStrong extends  Param<String>{


    public ParamStrong(String name, String value){
      super(name,value);
      Validate.isTrue(isValid(value),"Parameter \""+name+"\" must be present.");
    }

    public static boolean isValid(String input){
      return null != input;
    }

    public Double getNumber() throws NumberFormatException{
     return Double.valueOf(value);
    }

    public int getLevel(){return 3;}

  }

  public static class ParamWeak extends  Param<Optional<String>>{


    public ParamWeak(String name, String value){
      super(name,Optional.ofNullable(value));

    }



    public Double getNumber() throws NumberFormatException{
      return Double.valueOf(value.orElseThrow(
        ()->{
          return new NumberFormatException();
        }
        ));

    }

    public int getLevel(){return 2;}

  }

  public static class ParamLong extends  Param<String[]>{


    public ParamLong(String name, String[] value){
      super(name,value);
      Validate.isTrue(isValid(value),"Parameter \""+name+"\" must be present.");
    }
    public static boolean isValid(String[] input){
      return input != null && input.length != 0;
    }


    public String getResult(){
      StringBuilder builder = new StringBuilder(value[0]);


        for(int i =1; i< value.length; i++){
          builder.append(" "+ value[i]);
        }
      return builder.toString();
    }


    public Double getNumber() throws NumberFormatException{
     throw new NumberFormatException();

    }

    public int getLevel(){return 1;}

  }


  public static class ParamFlag extends  Param<String>{


    public ParamFlag(String name, String value){
      super(name,value);
      Validate.isTrue(isValid(value),"Parameter \""+name+"\" must be present.");

    }

    public static boolean isValid(String input){
     return input != null;
    }


    public Double getNumber() throws NumberFormatException{
      throw new NumberFormatException();

    }

    public int getLevel(){return -10;}

  }



  public class Argument{
    private final CmdData data;
    private final ImmutableMap<String,ParamStrong> paramsStrong;
    private final ImmutableMap<String,ParamWeak> paramsWeak;
    private final ImmutableMap<String,ParamLong> paramsLong;
  //  private Map<String,ParamFlag> paramsFlag;

    public Argument (CmdData data){
      this.data = data;


     Map<String,ParamStrong> pS = new HashMap<>();
     Map<String,ParamWeak> pW= new HashMap<>();
     Map<String,ParamLong> pL= new HashMap<>();

     Iterator<String> params = data.getArguments().iterator();

      for(int arg = 0 ; params.hasNext() ;){
        String in = params.next();

        Matcher paramS = paramStrong.matcher(format[arg]);
        Matcher paramW = paramWeak.matcher(format[arg]);
        Matcher paramL = paramLong.matcher(format[arg]);
        Matcher isConst = paramFlag.matcher(format[arg]);

        if(paramS.matches() && ParamStrong.isValid(in)){
          pS.put(paramS.group(1),new ParamStrong(paramS.group(1),in));
          arg++;
        }else if(paramW.matches()){
          pW.put(paramW.group(1),new ParamWeak(paramS.group(1),in));
          arg++;
        }else if(paramL.matches()){
          String[] stuff = Arrays.copyOfRange(data.getArgArray(), arg, data.getArguments().size());

          pL.put(paramL.group(1),new ParamLong(paramL.group(1),stuff));
          break;
        } else if(isConst.matches() ){
          Validate.isTrue(isConst.group(1).equalsIgnoreCase(in) );
          arg++;
        }

      }

      paramsStrong = ImmutableMap.copyOf(pS);
      paramsWeak = ImmutableMap.copyOf(pW);
      paramsLong = ImmutableMap.copyOf(pL);

    }




    public ParamStrong getStrongParam(String name){
      Validate.notNull(name);
      Validate.isTrue(paramsStrong.containsKey(name));
      return paramsStrong.get(name);
    }
    public ParamWeak getWeakParam(String name){
      Validate.notNull(name);
      Validate.isTrue(paramsWeak.containsKey(name));
      return paramsWeak.get(name);
    }

    public ParamLong getLongParam(String name){
      Validate.notNull(name);
      Validate.isTrue(paramsLong.containsKey(name));
      return paramsLong.get(name);
    }

    public CmdData getCmdData(){
      return data;
    }
   /* public ParamFlag getFlagParam(String name){
      Validate.notNull(name);
      Validate.isTrue(paramsFlag.containsKey(name));
      return paramsFlag.get(name);
    }*/



  }
}
