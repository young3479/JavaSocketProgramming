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


    public Stage1(Player player1, Player player2, GamePanel gamePanel) {
        this.player1 = player1;
        this.player2 = player2;
        this.gamePanel = gamePanel;
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
        platforms.add(new Platform(100, 500, 520, 20, "Image/block/block2.png"));
        platforms.add(new Platform(250, 400, 100, 20, "Image/block/block2.png"));
        platforms.add(new Platform(400, 300, 100, 20, "Image/block/block2.png"));
        platforms.add(new Platform(590, 200, 100, 20, "Image/block/block2.png"));
        platforms.add(new Platform(40, 200, 100, 20, "Image/block/block2.png"));



        // 도착 지점 생성, 예를 들어 x 좌표가 590인 지점에 도착 지점 설정
        finishLine = new Platform(400, 444, 70, 60,"Image/Item/door3.png");
        platforms.add(finishLine);

        platforms.add(new Platform(590, 115, 100, 100, "Image/Item/door.png"));


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


    public void checkCollisions() {
        checkCollisionForPlayer(player1);
        checkCollisionForPlayer(player2);
    }


    private void checkCollisionForPlayer(Player player) {
        for (Platform platform : platforms) {
            if (player.getBounds().intersects(platform.getBounds())) {
                // 반발력 설정
                int reboundForceX = 10; // 수평 반발력의 크기
                int reboundForceY = 10; // 수직 반발력의 크기

                // 플레이어의 현재 속도와 위치를 고려하여 충돌 처리
                int velocityX = player.getVelocityX();
                int velocityY = player.getVelocityY();

                // 수평 충돌 처리
                if (Math.abs(velocityX) > 0) {
                    player.setVelocityX((int) (-reboundForceX * Math.signum(velocityX)));
                    player.setX(player.getPreviousX());
                }

                // 수직 충돌 처리
                if (Math.abs(velocityY) > 0) {
                    player.setVelocityY((int) (-reboundForceY * Math.signum(velocityY)));
                    player.setY(player.getPreviousY());
                }


                break; // 하나의 플랫폼과 충돌이 감지되면 더 이상 검사할 필요 없음
            }
        }
    }
}






    // 필요한 경우 추가 메소드 구현
