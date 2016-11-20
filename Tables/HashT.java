package Tables;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class HashT<K, V> {
    private final static float DEFAULT_CHARGE = 0.75f;
    private final static int DEFAULT_SIZE = 131;
    private int m, //tamaño
                n, //cantidad de datos
                max;
    private float charge;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public HashT(int size, float charge){
        this.charge = charge;
        this.max = (int) (size * charge);
        this.m = size;
        this.n = 0;
        this.table = (Node<K, V>[])new Node[size];
    }
    public HashT(){
        this(DEFAULT_SIZE, DEFAULT_CHARGE);
    }
    public int hash(K key){
        return (key.hashCode() & (~0 >>> 1)) % this.m;
    }
    public int size(){
        return this.n;
    }
    public boolean isEmpty(){
        return this.n == 0;
    }
    public V get(K key){
        if(key == null) throw new NullPointerException("null argument");
        int pos = this.hash(key);
        for(Node<K, V> x = this.table[pos]; x != null; x = x.next){
            if(key.equals(x.key))
                return x.value;
        }
        return null;
    }
    public boolean contains(K key){
        return this.get(key) != null;
    }
    public void put(K key, V value){
        if(key == null || value == null) throw new NullPointerException("null argument");
        if(this.n >= this.max) this.rehash();
        int pos = this.hash(key);
        Node<K, V> x;
        for(x = this.table[pos]; x != null; x = x.next){
            if(key.equals(x.key)){
                x.value = value;
                return;
            }
        }
        ++this.n;
        this.table[pos] = new Node<>(key, value, this.table[pos]);
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
    public Iterable<K> keys(){
        Queue<K> queue = new LinkedList<K>();
        for(int i = 0; i < this.m; ++i){
            for(Node<K, V> x = this.table[i]; x != null; x = x.next){
                queue.add(x.key);
            }
        }
        return queue;
    }
    public Iterator<K> keyItr(){
        return new KeyIterator();
    }
    public Iterator<V> valueItr(){
        return new ValueIterator();
    }
    @SuppressWarnings("unchecked")
    private void rehash(){
        this.n = 0;
        this.m = this.m * 2 + 1;
        this.max = (int)(this.m * this.charge);
        Node<K, V>[] oldTable = this.table;
        this.table = (Node<K, V>[])new Node[this.m];
        int i = 0;
        Node<K, V> tmp = oldTable[i];
        while(i < oldTable.length){
            if(tmp != null){
                this.put(tmp.key, tmp.value);
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
            for(this.index = 0; this.index < HashT.this.m; ++this.index){
                if(HashT.this.table[this.index] != null){
                    this.next = HashT.this.table[this.index];
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
                for(++this.index; this.index < HashT.this.m && this.next == null; ++this.index){
                    this.next = HashT.this.table[this.index];
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
/*
    public static void main(String[] args) {
        HashT<String, String> t = new HashT<>();
        t.put("1", "hola");
        t.put("2", "bye");
        t.put("no", "si");
        //t.remove("1");
        Iterator<String> itr = t.keyItr();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
        itr = t.valueItr();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
    }
*/
}
