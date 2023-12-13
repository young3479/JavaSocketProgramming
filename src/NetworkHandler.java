import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

// 네트워크 통신을 위한 클래스 (예시)
public class NetworkHandler {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public NetworkHandler(String address, int port) throws IOException {
        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void sendPlayerState(Player player) throws IOException {
        out.writeObject(player);
        out.flush();
    }

    public Player receivePlayerState() throws IOException, ClassNotFoundException {
        return (Player) in.readObject();
    }

    // 여기에 연결 종료 등의 추가 메서드를 구현할 수 있습니다.
}
