import java.io.Serializable;
import javax.swing.ImageIcon;

public class ChatMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String code;
    private String data;

    public ChatMsg(String id, String code, String msg) {
        this.id = id;
        this.code = code;
        this.data = msg;
    }

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    public String getId() {
        return id;
    }

}