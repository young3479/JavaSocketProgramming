import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.awt.Graphics;
import java.util.Set;

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

    private ObjectInputStream ois;

    int panelWidth = 730;
    int panelHeight = 730;


    /**
     * Create the panel.
     */
    public GamePanel(Player player1, Player player2, int myPlayerNum, String ip, int port) {
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

        this.addKeyListener(new KeyListener());
        this.requestFocus();
        this.setFocusable(true);
        //게임패널 크기
        setPreferredSize(new Dimension(730, 730));

        this.gameThread = new GameThread();
        gameThread.start();

        new ServerMessageListener(ois).start();

        // 서버에 연결
        connectToServer(ip, port);

        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        stage1.draw(g); // Stage1 그리기
        player1.draw(g);
        player2.draw(g);
    }


    public String getUserName() {
        return userName;
    }



    class GameThread extends Thread {

        @Override
        public void run() {
            while (true) {
                try {

//                    // 플레이어의 위치와 상태를 업데이트
//                    player1.update();
//                    player2.update();
//                    // 충돌 처리
//                    stage1.checkCollisions(); //수정 성공 (중력 구현)
                    //gameControll();
//여기 함수 어떻게 순서 정할지도 중요한듯
                    repaint();
                    Thread.sleep(20); //수정 초기값 20

                    // 승자가 있는지 확인
                    Player winner = stage1.checkWinner();
                    if (winner != null) {
                        // 승자가 결정되면 GameEndingPanel로 전환
                        EventQueue.invokeLater(() -> switchToEndingPanel(winner.getPlayerNum()));
                        break;
                    }

                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    // 승자가 결정되었을 때 서버에 결과를 전송
    private void sendGameResult(int winnerPlayerNum) {
        try {
            ChatMsg message = new ChatMsg(userName, "game_result", "Winner is Player " + winnerPlayerNum);
            oos.writeObject(message);
        } catch (IOException e) {
            System.err.println("Error sending game result: " + e.getMessage());
        }
    }

    // 승자가 결정되었을 때 호출되는 메서드
    private void switchToEndingPanel(int winnerPlayerNum) {
        GameEndingPanel endingPanel = new GameEndingPanel(winnerPlayerNum);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(endingPanel);
        frame.revalidate();
        frame.repaint();
    }



    // 서버와의 연결을 설정하는 메소드 수정
    public void connectToServer(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            new ServerMessageListener(ois).start(); // 메시지 리스너 시작
        } catch (IOException e) {
            e.printStackTrace();
            // 연결 오류 처리
        }
    }
    public void gameControll() {


                    player1.update();
                    player2.update();
                    // 충돌 처리
                    stage1.checkCollisions(); //수정 성공 (중력 구현)
    }

    // 플레이어 위치 업데이트 메서드
    private void updatePlayerPosition(Player player, boolean left, boolean right, boolean up, boolean down) {

        if (oos == null) {
            return; // ObjectOutputStream이 초기화되지 않았다면 함수 종료
        }
        if (left) {
            player.setX(player.getX() - MOVE_SPEED);
        }
        if (right) {
            player.setX(player.getX() + MOVE_SPEED);
        }if (up) {
            player.setY(player.getY() - MOVE_SPEED);
        }if (down) {
            player.setY(player.getY() + MOVE_SPEED);
        }// 위치가 변경되면 서버에 전송
        sendPlayerPosition(player);
    }



//// 수평 이동
//        if (left) {
//            player.setVelocityX(-MOVE_SPEED);
//        } else if (right) {
//            player.setVelocityX(MOVE_SPEED);
//        } else {
//            player.setVelocityX(0); // 왼쪽이나 오른쪽이 눌리지 않으면 수평 속도를 0으로 설정
//        }
//
//        // 점프 체크
//
//       if (up && player.isOnGround()) {
//            player.jump();
//        }
//
//        // 중력 적용 (점프 후에 중력을 적용하도록 순서 변경)
//        if (!player.isOnGround()) {
//            player.setVelocityY(player.getVelocityY() + Player.GRAVITY);
//        }
//
//
//        player.update(); // 플레이어의 위치 업데이트
//
//
//        // 충돌 처리
//        stage1.checkCollisions();
//
//
//
//        // 위치가 변경되면 서버에 전송
//        sendPlayerPosition(player);




    // 서버에 플레이어 움직임을 전송하는 메소드
    public void sendPlayerMovement(String action) {
        try {
            ChatMsg message = new ChatMsg(userName, "player_move", myPlayerNum + "@@" + action);
            oos.writeObject(message);
        } catch (IOException e) {
            System.err.println("Error sending player movement: " + e.getMessage());
        }
    }
    class KeyListener extends KeyAdapter {
        private Set<Integer> pressedKeys = new HashSet<>();
        @Override
        public void keyPressed(KeyEvent e) {
            pressedKeys.add(e.getKeyCode());
            updateMovement();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            pressedKeys.remove(e.getKeyCode());
            updateMovement();
        }
        private void updateMovement() {
            boolean left = pressedKeys.contains(KeyEvent.VK_LEFT);
            boolean right = pressedKeys.contains(KeyEvent.VK_RIGHT);
            boolean up = pressedKeys.contains(KeyEvent.VK_UP);
            boolean down = pressedKeys.contains(KeyEvent.VK_DOWN);

            if (oos != null) { // Check if the ObjectOutputStream is not null
                Player currentPlayer = myPlayerNum == 1 ? player1 : player2;
                updatePlayerPosition(currentPlayer, left, right, up, down);
                sendPlayerMovement(generateMovementString(left, right, up, down));
            }
        }
        private String generateMovementString(boolean left, boolean right, boolean up, boolean down) {
            // Create a string representing the current movement state
            // Example: "Left:True, Right:False, Up:True, Down:False"
            return "Left:" + left + ", Right:" + right + ", Up:" + up + ", Down:" + down;
        }
    }

    // 게임 패널 내 위치 정보 전송 메소드 추가
    public void sendPlayerPosition(Player player) {
        try {
            String positionData = player.getX() + "," + player.getY();
            ChatMsg message = new ChatMsg(userName, "player_position", myPlayerNum + "@@" + positionData);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerFromServer(String playerInfo) {
        SwingUtilities.invokeLater(() -> {
        String[] parts = playerInfo.split("@@");
        int playerNum = Integer.parseInt(parts[0]);
        String[] position = parts[1].split(",");
        int x = Integer.parseInt(position[0]);
        int y = Integer.parseInt(position[1]);

        Player playerToUpdate = playerNum == 1 ? player1 : player2;

        playerToUpdate.setX(x);
        playerToUpdate.setY(y);

//// 중력 적용 및 위치 업데이트
//        playerToUpdate.update(); //수정성공 (중력 기능구현)
//
//        // 화면을 다시 그려 변경 사항 반영
//        repaint();
        });
    }


    class SendThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // 현재 플레이어의 위치 데이터 생성
                    int x = myself.getX();
                    int y = myself.getY();
                    ChatMsg message = new ChatMsg(userName, "player_position", myPlayerNum + "@@" + x + "," + y);

                    // 서버로 데이터 전송
                    if (message != null) {
                        oos.writeObject(message);
                    }

                    // 지정된 시간만큼 대기
                    Thread.sleep(300);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //서버로부터 메시지 수신
    class ServerMessageListener extends Thread {

        private final ObjectInputStream ois;
        private GamePanel gamePanel; // GamePanel에 대한 참조 추가
        public ServerMessageListener(ObjectInputStream ois) {
            this.ois = ois;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Object obj = ois.readObject();
                    if (obj instanceof ChatMsg) {
                        ChatMsg msg = (ChatMsg) obj;
                        switch (msg.getCode()) {
                            case "update_position":
                                // 다른 플레이어의 위치 업데이트
                             //   updatePlayerFromServer(msg.getData()); 수정진행중(통신최적화)
                                // 다른 플레이어의 위치 업데이트 수정진행중(통신최적화)
                                    updatePlayerFromServer(msg.getData());
                                break;
                            case "player_move":
                                // 다른 플레이어의 움직임 처리
                                //processPlayerMove(msg.getData());
                                // 다른 플레이어의 움직임 처리
                                SwingUtilities.invokeLater(() -> {
                                    processPlayerMove(msg.getData());
                                });
                                break;
                            case "game_result":
                                String[] resultData = msg.getData().split(" ");
                                int winnerPlayerNum = Integer.parseInt(resultData[resultData.length - 1]);
                                EventQueue.invokeLater(() -> switchToEndingPanel(winnerPlayerNum));
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 다른 플레이어의 움직임 처리
        private void processPlayerMove(String data) {
            // 데이터 분할: "playerNumber,x,y"
            String[] parts = data.split(",");
            if (parts.length != 3) {
                return; // 데이터 형식이 잘못되었을 경우
            }

            int playerNumber = Integer.parseInt(parts[0]);
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            // 현재 플레이어와 다른 플레이어의 움직임을 업데이트
            if (playerNumber != myPlayerNum) {
                Player otherPlayer = playerNumber == 1 ? player1 : player2;
               // stage1.checkCollisions(); //수정
                otherPlayer.setX(x); //(기능 구현 중) -이전 코드
                otherPlayer.setY(y);//(기능 구현 중) -이전 코드

                // 인터폴레이션을 사용하여 점진적으로 목표 위치로 이동
                //interpolatePlayerPosition(otherPlayer, x, y); //(기능 구현 중)
                repaint(); // 화면을 다시 그려서 변경 사항 반영
            }
        }
        // 플레이어 위치를 점진적으로 목표 위치로 이동시키는 메서드
        private void interpolatePlayerPosition(Player player, int targetX, int targetY) {
            // 이동 속도나 인터폴레이션 비율을 설정
            final float interpolationRate = 0.1f;
            int newX = (int)(player.getX() + (targetX - player.getX()) * interpolationRate);
            int newY = (int)(player.getY() + (targetY - player.getY()) * interpolationRate);

            player.setX(newX);
            player.setY(newY);
        }

        // 서버로부터 받은 위치 데이터를 사용하여 플레이어 위치 업데이트
        private void updatePlayerPositionFromServer(Player player, int newX, int newY) {
            // 서버로부터 받은 위치로 플레이어 위치 설정
            player.setX(newX);
            player.setY(newY);

            // 중력 적용
            if (!player.isOnGround()) {
                player.setVelocityY(player.getVelocityY() + Player.GRAVITY);
            } else {
                player.setVelocityY(0);
            }

            player.update(); // 플레이어의 위치 업데이트

            // 충돌 처리
            stage1.checkCollisions();
        }

        //플레이어가 창 경계 넘지 않도록 (근데 안됨)
        private void updatePlayerPosition(Player player) {

            int newX = player.getX();
            int newY = player.getY();

            // 수평 이동 처리
            if (leftPressed) {
                newX -= MOVE_SPEED;
            }
            if (rightPressed) {
                newX += MOVE_SPEED;
            }

            // 수직 이동 처리
            if (upPressed) {
                newY -= MOVE_SPEED;
            }
            if (downPressed) {
                newY += MOVE_SPEED;
            }

            // 플레이어가 창의 경계를 넘지 않도록 위치를 조정
            newX = Math.max(newX, 0); // 왼쪽 경계
            newX = Math.min(newX, panelWidth - player.getWidth()); // 오른쪽 경계

            newY = Math.max(newY, 0); // 상단 경계
            newY = Math.min(newY, panelHeight - player.getHeight()); // 하단 경계

            // 플레이어 위치 업데이트
            player.setX(newX);
            player.setY(newY);
        }




    }



}
