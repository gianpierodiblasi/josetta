package giada.josetta;

public class JosettaTest {

  private final static String[] args1 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\src\\giada\\pizzapazza\\setting\\Z4MessageFactory.java",
    "-out",
    "C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\transpile\\giada\\pizzapazza\\setting\\Z4MessageFactory.js"
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
