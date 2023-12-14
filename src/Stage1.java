import java.awt.*;
import java.util.ArrayList;
//오픈소스 사용안한 클래스

public class Stage1 {
    private ArrayList<Platform> platforms;
    private Player player1, player2;
    // 패널의 크기
    int panelWidth = 730;
    int panelHeight = 730;
    int borderThickness = 40;

    public Stage1(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        platforms = new ArrayList<>();


        // 블럭추가

        // 상단
        platforms.add(new Platform(0, 0, panelWidth, borderThickness,"Image/block/block2.png"));
        // 하단
        platforms.add(new Platform(0, panelHeight - borderThickness, panelWidth, borderThickness, "Image/block/block2.png"));
        // 왼쪽
        platforms.add(new Platform(0, 0, borderThickness, panelHeight, "Image/block/block3.png"));
        // 오른쪽
        platforms.add(new Platform(panelWidth - borderThickness, 0, borderThickness, panelHeight, "Image/block/block3.png"));

        // 계단
        platforms.add(new Platform(100, 500, 550, 20, "Image/block/block2.png"));
        platforms.add(new Platform(250, 400, 100, 20, "Image/block/block2.png"));
        platforms.add(new Platform(400, 300, 100, 20, "Image/block/block2.png"));
        platforms.add(new Platform(550, 200, 100, 20, "Image/block/block2.png"));
    }


    public void draw(Graphics g) {
        // 여기에 플랫폼과 플레이어를 그리는 코드를 추가
        for (Platform platform : platforms) {
            platform.draw(g);
        }
       // player.draw(g);
    }

    public void checkCollisions() {
        for (Platform platform : platforms) {
            if (player1.getBounds().intersects(platform.getBounds())) {
                player1.setOnGround(true);
                player1.setY(platform.getY() - player1.getSize());
                return;
            }
        }
        player1.setOnGround(false);
    }

    // 필요한 경우 추가 메소드 구현
}
