package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Bucket {
    protected int profundidade;
    protected int length;
    protected int emptyLength;
    protected HashMap<Integer, Integer> data;
    

    public Bucket(int profundidade, int length) {
        setProfundidade(profundidade);
        setLength(length);
        setEmptyLength(length);
        initBucket();
    }
    
    private void initBucket() {
        data = new HashMap<>();
    }
    
    public boolean insertData (int key, int index) throws IndexOutOfBoundsException {
        if (emptyLength > 0) {
            data.put(key, index);
            emptyLength --;
            return true;
        } else {
            throw new IndexOutOfBoundsException("Bucket Cheio");
        }
    }

    public boolean removeData (int key) {
        if (length > emptyLength) {
            if (data.remove(key) != null) {
                emptyLength ++;
                return true;
            } else {
                return false;
            }
        } else { // bucket está vazio
            return false;
        }
    }

    public int getKeyData (int key) {
        if (length > emptyLength) {
            Integer data = this.data.get(key);
            return data != null ? data : -1;
        } else { // bucket está vazio
            return -1;
        }
    }

    public byte[] toByteArray () {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        try {
            dos.writeInt(this.getProfundidade());
            dos.writeInt(this.getLength());
            dos.writeInt(this.getEmptyLength());
            data.forEach((k, v) -> {
                try {
                    dos.writeInt(k);
                    dos.writeInt(v);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            for (int i = 0; i < getEmptyLength(); i++) {
                dos.writeInt(-1);
                dos.writeInt(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            new IOException(e.getCause()).printStackTrace();;
        }
        
        return null;
    }
    
    public void fromByteArray (byte[] bucket) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bucket);
        DataInputStream dis = new DataInputStream(bais);
        
        try {
            this.setProfundidade(dis.readInt());
            this.setLength(dis.readInt());
            this.setEmptyLength(dis.readInt());
            for (int i = 0; i < getEmptyLength(); i++) {
                data.put(dis.readInt(), dis.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getProfundidade() {
        return this.profundidade;
    }

    public void setProfundidade(int profundidade) {
        this.profundidade = profundidade;
    }

    public int getLength() {
        return this.length;
    }

    private void setLength(int length) {
        this.length = length;
    }

    public int getEmptyLength() {
        return this.emptyLength;
    }
    
    private void setEmptyLength(int emptyLength) {
        this.emptyLength = emptyLength;
    }
}
