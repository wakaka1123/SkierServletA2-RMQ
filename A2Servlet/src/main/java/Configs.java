public class Configs {
  public static final String VALID = "Valid";
  public static final String MISSING_PARAM = "Missing parameter";
  public static final String INVALID_INPUTS = "Invalid inputs";
  public static final String DATA_NOT_FOUND = "Data not found";
  public static final String WRITE_SUCCESS = "Write successful";
  public static final String EXCHANGE_NAME = "exchange_fanout";
  public static final String ROUTE_KEY = "";

  public static boolean isSkierIDValid(int skierID) {
    return skierID >= 1 && skierID < 100001;
  }

  public static boolean isResortIDValid(int resortID) {
    return resortID >= 1 && resortID < 11;
  }

  public static boolean isSeasonIDValid(int seasonID) {
    return seasonID == 2024;
  }

  public static boolean isDayIDValid(int dayID) {
    return dayID >= 1 && dayID < 367;
  }

  public static boolean isLiftIDValid(int liftID) {
    return liftID >= 1 && liftID < 41;
  }

  public static boolean isTimeValid(int time) {
    return time >= 1 && time < 361;
  }

}
