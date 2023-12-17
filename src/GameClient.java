import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameClient extends KeyAdapter {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // GameWaitingPanel 시작
                GameWaitingPanel waitingPanel = new GameWaitingPanel();
                waitingPanel.setVisible(true);
            }
        });
    }
}
