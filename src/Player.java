import java.awt.*;
import java.io.*;
import java.awt.Rectangle;



// 플레이어 클래스 수정
class Player implements Serializable  {
    private Color color; // 색상 필드 추가
    // 기존 코드 유지...
    private int x, y;
    private final int SIZE = 30;
    private int velocityX, velocityY;
    private final int GRAVITY = 1;
    private boolean onGround;
    private static final int JUMP_VELOCITY = -20; // 점프할 때의 초기 속도

    public Player(int playerNumber, int startX, int startY, Color color) {
        this.color = color;
        //super();
        x = startX;
        y = startY;
        onGround = false;
    }
    public void jump() {
        velocityY = JUMP_VELOCITY;
        onGround = false;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void update() {
        x += velocityX;
        if (!onGround) {
            velocityY += GRAVITY;
        } else {
            velocityY = 0;
        }
        y += velocityY;
    }

    public void draw(Graphics g) {
        //g.setColor(Color.RED);
        g.setColor(this.color); // 설정된 색상 사용
        g.fillOval(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return SIZE;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    // 네트워크 업데이트 메서드 추가
    public void networkUpdate(NetworkHandler networkHandler) {
        // 네트워크를 통해 플레이어 상태 전송
        try {
            networkHandler.sendPlayerState(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//// 메인 게임 클래스
//public class Game {
//    // 게임 초기화 및 루프 관련 코드...
//
//    public void gameLoop() {
//        // 게임 루프 코드...
//        // 네트워크 상태 업데이트를 포함합니다.
//    }
//
//    // 게임 시작 메서드 등 기타 메서드...
//}

