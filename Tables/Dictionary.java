package Tables;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Dictionary{
    DictionaryInterface<Long, Touple> table;

    public Dictionary() {
        table = new HashTableOpenAddressing<>();
    }
    public void add(long number, String item, double expense){
        if(!table.contains(number)){
            table.add(number, new Touple(null, -1.0));
            this.add(number, item, expense);
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

    /*private Touple getTouple(long number, String item){
        if(item == null) throw new NoSuchElementException("Parameters can't be null.");
        Touple tmp = table.getValue(number);
        if(tmp == null) throw new NoSuchElementException("Ivoice not found.");
        tmp = tmp.next;
        while(tmp != null){
            if(item.equals(tmp.item))
                return tmp;
            tmp = tmp.next;
        }
        throw new NoSuchElementException("Item or number not found.");
    }*/

    public double getExpense(long number, String item){
        if(item == null) throw new NoSuchElementException("Parameters can't be null.");
        Touple tmp = table.getValue(number);
        if(tmp == null) throw new NoSuchElementException("Ivoice not found.");
        tmp = tmp.next;
        while(tmp != null){
            if(item.equals(tmp.item))
                return tmp.expense;
            tmp = tmp.next;
        }
        throw new NoSuchElementException("Item or number not found.");
    }

    public Iterator<String> getItemsItr(long number){
        /*ArrayList<String> items = new ArrayList<>();
        Touple tmp = table.getValue(number);
        if(tmp == null) throw new NoSuchElementException("Ivoice not found.");
        tmp = tmp.next;
        while(tmp != null){
            items.add(tmp.item);
            tmp = tmp.next;
        }
        return items;*/
        if(!this.contains(number)) throw new NoSuchElementException("Ivoice not found.");
        return new itemsItr(number);
    }
    public Iterator<Double> getExpensesItr(long number){
        if(!this.contains(number)) throw new NoSuchElementException("Invoice not found.");
        return new expensesItr(number);
    }
    public double removeLast(long number){
    	if(!this.contains(number)) throw new NoSuchElementException("Invoice does not exist");
        Touple tmp = this.table.getValue(number);
        double expense = tmp.next.expense;
        tmp.next = tmp.next.next;

        if(tmp.next == null){
        	this.remove(number);
        }

        return expense;
    }
    public String toString(){
        return this.table.toString();
    }

    private static class Touple{
        public String item;
        public double expense;
        public Touple next;

        public Touple(String item, Double expense){
            this.item = item;
            this.expense = expense;
        }
        public String toString(){
            return this.item + "$" + this.expense + ", " + (this.next != null? this.next.toString():"");
        }
    }

    private class itemsItr implements Iterator<String>{
        Touple current;

        public itemsItr(long number){
            this.current = Dictionary.this.table.getValue(number);
        }
        public boolean hasNext(){
            return this.current.next != null;
        }
        public String next(){
            this.current = this.current.next;
            return this.current.item;
        }
    }

    private class expensesItr implements Iterator<Double>{
        Touple current;

        public expensesItr(long number){
            this.current = Dictionary.this.table.getValue(number);
        }
        public boolean hasNext(){
            return this.current.next != null;
        }
        public Double next(){
            this.current = this.current.next;
            return this.current.expense;
        }
    }

    /*
    private abstract class Itr<E> implements Iterator<E>{
        Iterator<Touple> itr;
        Touple current;

        public Itr(){
            this.itr = Dictionary.this.table.getValueIterator();
            if(this.itr.hasNext()){
                this.current = this.itr.next().next;
            }
        }
        public boolean hasNext(){
            return this.current.next != null || this.itr.hasNext();
        }
        public Touple nextTouple(){
            if(this.current.next != null){
                this.current = this.current.next;
            }
            else{
                this.current = this.itr.next().next;
            }
            return this.current;
        }
    }
    private class itemItr extends Itr<String>{
        public String next(){
            return this.nextTouple().item;
        }
    }
    private class expenseItr extends Itr<Double>{
        public Double next(){
            return this.nextTouple().expense;
        }
    }
    */
    /*
    public static void main(String[] args) {
        Dictionary pdb = new Dictionary();
        pdb.add(1, "x1.0", 1.0);
        pdb.add(2, "x2.0", 2.0);
        pdb.add(1, "x1.1", 1.1);
        pdb.add(1, "x1.2", 1.2);
        //pdb.remove(1);
        System.out.println();
        System.out.println(pdb.getExpenses(1));
        System.out.println(pdb.getItems(2));
    }
    */
}
