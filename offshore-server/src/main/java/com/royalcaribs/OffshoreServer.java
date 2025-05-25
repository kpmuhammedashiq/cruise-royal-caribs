package com.royalcaribs;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class OffshoreServer {
    private static final int OFFSHORE_PORT = 8082;

    public static void main(String[] args) throws IOException {
        ServerSocket offshoreSocket = new ServerSocket(OFFSHORE_PORT);
        System.out.println("Offshore Server - up and running:"+offshoreSocket.toString());
        Socket clientSocket = offshoreSocket.accept();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        while (true) {
            String data;
            StringBuilder request = new StringBuilder();
            while ((data = bufferedReader.readLine()) != null  && !data.isEmpty()) {
                request.append(data).append("\r\n");
            }
            System.out.println("Request: " + request);

            String[] requestParts = request.toString().split("\r\n")[0].split(" ");
            String method = requestParts[0];
            String reqUrl = requestParts[1];
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = null;
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                requestBody = RequestBody.create("", null);
            }
            Request rqExternal = new Request.Builder()
                    .url(reqUrl)
                    .method(method, requestBody)
                    .build();
            Response rpExternal = client.newCall(rqExternal).execute();
            byte[] bodyBytes = rpExternal.body().bytes();

            bufferedWriter.write("HTTP/1.1 200 OK\r\n");
            bufferedWriter.write("Content-Type: text/plain\r\n");
            bufferedWriter.write("Content-Length: " + bodyBytes.length + "\r\n");
            bufferedWriter.write("\r\n");
            bufferedWriter.flush();
            clientSocket.getOutputStream().write(bodyBytes);
            clientSocket.getOutputStream().flush();
        }

    }
}