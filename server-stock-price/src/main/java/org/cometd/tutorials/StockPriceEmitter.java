package org.cometd.tutorials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StockPriceEmitter implements Runnable
{
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final List<String> symbols = new ArrayList<String>();
    private final Map<String, Float> values = new HashMap<String, Float>();
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();

    public StockPriceEmitter()
    {
        symbols.addAll(Arrays.asList("ORCL", "MSFT", "GOOG", "YHOO", "FB"));
        values.put("ORCL", 29.94f);
        values.put("MSFT", 27.10f);
        values.put("GOOG", 655.37f);
        values.put("YHOO", 17.82f);
        values.put("FB", 21.33f);
    }

    public List<Listener> getListeners()
    {
        return listeners;
    }

    public void start()
    {
        run();
    }

    public void stop()
    {
        scheduler.shutdownNow();
    }

    public void run()
    {
        Random random = new Random();

        List<Update> updates = new ArrayList<Update>();

        // Randomly choose how many stocks to update
        int howMany = random.nextInt(symbols.size()) + 1;
        for (int i = 0; i < howMany; ++i)
        {
            // Randomly choose which one to update
            int which = random.nextInt(symbols.size());
            String symbol = symbols.get(which);
            float oldValue = values.get(symbol);

            // Randomly choose how much to update
            boolean sign = random.nextBoolean();
            float howMuch = random.nextFloat();
            float newValue = oldValue + (sign ? howMuch : -howMuch);

            // Store the new value
            values.put(symbol, newValue);

            updates.add(new Update(symbol, oldValue, newValue));
        }

        // Notify the listeners
        for (Listener listener : listeners)
        {
            listener.onUpdates(updates);
        }

        // Randomly choose how long for the next update
        // We use a max delay of 1 second to simulate a high rate of updates
        long howLong = random.nextInt(1000);
        scheduler.schedule(this, howLong, TimeUnit.MILLISECONDS);
    }

    public static class Update
    {
        private final String symbol;
        private final float oldValue;
        private final float newValue;

        public Update(String symbol, float oldValue, float newValue)
        {
            this.symbol = symbol;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public String getSymbol()
        {
            return symbol;
        }

        public float getOldValue()
        {
            return oldValue;
        }

        public float getNewValue()
        {
            return newValue;
        }
    }

    public interface Listener extends EventListener
    {
        void onUpdates(List<Update> updates);
    }
}
