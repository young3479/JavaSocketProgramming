import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

// 네트워크 통신을 위한 클래스 (예시)
public class NetworkHandler {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ExecutorService executorService = Executors.newSingleThreadExecutor(); //수정중

    public NetworkHandler(String address, int port) throws IOException {
        socket = new Socket(address, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
//수정중
//    public void sendPlayerState(Player player) throws IOException {
//        out.writeObject(player);
//        out.flush();
//    }

    public void sendPlayerState(Player player) {
        executorService.submit(() -> {
            try {
                out.writeObject(player);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //수정중
//    public Player receivePlayerState() throws IOException, ClassNotFoundException {
//        return (Player) in.readObject();
//    }

    public void receivePlayerStateAsync(Consumer<Player> callback) {
        executorService.submit(() -> {
            try {
                Player player = (Player) in.readObject();
                callback.accept(player);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    // 여기에 연결 종료 등의 추가 메서드를 구현할 수 있습니다.
}
