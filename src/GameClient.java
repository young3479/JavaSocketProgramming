import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;

public class GameClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // 플레이어 생성
        Player player1 = new Player(1, 200, 200, Color.red);
        Player player2 = new Player(2, 205, 300, Color.yellow);


        GamePanel gamePanel = new GamePanel(player1, player2);
        frame.getContentPane().add(gamePanel);
        //타이틀 바 제외하고 730 X 730을 생성
        gamePanel.setPreferredSize(new Dimension(730, 730));

        // 서버 연결 설정 (IP 주소와 포트 번호에 따라)
        gamePanel.connectToServer("127.0.0.1", 30000);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
