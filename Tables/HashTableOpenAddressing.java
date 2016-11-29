package Tables;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class HashTableOpenAddressing<Key, Value> implements DictionaryInterface<Key, Value>{
    private final static int DEFAULT_SIZE = 131,
                             MAX_A_B = 100;
    private int m,
                n;
    private long a,
                 b,
                 p;
    private Pair<Key, Value>[] table;

    public HashTableOpenAddressing(){
        this(DEFAULT_SIZE);
    }

    @SuppressWarnings("unchecked")
    public HashTableOpenAddressing(int size){
        this.m = size;
        this.p = nextPrime(this.m);
        this.n = 0;
        this.table = (Pair<Key, Value>[])new Pair[this.m];
        Random rn = new Random();
        this.a = nextPrime(rn.nextInt(MAX_A_B) + 1);
        this.b = nextPrime(rn.nextInt(MAX_A_B));
    }

    public Value add(Key k, Value item) {
        if(k == null) throw new NoSuchElementException("Key can't be null.");
        this.resizeIfNeeded();
        int hash = this.hash(k);
        int pos = hash;
        //System.out.println(hash+": bye");
        while(this.table[pos] != null && !this.table[pos].key.equals(k)){
            //System.out.println("collision");
            pos = (pos + 1) % this.m;
        }
        Value tmp = null;
        if(this.table[pos] != null){
            tmp = this.table[pos].value;
            this.table[pos].value = item;
        }
        else{
            this.table[pos] = new Pair<Key, Value>(k, item);
        }
        ++this.n;
        return tmp;
    }

    public Value remove(Key k) {
        if(k == null) throw new NoSuchElementException("Key can't be null.");
        int hash = this.hash(k);
        int pos = hash;
        if(this.table[pos] == null)
            throw new NoSuchElementException("Table doesn't contain the key.");
        while(!k.equals(this.table[pos].key)){
            pos = (pos + 1) % this.m;
            if(this.table[pos] == null || pos == hash)
                throw new NoSuchElementException("Table doesn't contain the key.");
        }
        Pair<Key, Value> tmp = this.table[pos];
        this.table[pos] = null;
        while(this.table[(pos = (pos + 1) % this.m)] != null){
            Pair<Key, Value> pair = this.table[pos];
            this.table[pos] = null;
            --this.n;
            this.add(pair.key, pair.value);
        }
        --this.n;
        return tmp.value;
    }

    public Value getValue(Key k) {
        if(k == null) throw new NoSuchElementException("Key can't be null.");
        int hash = this.hash(k);
        int pos = hash;
        if(this.table[pos] == null)
            throw new NoSuchElementException("Table doesn't contain the key.");
        while(!k.equals(this.table[pos].key)){
            pos = (pos + 1) % this.m;
            if(this.table[pos] == null || pos == hash)
                throw new NoSuchElementException("Table doesn't contain the key.");
        }
        return this.table[pos].value;
    }

    public boolean contains(Key k) {
        if(k == null) throw new NoSuchElementException("Key can't be null.");
        int hash = this.hash(k);
        int pos = hash;
        if(this.table[pos] == null)
            return false;
        while(!k.equals(this.table[pos].key)){
            //System.out.println("hi");
            pos = (pos + 1)%this.m;
            if(this.table[pos] == null || pos == hash) return false;
        }
        return true;
    }

    public Iterator<Key> getKeyIterator() {
        return new ItrK();
    }

    public Iterator<Value> getValueIterator() {
        return new ItrV();
    }

    public boolean isEmpty() {
        return this.n == 0;
    }

    public int getSize() {
        return this.n;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        this.n = 0;
        this.table = (Pair<Key, Value>[])new Pair[this.m];
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for(Pair<Key, Value> p : this.table){
            if(p != null)
                sb.append(p.key + ":" + p.value + ", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private void resizeIfNeeded(){
        if(this.n < (this.m*75)/100) return;
        this.m = (m * 2) + 1;
        this.p = nextPrime(this.m);
        Pair<Key, Value>[] old = this.table;
        this.table = (Pair<Key, Value>[])new Pair[this.m];
        for(Pair<Key, Value> e: old){
            if(e != null){
                this.add(e.key, e.value);
            }
        }
    }

    private static int nextPrime(int n){
        return (new BigInteger(Integer.toString(n))).nextProbablePrime().intValue();
    }

    private int hash(Key k){
        return (int) (((this.a * (k.hashCode() & (~0 >>> 1)) + this.b) % this.p) % this.m);
    }

    private static class Pair<K, V>{
        public K key;
        public V value;

        public Pair(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

    private class ItrK implements Iterator<Key>{
        private int pos;

        public ItrK(){
            this.pos = -1;
            while(++this.pos < HashTableOpenAddressing.this.m){
                if(HashTableOpenAddressing.this.table[this.pos] != null){
                    break;
                }
            }
        }

        public boolean hasNext() {
            return this.pos < HashTableOpenAddressing.this.m;
        }

        public Key next(){
            Key tmp = HashTableOpenAddressing.this.table[this.pos].key;
            while(++this.pos < HashTableOpenAddressing.this.m){
                if(HashTableOpenAddressing.this.table[this.pos] != null){
                    break;
                }
            }
            return tmp;
        }

        public void remove(){}
    }

    private class ItrV implements Iterator<Value>{
        private int pos;

        public ItrV(){
            this.pos = -1;
            while(++this.pos < HashTableOpenAddressing.this.m){
                if(HashTableOpenAddressing.this.table[this.pos] != null){
                    break;
                }
            }
        }

        public boolean hasNext() {
            return this.pos < HashTableOpenAddressing.this.m;
        }

        public Value next(){
            Value tmp = HashTableOpenAddressing.this.table[this.pos].value;
            while(++this.pos < HashTableOpenAddressing.this.m){
                if(HashTableOpenAddressing.this.table[this.pos] != null){
                    break;
                }
            }
            return tmp;
        }

        public void remove(){}
    }

    public static void main(String[] args) {
        
        HashTableOpenAddressing<Integer, String> ht = new HashTableOpenAddressing<>();
        ht.add(2, "1hola");
        System.out.println(ht.getValue(2));
        ht.add(1, "-1lol");
        System.out.println(ht.getValue(1));
        ht.remove(1);
        System.out.println(ht);
        Iterator<Integer> itr1 = ht.getKeyIterator();
        Iterator<String> itr2 = ht.getValueIterator();
        while(itr1.hasNext()){
            System.out.println(itr1.next());
        }
        while(itr2.hasNext()){
            System.out.println(itr2.next());
        }
    }
}
