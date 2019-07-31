package com.wap.client;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Client cl = new Client();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("메세지 입력 : ");

        String message = br.readLine();
        cl.clientRun(message);

    }

    public void clientRun(String data) {
        Socket socket = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        int port = 4000;
        String host = "localhost";

        try {
            //서버로 전송을 위한 OuputStream
            socket = new Socket(host, port);
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            //서버로부터 data를 받음
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            bw.write(data);
            bw.newLine();
            bw.flush();

            String receiveData = "";
            receiveData = br.readLine();
            System.out.println("서버로부터 받은 데이터 : "+receiveData);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                osw.close();
                os.close();

                br.close();
                isr.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void simpleReview(String data) {
        Socket socket = null;
        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        int port = 4000;
        String host = "localhost";

        try {
            //서버로 전송을 위한 OuputStream
            socket = new Socket(host, port);
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            //서버로부터 data를 받음
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            bw.write(data);
            bw.newLine();
            bw.flush();

            String receiveData = "";
            receiveData = br.readLine();
            System.out.println("서버로부터 받은 데이터 : "+receiveData);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                osw.close();
                os.close();

                br.close();
                isr.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

