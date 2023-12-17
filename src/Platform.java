import javax.swing.*;
import java.awt.*;
import java.util.Objects;

//오픈소스 안쓴 클래스
class Platform {
    private int x, y, width, height;
    private Image image;
    private String imagePath;

    public Platform(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imagePath = imagePath; // imagePath를 초기화
        this.image = loadImage(imagePath);
    }

        public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() { return x; }
    public int getY() {
        return y;
    }

    public int getHeight() { return height;}
    public int getWidth() { return width; }
    private Image loadImage(String imagePath) {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
    }

    // imagePath를 설정하고 이미지를 다시 로드하는 메서드
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        this.image = loadImage(imagePath);
    }
}
