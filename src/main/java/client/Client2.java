package client;

import logger.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client2 {
        private static final Logger LOGGER = Logger.getInstance();
        static String host;
        static String port;
        private static final String EXITCHAT = "exit";
        private static Socket clientSocket = null;
        private static BufferedReader inMess;
        private static PrintWriter outMess;
        private static Scanner scannerConsole;

        public static void main(String[] args) throws IOException {
                connectingClientToHost();
                clientSocket = new Socket(host, Integer.parseInt(port));
                outMess = new PrintWriter(clientSocket.getOutputStream(), true);
                inMess = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                scannerConsole = new Scanner(System.in);

                String greeting = "Приветствуем вас в чате! Введите свое имя и начните общение: \nДля выхода из чата напишите \"exit\".";
                System.out.println(greeting);
                LOGGER.log(greeting);

                AtomicBoolean flag = new AtomicBoolean(true);
                //поток принимающий сообщения от сервера и печатающий в консоль
                new Thread(() -> {
                        try {
                                while (true) {
                                        if (flag.get() == false) {
                                                inMess.close();
                                                clientSocket.close();
                                                break;
                                        }
                                        if (inMess.ready()) {
                                                String messFromServer = inMess.readLine();
                                                System.out.println(messFromServer);
                                        }
                                }
                        } catch (IOException ex) {
                                ex.printStackTrace();
                        }
                }).start();

                //поток отправляет сообщения на сервер
                new Thread(() -> {
                        while (true) {
                                if (scannerConsole.hasNext()) {
                                        String mess = scannerConsole.nextLine();

                                        if (mess.equalsIgnoreCase(EXITCHAT)) {
                                                outMess.println(mess);
                                                scannerConsole.close();
                                                outMess.close();
                                                flag.set(false);
                                                break;
                                        }
                                        outMess.println(mess);
                                        outMess.flush();
                                }
                        }
                }).start();
        }
        public static void connectingClientToHost() {
                String path = new File("../src/settings.properties").getAbsolutePath();

                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
                        Properties properties = new Properties();
                        properties.load(bufferedReader);

                        port = properties.getProperty("port");
                        host = properties.getProperty("host");

                } catch (IOException e) {
                        e.printStackTrace();
                }

        }
}
