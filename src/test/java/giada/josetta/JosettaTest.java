package giada.josetta;

public class JosettaTest {

  private final static String[] args1 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\pizzApazzAinfinity\\src\\giada\\pizzapazza\\ui\\Z4ModalMessageUI.java",
    "-out",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\pizzApazzAinfinity\\transpile\\giada\\pizzapazza\\ui\\Z4ModalMessageUI.js"
  };

  private final static String[] args2 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\pizzApazzAinfinity\\src\\",
    "-out",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\pizzApazzAinfinity\\transpile\\",
    "-w"
  };

  private final static String[] args3 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\swing.js\\test\\giada\\swingjs\\TestJFrame1.java",
    "-out",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\swing.js\\transpile-test\\giada\\swingjs\\TestJFrame1.js",
  };
  
  private final static String[] args4 = new String[]{
    "-in",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\swing.js\\src\\giada\\swingjs\\JButton.java",
    "-out",
    "C:\\Users\\gianpiero.diblasi\\codice\\Personale\\swing.js\\transpile\\giada\\swingjs\\JButton.js",
  };
  
  public static void main(String[] args) throws Exception {
    Josetta.main(args4);
  }
}
