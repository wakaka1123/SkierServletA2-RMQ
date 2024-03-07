public class Utils {

  //resortID - between 1 and 10
  //seasonID - 2024
  //dayID - 1, swagger says 1-366
  //skierID - between 1 and 100000

  //req body
  //liftID - between 1 and 40
  //time - between 1 and 360
  public static boolean isUrlValid(String urlPath) {
    String[] urlParts = urlPath.split("/");
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/days/1/skiers/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    if (urlParts.length != 8 || !urlParts[2].equals("seasons") || !urlParts[4].equals("days")
        || !urlParts[6].equals("skiers")) {
      return false;
    }

    try {
      int resortID = Integer.parseInt(urlParts[1]);
      int seasonID = Integer.parseInt(urlParts[3]);
      int dayID = Integer.parseInt(urlParts[5]);
      int skierID = Integer.parseInt(urlParts[7]);
      if (isValidResortID(resortID) && isValidSeasonID(seasonID) && isValidDayID(dayID)
          && isValidSkierID(skierID)) {
        return true;
      } else return false;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return false;
    }

  }

  private static boolean isValidResortID(int resortID) {
    return resortID >= 1 && resortID < 11;
  }

  private static boolean isValidSeasonID(int seasonID) {
    return seasonID == 2024;
  }

  private static boolean isValidDayID(int dayID) {
    return dayID >= 1 && dayID < 367;
  }

  private static boolean isValidSkierID(int skierID) {
    return skierID >= 1 && skierID < 100001;
  }

}
