package DBase;

import AVL.AVLTree;
import Tables.DictionaryInterface;
import Tables.Dictionary;
import Tables.HashTableOpenAddressing;

import java.util.Iterator;
import java.util.List;

public class DataBase {
    DictionaryInterface<String, String> Table1;
    DictionaryInterface<String, AVLTree<InvoiceNode>> Table2;
    Dictionary Table3;
    //Tabla4;

    public DataBase(){
        Table1 = new HashTableOpenAddressing<String,String>();
        Table2 = new HashTableOpenAddressing<String, AVLTree<InvoiceNode>>();
        Table3 = new Dictionary();
    }

    public void newUser(String name, String address){
        Table1.add(name, address);
        Table2.add(name, new AVLTree<InvoiceNode>());
    }

    public void newInvoice(String name, long invoice){
        Table2.getValue(name).insert(new InvoiceNode(invoice, 0));
    }

    public void newExpense(String name, long invoice, String item, double expense){
        Table2.getValue(name).get(new InvoiceNode(invoice, 0)).expenses += expense; //TODO hacerlo bonito
        Table3.add(invoice, item, expense);
    }

    public void removeLastExpense(String name, long invoice){

    }

    public void removeInvoice(String name, long invoice){
        Table2.getValue(name).remove(/*---->*/new InvoiceNode(invoice,0)/*<----*/);//Esto essta asqueroso
        Table3.remove(invoice);
    }

    public void removeUser(String name){
        Table1.remove(name);
        Table2.remove(name);
        //Remover cada invpoice de la tabla3 que pertenezca a este name
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        Iterator<String> itrName = this.Table1.getKeyIterator();
        Iterator<InvoiceNode> itrInvoice;
        Iterator<String> items;
        Iterator<Double> expenses;
        String name;
        InvoiceNode invoiceN;
        while(itrName.hasNext()){
            name = itrName.next();
            sb.append(name + ": " + this.Table1.getValue(name) + "\n");
            itrInvoice = this.Table2.getValue(name).getItr();
            while(itrInvoice.hasNext()){
                invoiceN = itrInvoice.next();
                sb.append("\t" + invoiceN.invoice + " : $" + invoiceN.expenses + "\n");
                items = this.Table3.getItemsItr(invoiceN.invoice);
                expenses = this.Table3.getExpensesItr(invoiceN.invoice);
                while(items.hasNext() && expenses.hasNext()){
                    sb.append("\t\t" + items.next() + " : $" + expenses.next() + "\n");
                }
            }
        }
        return sb.toString();
        /*return this.Table1.toString() + "\n" +
            this.Table2.toString() + "\n" +
            this.Table3.toString();
        */
    }

    private class InvoiceNode implements Comparable<InvoiceNode>{
        public long invoice;
        public double expenses;

        public InvoiceNode(long invoice, double expenses){
            this.invoice = invoice;
            this.expenses = expenses;
        }

        @Override
        public int compareTo(InvoiceNode node) {
            return (int) (this.invoice - node.invoice);
        }
    }

    public static void main(String[] args) {
        DataBase db = new DataBase();
        db.newUser("Andres", "mi casa");
        db.newInvoice("Andres", 123l);
        db.newExpense("Andres", 123l, "asd", 45.5);
        db.newUser("Miguel", "su casa");
        db.newInvoice("Miguel", 1234l);
        db.newExpense("Miguel", 1234l, "asd", 45.5);
        System.out.println(db);
    }
}
