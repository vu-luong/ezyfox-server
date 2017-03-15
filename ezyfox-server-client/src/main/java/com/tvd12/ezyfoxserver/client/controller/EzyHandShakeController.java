package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.controller.EzyServerController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzyHandShakeController 
		extends EzyAbstractController 
		implements EzyServerController<EzySession> {

	@Override
	public void handle(EzyContext ctx, EzySession session, EzyArray data) {
		getLogger().info("begin handle handshake");
		sendLoginRequest(ctx, session);
		getLogger().info("end handshake sending login request");
	}
	
	protected void sendLoginRequest(EzyContext ctx, EzySession session) {
		ctx.get(EzySendMessage.class)
			.data(newLoginData(ctx, "dungtv", "123456"))
			.sender(session)
			.execute();
	}
	
	protected EzyArray newLoginData(EzyContext ctx, String name, String pass) {
		return serializeToArray(ctx, newLoginRequest(name, pass));
	}
	
	protected EzyLoginRequest newLoginRequest(String name, String pass) {
		return EzyLoginRequest.builder()
				.username(name)
				.password(pass)
				.data(newLoginInData())
				.build();
	}
	
	protected EzyData newLoginInData() {
		return newObjectBuilder()
				.append("v", "1.0.0")
				.append("t", "android")
				.build();
	}

}
