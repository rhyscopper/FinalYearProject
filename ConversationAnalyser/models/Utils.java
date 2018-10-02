// some general utilities
// copyright 2016 Matthew Purver, Queen Mary University of London

public static class Utils {

  // lower-case, strip punctuation & repeats, tokenise
  public static String[] preProcess(String s) {
    s = s.trim().toLowerCase();
    s = s.replaceAll("\\s+"," ");
    s = s.replaceAll("(\\w)([.,;:!?'\"])","$1 $2");
    s = s.replaceAll("(\\S)\\1\\1+","$1$1$1");
    return s.split("\\s+");
  }

  // read some lines from a text file 
  public static List<String> readFileLines(String fileName) {
    ArrayList<String> lines = new ArrayList<String>();
    try {
      FileInputStream instream = new FileInputStream(new File(fileName));
      BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
      String inLine;
      while ((inLine = reader.readLine()) != null) {
        lines.add(inLine);
      }
      reader.close();        
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }
    return lines;
  }
  
}
