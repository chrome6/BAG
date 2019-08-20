package android.com.beacon1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnect {
    public static final int ServerPort = 6556;
    public static final String ServerIP = "210.119.104.191";
    //public static final String ServerIP = "192.168.0.54";
    //public static final String ServerIP = "172.20.10.5";
    public static Socket client = null;

    public SocketConnect() {
        try {
            Log.d("###","socketconnect");
            if (client == null)
                client = new Socket(ServerIP, ServerPort);
            Log.d("###",client.toString());

        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }

    }
    public void socketClose()
    {
        try{
            if(client==null){
                return;
            }
            client.close();
            client=null;
        }catch (Exception e){
            Log.d("###","closefail");
            e.printStackTrace();
        }
    }

    public void sendData(String sendStr) {
        PrintWriter out;
        String msg;
        try {
            //Socket client = new Socket(ServerIP,ServerPort);
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                out.println(sendStr);
                Log.d("###",sendStr);
            } catch (Exception e) {
                //client.close();
                System.out.println("S: Error");
                e.printStackTrace();
            }
        }catch (Exception e){
            System.out.println("S: Error");
        }finally {
            //client.close();
            System.out.println("S: Done.");
        }

    }

    public String receiveData() {
        try {
            //Socket client = serverSocket.accept();
            System.out.println("S: Receiving...");
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String str = in.readLine();
                System.out.println("S: Received: '" + str + "'");

                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                out.println("Server Received " + str);
                return str;
            } catch (Exception e) {
                client.close();
                System.out.println("S: Error");
                e.printStackTrace();
                return null;
            } finally {
                //client.close();
                System.out.println("S: Done.");
            }
        } catch (Exception e) {
            System.out.println("S: Error");
            return null;
        }
    }

    public String sendAndReceive(String sendStr) {
        PrintWriter printwriter;
        BufferedReader bufferedReader;

        try {

            OutputStream os = null;
            OutputStreamWriter osw =null;
            BufferedWriter bw = null;

            InputStream is =null;
            InputStreamReader isr = null;
            BufferedReader br = null;

            try{
                os = client.getOutputStream();
                osw = new OutputStreamWriter(os);
                bw = new BufferedWriter(osw);            //서버로 전송을 위한 OutputStream


                is = client.getInputStream();
                Log.d("###","inputMake");
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);        // 서버로부터 Data를 받음

                bw.write(sendStr);
                bw.newLine();
                bw.flush();
                Log.d("####","sendFinish");

                String receiveData = "";
                receiveData = br.readLine();        // 서버로부터 데이터 한줄 읽음
                Log.d("####","서버로부터 받은 데이터 : " + receiveData);
                return receiveData;
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try{
                    //bw.close();
                    //osw.close();
                    //os.close();
                    //br.close();
                    //isr.close();
                    //is.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            System.out.println("S: Error");
            return null;
        }
        return null;
    }

}
