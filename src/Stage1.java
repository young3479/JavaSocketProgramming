import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//오픈소스 사용안한 클래스

public class Stage1 {
    private ArrayList<Platform> platforms;
    private Player player1, player2;
    // 패널의 크기
    int panelWidth = 730;
    int panelHeight = 730;
    int borderThickness = 40;
    private Platform finishLine;

    private List<String> newFinishLineImages = Arrays.asList("Image/Item/door3.png", "Image/Item/door4.png", "Image/Item/door5.png", "Image/Item/door6.png");
    private int imageIndex = 0;
    private GamePanel gamePanel; // GamePanel 참조를 위한 필드 추가
    private int[][] map = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,0,1,1,0,1,0,1,0,1,1,1,1,1},
            {1,0,0,0,0,0,1,0,0,1,0,1,0,0,0,0,0,0,0,1},
            {1,0,1,0,1,1,1,1,1,1,0,1,1,1,0,1,1,1,0,1},
            {1,0,1,0,0,0,1,0,0,0,0,0,0,1,0,0,0,1,0,1},
            {1,0,1,1,1,0,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,1},
            {1,0,1,1,0,0,1,0,1,0,0,1,0,1,1,1,1,1,0,1},
            {1,0,1,0,0,0,1,0,1,1,0,0,0,0,0,0,0,1,0,1},
            {1,0,1,1,1,0,1,1,1,1,0,1,1,1,1,1,0,1,0,1},
            {1,0,1,0,1,0,0,0,1,0,0,1,0,0,0,1,0,1,0,1},
            {1,0,1,0,1,1,1,1,1,0,0,1,0,1,0,1,0,1,0,1},
            {1,0,0,0,0,0,0,0,1,0,1,1,0,1,0,1,0,0,0,1},
            {1,1,0,1,1,1,1,0,1,0,0,1,0,1,1,1,1,1,1,1},
            {1,1,0,1,0,0,1,0,1,1,0,1,0,0,0,0,0,0,0,1},
            {1,0,0,1,0,0,0,0,1,0,0,1,0,1,1,1,0,1,1,1},
            {1,1,1,1,0,1,1,0,1,0,1,1,0,1,0,1,0,0,0,1},
            {1,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0,1,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}

    };

    private int mapRows = 20; // 맵 배열의 행 수
    private int mapColumns = 20; // 맵 배열의 열 수

    private int platformWidth = panelWidth / mapColumns; // 플랫폼 너비
    private int platformHeight = panelHeight / mapRows; // 플랫폼 높이


    public Stage1(Player player1, Player player2, GamePanel gamePanel) {
        this.player1 = player1;
        this.player2 = player2;
        this.gamePanel = gamePanel;
        platforms = new ArrayList<>();

        initializePlatforms();


        // 블럭추가

//        // 상단
//        platforms.add(new Platform(0, 0, panelWidth, borderThickness,"Image/block/block2.png"));
//        // 하단
//        platforms.add(new Platform(0, panelHeight - borderThickness, panelWidth, borderThickness, "Image/block/block2.png"));
//        // 왼쪽
//        platforms.add(new Platform(0, 0, borderThickness, panelHeight, "Image/block/block3.png"));
//        // 오른쪽
//        platforms.add(new Platform(panelWidth - borderThickness, 0, borderThickness, panelHeight, "Image/block/block3.png"));

        // 계단
//        platforms.add(new Platform(100, 500, 520, 20, "Image/block/block2.png"));
//        platforms.add(new Platform(250, 400, 100, 20, "Image/block/block2.png"));
//        platforms.add(new Platform(400, 300, 100, 20, "Image/block/block2.png"));
//        platforms.add(new Platform(590, 200, 100, 20, "Image/block/block2.png"));
//        platforms.add(new Platform(40, 200, 100, 20, "Image/block/block2.png"));



        // 도착 지점 생성
        finishLine = new Platform(145, 290, 70, 60,"Image/Item/door3.png");
        platforms.add(finishLine);

        //platforms.add(new Platform(590, 115, 100, 100, "Image/Item/door.png"));


    }

    public boolean isMoveValid(int newX, int newY) {
        // 보정값 추가
        int offsetX = 23; // 플레이어 위치의 오른쪽 보정값
        int offsetY = 23; // 플레이어 위치의 아래쪽 보정값

        // 보정된 위치 계산
        int correctedX = newX + offsetX;
        int correctedY = newY + offsetY;

        // 보정된 위치를 맵의 그리드에 매핑
        int gridX = correctedX / platformWidth;
        int gridY = correctedY / platformHeight;

        // 맵의 경계를 벗어나는 경우 체크
        if (gridX < 0 || gridX >= mapColumns || gridY < 0 || gridY >= mapRows) {
            return false;
        }

        // 해당 위치가 벽(1)인지 확인
        return map[gridY][gridX] != 1;
    }

    // 도착 지점에 도달했는지 확인하는 메서드
    public Player checkWinner() {
        if (player1.getBounds().intersects(finishLine.getBounds()) || player2.getBounds().intersects(finishLine.getBounds())) {
            Timer timer = new Timer(500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (imageIndex < newFinishLineImages.size()) {
                        finishLine.setImagePath(newFinishLineImages.get(imageIndex++));
                        gamePanel.repaint(); // 화면 갱신을 위한 repaint 호출
                    } else {
                        ((Timer)e.getSource()).stop(); // 타이머 중지
                    }
                }
            });
            timer.start();
            return player1.getBounds().intersects(finishLine.getBounds()) ? player1 : player2;
        }
        return null;
    }
    public void draw(Graphics g) {
        // 여기에 플랫폼과 플레이어를 그리는 코드를 추가
        for (Platform platform : platforms) {
            platform.draw(g);
        }
       // player.draw(g);
    }

    private void initializePlatforms() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 1) {
                    // 플랫폼 생성 로직
                    platforms.add(new Platform(j * platformWidth, i * platformHeight, platformWidth, platformHeight, "Image/block/block3.png"));
                }
                // 다른 숫자에 대한 처리
            }
        }
    }
}






    // 필요한 경우 추가 메소드 구현
