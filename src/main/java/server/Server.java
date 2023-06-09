package server;

import logger.Logger;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private static final Logger LOGGER = Logger.getInstance();
    private static Map<Integer, User> usersList = new HashMap<>();
    final static String waySettings  = new File("../src/settings.properties").getAbsolutePath();
    private static int port ;

    public static void main(String[] args)  {

        getPort(waySettings);

        LOGGER.log("Start Server");
        try (ServerSocket serverSocket = new ServerSocket(port)  ) {
            while (true){
                try {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() ->{
                        try(PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true)){
                                User user = new User(clientSocket, out);
                                usersList.put(clientSocket.getPort(), user);
                                LOGGER.log(user + " Подключился к чату");
                                waitMessAndSend(clientSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                           try {
                               clientSocket.close(); //закрытие сокета клиент
                           }catch (IOException e){
                               e.printStackTrace();
                           }
                        }
                    }).start();
                }catch (IOException q){
                    q.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static synchronized void sendMessToAll(String mess) {
        LOGGER.log(mess);
        for (Map.Entry<Integer, User> entry : usersList.entrySet()) {
                entry.getValue().sendMsg(mess);
            LOGGER.log("Сообщение отправлено");
        }
    }
    public static void waitMessAndSend(Socket clientSocket) {

        try (Scanner inMess = new Scanner(clientSocket.getInputStream())) {
            boolean hasName = false;
            User user = usersList.get(clientSocket.getPort());
            while (true) {
                if (inMess.hasNext()) {
                    String mess = inMess.nextLine();
                    if (exitCheck(mess,user,clientSocket)){
                        continue;
                    }
                    if (hasName == false){
                        user.setName(mess);
                        hasName = true;
                        sendMessToAll("К чату подключился новый участник: : " + user.getName());
                    }else{
                        sendMessToAll(user.getName() + ": " + mess);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean exitCheck(String mess, User user, Socket clientSocket ) {
            if (mess.equalsIgnoreCase("exit")) {
                if (user.getName() == null) {
                    sendMessToAll("Пользователь c портом " + clientSocket.getPort() + " покинул чат");
                } else {
                    sendMessToAll("Пользователь " + user.getName() + " покинул чат");
                }
                return true;
            }
            return false;
    }
    public static boolean getPort(String waySettings){

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(waySettings))) {
            Properties properties = new Properties();
            properties.load(bufferedReader);

            port = Integer.parseInt(properties.getProperty("port"));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
