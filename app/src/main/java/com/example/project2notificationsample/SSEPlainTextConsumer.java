package com.example.project2notificationsample;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SSEPlainTextConsumer {

    private final URL url;
    private HttpURLConnection con;

    /**
     * 0: uninitialized
     * 1: connected
     * 2: disconnected
     * 9: failed
     */
    private int state = 0;

    public void connect() {
        if(state == 1) {
            return;
        }

        try {
            con = (HttpURLConnection) url.openConnection();
            // SSE supports GET only
            con.setRequestMethod("GET");
            // con.getInputStream blocks if no input is present

            state = 1;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            state = 9;
        }
    }

    public void disconnect() {
        if(state != 1) {
            return;
        }
        con.disconnect();
    }

    public boolean isConnected() {
        return state == 1;
    }

    /**
     * Must be placed in a separate thread.
     * Blocking until content is available.
     *
     * @return
     * @throws IOException
     */
    public String getContent() throws IOException{
        if(state == 1) {
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = br.readLine();
            if(line != null) {
                return line;
            }
            else {
                state = 2;
                return "";
            }
        }
        throw new RuntimeException("Connection closed or failed.");
    }

    public SSEPlainTextConsumer(URL url) {
        this.url = url;
    }

    @Deprecated
    public SSEPlainTextConsumer() {
        this.url = null;
    }

    @Deprecated
    public void initiate () {
        System.err.println("Test");
        try {
            URL url = new URL("http://10.0.2.2:8080/api/notification?userid=1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
