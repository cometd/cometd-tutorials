package org.cometd.tutorials;

import org.cometd.annotation.Listener;
import org.cometd.annotation.Service;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;

@Service
public class ClientHelloService
{
    @Listener("/service/hello")
    public void processClientHello(ServerSession session, ServerMessage message)
    {
        System.out.printf("Received greeting '%s' from remote client %s%n", message.getData(), session.getId());
    }
}
