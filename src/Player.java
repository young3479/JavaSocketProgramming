import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.awt.Rectangle;
import java.net.URL;
import java.util.Objects;

// Player 클래스
class Player implements Serializable {
    private ImageIcon imageIcon;
    private int playerNum;
    public static final int GRAVITY = 1;
    private Color color; // 플레이어 색상
    private int x, y; // 플레이어 위치
    private final int SIZE = 30; // 플레이어 크기
    private int velocityX, velocityY; // 플레이어 속도
    //private final int GRAVITY = 1; // 중력
    private boolean onGround; // 땅에 닿았는지 여부
    private static final int JUMP_VELOCITY = -20; // 점프 초기 속도

    public Player(int playerNum, int x, int y, String imagePath) {
        this.playerNum = playerNum;
        this.x = x;
        this.y = y;
        // 이미지 경로에서 ImageIcon 생성
        URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image originalImage = originalIcon.getImage();

            // 이미지 크기 설정
            int newWidth = 37; // 이미지의 가로 크기
            int newHeight = (originalIcon.getIconHeight() * newWidth) / originalIcon.getIconWidth(); // 세로 크기를 비율에 맞게 계산

            // 크기가 조정된 이미지로 ImageIcon 생성
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            this.imageIcon = new ImageIcon(scaledImage);
        } else {
            throw new RuntimeException("Resource not found: " + imagePath);
        }
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

    // Player 클래스 내부의 update 메서드는 플레이어의 움직임을 계산합니다.
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
        Image image = imageIcon.getImage(); // ImageIcon으로부터 Image 객체 획득
        g.drawImage(image, x, y, null); // 이미지 그리기
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

    public int getVelocityY() {
        return velocityY;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    // 플레이어 번호를 반환하는 메서드
    public int getPlayerNum() { return playerNum; }

    // 네트워크 업데이트 메서드
    public void networkUpdate(NetworkHandler networkHandler) {
        // 네트워크를 통해 플레이어 상태 전송
        networkHandler.sendPlayerState(this);
    }


}
