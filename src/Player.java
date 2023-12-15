import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.awt.Rectangle;

// Player 클래스
class Player implements Serializable {
    private Color color; // 플레이어 색상
    private int x, y; // 플레이어 위치
    private final int SIZE = 30; // 플레이어 크기
    private int velocityX, velocityY; // 플레이어 속도
    private final int GRAVITY = 1; // 중력
    private boolean onGround; // 땅에 닿았는지 여부
    private static final int JUMP_VELOCITY = -20; // 점프 초기 속도

    public Player(int playerNumber, int startX, int startY, Color color) {
        this.color = color;
        this.x = startX;
        this.y = startY;
        this.onGround = false;
    }

    public void jump() {
        if (onGround) {
            velocityY = JUMP_VELOCITY;
            onGround = false;
        }
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void update() {
        x += velocityX;
        velocityY += GRAVITY; // 항상 중력 적용
        y += velocityY;

        // 땅에 닿았는지 확인 (충돌 처리 필요)
        // 예를 들어, 바닥이 y=300이라고 가정했을 때의 간단한 예시
        // 실제로는 블록과의 충돌 처리가 필요합니다.
        if (y >= 300) {
            y = 300;
            onGround = true;
            velocityY = 0;
        }
    }

    public void draw(Graphics g) {
        g.setColor(this.color);
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return SIZE;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    // 네트워크 업데이트 메서드
    public void networkUpdate(NetworkHandler networkHandler) {
        // 네트워크를 통해 플레이어 상태 전송
        try {
            networkHandler.sendPlayerState(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
