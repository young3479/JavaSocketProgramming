//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//
//
//public class Playertesttest extends JLabel{
//
//
//    private int x, y;
//    private final int SIZE = 30;
//    private int velocityX, velocityY;
//    private final int GRAVITY = 1;
//    private boolean onGround;
//    private static final int JUMP_VELOCITY = -20; // 점프할 때의 초기 속도
//
//    private int circleX = 100; // 동그라미의 x 좌표;
//    private int circleY = 100; // 동그라미의 y 좌표
//    private int circleRadius = 20; // 동그라미의 반지름
//
//    // 플레이어 이미지를 저장할 변수
//    private Image playerImage;
//    public int playerNumber;
//
//    private double xStartLocation=40;
//    private double yStartLocation= 480;
//
//    private double speed;
//    private int score;
//    private float alpha = 255;
//
//    Thread moveThread;
//
//
////    int x = 400; // 패널의 중앙
////    int y = 550; // 패널의 하단에서 50 픽셀 위
//    int width = 50; // 플레이어 이미지의 너비
//    int height = 50; // 플레이어 이미지의 높이
//    private double playerMinX;
//    private double playerMaxX;
//    private double playerMinY;
//    private double playerMaxY;
//
//    private boolean isJumping;
//    private int counter;
//    private boolean isDelayed;
//    private boolean isAbleToJump;
//    private boolean isDead;
//
//    private boolean isMoveLeft;
//    private boolean isMoveRight;
//    private boolean isMoveUp;
//    private boolean isShoot;
//
//    private boolean isMoveDown;
//    private boolean isRealDead = false;
//
//    private boolean isDirection;
//    private boolean isWallCrush;
//    private boolean isMonsterCrush;
//    private boolean isImmortal = false;
//    private boolean threadFlag = true;
//
//    public static int player1Live = 3;
//    public static int player2Live = 3;
//
//    public void setThreadFlag(boolean threadFlag) {
//        this.threadFlag = threadFlag;
//    }
//    private boolean isBlink = false;
////    private ScorePanel.ScoreLabel scoreLabel;
//    private long beforeTime;// 코드 실행 전에 시간 받아오기
//
//    public Playertesttest(int playerNumber, double startX, double startY) {
//        super();
//        this.playerNumber = playerNumber;
////        this.scoreLabel = scoreLabel;
//
//
//        // 플레이어 이미지 로드
//        loadImage(playerNumber);
//        //getImagePaths();
//
//        System.out.println("player: "+playerNumber + " 생성");
//
//        moveThread = new moveThread();
//
//        moveThread.start();
//        setBounds((int) startX, (int) startY, width, height);
//        setUp();
//
//
//    }
//    //변수들 초기 셋팅
//    private void setUp() {
//
//        this.isDead = false;
//
//        this.counter = 31;
//        this.isAbleToJump = true;
//        this.isJumping = false;
//        this.isDirection = (playerNumber==1);
//        this.isWallCrush = false;
//        this.isShoot = false;
//        this.isMonsterCrush = false;
//        //this.isImmortal =false;
//
//        //playerMinX = Settings.SPRITE_SIZE / 2;
//        playerMinX = 27;
//
//
//
//    }
//
//    public int getPlayerNumber() {
//        return playerNumber;
//    }
//
//    // 플레이어 이미지를 로드하는 메서드
//    private void loadImage(int playerNumber) {
//        String path = "src/player" + playerNumber + ".png"; // 예시 파일 경로
//        File imageFile = new File(path);
//        if (imageFile.exists()) {
//            ImageIcon icon = new ImageIcon(path);
//            playerImage = icon.getImage();
//        } else {
//            System.err.println("플레이어 이미지 파일을 찾을 수 없습니다: " + path);
//        }
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g;
//
//        // 불투명도 설정
//        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) this.alpha / 255);
//        g2.setComposite(alphaComposite);
//
////        // 플레이어 이미지 그리기
////        if (playerImage != null) {
////            g2.drawImage(playerImage, 0, 0, this.getWidth(), this.getHeight(), this);
////        }
//        // 동그라미 그리기
//        g.setColor(Color.RED); // 동그라미의 색상 설정
//        g.fillOval(circleX - circleRadius, circleY - circleRadius, 2 * circleRadius, 2 * circleRadius);
//
//    }
//
//
//    //상태변수, 키보드 입력에 따른 좌표 변화 실행
//    class moveThread extends Thread {
//
//        @Override
//        public void run() {
//            while (true) {
//                //immortalEvent();
//                threadInterrupt();
//
//                //repaint();
//                //wallCrush();
////                monsterCrushEvent();
//
//                try {
//                    Thread.sleep(20);
//
//                } catch (InterruptedException e) {
//
//                    return;
//                }
//            }
//        }
//    }
//
//    public void threadInterrupt() {
//        if(!this.threadFlag)
//            this.moveThread.interrupt();
//    }
//
//
//
//    /*캐릭터 부활 */
//    public void revival() {
//        System.out.println(this.playerNumber + ": 부활");
//        this.setDead(false);
//
//        //무적 시간 초기화
//        beforeTime = System.currentTimeMillis();
//        this.isBlink = true;
//        setUp();
//
//    }
//
//    /*캐릭터가 다시 부활할 때 깜빡이는 이벤트 */
//    public void blinkEvent() {
//        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//        long secDiffTime = (afterTime - beforeTime); // 두 시간에 차 계산
//        if((secDiffTime)%150<= 30) {
//            if(this.alpha == 255)
//                this.alpha = 100;
//            else
//                this.alpha = 255;
//        }
//        if(secDiffTime >= 2000) {
//            this.isBlink = false;
//            this.isImmortal = false;
//            this.alpha = 255;
//        }
//    }
//    //
//    public void deadEvent() {
//        this.setDead(false);
//        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
//        long secDiffTime = (afterTime - beforeTime); // 두 시간에 차 계산
//
//        for(int i=0; i< 55; i++) {
//            if(i< 55/2) {
//
//                //	this.coordinate.setRotation(this.coordinate.getRotation() + 15);;
//            }
//
//            else {
//
//                //this.coordinate.setRotation(this.coordinate.getRotation() + 30);
//            }
//
//        }
//    }
//
//
//    public boolean isDead() {
//        return isDead;
//    }
//    public void setDead(boolean isDead) {
//        this.isDead = isDead;
//    }
//
//
//    //벽 위에 있는지 체크
//    public boolean wallCollision(double minX, double maxX, double minY, double  maxY) {
//        double minX2 = this.getX();
//        double maxX2 = minX2 + getWidth();
//        double minY2 = this.getY()+getHeight() +3;
//        double maxY2 = minY2;
//        return ((minX > minX2 && minX < maxX2)
//                || (maxX > minX2 && maxX < maxX2)
//                || (minX2 > minX && minX2 < maxX)
//                || (maxX2 > minX && maxX2 < maxX))
//                && ((minY > minY2 && minY < maxY2)
//                || (maxY > minY2 && maxY < maxY2)
//                || (minY2 > minY && minY2 < maxY)
//                || (maxY2 > minY && maxY2 < maxY));
//        // return spriteBase.causesCollision(minX, maxX, minY, maxY);
//    }
//
//
//
//    /**
//     * This function applies gravity.
//     */
//
//
//
//    public boolean isAbleToJump() {
//        return isAbleToJump;
//    }
//    public boolean isMoveLeft() {
//        return isMoveLeft;
//    }
//    public void setMoveLeft(boolean isMoveLeft) {
//        this.isMoveLeft = isMoveLeft;
//    }
//    public boolean isMoveRight() {
//        return isMoveRight;
//    }
//    public void setMoveRight(boolean isMoveright) {
//        this.isMoveRight = isMoveright;
//    }
//    public boolean isMoveUp() {
//        return isMoveUp;
//    }
//    public void setMoveUp(boolean isMoveUp) {
//        this.isMoveUp = isMoveUp;
//    }
//    public boolean isMoveDown() {
//        return isMoveDown;
//    }
//    public void setMoveDown(boolean isMoveDown) {
//        this.isMoveDown = isMoveDown;
//    }
//    public void setJumping(boolean jumping) {
//        isJumping = jumping;
//    }
//    public boolean getAbleToJump() {
//        return isAbleToJump;
//    }
//    public void setAbleToJump(boolean ableToJump) {
//        isAbleToJump = ableToJump;
//    }
//    public boolean isDirection() {
//        return isDirection;
//    }
//    public void setDirection(boolean isDirection) {
//        this.isDirection = isDirection;
//
//    } public boolean isWallCrush() {
//        return isWallCrush;
//    }
//    public void setWallCrush(boolean isWallCrush) {
//        this.isWallCrush = isWallCrush;
//    }
//    public boolean isJumping() {
//        return isJumping;
//    }
//
//
//    public boolean isShoot() {
//        return isShoot;
//    }
//    public void setShoot(boolean isShoot) {
//        this.isShoot = isShoot;
//    }
//
//    public boolean isMonsterCrush() {
//
//        return isMonsterCrush;
//    }
//    public void setMonsterCrush(boolean isMonsterCrush) {
//
//        this.isMonsterCrush = isMonsterCrush;
//    }
//
//    public boolean isImmortal() {
//        return isImmortal;
//    }
//    public void setImmortal(boolean isImmortal) {
//
//        this.isImmortal = isImmortal;
//    }
//
//}
//
