package pojo;

public class Payload {
  private int time;
  private int liftID;

  public int getTime() {
    return time;
  }

  public int getLiftID() {
    return liftID;
  }

  public Payload() {
  }

  public Payload(int time, int liftID) {
    this.time = time;
    this.liftID = liftID;
  }
}
