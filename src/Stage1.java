import java.awt.*;
import java.util.ArrayList;

public class Stage1 {
    private ArrayList<Platform> platforms;
    private Player player1, player2;
    // 패널의 크기
    int panelWidth = 730;
    int panelHeight = 730;
    int borderThickness = 20;

    public Stage1(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        platforms = new ArrayList<>();


        // 블럭추가

        // 상단
        platforms.add(new Platform(0, 0, panelWidth, borderThickness, Color.ORANGE));
        // 하단
        platforms.add(new Platform(0, panelHeight - borderThickness, panelWidth, borderThickness, Color.ORANGE));
        // 왼쪽
        platforms.add(new Platform(0, 0, borderThickness, panelHeight, Color.ORANGE));
        // 오른쪽
        platforms.add(new Platform(panelWidth - borderThickness, 0, borderThickness, panelHeight, Color.ORANGE));
        // 계단
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
