import TSim.*;

public class Example {

  public static void main(String[] args) {
    new Example(args);
  }

  public Example(String[] args) {
    TSimInterface tsi = TSimInterface.getInstance();

    try {
      tsi.setSpeed(1,10);
    }
    catch (CommandException e) {
      e.printStackTrace();    // or only e.getMessage() for the error
      System.exit(1);
    }
  }
}
