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

        GamePanel gamePanel = new GamePanel(); // GamePanel 인스턴스 생성

        // 플레이어 생성
        Player player1 = new Player(1, 40, 480); // 예시 좌표
        Player player2 = new Player(2, 760, 480); // 예시 좌표

        // 플레이어를 게임 패널에 추가
        gamePanel.add(player1);
        gamePanel.add(player2);


        // 서버 연결 설정 (IP 주소와 포트 번호에 따라)
        gamePanel.connectToServer("127.0.0.1", 30000);

        frame.add(gamePanel);
        frame.setSize(800, 600); // 적절한 크기로 설정
        frame.setVisible(true);
    }
}
