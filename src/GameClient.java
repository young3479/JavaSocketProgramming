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


        GamePanel gamePanel = new GamePanel(player1, player2); // 수정된 생성자
        // 플레이어를 게임 패널에 추가
//        gamePanel.add(player1);
//        gamePanel.add(player2);


        // 서버 연결 설정 (IP 주소와 포트 번호에 따라)
        gamePanel.connectToServer("127.0.0.1", 30000);

        frame.add(gamePanel);
        frame.setSize(730, 730); // 적절한 크기로 설정
        frame.setVisible(true);
    }
}
