package com.example.project2notificationsample;

import java.io.IOException;
import java.net.URL;

public class Notifier {

    private long userId;
    private SSEPlainTextConsumer sse;
    private Thread t;
    private int sleeptime;
    private NotifierListener nl;
    /**
     * 0: not started
     * 1: running
     * 2: ended
     */
    private int state;

    public void start() {
        if(state != 0) {
            throw new RuntimeException("Error state: " + state);
        }

        try {
            String urlFmt = "http://10.0.2.2:8080/stream-sse-mvc";
            URL url = new URL(String.format(urlFmt, userId));
            sse = new SSEPlainTextConsumer(url);
            sse.connect();

            t = new Thread(() -> {
                // Listen to event
                while (true) {
                    try {
                        nl.listen(sse.getContent());
                        Thread.sleep(sleeptime);
                    }
                    catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            });
            t.start();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException("Failed to create notifier.");
        }
    }

    public void stop() {
        if(state != 1) {
            throw new RuntimeException("Error state: " + state);
        }

        t.interrupt();
        sse.disconnect();
    }

    /**
     * Creates a new {@code Notifier} that polls availability.
     * Wrapper for {@link SSEPlainTextConsumer}.
     * To prevent busy polling, either the {@code Notifier} must sleep,
     * or the {@code NotifierListener} must sleep.
     *
     * @param userId
     * @param nl action to take after receiving data
     * @param sleeptime time to sleep after each poll
     */
    public Notifier(long userId, int sleeptime, NotifierListener nl) {
        this.userId = userId;
        this.sleeptime = sleeptime;
        this.nl = nl;
        this.state = 0;
    }
}
