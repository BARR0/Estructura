package Tables;
//
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Dict<K, V> {
    private final static float DEFAULT_CHARGE = 0.75f;
    private final static int DEFAULT_SIZE = 131;
    private int m, //tamaño
                n, //cantidad de datos
                max;
    private float charge;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public Dict(int size, float charge){
        this.charge = charge;
        this.max = (int) (size * charge);
        this.m = size;
        this.n = 0;
        this.table = (Node<K, V>[])new Node[size];
    }
    public Dict(){
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
                return x.defs.value;
        }
        return null;
    }
    public ArrayList<V> getAll(K key){
        if(key == null) throw new NullPointerException("null argument");
        int pos = this.hash(key);
        for(Node<K, V> x = this.table[pos]; x != null; x = x.next){
            if(key.equals(x.key))
                return this.getDefs(x.defs);
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
                Def<V> tmp = new Def<>(value);
                tmp.next = x.defs;
                x.defs = tmp;
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
        Def<V> tmp;
        if(x != null && x.key.equals(key)){
            tmp = x.defs;
            this.table[pos] = x.next;
            --this.n;
            return tmp.value;
        }
        for(; x != null; x = x.next){
            if(x.next != null && x.next.key.equals(key)){
                tmp = x.next.defs;
                x.next = x.next.next;
                --this.n;
                return tmp.value;
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
    public Iterator<ArrayList<V>> valueItr(){
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
        ArrayList<V> list;
        while(i < oldTable.length){
            if(tmp != null){
                list = this.getDefs(tmp.defs);
                for(V val : list){
                    this.put(tmp.key, val);
                }
                tmp = tmp.next;
            }
            else if(++i < oldTable.length){
                tmp = oldTable[i];
            }
        }
    }
    private ArrayList<V> getDefs(Def<V> defs){
        Def<V> tmp = defs;
        ArrayList<V> list = new ArrayList<>();
        while(tmp != null){
            list.add(tmp.value);
            tmp = tmp.next;
        }
        return list;
    }

    private abstract class HashIterator<E> implements Iterator<E>{
        Node <K, V> next;
        int index;

        public HashIterator(){
            for(this.index = 0; this.index < Dict.this.m; ++this.index){
                if(Dict.this.table[this.index] != null){
                    this.next = Dict.this.table[this.index];
                    break;
                }
            }
        }
        public boolean hasNext(){
            return this.next != null;
        }
        public Node <K, V> nextNode(){
            if(this.next == null) throw new NoSuchElementException("hola prro");
            Node<K, V> tmp = this.next;
            if((this.next = this.next.next) == null){
                for(++this.index; this.index < Dict.this.m && this.next == null; ++this.index){
                    this.next = Dict.this.table[this.index];
                }
                --this.index;
            }
            return tmp;
        }
    }
    public class ValueIterator extends HashIterator<ArrayList<V>>{
        public ArrayList<V> next() {
            return Dict.this.getDefs(this.nextNode().defs);
        }
        public void remove(){}
    }
    public class KeyIterator extends HashIterator<K>{
        public K next() {
            return this.nextNode().key;
        }
        public void remove(){}
    }
    private static class Def<V>{
        public V value;
        public Def<V> next;

        public Def(V val){
            this.value = val;
        }
    }
    private static class Node<K, V>{
        public K key;
        public Def<V> defs;
        public Node<K, V> next;

        public Node(K key, V value, Node<K, V> next){
            this.key = key;
            this.defs = new Def<V>(value);
            this.next = next;
        }
    }
/*
    public static void main(String[] args) {
        Dict<String, String> t = new Dict<>();
        t.put("1", "hola");
        t.put("2", "bye");
        t.put("no", "si");
        t.put("1", "hola2");
        t.put("1", "ola prro");
        //t.remove("1");
        Iterator<String> itr = t.keyItr();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }
        System.out.println("----------");
        Iterator<ArrayList<String>>itr2 = t.valueItr();
        while(itr2.hasNext()){
            System.out.println(itr2.next());
        }
    }
*/
}
