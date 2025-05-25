package com.royalcaribs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class OffshoreServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8082);
        Socket clientSocket = serverSocket.accept();
        System.out.println("Offshore Server - up and running:"+8082);

        BufferedReader bRead = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            while (true) {
                String data;
                StringBuilder request = new StringBuilder();
                while ((data = bRead.readLine()) != null  && !data.isEmpty()) {
                    request.append(data).append("\r\n");
                }

                System.out.println("Received request:" + request);

//                clientSocket.close();
//                serverSocket.close();
            }

    }
}