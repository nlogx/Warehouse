package head;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws Exception {
    MainSystem ms = new MainSystem();
    ms.readEvents(args[0]);
  }
}
