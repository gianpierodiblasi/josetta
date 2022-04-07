package giada.josetta;

public class JosettaTest {

  private static String[] args1 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\src\\giada\\pizzapazza\\math\\Z4Sign.java",
    "-out",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\transpile\\giada\\pizzapazza\\math\\Z4Sign.js"
  };

  private static String[] args2 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\src\\",
    "-out",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\transpile\\",
    "-w"
  };

  public static void main(String[] args) throws Exception {
    Josetta.main(args1);
  }
}
