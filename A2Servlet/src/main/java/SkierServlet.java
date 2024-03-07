import com.google.gson.Gson;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import pojo.Message;
import pojo.Payload;
import pojo.Request;
import pojo.Status;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

  private GenericObjectPool<Channel> channelPool;

  @Override
  public void init() throws ServletException {
    super.init();
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("44.230.124.84");
      factory.setUsername("guest");
      factory.setPassword("guest");

      Connection connection = factory.newConnection();
      GenericObjectPoolConfig<Channel> poolConfig = new GenericObjectPoolConfig<>();
      poolConfig.setMaxTotal(10);
      channelPool = new GenericObjectPool<>(new RMQChannelFactory(connection), poolConfig);
      System.out.println("Channel pool initialized successfully");
    } catch (Exception e) {
      throw new ServletException("Failed to initialize channel pool", e);
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    try {
      if (channelPool != null) {
        channelPool.close();
        System.out.println("Channel pool closed");
      }
    } catch (Exception e) {
      System.err.println("Failed to close channel pool" + e.getMessage());
      throw new RuntimeException("Failed to close channel pool", e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    StringBuilder sb = new StringBuilder();
    String s;
    while ((s = req.getReader().readLine()) != null) {
      sb.append(s);
    }
    Gson gson = new Gson();
    Status status = new Status();
    UrlParser urlParser = new UrlParser(urlPath);
    PayloadParser payloadParser = new PayloadParser(sb.toString());

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty() || urlPath.equals("/")) {
      getResponse(res, gson, status, HttpServletResponse.SC_BAD_REQUEST, Configs.MISSING_PARAM);
      return;
    }

    if (urlParser.parseUrl().equals(Configs.VALID)) {
      if (payloadParser.parsePayLoad().equals(Configs.VALID)) {
        sendToConsumer(urlParser, payloadParser);
        getResponse(res, gson, status, HttpServletResponse.SC_CREATED, Configs.WRITE_SUCCESS);
        return;
      } else if (payloadParser.parsePayLoad().equals(Configs.DATA_NOT_FOUND)) {
        getResponse(res, gson, status, HttpServletResponse.SC_NOT_FOUND, Configs.DATA_NOT_FOUND);
        return;
      } else {
        getResponse(res, gson, status, HttpServletResponse.SC_BAD_REQUEST, Configs.INVALID_INPUTS);
        return;
      }
    } else if (urlParser.parseUrl().equals(Configs.MISSING_PARAM)) {
      getResponse(res, gson, status, HttpServletResponse.SC_BAD_REQUEST, Configs.MISSING_PARAM);
      return;
    } else {
      getResponse(res, gson, status, HttpServletResponse.SC_BAD_REQUEST, Configs.INVALID_INPUTS);
      return;
    }

  }

  private void getResponse(HttpServletResponse res, Gson gson, Status status, int statusCode,
      String message) throws IOException {
    res.setStatus(statusCode);
    status.setMessage(message);
    res.getOutputStream().print(gson.toJson(status));
    res.getOutputStream().flush();
  }

  private void sendToConsumer(UrlParser urlParser, PayloadParser payloadParser)
      throws ServletException, IOException {
    try {
      Request request = urlParser.getRequest();
      Payload payload = payloadParser.getPayload();
      //    this.time = time;
      //    this.liftID = liftID;
      //    this.resortID = resortID;
      //    this.seasonID = seasonID;
      //    this.dayID = dayID;
      //    this.skierID = skierID;
      String msg = new Message(payload.getTime(), payload.getLiftID(), request.getResortID(),
          request.getSeasonID(), request.getDayID(),
          request.getSkierID()).toString();
      System.out.println("Message to be sent: " + msg);

      Channel channel = channelPool.borrowObject();
      channel.exchangeDeclare(Configs.EXCHANGE_NAME, BuiltinExchangeType.FANOUT, false, false, false, null);

      byte[] message = msg.getBytes();
      channel.basicPublish(Configs.EXCHANGE_NAME, Configs.ROUTE_KEY, null, message);
      channelPool.returnObject(channel);

    } catch (Exception e) {
      Logger.getLogger(SkierServlet.class.getName()).log(Level.INFO, null, e);
      throw new ServletException("Failed to send message to consumer", e);
    }


  }


}