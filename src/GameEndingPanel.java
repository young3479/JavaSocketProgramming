import javax.swing.*;
import java.awt.*;

public class GameEndingPanel extends JPanel {
    public GameEndingPanel(int winnerPlayerNum){
        setLayout(new BorderLayout());
        add(new JLabel("Player " + winnerPlayerNum + " Wins!"), BorderLayout.CENTER);
        // 추가적인 버튼 및 기능 구현
    }
}
