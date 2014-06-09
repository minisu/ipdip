package minisu.ipdip.websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class MyWebSocketServlet extends WebSocketServlet
{
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(WebSocketEndpoint.class);
	}
}