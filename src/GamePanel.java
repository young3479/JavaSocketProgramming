import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.awt.Graphics;

import javax.swing.*;


public class GamePanel extends JLayeredPane {


    private ArrayList<Platform> platforms;
    private Player player1;
    private Player player2;


    private Player myself;
    private String userName;

    private int myPlayerNum;

    private Map map;

    private int score = 0;
    private GameThread gameThread;

    Image heartImg = new ImageIcon("src/image/heart.png").getImage();
    public boolean threadFlag = true;
    private ObjectOutputStream oos;

    private Stage1 stage1; //맵추가

    /**
     * Create the panel.
     */
    public GamePanel(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        // 배경 색 설정
        setOpaque(true);
        this.setBackground(Color.WHITE);


       this.stage1 = new Stage1(player1, player2);

        //add(player1, new Integer(10));
        //add(player2, new Integer(10));



        // 맵 그리기
        // map = new Map("src/resource/map1.txt");
        // blocks = map.getBlocks();



        this.addKeyListener(new KeyListener());
        this.requestFocus();
        this.setFocusable(true);
        //게임패널 크기
        setPreferredSize(new Dimension(730, 730));

        this.gameThread = new GameThread();
        gameThread.start();



//		this.sendThread = new SendThread();
//		sendThread.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        stage1.draw(g); // Stage1 그리기
        player1.draw(g);
        player2.draw(g);
    }

    // 게임 루프 - 플레이어 상태 업데이트 및 화면 다시 그리기
    public void gameLoop() {
        while(true) {
            updateGame();
            player1.update();
            player2.update();
            repaint(); // 화면 다시 그리기

            // 시간 지연을 위한 대기 (예: 16ms, 대략 60FPS)
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void updateGame() {
        // 플레이어1의 움직임 업데이트
        updatePlayer(player1);
        // 플레이어2의 움직임 업데이트 (네트워크 데이터를 기반으로 할 수 있음)
        updatePlayer(player2);

        stage1.checkCollisions();
        repaint();
    }

    private void updatePlayer(Player player) {
//        if (leftPressed) {
//            player.setVelocityX(-5);
//        } else if (rightPressed) {
//            player.setVelocityX(5);
//        } else {
//            player.setVelocityX(0);
//        }
//
//        if (upPressed && player.isOnGround()) {
//            player.jump();
//        }

        player.update();
    }





    public String getUserName() {
        return userName;
    }

    public void meetBubbleMonster(int bubbleNum, int monsterNum, int x, int y) {

    }


    class GameThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    repaint();
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }


    public void movePlayerPosition(String[] playerInfo) {
        String[] position = playerInfo[1].split(",");
        double x = Integer.parseInt(position[0]);
        double y = Integer.parseInt(position[1]);
        //System.out.println("GamePanel position ###### " + x + ":" + y);

        Player other;
        if (Integer.parseInt(playerInfo[0]) == 1)
            other = player1;
        else
            other = player2;


    }

//    public void movePlayerTrue(String[] playerInfo) {
//        String KeyCode = playerInfo[1];
//        System.out.println("GamePanel ###### " + playerInfo[1]);
//
//        Player other;
//        if (playerInfo[0].equals("1"))
//            other = player1;
//        else
//            other = player2;
//
//        switch (KeyCode) {
//            case "VK_DOWN":
//                other.setMoveDown(true);
//                break;
//            case "VK_UP":
//                if (other.getAbleToJump()) {
//                    other.setMoveUp(true);
//                    other.setJumping(true);
//                }
//                break;
//            case "VK_LEFT":
//                other.setMoveLeft(true);
//                break;
//            case "VK_RIGHT":
//                other.setMoveRight(true);
//                break;
//            case "VK_SPACE":
//                other.setShoot(true);
//                break;
//            case "VK_ESCAPE":
//                System.exit(0);
//                break;
//        }
//    }
//
//    public void movePlayerFalse(String[] playerInfo) {
//        String KeyCode = playerInfo[1];
//        //System.out.println("GamePanel ###### " + playerInfo[0] + ":" + playerInfo[1]);
//        Player other;
//        if (Integer.parseInt(playerInfo[0]) == 1)
//            other = player1;
//        else
//            other = player2;
//
//        switch (KeyCode) {
//            case "VK_DOWN":
//                other.setMoveDown(false);
//                break;
//            case "VK_UP":
//                other.setMoveUp(false);
//                break;
//            case "VK_LEFT":
//                other.setMoveLeft(false);
//                break;
//            case "VK_RIGHT":
//                other.setMoveRight(false);
//                break;
//            case "VK_SPACE":
//                other.setShoot(false);
//
//                break;
//        }
//    }
    // 서버와의 연결을 설정하는 메소드
    public void connectToServer(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            // 연결 오류 처리
        }
    }

    // 네트워크를 통해 메시지 전송하는 메소드
    public void sendKeyAction(String actionCode) {
        try {
            ChatMsg message = new ChatMsg(userName, actionCode, myPlayerNum + "@@");
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            // 메시지 전송 오류 처리
        }
    }

    class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    sendKeyAction("401@@VK_DOWN");
                    break;
                case KeyEvent.VK_UP:
                    sendKeyAction("401@@VK_UP");
                    break;
                case KeyEvent.VK_LEFT:
                    sendKeyAction("401@@VK_LEFT");
                    break;
                case KeyEvent.VK_RIGHT:
                    sendKeyAction("401@@VK_RIGHT");
                    break;
                case KeyEvent.VK_SPACE:
                    sendKeyAction("401@@VK_SPACE");
                    break;
                // 추가적인 키 이벤트 처리
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    sendKeyAction("402@@VK_DOWN");
                    break;
                case KeyEvent.VK_UP:
                    sendKeyAction("402@@VK_UP");
                    break;
                case KeyEvent.VK_LEFT:
                    sendKeyAction("402@@VK_LEFT");
                    break;
                case KeyEvent.VK_RIGHT:
                    sendKeyAction("402@@VK_RIGHT");
                    break;
                case KeyEvent.VK_SPACE:
                    sendKeyAction("402@@VK_SPACE");
                    break;
                // 추가적인 키 이벤트 처리
            }
        }
    }


}
