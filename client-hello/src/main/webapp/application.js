require(['dojox/cometd', 'dojo/dom', 'dojo/domReady!'], function(cometd, dom)
{
    cometd.configure({
        url: location.protocol + '//' + location.host + config.contextPath + '/cometd',
        logLevel: 'info'
    });

    cometd.addListener('/meta/handshake', function(message)
    {
        if (message.successful)
        {
            dom.byId('status').innerHTML += '<div>CometD handshake successful</div>';
        }
        else
        {
            dom.byId('status').innerHTML += '<div>CometD handshake failed</div>';
        }
    });

    dom.byId('greeter').onclick = function()
    {
        cometd.publish('/service/hello', 'Hello, World');
    };

    cometd.handshake();
});
