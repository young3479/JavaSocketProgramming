import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClient extends KeyAdapter {
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private Socket serverSocket;
    private ObjectOutputStream out;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // GameWaitingPanel 시작
                GameWaitingPanel waitingPanel = new GameWaitingPanel();
                waitingPanel.setVisible(true);
            }
        });
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
        }
        String actionCode = "401"; // 예를 들어 "401"은 키 눌림을 의미
        sendKeyAction(actionCode, KeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
        }
        String actionCode = "402"; // 예를 들어 "402"는 키 뗌을 의미
        sendKeyAction(actionCode, KeyEvent.getKeyText(e.getKeyCode()));
    }

    // 서버에 연결하는 메서드
    public void connectToServer(String address, int port) {
        try {
            serverSocket = new Socket(address, port);
            out = new ObjectOutputStream(serverSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            // 에러 처리
        }
    }
    // 서버로 키 이벤트를 전송하는 메서드
    public void sendKeyAction(String actionCode, String keyEvent) {
        try {
            // 서버로 메시지 객체를 전송
            out.writeObject(new ChatMsg("username", actionCode, keyEvent));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            // 에러 처리
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 플레이어 생성
        Player player1 = new Player(1, 200, 200, Color.red);
        Player player2 = new Player(2, 205, 300, Color.yellow);

        GamePanel gamePanel = new GamePanel(player1, player2);

        // 서버 연결 설정 (IP 주소와 포트 번호에 따라)
        gamePanel.connectToServer("127.0.0.1", 30000);
        // 게임 루프 시작
        new Thread(() -> gamePanel.gameLoop()).start();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
