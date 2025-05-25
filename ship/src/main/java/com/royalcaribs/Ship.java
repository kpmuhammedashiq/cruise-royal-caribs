package com.royalcaribs;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Ship {
    private static final int SHIP_PORT = 8080;
    private static final int OFFSHORE_PORT = 8082;
    private static final String OFFSHORE_ADDRESS = "localhost";

    public static void main(String[] args) throws IOException {
        Socket offshoreSocket = new Socket(OFFSHORE_ADDRESS, OFFSHORE_PORT);
        System.out.println("Connected to Offshore Server: " +offshoreSocket.toString());

        ServerSocket shipServerSocket = new ServerSocket(SHIP_PORT);
        System.out.println("Ship client running on port:" + SHIP_PORT);

        while (true) {
            Socket shipSocket = shipServerSocket.accept();
            System.out.println("Received request from client: " + shipSocket);
            try (
                    BufferedReader shipReader = new BufferedReader(new InputStreamReader(shipSocket.getInputStream()));
                    OutputStream shipWriter = shipSocket.getOutputStream();
                    PrintWriter offshoreWriter = new PrintWriter(offshoreSocket.getOutputStream(), true);
                    InputStream offshoreReader = offshoreSocket.getInputStream();
            ) {
                StringBuilder requestBuilder = new StringBuilder();
                String data;
                while ((data = shipReader.readLine()) != null && !data.isEmpty()) {
                    requestBuilder.append(data).append("\r\n");
                }
                requestBuilder.append("\r\n");

                String fullRequest = requestBuilder.toString();
                System.out.println("Forward Request:\n" + fullRequest);
                offshoreWriter.print(fullRequest);
                offshoreWriter.flush();

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = offshoreReader.read(buffer)) != -1) {
                    shipWriter.write(buffer, 0, bytesRead);
                    shipWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}