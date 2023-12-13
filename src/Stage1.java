import java.awt.*;
import java.util.ArrayList;

public class Stage1 {
    private ArrayList<Platform> platforms;
    private Player player1, player2;

    public Stage1(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        platforms = new ArrayList<>();


        // 플랫폼 추가
        platforms.add(new Platform(0, 0, 730, 20, Color.BLACK));
        platforms.add(new Platform(100, 500, 100, 20, Color.RED));
        platforms.add(new Platform(250, 400, 100, 20, Color.YELLOW));
        platforms.add(new Platform(400, 300, 100, 20, Color.GREEN));
        platforms.add(new Platform(550, 200, 100, 20, Color.BLUE));
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
