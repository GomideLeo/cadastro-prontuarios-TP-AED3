package model;

public class Directory {
    private int profundidade;
    private int buckets[];

    public Directory(){
        this.profundidade = 3;
        this.buckets = new int[(int) Math.pow(2, this.profundidade)];
    }

    public Directory(int profundidade){
        this.profundidade = profundidade;
        this.buckets = new int[(int) Math.pow(2, this.profundidade)];
    }

    public int hashFunction(int valor) {
        return valor % (int) Math.pow(2, this.profundidade);
    }

    public void extendDir(){
        int n = this.buckets.length;
        this.profundidade++;
        int newBuckets[] = new int[n * 2];

        for (int i = 0; i < n; i++ ){
            newBuckets[i] = newBuckets[n+i] = this.buckets[i];
        }
        this.buckets = newBuckets;
    }

    public int getProfundidade(){
        return this.profundidade;
    }

    public int getBucket(int posicao){
        return this.buckets[posicao];
    }

    public String toString(){
        String aux = "";
        for(int i : this.buckets){
            aux +=(i +" ");
        }
        aux += "\n";
        return aux;
    }

}
