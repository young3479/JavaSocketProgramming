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

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;


    private ArrayList<Platform> platforms;
    private Player player1;
    private Player player2;


    private Player myself;
    private String userName;

    private int myPlayerNum;

    private Map map;

    private GameThread gameThread;

    // 플레이어 움직임 속도 설정
    private final int MOVE_SPEED = 5;

    public boolean threadFlag = true;
    private ObjectOutputStream oos;

    private Stage1 stage1; //맵추가

    /**
     * Create the panel.
     */
    public GamePanel(Player player1, Player player2, int myPlayerNum) {
        this.player1 = player1;
        this.player2 = player2;
        this.myPlayerNum = myPlayerNum;

        // 플레이어 객체 할당
        if (myPlayerNum == 1)
            myself = player1;
        else
            myself = player2;


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

    // 플레이어 위치 업데이트 메서드
    private void updatePlayerPosition(Player player, boolean left, boolean right, boolean up, boolean down) {
        if (left) {
            player.setX(player.getX() - MOVE_SPEED);
        }
        if (right) {
            player.setX(player.getX() + MOVE_SPEED);
        }
        if (up) {
            player.setY(player.getY() - MOVE_SPEED);
        }
        if (down) {
            player.setY(player.getY() + MOVE_SPEED);
        }
    }
    // 서버에 플레이어 움직임을 전송하는 메소드
    public void sendPlayerMovement(String action) {
        try {
            // 플레이어의 현재 위치에 대한 메시지 생성
            ChatMsg message = new ChatMsg(userName, "player_move", action);
            oos.writeObject(message);
        } catch (IOException e) {
            System.err.println("Error sending player movement: " + e.getMessage());
        }
    }
    class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // 현재 플레이어 객체를 결정합니다.
            Player currentPlayer = myPlayerNum == 1 ? player1 : player2;

            // 현재 플레이어의 키 이벤트를 처리합니다.
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    leftPressed = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = true;
                    break;
                case KeyEvent.VK_UP:
                    upPressed = true;
                    break;
                case KeyEvent.VK_DOWN:
                    downPressed = true;
                    break;
            }
            updatePlayerPosition(currentPlayer, leftPressed, rightPressed, upPressed, downPressed);
            sendPlayerMovement("keyPressed:" + e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // 현재 플레이어 객체를 결정합니다.
            Player currentPlayer = myPlayerNum == 1 ? player1 : player2;

            // 현재 플레이어의 키 이벤트를 처리합니다.
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    leftPressed = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = false;
                    break;
                case KeyEvent.VK_UP:
                    upPressed = false;
                    break;
                case KeyEvent.VK_DOWN:
                    downPressed = false;
                    break;
            }
            updatePlayerPosition(currentPlayer, leftPressed, rightPressed, upPressed, downPressed);
            sendPlayerMovement("keyReleased:" + e.getKeyCode());
        }
    }

    public void updatePlayerFromServer(String playerInfo) {
        String[] parts = playerInfo.split("@@");
        int playerNum = Integer.parseInt(parts[0]);
        String action = parts[1];

        Player playerToUpdate = playerNum == 1 ? player1 : player2;

        switch (action) {
            case "VK_LEFT_PRESSED":
                playerToUpdate.setX(playerToUpdate.getX() - MOVE_SPEED);
                break;
            case "VK_RIGHT_PRESSED":
                playerToUpdate.setX(playerToUpdate.getX() + MOVE_SPEED);
                break;
            case "VK_UP_PRESSED":
                playerToUpdate.setY(playerToUpdate.getY() - MOVE_SPEED);
                break;
            case "VK_DOWN_PRESSED":
                playerToUpdate.setY(playerToUpdate.getY() + MOVE_SPEED);
                break;
            case "VK_LEFT_RELEASED":
            case "VK_RIGHT_RELEASED":
                // 가로 이동 중지
                break;
            case "VK_UP_RELEASED":
            case "VK_DOWN_RELEASED":
                // 세로 이동 중지
                break;
            // 추가적인 키 이벤트 처리
        }
    }


}
