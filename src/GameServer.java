

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;



public class GameServer extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    JTextArea textArea;
    private JTextField txtPortNumber;

    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓
    private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
    private int [] RoomCount = new int[5];
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

    public void addRoomCount(int roomNum) {
        RoomCount[roomNum]++;
    }

    public void reduceRoomCount(int roomNum) {
        RoomCount[roomNum]--;
    }

    public int getRoomCount(int roomNum) {
        return RoomCount[roomNum];
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameServer frame = new GameServer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public GameServer() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 338, 440);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 10, 300, 298);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JLabel lblNewLabel = new JLabel("Port Number");
        lblNewLabel.setBounds(13, 318, 87, 26);
        contentPane.add(lblNewLabel);

        txtPortNumber = new JTextField();
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setText("30000");
        txtPortNumber.setBounds(112, 318, 199, 26);
        contentPane.add(txtPortNumber);
        txtPortNumber.setColumns(10);

        JButton btnServerStart = new JButton("Server Start");
        btnServerStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
                } catch (NumberFormatException | IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                AppendText("Chat Server Running..");
                btnServerStart.setText("Chat Server Running..");
                btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
                txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
                AcceptServer accept_server = new AcceptServer();
                accept_server.start();
            }
        });
        btnServerStart.setBounds(12, 356, 300, 35);
        contentPane.add(btnServerStart);
    }

    // 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {
                    AppendText("Waiting new clients ...");
                    client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
                    AppendText("새로운 참가자 from " + client_socket);
                    // User 당 하나씩 Thread 생성
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user); // 새로운 참가자 배열에 추가
                    new_user.start(); // 만든 객체의 스레드 실행
                    AppendText("현재 참가자 수 " + UserVec.size());
                } catch (IOException e) {
                    AppendText("accept() error");
                    // System.exit(0);
                }
            }
        }
    }

    public void AppendText(String str) {
        // textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }



    // User 당 생성되는 Thread
    // Read One 에서 대기 -> Write All
    class UserService extends Thread {
        private InputStream is;
        private OutputStream os;
        private DataInputStream dis;
        private DataOutputStream dos;

        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        private Socket client_socket;
        private Vector user_vc;
        public String UserName = "";
        public String UserStatus;

        public int roomNum = 0;

        public UserService(Socket client_socket) {
            // TODO Auto-generated constructor stub
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            this.user_vc = UserVec;
            try {
//				is = client_socket.getInputStream();
//				dis = new DataInputStream(is);
//				os = client_socket.getOutputStream();
//				dos = new DataOutputStream(os);

                oos = new ObjectOutputStream(client_socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream());

                // line1 = dis.readUTF();
                // /login user1 ==> msg[0] msg[1]
//				byte[] b = new byte[BUF_LEN];
//				dis.read(b);
//				String line1 = new String(b);
//
//				//String[] msg = line1.split(" ");
//				//UserName = msg[1].trim();
//				UserStatus = "O"; // Online 상태
//				Login();
            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        public int getRoomNum() {
            return roomNum;
        }

        public void setRoomNum(int roomNum) {
            this.roomNum = roomNum;
        }



        public void Login() {
            AppendText("새로운 참가자 " + UserName + " 입장.");
            //WriteOne("Welcome to Java chat server\n", "101");
            //WriteOne(UserName + "님 환영합니다.\n", "101"); // 연결된 사용자에게 정상접속을 알림
            String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
        }

//        public void Logout() {
//            String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
//            UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
//            WriteAll(msg, "104"); // 나를 제외한 다른 User들에게 전송
//            reduceRoomCount(getRoomNum());
//            AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
//        }

        public void Logout() {
            String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
            UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
            WriteAll(msg, "104"); // 나를 제외한 다른 User들에게 전송
            reduceRoomCount(getRoomNum());
            AppendText("사용자 퇴장. 현재 참가자 수 " + UserVec.size());
        }

       // 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
        public void WriteAll(String str, String code) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user.UserStatus == "O" && user.getRoomNum() == roomNum)
                    user.WriteOne(str, code);
            }
        }


        // 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
        public void WriteAllObject(Object ob) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user.UserStatus == "O")
                    user.WriteOneObject(ob);
            }
        }

        // 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
        public void WriteOthers(String str, String code) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user != this && user.getRoomNum() == roomNum)
                    user.WriteOne(str, code);
            }
        }

        // 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
        public void WriteJoin() {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (user != this && user.getRoomNum() == roomNum) {
                    user.WriteOne("player1@@" + UserName, "102");
                    WriteOne("player2@@" + user.UserName, "102");
                }
            }
        }
        public void WriteOne(String msg, String code) {
            try {
                ChatMsg obcm = new ChatMsg("SERVER", code, msg);
                if(obcm!=null) oos.writeObject(obcm);
            } catch (IOException e) {
                AppendText("dos.writeObject() error");
                try {
                    ois.close();
                    oos.close();
                    client_socket.close();
                    client_socket = null;
                    ois = null;
                    oos = null;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Logout(); // 에러가난 현재 객체를 벡터에서 지운다
            }
        }
        // Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
        public byte[] MakePacket(String msg) {
            byte[] packet = new byte[BUF_LEN];
            byte[] bb = null;
            int i;
            for (i = 0; i < BUF_LEN; i++)
                packet[i] = 0;
            try {
                bb = msg.getBytes("euc-kr");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (i = 0; i < bb.length; i++)
                packet[i] = bb[i];
            return packet;
        }


        public void WriteOneObject(Object ob) {
            try {
                oos.writeObject(ob);
            }
            catch (IOException e) {
                AppendText("oos.writeObject(ob) error");
                try {
                    ois.close();
                    oos.close();
                    client_socket.close();
                    client_socket = null;
                    ois = null;
                    oos = null;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Logout();
            }
        }

        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {

                    Object obcm = null;
                    String msg = null;
                    ChatMsg cm = null;
                    if (socket == null)
                        break;
                    try {
                        obcm = ois.readObject();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return;
                    }
                    if (obcm == null)
                        break;
                    if (obcm instanceof ChatMsg) {
                        cm = (ChatMsg) obcm;
                    } else
                        continue;
                    if (cm.getCode().matches("101")) { // 방 만들기/방 참가
                        UserName = cm.getId();
                        UserStatus = "O"; // Online 상태
                        //1,2,3
                        roomNum = Integer.parseInt(cm.getData());
                        addRoomCount(roomNum);
                        if(getRoomCount(roomNum) >= 1) {
                            WriteJoin();
                        }
                        Login();
                    } else if (cm.getCode().matches("103")) { // 게임 시작
                        WriteOthers("start","103");
                    }else if (cm.getCode().matches("300")) { // stage 이동
                        WriteOthers(cm.getData(),"300");
                        WriteOne(cm.getData(),"300");
                    } else if (cm.getCode().matches("401")) { // player 움직임 keyPressed
                        System.out.println("401");

                        WriteOthers(cm.getData(),"401");
                        WriteOne(cm.getData(),"401");
                    } else if (cm.getCode().matches("402")) { // player 움직임 keyReleased
                        System.out.println("402");

                        WriteOthers(cm.getData(),"402");
                        WriteOne(cm.getData(),"402");
                    } else if (cm.getCode().matches("403")) { // player 움직임 (x,y)
                        System.out.println("403");

                        WriteOthers(cm.getData(),"403");
                        WriteOne(cm.getData(),"403");
                    } else if (cm.getCode().matches("501")) { // bubble이랑 monster이랑 만남
                        System.out.println("501");

                        WriteOthers(cm.getData(),"501");
                        WriteOne(cm.getData(),"501");
                    } else if (cm.getCode().matches("502")) { // bubble 천장 랜덤 움직임
                        System.out.println("502");

                        WriteOthers(cm.getData(),"502");
                    } else if (cm.getCode().matches("601")) { // bubble 터짐 > item create
                        System.out.println("601");

                        WriteOthers(cm.getData(),"601");
                        WriteOne(cm.getData(),"601");
                    } else if (cm.getCode().matches("602")) { // item 위치 조정
                        System.out.println("602");

                        WriteOthers(cm.getData(),"602");
                        WriteOne(cm.getData(),"602");
                    }   else if (cm.getCode().matches("603")) { // item 점수 증가
                        System.out.println("603");

                        WriteOthers(cm.getData(),"603");
                        WriteOne(cm.getData(),"603");
                    }
                } catch (IOException e) {
                    AppendText("ois.readObject() error");
                    try {
//						dos.close();
//						dis.close();
                        ois.close();
                        oos.close();
                        client_socket.close();
                        Logout(); // 에러가난 현재 객체를 벡터에서 지운다
                        break;
                    } catch (Exception ee) {
                        break;
                    } // catch문 끝
                } // 바깥 catch문끝
            } // while
        } // run
    }

}
