package MyChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyChat extends JFrame {
    private static final List<PrintWriter> clients = new ArrayList<>();
    private PrintWriter writer;
    private JTextArea chatArea;
    private JTextField messageField;
    private String nickname;

    private JComboBox<String> colorComboBox; // 추가: 배경 색상 선택을 위한 콤보박스
    private static final String[] COLORS = { "Pink", "Sky Blue", "White", "Yellow" };

    private JScrollPane scrollPane; // 수정: 채팅 영역의 JScrollPane를 전역으로 선언

    private JFrame frame; // 수정: MyChat 클래스의 인스턴스 변수로 추가

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyChat().startClient());
        startServer();
    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("서버시작");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트 접속");

                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                clients.add(writer);

                Thread listenerThread = new Thread(new ClientListener(clientSocket, writer));
                listenerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startClient() {
        JFrame frame = new JFrame("채팅 앱");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        // 콤보박스 초기화
        colorComboBox = new JComboBox<>(COLORS);
        colorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedColor = (String) colorComboBox.getSelectedItem();
                changeChatBackgroundColor(selectedColor);
            }
        });

        // 콤보박스를 상단에 추가
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.add(new JLabel("배경 색상: "));
        comboPanel.add(colorComboBox);
        frame.add(comboPanel, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        // 채팅 영역의 JScrollPane 초기화
        scrollPane = new JScrollPane(chatArea);
        // 배경 색상 설정
        scrollPane.getViewport().setBackground(Color.PINK);
        frame.add(scrollPane, BorderLayout.CENTER);

        messageField = new JTextField();
        JButton sendButton = new JButton("전송");
        sendButton.addActionListener(new SendButtonListener());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(inputPanel, BorderLayout.SOUTH);

        // 닉네임 입력
        nickname = JOptionPane.showInputDialog(frame, "이름을 입력하세요", "이름 입력창", JOptionPane.PLAIN_MESSAGE);
        // 채팅 서버에 연결
        String serverIP = "127.0.0.1";
        connectToServer(serverIP);

        frame.setVisible(true);
    }

    private void connectToServer(String serverAddress) {
        try (Socket socket = new Socket(serverAddress, 5555)) {
            writer = new PrintWriter(socket.getOutputStream(), true);
            Thread readerThread = new Thread(new ServerListener(socket, this));
            readerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeChatBackgroundColor(String selectedColor) {
        switch (selectedColor) {
            case "Pink":
                chatArea.setBackground(Color.PINK);
                break;
            case "Sky Blue":
                chatArea.setBackground(Color.CYAN);
            case "White":
                chatArea.setBackground(Color.WHITE);
                break;
            case "Yellow":
                chatArea.setBackground(Color.YELLOW);
                break;
            default:
                break;
        }
    }

    private class ColorComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
            String selectedColor = (String) comboBox.getSelectedItem();
            changeChatBackgroundColor(selectedColor);
        }
    }

    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = messageField.getText();
            String nk = nickname;
            writer.println(nk + " : " + message);
            System.out.println(nk + " : " + message);
            messageField.setText("");
        }
    }

    private class ServerListener implements Runnable {
        private Socket socket;
        private MyChat myChatInstance;

        public ServerListener(Socket socket, MyChat myChatInstance) {
            this.socket = socket;
            this.myChatInstance = myChatInstance;
        }

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(socket.getInputStream());
                while (scanner.hasNextLine()) {
                    String message = scanner.nextLine();
                    // 수신한 메시지로 GUI를 업데이트
                    updateChatArea(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 수신한 메시지로 GUI를 업데이트하는 메서드
        private void updateChatArea(String message) {
            SwingUtilities.invokeLater(() -> {
                // 채팅창에 메시지 추가
                myChatInstance.chatArea.append(message + "\n");

                // 채팅창이 항상 가장 아래쪽으로 스크롤되도록 설정
                JScrollBar verticalScrollBar = myChatInstance.scrollPane.getVerticalScrollBar();
                verticalScrollBar.setValue(verticalScrollBar.getMaximum());
            });
        }
    }

    private static class ClientListener implements Runnable {
        private Socket socket;
        private PrintWriter writer;

        public ClientListener(Socket socket, PrintWriter writer) {
            this.socket = socket;
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(socket.getInputStream());
                while (scanner.hasNextLine()) {
                    String message = scanner.nextLine();
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void broadcast(String message) {
            for (PrintWriter client : clients) {
                client.println(message);
            }
        }
    }
}
