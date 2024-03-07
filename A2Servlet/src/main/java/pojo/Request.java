package pojo;

public class Request {

  private int resortID;
  private int seasonID;
  private int dayID;
  private int skierID;

  public int getResortID() {
    return resortID;
  }

  public int getSeasonID() {
    return seasonID;
  }

  public int getDayID() {
    return dayID;
  }

  public int getSkierID() {
    return skierID;
  }

  public Request() {
  }

  public Request(int resortID, int seasonID, int dayID, int skierID) {
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.skierID = skierID;
  }
}
