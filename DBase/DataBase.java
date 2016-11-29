package DBase;

import AVL.AVLTree;
import Tables.DictionaryInterface;
import Tables.Dictionary;
import Tables.HashTableOpenAddressing;
import Graph.Graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DataBase {
    DictionaryInterface<String, String> Table1;
    DictionaryInterface<String, AVLTree<InvoiceNode>> Table2;
    Dictionary Table3;
    Graph Table4;

    public DataBase(){
        Table1 = new HashTableOpenAddressing<String,String>();
        Table2 = new HashTableOpenAddressing<String, AVLTree<InvoiceNode>>();
        Table3 = new Dictionary();
        Table4 = new Graph();
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
        Table4.removeVertexUndirected(name);
    }

    public void removeLastExpense(String name, long invoice){
    	Table2.getValue(name).get(new InvoiceNode(invoice, 0)).expenses -= Table3.removeLast(invoice);
    	Table4.removeVertexUndirected(name);
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

    //Returns the abosulte difference of the expenses of two users

    public double compareUsers(String user1Name, String user2Name){
    	if(this.Table1.contains(user1Name) && this.Table1.contains(user2Name)){
            if(this.Table4.isAdjacent(user1Name, user2Name)){
            	return this.Table4.getCost(user1Name, user2Name);
            }

    		//Here we calulate the difference between the users' expenses in absolute value
    		double expensesDifference = this.getTotalExpenses(user1Name) - this.getTotalExpenses(user2Name);
    		if(expensesDifference < 0){
    			expensesDifference *= -1.0;
    		}

    		//Now we add that relation in the graph, the expense difference is the weight
    		this.Table4.addEdgeUndirected(user1Name, user2Name, expensesDifference);
            return expensesDifference;
    	}
        throw new NoSuchElementException("One of the users wasn't found.");
    }

    //Agregar una forma de calcular todos los expenses de un usario
    public double getTotalExpenses(String userName){
    	//Aun no se si esto funciona
    	if(this.Table2.contains(userName)){
    		Iterator<InvoiceNode> itr = this.Table2.getValue(userName).getItr();
            double total = 0.0;
            while(itr.hasNext()){
                total += itr.next().expenses;
            }
            return total;
    	}
    	return 0;
    }

    //Calcula cuanto cuesta el total de items en el invoice
    public double getInvoicePayments(long invoice){
    	double payments = 0;
    	if(this.Table3.contains(invoice)){

    		Iterator<Double> expenses = this.Table3.getExpensesItr(invoice);
        	while(expenses.hasNext()){
        		payments += expenses.next();
        	}
    	}
    	return payments;
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
        
        db.newUser("Jose", "MiCasa2");
        db.newInvoice("Jose", 431l);
        db.newInvoice("Jose", 5632l);
        db.newExpense("Jose", 431l, "Atún", 12.0);
        db.newExpense("Jose", 431l, "Huevo", 60.0);
        db.newExpense("Jose", 431l, "Coca", 23.0);
        db.newExpense("Jose", 431l, "Gansito", 13.0);
        db.newExpense("Jose", 5632l, "Overwatch", 1000.0 );
        
        System.out.println(db);
        
        db.removeLastExpense("Jose", 431l);
        
        System.out.println(db);
    }
}
