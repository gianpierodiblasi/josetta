package giada.josetta;

import java.io.File;

public class JosettaTest {

  public static void main(String[] args) throws Exception {
    File in = new File("C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\src\\giada\\pizzapazza\\math\\Z4Math.java");
    File out = new File("C:\\Users\\gianpiero.di.blasi\\codice\\Personale\\pizzApazzAinfinity\\src\\giada\\pizzapazza\\math\\Z4Math.js");
    Josetta.transpile(in, out);
  }
}
