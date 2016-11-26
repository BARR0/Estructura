package HashTable;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;

public class MyHashTable<K, V> implements DictionaryInterface<K, V>{
    private final static float DEFAULT_CHARGE = 0.75f;
    private final static int DEFAULT_SIZE = 131,
                             MAX_A_B = 100;;
    private int m, //tamaño
                n, //cantidad de datos
                max;
    private long a,
                 b,
                 p;
    private float charge;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashTable(int size, float charge){
        this.charge = charge;
        this.max = (int) (size * charge);
        this.m = size;
        this.n = 0;
        this.table = (Node<K, V>[])new Node[size];
        this.p = nextPrime(this.m);
        Random rn = new Random();
        this.a = nextPrime(rn.nextInt(MAX_A_B) + 1);
        this.b = nextPrime(rn.nextInt(MAX_A_B));
    }
    public MyHashTable(){
        this(DEFAULT_SIZE, DEFAULT_CHARGE);
    }
    private static int nextPrime(int n){
        return (new BigInteger(Integer.toString(n))).nextProbablePrime().intValue();
    }

    private int hash(K k){
        return (int) (((this.a * (k.hashCode() & (~0 >>> 1)) + this.b) % this.p) % this.m);
    }
    public int getSize(){
        return this.n;
    }
    public boolean isEmpty(){
        return this.n == 0;
    }
    public V getValue(K key){
        if(key == null) throw new NullPointerException("null argument");
        int pos = this.hash(key);
        for(Node<K, V> x = this.table[pos]; x != null; x = x.next){
            if(key.equals(x.key))
                return x.value;
        }
        return null;
    }
    public boolean contains(K key){
        return this.getValue(key) != null;
    }
    public V add(K key, V value){
        if(key == null || value == null) throw new NullPointerException("null argument");
        if(this.n >= this.max) this.rehash();
        int pos = this.hash(key);
        Node<K, V> x;
        for(x = this.table[pos]; x != null; x = x.next){
            if(key.equals(x.key)){
                V tmp = x.value;
                x.value = value;
                return tmp;
            }
        }
        ++this.n;
        this.table[pos] = new Node<>(key, value, this.table[pos]);
        return null;
    }
    public V remove(K key){
        if(key == null) throw new NullPointerException("null argument");
        int pos = this.hash(key);
        Node<K, V> x = this.table[pos];
        V tmp;
        if(x != null && x.key.equals(key)){
            tmp = x.value;
            this.table[pos] = x.next;
            --this.n;
            return tmp;
        }
        for(; x != null; x = x.next){
            if(x.next != null && x.next.key.equals(key)){
                tmp = x.next.value;
                x.next = x.next.next;
                --this.n;
                return tmp;
            }
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public void clear(){
        this.n = 0;
        this.table = (Node<K, V>[])new Node[this.m];
    }
    public Iterable<K> keys(){
        Queue<K> queue = new LinkedList<K>();
        for(int i = 0; i < this.m; ++i){
            for(Node<K, V> x = this.table[i]; x != null; x = x.next){
                queue.add(x.key);
            }
        }
        return queue;
    }
    public Iterator<K> getKeyIterator(){
        return new KeyIterator();
    }
    public Iterator<V> getValueIterator(){
        return new ValueIterator();
    }
    @SuppressWarnings("unchecked")
    private void rehash(){
        this.n = 0;
        this.m = this.m * 2 + 1;
        this.p = nextPrime(this.m);
        this.max = (int)(this.m * this.charge);
        Node<K, V>[] oldTable = this.table;
        this.table = (Node<K, V>[])new Node[this.m];
        int i = 0;
        Node<K, V> tmp = oldTable[i];
        while(i < oldTable.length){
            if(tmp != null){
                this.add(tmp.key, tmp.value);
                tmp = tmp.next;
            }
            else if(++i < oldTable.length){
                tmp = oldTable[i];
            }
        }
    }

    private abstract class HashIterator<E> implements Iterator<E>{
        Node <K, V> next;
        int index;

        public HashIterator(){
            for(this.index = 0; this.index < MyHashTable.this.m; ++this.index){
                if(MyHashTable.this.table[this.index] != null){
                    this.next = MyHashTable.this.table[this.index];
                    break;
                }
            }
        }
        public boolean hasNext(){
            return next != null;
        }
        public Node <K, V> nextNode(){
            if(this.next == null) throw new NoSuchElementException();
            Node<K, V> tmp = this.next;
            if((this.next = this.next.next) == null){
                for(++this.index; this.index < MyHashTable.this.m && this.next == null; ++this.index){
                    this.next = MyHashTable.this.table[this.index];
                }
                --this.index;
            }
            return tmp;
        }
    }
    public class ValueIterator extends HashIterator<V>{
        public V next() {
            return this.nextNode().value;
        }
    }
    public class KeyIterator extends HashIterator<K>{
        public K next() {
            return this.nextNode().key;
        }
    }

    private static class Node<K, V>{
        public K key;
        public V value;
        public Node<K, V> next;

        public Node(K key, V value, Node<K, V> next){
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
    /*public static void main(String[] args) {
        
        MyHashTable<Integer, String> ht = new MyHashTable<>();
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
    public static void main(String[] args) {
        MyHashTable<String, String> t = new MyHashTable<>();
        t.add("1", "hola");
        t.add("2", "bye");
        t.add("no", "si");
        t.remove("1");
        Iterator<String> itr = t.getKeyIterator();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
        itr = t.getValueIterator();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
    }
    */
}
