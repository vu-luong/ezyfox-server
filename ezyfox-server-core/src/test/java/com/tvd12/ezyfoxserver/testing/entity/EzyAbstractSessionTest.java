package com.tvd12.ezyfoxserver.testing.entity;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyDroppedPackets;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliver;
import com.tvd12.ezyfoxserver.socket.EzyBlockingSessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyNonBlockingPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzyPacket;
import com.tvd12.ezyfoxserver.socket.EzyPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzySessionTicketsQueue;
import com.tvd12.ezyfoxserver.socket.EzySocketDisconnectionQueue;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import com.tvd12.ezyfoxserver.testing.MyTestSession;

public class EzyAbstractSessionTest extends BaseCoreTest {

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void test() {
        MyTestSession session = new MyTestSession();
        session.setId(100);
        session.setPrivateKey(new byte[] {1, 2, 3});
        session.addReadBytes(10);
        session.addWrittenBytes(10);
        session.setLoggedInTime(12345);
        session.setMaxWaitingTime(123);
        session.setMaxIdleTime(12345L);
        assertEquals(session.getPrivateKey(), new byte[] {1, 2, 3});
        assertEquals(session.getReadBytes(), 10);
        assertEquals(session.getWrittenBytes(), 10);
        assertEquals(session.getLoggedInTime(), 12345L);
        assertEquals(session.getMaxWaitingTime(), 123L);
        MyTestSession session2 = new MyTestSession();
        session2.setId(1);
        
        MyTestSession session3 = new MyTestSession();
        session3.setId(100);
        
        assert !session.equals(null);
        assert session.equals(session);
        assert !session.equals(new Object());
        assert !session.equals(session2);
        assert !session.equals(new PrivateSession());
        assert session.equals(session3);
        assert session.hashCode() != session2.hashCode();
        
        session.setClientId("clientId");
        assert session.getClientId().equals("clientId");
        session.setOwnerName("owner");
        assert session.getOwnerName().equals("owner");
        session.setLastReadTime(1);
        assert session.getLastReadTime() == 1;
        session.setLastWriteTime(2);
        assert session.getLastWriteTime() == 2;
        session.setLastActivityTime(3);
        assert session.getLastActivityTime() == 3;
        session.setReadRequests(4);
        assert session.getReadRequests() == 4;
        session.setWrittenResponses(5);
        assert session.getWrittenResponses() == 5;
        session.addWrittenResponses(1);
        assert session.getWrittenResponses() == 6;
        session.setDestroyed(false);
        assert !session.isDestroyed();
        session.setStreamingEnable(true);
        assert session.isStreamingEnable();
        session.setClientType("android");
        assert session.getClientType().equals("android");
        session.setClientVersion("1.0.0");
        assert session.getClientVersion().equals("1.0.0");
        session.setBeforeToken("before");
        assert session.getBeforeToken().equals("before");
        session.setDisconnectReason(EzyDisconnectReason.ADMIN_BAN);
        assert session.getDisconnectReason() == EzyDisconnectReason.ADMIN_BAN;
        session.setMaxIdleTime(6);
        assert session.getMaxIdleTime() == 6;
        session.setDroppedPackets(mock(EzyDroppedPackets.class));
        assert session.getDroppedPackets() != null;
        session.setImmediateDeliver(mock(EzyImmediateDeliver.class));
        assert session.getImmediateDeliver() != null;
        
        EzySessionTicketsQueue sessionTicketsQueue = new EzyBlockingSessionTicketsQueue();
        session.setSessionTicketsQueue(sessionTicketsQueue);
        assert session.getSessionTicketsQueue() == sessionTicketsQueue;
        
        EzySocketDisconnectionQueue disconnectionQueue = mock(EzySocketDisconnectionQueue.class);
        session.setDisconnectionQueue(disconnectionQueue);
        assert session.getDisconnectionQueue() == disconnectionQueue;
        assert !session.isDisconnectionRegistered();
        assert session.getDisconnectionLock() != null;
        assert session.getLocks().isEmpty();
        assert session.getLock("test") != null;
        
        assert !session.isActivated();
        session.send(mock(EzyPacket.class));
        session.setActivated(true);
        session.send(mock(EzyPacket.class));
        EzyPacketQueue packetQueue = new EzyNonBlockingPacketQueue(3);
        session.setPacketQueue(packetQueue);
        assert session.getPacketQueue() != null;
        session.send(mock(EzyPacket.class));
        session.sendNow(mock(EzyPacket.class));
        session.send(mock(EzyPacket.class));
        session.send(mock(EzyPacket.class));
        session.send(mock(EzyPacket.class));
        
        assert session.getChannel() == null;
        assert session.getConnection() == null;
        
        session.disconnect();
        session.close();
        session.destroy();
        session.destroy();
        assert session.isDestroyed();
        
        PrivateSession privateSession = new PrivateSession();
        privateSession.setDisconnectionQueue(disconnectionQueue);
        privateSession.disconnect();
        privateSession.disconnect();
        privateSession.setChannel(mock(EzyChannel.class));
        privateSession.close();
        assert privateSession.getConnection() == null;
        assert privateSession.getServerAddress() == null;
    }
    
    private static class PrivateSession extends EzyAbstractSession {
        private static final long serialVersionUID = -3656335144134244222L;
    }
}
