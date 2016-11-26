package Tables;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Dictionary{
    DictionaryInterface<Long, Touple> table;

    public Dictionary() {
        table = new HashTableOpenAddressing<>();
    }
    public void add(long number, String item, double expense){
        if(!table.contains(number)){
            table.add(number, new Touple(item, expense));
        }
        else{
            Touple tmp = table.getValue(number);
            Touple toAdd = new Touple(item, expense);
            toAdd.next = tmp.next;
            tmp.next = toAdd;
        }
    }
    public void remove(long number){
        table.remove(number);
    }
    public boolean contains(long number){
        return table.contains(number);
    }
    public boolean isEmpty(){
        return table.isEmpty();
    }
    public int getSize(){
        return table.getSize();
    }
    public void clear(){
        table.clear();
    }

    private Touple getTouple(long number, String item){
        if(item == null) throw new NoSuchElementException("Parameters can't be null.");
        Touple tmp = table.getValue(number);
        while(tmp != null){
            if(item.equals(tmp.item))
                return tmp;
            tmp = tmp.next;
        }
        throw new NoSuchElementException("Item or number not found.");
    }

    public double getExpense(long number, String item){
        return this.getTouple(number, item).expense;
    }
    
    public List<String> getItems(long number){
        List<String> items = new ArrayList<>();
        Touple tmp = table.getValue(number);
        while(tmp != null){
            items.add(tmp.item);
            tmp = tmp.next;
        }
        return items;
    }
    public List<Double> getExpenses(long number){
        List<Double> items = new ArrayList<>();
        Touple tmp = table.getValue(number);
        while(tmp != null){
            items.add(tmp.expense);
            tmp = tmp.next;
        }
        return items;
    }

    private static class Touple{
        public String item;
        public double expense;
        public Touple next;

        public Touple(String item, Double expense){
            this.item = item;
            this.expense = expense;
        }
    }

    public static void main(String[] args) {
        Dictionary pdb = new Dictionary();
        pdb.add(1, "x1.0", 1.0);
        pdb.add(2, "x2.0", 2.0);
        pdb.add(1, "x1.1", 1.1);
        pdb.add(2, "x1.2", 1.2);
    }
}