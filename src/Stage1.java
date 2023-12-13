import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

public class Stage1 {
    private ArrayList<Platform> platforms;
    private Player player1, player2;

    public Stage1(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        platforms = new ArrayList<>();


        // 플랫폼 추가
        platforms.add(new Platform(200, 500, 100, 20));
        platforms.add(new Platform(350, 400, 100, 20));
        platforms.add(new Platform(500, 300, 100, 20));
        platforms.add(new Platform(650, 200, 100, 20));
    }


    public void draw(Graphics g) {
        // 여기에 플랫폼과 플레이어를 그리는 코드를 추가
        for (Platform platform : platforms) {
            platform.draw(g);
        }
       // player.draw(g);
    }

    // 필요한 경우 추가 메소드 구현
}
