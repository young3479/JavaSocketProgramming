import javax.swing.*;
import java.awt.*;

public class GameEndingPanel extends JPanel {
    public GameEndingPanel(int winnerPlayerNum){
        setLayout(new BorderLayout());
        Color EmberColor = new Color(255, 200, 200); // 빨간색
        Color WadeColor = new Color(195, 216, 255); // 파란색
        Color WinnerColor = new Color(154, 184, 151); // 기본 배경색
        String winnerText;

        if (winnerPlayerNum == 1) {
            WinnerColor = EmberColor; // Ember가 이겼을 때 배경색을 빨간색으로 설정
            winnerText = "Ember Wins!";
        } else if (winnerPlayerNum == 2) {
            WinnerColor = WadeColor; // Wade가 이겼을 때 배경색을 파란색으로 설정
            winnerText = "Wade Wins!";
        } else {
            winnerText = "Player " + winnerPlayerNum + " Wins!";
        }

        setBackground(WinnerColor); // 배경색 설정
        Font font = new Font("Arial", Font.BOLD, 33); // 폰트 설정

        JLabel winnerLabel = new JLabel(winnerText, SwingConstants.CENTER);
        winnerLabel.setFont(font);
        winnerLabel.setForeground(Color.white); // 글자색 설정
        add(winnerLabel, BorderLayout.CENTER);

        // 추가적인 버튼 및 기능 구현
        JButton endButton = new JButton("GameOver");
        endButton.setFont(font);
        endButton.setForeground(Color.white);
        endButton.setBackground(new Color(173, 173, 173));
        endButton.setOpaque(true);
        endButton.setBorderPainted(false);
        add(endButton, BorderLayout.SOUTH);
    }
}
