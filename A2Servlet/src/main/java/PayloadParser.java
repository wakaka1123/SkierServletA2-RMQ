import com.google.gson.Gson;
import pojo.Payload;

public class PayloadParser {

  String payloadStr;
  Payload payload;

  public PayloadParser() {
  }

  public PayloadParser(String payloadStr) {
    this.payloadStr = payloadStr;
  }

  public String parsePayLoad() {
    try {
      payload = new Gson().fromJson(payloadStr, Payload.class);
      if (Configs.isLiftIDValid(payload.getLiftID()) && Configs.isTimeValid(payload.getTime())) {
        payload = new Payload(payload.getTime(), payload.getLiftID());
        return Configs.VALID;
      } else {
        return Configs.DATA_NOT_FOUND;
      }
    } catch (Exception e) {
      return Configs.DATA_NOT_FOUND;
    }
  }

  public Payload getPayload() {
    if (Configs.isLiftIDValid(payload.getLiftID()) && Configs.isTimeValid(payload.getTime())) {
      return payload;
    } else return null;
  }


}
