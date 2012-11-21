package org.cometd.tutorials;

import java.io.IOException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;

public class Initializer extends GenericServlet
{
    private StockPriceEmitter emitter;

    @Override
    public void init() throws ServletException
    {
        // Create the emitter
        emitter = new StockPriceEmitter();

        // Retrieve the CometD service instantiated by AnnotationCometdServlet
        StockPriceService service = (StockPriceService)getServletContext().getAttribute(StockPriceService.class.getName());

        // Register the service as a listener of the emitter
        emitter.getListeners().add(service);

        // Start the emitter
        emitter.start();
    }

    @Override
    public void destroy()
    {
        // Stop the emitter
        emitter.stop();
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException
    {
        throw new UnavailableException("Initializer");
    }
}
