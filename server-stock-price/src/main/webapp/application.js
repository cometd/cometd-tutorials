require(['dojox/cometd', 'dojo/dom', 'dojo/dom-construct', 'dojo/domReady!'],
function(cometd, dom, doc)
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
            cometd.subscribe('/stock/*', function(message)
            {
                var data = message.data;
                var symbol = data.symbol;
                var value = data.newValue;

                // Find the div for the given stock symbol
                var id = 'stock_' + symbol;
                var symbolDiv = dom.byId(id);
                if (!symbolDiv)
                {
                    symbolDiv = doc.place('<div id="' + id + '"></div>', dom.byId('stocks'));
                }
                symbolDiv.innerHTML = '<span>' + symbol + ': ' + value + '</span>';
            });
        }
        else
        {
            dom.byId('status').innerHTML += '<div>CometD handshake failed</div>';
        }
    });

    cometd.handshake();
});
