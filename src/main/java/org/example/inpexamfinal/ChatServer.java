package org.example.inpexamfinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    //Initialize PORT and Number
    private static final int PORT=5000;
    private static final Set<ClientHandler> clients=new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat Server is Running...!");
        try(
                //Add PORT to the Server
                ServerSocket serverSocket=new ServerSocket(PORT);
        ){
            while (true){
                Socket socket=serverSocket.accept();
                ClientHandler client=new ClientHandler(socket);
                clients.add(client);
                client.start();


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //use Server for Threads
    public static class ClientHandler extends Thread{
        //initialize socket,writer,username;
        private Socket socket;
        private PrintWriter writer;
        private String username;

         //initialize the Socket
        public ClientHandler(Socket socket) {
            this.socket=socket;
        }
        //Overide the run method in Thread
        @Override
        public void run(){
            try(
                    BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //Validate the autoFlush
                    PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
            ){
                //initialize the username and password
                this.username= reader.readLine();
                this.writer=writer;

                //show that the joined the chat
                System.out.println(username+" has Joined the Chat!");
                broadcastMessage("Server:"+username+" has Joined the Chat!");


                String message;
                while ((message= reader.readLine())!=null){
                    System.out.println("Received:"+message);
                    broadcastMessage(message);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //check the users are not in the chat
                if (username!=null){
                    System.out.println(username+" has Left the Chat!");
                    broadcastMessage("Server:"+username+" has Left the Chat!");
                }
            }

        }

        private void broadcastMessage(String message) {
            synchronized (clients){
                for (ClientHandler client:clients){
                    if (client!=this){
                        client.writer.println(message);
                    }
                }
            }
        }
    }
}
