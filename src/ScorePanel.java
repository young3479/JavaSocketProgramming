//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.GridLayout;
//import java.util.ArrayList;
//
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//
//
//
//public class ScorePanel extends JPanel{
//
//    private ScoreLabel up1Score;
//    private ScoreLabel highScore;
//    private ScoreLabel up2Score;
//    private JLabel up1;
//    private JLabel up2;
//
//    public ScorePanel() {
//        this.setBackground(Color.black);
//        setLayout(new GridLayout(0,3));
//        settingPanel();
//    }
//
//    private void settingPanel() {
//        up1 = new ToolBarLabel("1UP");
//        up1.setForeground(Color.GREEN);
//        JLabel highScoreText = new ToolBarLabel("HIGH SCORE");
//        highScoreText.setForeground(Color.RED);
//        up2 = new ToolBarLabel("2UP");
//        up2.setForeground(Color.BLUE);
//
//        add(up1);
//        add(highScoreText);
//        add(up2);
//
//        up1Score = new ScoreLabel();
//        //up1Score.setHorizontalAlignment(JLabel.RIGHT);
//        highScore = new ScoreLabel(3000);
//        up2Score = new ScoreLabel();
//        //up2Score.setHorizontalAlignment(JLabel.RIGHT);
//
//        add(up1Score);
//        add(highScore);
//        add(up2Score);
//    }
//
//    public ScoreLabel getScorePanel(int i) {
//        if(i==1)
//            return up1Score;
//        return up2Score;
//    }
//
//    public void setUp1Name(String name) {
//        this.up1.setText(name);
//    }
//
//    public void setUp2Name(String name) {
//        this.up2.setText(name);
//    }
//
//    public void HighScoreCheck(int score) {
//        if(highScore != null && highScore.getScore() <= score) {
//            highScore.setScore(score);
//            //System.out.println("highScore.getScore(): "+highScore.getScore());
//        }
//    }
//
//    public class ToolBarLabel extends JLabel {
//        public ToolBarLabel(String s) {
//            super(s);
//            setHorizontalAlignment(JLabel.CENTER);
//            Font font = new Font ("Press Start 2P", Font.PLAIN, 20);
//            setFont(font);
//            setForeground(Color.WHITE);
//        }
//
//    }
//
//    public class ScoreLabel extends ToolBarLabel {
//        private int score = 0;
//        public ScoreLabel() {
//            super("");
//            setText('0'+Integer.toString(score));
//        }
//        public ScoreLabel(int n) {
//            super("");
//            addScore(n);
//        }
//        public int getScore() {
//            return score;
//        }
//        public void setScore(int score) {
//            this.score = score;
//            setText(Integer.toString(this.score));
//        }
//        public void addScore(int score) {
//            this.score += score;
//            if(this.score<10)
//                setText('0'+Integer.toString(this.score));
//            else
//                setText(Integer.toString(this.score));
//
//            if(highScore != this)
//                HighScoreCheck(this.score);
//        }
//
//    }
//}
