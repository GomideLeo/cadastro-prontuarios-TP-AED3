package manager;
import java.util.HashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HeaderManager {
    protected int headerSize;
    protected HashMap<String, String> header;

    public HeaderManager(int headerSize){
        header = new HashMap<String, String>();
        this.headerSize = headerSize;
    }

    public void byteArrayToHeader(byte[]data){
        this.headerSize = data.length;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);

        try {
            int numOfPairs = dis.readInt();
            for(int i = 0; i < numOfPairs; i++){
                String key = dis.readUTF();
                String value = dis.readUTF();
                header.put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] headerToByteArray(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(this.headerSize + 4);
            dos.writeInt(this.header.size());

            for (HashMap.Entry<String, String> entry : this.header.entrySet()) {
                dos.writeUTF(entry.getKey());
                dos.writeUTF(entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }
}
