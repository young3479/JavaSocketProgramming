import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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

//        ScorePanel scorePanel = new ScorePanel(); // ScorePanel 인스턴스 생성
        GamePanel gamePanel = new GamePanel(); // GamePanel 인스턴스 생성

        // 서버 연결 설정 (IP 주소와 포트 번호에 따라)
        gamePanel.connectToServer("127.0.0.1", 30000);

        frame.add(gamePanel);
        frame.setSize(800, 600); // 적절한 크기로 설정
        frame.setVisible(true);
    }
}
