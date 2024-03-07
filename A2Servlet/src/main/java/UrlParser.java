import java.util.HashMap;
import java.util.Map;
import pojo.Request;

public class UrlParser {

  private Map<String, String> transitions = new HashMap<>();
  private Map<String, Integer> params = new HashMap<>();
  private String path;
  private Request req;

  public UrlParser() {
  }

  public UrlParser(String urlPath) {
    this.path = urlPath;
  }

  public String parseUrl() {
    String[] parts = path.split("/");
    String curState = "resortID";
    if (!parts[0].equals("")) {
      return Configs.INVALID_INPUTS;
    }
    // urlPath  = "/1/seasons/2019/days/1/skiers/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]

    transitions.put("resortID", "seasons");
    transitions.put("seasons", "seasonID");
    transitions.put("seasonID", "days");
    transitions.put("days", "dayID");
    transitions.put("dayID", "skiers");
    transitions.put("skiers", "skierID");
    transitions.put("skierID", null);

    for (int i = 1; i < parts.length; i++) {
      String part = parts[i];
      if (curState.equals("resortID") || curState.equals("seasonID") || curState.equals("dayID")
          || curState.equals("skierID")) {
        try {
          int val = Integer.parseInt(part);
          params.put(curState, val);
          curState = transitions.get(curState);
        } catch (NumberFormatException e) {
          return Configs.INVALID_INPUTS;
        }
      } else if (parts[i].equals(curState)) {
        curState = transitions.get(curState);
      } else {
        return Configs.INVALID_INPUTS;
      }
    }

    //check all params in url are present
    if (params.size() != 4) {
      return Configs.MISSING_PARAM;
    }

    //check params in url are in valid range
    if (Configs.isResortIDValid(params.get("resortID")) && Configs.isSeasonIDValid(
        params.get("seasonID")) && Configs.isDayIDValid(params.get("dayID"))
        && Configs.isSkierIDValid(
        params.get("skierID"))) {
      req = new Request(params.get("resortID"), params.get("seasonID"), params.get("dayID"),
          params.get("skierID"));
      return Configs.VALID;
    } else {
      return Configs.INVALID_INPUTS;
    }
  }

  public Request getRequest() {
    if (Configs.isResortIDValid(params.get("resortID")) && Configs.isSeasonIDValid(
        params.get("seasonID")) && Configs.isDayIDValid(params.get("dayID"))
        && Configs.isSkierIDValid(
        params.get("skierID"))) {
      return req;
    } else return null;
  }


}
