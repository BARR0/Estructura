package HashTable;

import java.util.Iterator;

public interface DictionaryInterface<Key, Value>{
    public Value add(Key k, Value Item);
    public Value remove(Key k);
    public Value getValue(Key k);
    public boolean contains(Key k);
    public Iterator<Key> getKeyIterator();
    public Iterator<Value> getValueIterator();
    public boolean isEmpty();
    public int getSize ();
    public void clear();
}
