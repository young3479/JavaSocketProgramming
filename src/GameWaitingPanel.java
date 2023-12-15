//Java Client 시작

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class GameWaitingPanel extends JFrame {

    private JPanel contentPane;
    private JTextField txtUserName;
    private JTextField txtIpAddress;
    private JTextField txtPortNumber;
    private ObjectInputStream in; // 서버로부터 메시지를 받기 위한 스트림

    private ObjectOutputStream out;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameWaitingPanel frame = new GameWaitingPanel();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public GameWaitingPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 254, 321);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("User Name");
        lblNewLabel.setBounds(12, 39, 82, 33);
        contentPane.add(lblNewLabel);

        txtUserName = new JTextField();
        txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
        txtUserName.setBounds(101, 39, 116, 33);
        contentPane.add(txtUserName);
        txtUserName.setColumns(10);

        JLabel lblIpAddress = new JLabel("IP Address");
        lblIpAddress.setBounds(12, 100, 82, 33);
        contentPane.add(lblIpAddress);

        txtIpAddress = new JTextField();
        txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
        txtIpAddress.setText("127.0.0.1");
        txtIpAddress.setColumns(10);
        txtIpAddress.setBounds(101, 100, 116, 33);
        contentPane.add(txtIpAddress);

        JLabel lblPortNumber = new JLabel("Port Number");
        lblPortNumber.setBounds(12, 163, 82, 33);
        contentPane.add(lblPortNumber);

        txtPortNumber = new JTextField();
        txtPortNumber.setText("30000");
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setColumns(10);
        txtPortNumber.setBounds(101, 163, 116, 33);
        contentPane.add(txtPortNumber);

        JButton btnConnect = new JButton("Connect");
        btnConnect.setBounds(12, 223, 205, 38);
        contentPane.add(btnConnect);
        Myaction action = new Myaction();
        btnConnect.addActionListener(action);
        txtUserName.addActionListener(action);
        txtIpAddress.addActionListener(action);
        txtPortNumber.addActionListener(action);
    }
    class Myaction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = txtUserName.getText().trim();
            String ip_addr = txtIpAddress.getText().trim();
            String port_no = txtPortNumber.getText().trim();

            try {
                Socket socket = new Socket(ip_addr, Integer.parseInt(port_no));
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                // 서버로 사용자 이름 전송 (입장 메시지 포함)
                String welcomeMessage = username + "님이 입장하셨습니다.";
                out.writeObject(new ChatMsg(username, "101", welcomeMessage));
                out.flush();

                new Thread(() -> {
                    while (true) {
                        try {
                            Object receivedObj = in.readObject();
                            if (receivedObj instanceof ChatMsg) {
                                ChatMsg chatMsg = (ChatMsg) receivedObj;
                                if (chatMsg.getCode().equals("GAME_START")) {
                                    // 서버로부터 게임 시작 메시지를 받으면
                                    EventQueue.invokeLater(() -> startGame());
                                }
                            }
                        } catch (ClassNotFoundException | IOException ex) {
                            ex.printStackTrace();
                            break;
                        }
                    }
                }).start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void startGame() {
            // GamePanel 시작
            Player player1 = new Player(1, 100, 100, Color.RED);
            Player player2 = new Player(2, 200, 100, Color.BLUE);

            GamePanel gamePanel = new GamePanel(player1, player2);
            setContentPane(gamePanel);
            pack();
            gamePanel.requestFocusInWindow();
        }
    }
}