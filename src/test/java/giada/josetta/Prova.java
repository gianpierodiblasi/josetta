package giada.josetta;

import java.util.ArrayList;

public class Prova extends ArrayList {

  private byte a1 = 0x1;
  private short a2 = 1;
  private short a3 = (short) 1;
  private int a4 = 1;
  private long a5 = 1L;
  protected float a6 = 1.0f;
  protected double a7 = 1.0;
  protected char a8 = 'c';
  public String a9 = "ciao";
  private boolean a10 = true;
  private Object a11 = new Object();

  private byte b1;
  private short b2;
  private short b3;
  private int b4;
  private long b5;
  protected float b6;
  protected double b7;
  protected char b8;
  public String b9;
  private boolean b10;
  private Object b11;

  public Prova(int a) {
  }

  public int method(int a, int b) {
    return a + b;
  }
}
