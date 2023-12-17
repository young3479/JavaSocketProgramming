import javax.swing.*;
import java.awt.*;

public class GameEndingPanel extends JPanel {
    public GameEndingPanel(int winnerPlayerNum){
        setLayout(new BorderLayout());
        Color WinnerColor = new Color(154, 184, 151);
        Color OverColor = new Color(173, 160, 142);
        setBackground(WinnerColor); // 배경색 설정
        Font font = new Font("Arial", Font.BOLD, 33); // 폰트 설정

        JLabel winnerLabel = new JLabel("Player " + winnerPlayerNum + " Wins!", SwingConstants.CENTER);
        winnerLabel.setFont(font);
        winnerLabel.setForeground(Color.white); // 글자색 설정
        add(winnerLabel, BorderLayout.CENTER);

       // 추가적인 버튼 및 기능 구현
        JButton endButton = new JButton("GameOver");
        endButton.setFont(font);
        endButton.setForeground(Color.white);
        endButton.setBackground(OverColor);
        endButton.setOpaque(true);
        endButton.setBorderPainted(false);
        add(endButton, BorderLayout.SOUTH);
    }
}
