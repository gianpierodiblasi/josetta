package giada.josetta;

public class JosettaTest {

  private final static String[] args1 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\test\\giada\\pizzapazza\\color\\test_color1.java",
    "-out",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\transpile-test\\giada\\pizzapazza\\color\\test_color1.js"
  };

  private final static String[] args2 = new String[]{
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
