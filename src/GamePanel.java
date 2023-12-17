import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.awt.Graphics;
import java.util.Set;

import javax.swing.*;


public class GamePanel extends JLayeredPane {

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
    public GamePanel(Player player1, Player player2, int myPlayerNum, ObjectOutputStream oos, ObjectInputStream ois) {
        this.player1 = player1;
        this.player2 = player2;
        this.myPlayerNum = myPlayerNum;
        this.oos = oos; // ObjectOutputStream 설정
        this.ois = ois; // ObjectInputStream 설정

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
        new ServerMessageListener(this.ois).start();

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
            }
        }
    }

    // 게임 패널 내 위치 정보 전송 메소드 추가 (꼭필요!!!!!! 있어야 코드 돌아감)
    public void sendPlayerPosition(Player player) {
        try {
            String positionData = player.getX() + "," + player.getY();
            ChatMsg message = new ChatMsg(userName, "player_position", myPlayerNum + "@" + positionData);
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //꼭 필요!!!!!!
    public void updatePlayerFromServer(String playerInfo) {
        SwingUtilities.invokeLater(() -> {
        String[] parts = playerInfo.split("@");
        int playerNum = Integer.parseInt(parts[0]);
        String[] position = parts[1].split(",");
        int x = Integer.parseInt(position[0]);
        int y = Integer.parseInt(position[1]);

        Player playerToUpdate = playerNum == 1 ? player1 : player2;

        playerToUpdate.setX(x);
        playerToUpdate.setY(y);

        });
    }

    //서버로부터 메시지 수신
    class ServerMessageListener extends Thread {

        private final ObjectInputStream ois;
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
                                    updatePlayerFromServer(msg.getData());
                                break;
                            case "player_move":
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
                otherPlayer.setX(x); //(기능 구현 중) -이전 코드
                otherPlayer.setY(y);//(기능 구현 중) -이전 코드
                repaint(); // 화면을 다시 그려서 변경 사항 반영
            }
        }
    }
}
