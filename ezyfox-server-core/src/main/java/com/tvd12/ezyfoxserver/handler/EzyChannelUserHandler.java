package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

import io.netty.channel.ChannelHandlerContext;

public class EzyChannelUserHandler extends EzyChannelSessionHandler {

	protected EzyUser user;
	protected EzyUserManager userManager;
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		this.chechAndRemoveUser();
	}
	
	protected void chechAndRemoveUser() {
		if(user != null)
			removeUser();
	}
	
	protected void removeUser() {
		userManager.removeUser(user);
		user.setSession(null);
	}
	
	@Override
	protected Object getReceiver() {
		return user != null ? user : checkAndGetReceiver();
	}
	
	protected Object checkAndGetReceiver() {
		if(userManager.containsUser(getSession()))
			user = userManager.getUser(getSession());
		return user != null ? user : super.getReceiver(); 
	}
	
	@Override
	public void setContext(EzyServerContext ctx) {
		super.setContext(ctx);
		this.userManager = getManagers().getManager(EzyUserManager.class);
	}
	
}
