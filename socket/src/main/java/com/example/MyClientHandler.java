package com.example;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MyClientHandler extends IoHandlerAdapter{

    @Override
    public void sessionCreated(IoSession session) throws Exception{
        super.sessionCreated(session);
        SL.i("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception{
        super.sessionOpened(session);
        SL.i("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception{
        super.sessionClosed(session);
        SL.i("sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception{
        super.sessionIdle(session, status);
        SL.i("sessionIdle");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception{
        super.exceptionCaught(session, cause);
        SL.i("exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception{
        super.messageReceived(session, message);
        SL.i("messageReceived " + message);
//		session.write("server reply: " + message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception{
        super.messageSent(session, message);
        SL.i("messageSent");
    }

    @Override
    public void inputClosed(IoSession session) throws Exception{
        super.inputClosed(session);
        SL.i("inputClosed");
    }
}
