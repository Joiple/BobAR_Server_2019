package com.wap.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Server sv = new Server();
        sv.serverRun();

    }

    public void serverRun() throws IOException {
        ServerSocket server = null;
        int port = 4000;
        Socket socket = null;

        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;


        try {
            server = new ServerSocket(port);
            while (true) {
                System.out.println("----------waiting----------");
                socket = server.accept();	//Ŭ���̾�Ʈ�� �����ϸ� ����� �� �ִ� ���� ��ȯ
                System.out.println(socket.getInetAddress() +" �� ���� �������");

                //Ŭ���̾�Ʈ�κ��� �����͸� �ޱ� ���� InputStream ����
                is = socket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                String data = null;
                data = br.readLine();
                System.out.println("Ŭ���̾�Ʈ�� ���� ���� ������ : "+data);

                //���� ������ �״��  �ٽ� ������
                sendData(data, socket);
                System.out.println("--------send success--------");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            br.close();
            isr.close();
            is.close();
            server.close();
        }

    }

    public void sendData(String data, Socket socket) {
        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try {
            //Ŭ���̾�Ʈ�� �����͸� ������ ���� OutputStream ����
            os = socket.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            bw.write("������ ���� ���� ������ : "+data);
            bw.flush();
        } catch (Exception e) {
            // TODO: handle exception

            e.printStackTrace();
        } finally {
            try {
                bw.close();
                osw.close();
                os.close();
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}

