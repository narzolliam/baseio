package com.gifisan.nio.jms.server;

import com.gifisan.nio.Encoding;
import com.gifisan.nio.common.ByteUtil;
import com.gifisan.nio.common.StringUtil;
import com.gifisan.nio.component.Parameters;
import com.gifisan.nio.jms.ErrorMessage;
import com.gifisan.nio.jms.Message;
import com.gifisan.nio.jms.NullMessage;
import com.gifisan.nio.server.session.Session;
import com.gifisan.nio.service.Request;
import com.gifisan.nio.service.Response;

public class JMSBrowserServlet extends JMSServlet {
	
	public static String SIZE = "0";
	
	public static String BROWSER = "1";
	
	public static String ONLINE = "2";
	
	public void accept(Request request, Response response,JMSSessionAttachment attachment) throws Exception {

		Parameters param = request.getParameters();

		String messageID = param.getParameter("messageID");

		Session session = request.getSession();

		MQContext context = getMQContext();

		Message message = NullMessage.NULL_MESSAGE;

		if (context.isLogined(session)) {

			String cmd = param.getParameter("cmd");
			if (StringUtil.isNullOrBlank(cmd)) {
				message = ErrorMessage.CMD_NOT_FOUND_MESSAGE;
			} else {
				if (SIZE.equals(cmd)) {
					response.write(String.valueOf(context.messageSize()));
				} else if (BROWSER.equals(cmd)) {

					if (!StringUtil.isNullOrBlank(messageID)) {
						message = context.browser(messageID);

						if (message == null) {

							message = NullMessage.NULL_MESSAGE;

							response.write(message.toString());
						} else {
							response.write(message.toString(), Encoding.DEFAULT);
						}
					}
				}else if(ONLINE.equals(cmd)){
					
					boolean bool = context.isOnLine(param.getParameter("queueName"));
					
					byte result =  ByteUtil.getByte(bool);
					
					response.write(result);
				}
			}
		} else {
			message = ErrorMessage.UNAUTH_MESSAGE;
			response.write(message.toString());
		}

		response.flush();
	}


	
	
}