package DBase;

import AVL.AVLTree;
import Tables.DictionaryInterface;
import Tables.Dictionary;
import Tables.HashTableOpenAddressing;
import Graph.Graph;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * DataBase is a class that has three HashTables and one Graph as parameters.
 * Its purpose is to store users, invoices for each user and expenses for each income.
 * It has control of insertion and deletion of each table and can search for elements on each level by the primary ID.
 * @author JOSECARLOS
 *
 */
public class DataBase {
    private DictionaryInterface<String, String> Table1;
    private DictionaryInterface<String, AVLTree<InvoiceNode>> Table2;
    private Dictionary Table3;
    private Graph Table4;

    /**
     * The constructor of the DataBase initializes the three tables and the graph.
     * All of the parameters use the default constructor, this means that each one of them initializes with 0 elements.
     */
    public DataBase(){
        Table1 = new HashTableOpenAddressing<String,String>();
        Table2 = new HashTableOpenAddressing<String, AVLTree<InvoiceNode>>();
        Table3 = new Dictionary();
        Table4 = new Graph();
    }

    //falta TODO
    public boolean containsUser(String name){
        return this.Table1.contains(name);
    }
    //falta TODO
    public boolean containsInvoice(String name, long invoice){
        return this.Table1.contains(name) && Table2.getValue(name).contains(new InvoiceNode(invoice, 0));
    }
    /**
     * It adds a new user in the DataBase.
     * Inserts a new element in table of addresses with key(String:name) and value (String:Address).
     * Inserts a new element in table of invoices with key(String:name) and value(new AVLTree).
     * @param name - String : Name of the new user.
     * @param address - String : Address of the new user.
     */

    public void newUser(String name, String address){
        Table1.add(name, address);
        Table2.add(name, new AVLTree<InvoiceNode>());
    }

    /**
     * Adds a new invoice for and already registered user.
     * Inserts a new invoice in table of invoices with key(String:name) and value(new InvoiceNode).
     * @param name - String : Name of the already registered user.
     * @param invoice - long : number of the new invoice.
     */
    public void newInvoice(String name, long invoice){
        Table2.getValue(name).insert(new InvoiceNode(invoice, 0));
    }

    /**
     * Adds a new expense for a user and an invoice.
     * Updates the value of the InvoiceNode(total invoice expenses) in table of invoices.
     * Inserts a new expense in table of expenses with key(long:invoice) and value(double:expense).
     * @param name - String :  Name of the already registered user who makes the expense.
     * @param invoice - long : Number of the already existing invoice.
     * @param item - String : Name of the item.
     * @param expense - double : Cost of the item.
     */
    public void newExpense(String name, long invoice, String item, double expense){
        Table2.getValue(name).get(new InvoiceNode(invoice, 0)).expenses += expense;
        Table3.add(invoice, item, expense);
        Table4.removeVertexUndirected(name);
    }

    /**
     * Removes a last expense realized by an user and an invoice.
     * Updates the value of the InvoiceNode(total invoice expenses) in table of invoices.
     * Deletes the last expense added in table of expenses with key(long:invoice).
     * @param name - String : Name of the already registered user.
     * @param invoice - long : Number of the already existing invoice.
     */
    public void removeLastExpense(String name, long invoice){
    	Table2.getValue(name).get(new InvoiceNode(invoice, 0)).expenses -= Table3.removeLast(invoice);
    	Table4.removeVertexUndirected(name);
    }

    /**
     * Removes a selected invoice from a user.
     * Deletes an InvoiceNode from table of invoices with key(String:name) and value(long:invoice).
     * Deletes an invoice from table of expenses with key(long:invoice).
     * @param name - String : Name of the already registered user.
     * @param invoice - long : Number of the already existing invoice that is going to be removed.
     */
    public void removeInvoice(String name, long invoice){
        Table2.getValue(name).remove(new InvoiceNode(invoice, 0));
        Table3.remove(invoice);
    }

    /**
     * Removes an user from the DataBase.
     * Deletes a user from table of addresses with key(String:name).
     * Deletes all expenses from that user in table of expenses.
     * Deletes all invoices from that user in table of invoices.
     * @param name - String : Name of the already registered user that is going to be removed.
     */
    public void removeUser(String name){
    	Table1.remove(name);
		Iterator<InvoiceNode> itr = this.Table2.getValue(name).getItr();
        while(itr.hasNext()){
            this.Table3.remove(itr.next().invoice);
        }
        Table2.remove(name);
        Table4.removeVertexUndirected(name);
    }

    /**
     * Calculates the difference of total expenses of two users.
     * Adds a connection in graph of relations between vertexA(String:user1) and vertexB(String:user2) with and edge with the difference of the total expenses of users.
     * The graph memorizes the relation, if the relation already exist the calculation will not be calculated again.
     * If the total expenses of one of the users changed the relation will be calculated again and then update the value in the graph of relations.
     * @param user1Name - String : Name of an already registered user.
     * @param user2Name - String : Name of another already registered user.
     * @return - double : Absolute difference of the total expenses of two users.
     */
    public double compareUsers(String user1Name, String user2Name){
    	if(this.Table1.contains(user1Name) && this.Table1.contains(user2Name)){
            if(this.Table4.contains(user1Name) && this.Table4.contains(user2Name) && this.Table4.isAdjacent(user1Name, user2Name)){
            	System.out.println("Gotten from the graph");
            	return this.Table4.getCost(user1Name, user2Name);
            }

    		//Here we calculate the difference between the users' expenses in absolute value
    		double expensesDifference = this.getTotalExpenses(user1Name) - this.getTotalExpenses(user2Name);
    		if(expensesDifference < 0){
    			expensesDifference *= -1.0;
    		}

    		//Now we add that relation in the graph, the expense difference is the weight
    		this.Table4.addEdgeUndirected(user1Name, user2Name, expensesDifference);
    		System.out.println("Recalculted");
            return expensesDifference;
    	}
        throw new NoSuchElementException("One of the users wasn't found.");
    }

   /**
    * Calculates the total expenses of an already registered user.
    * Iterates all the InvoiceNodes in the value of table of invoices with key(String:user).
    * @param userName - String : Name of the already registered user.
    * @return - double : Sum of the total payments of each invoice of the user.
    */
    public double getTotalExpenses(String userName){
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

    /**
     * Calculates the total payments of an already existing invoice.
     * Iterates all the payments in the value of the table of expenses with key(long:invoice).
     * @param invoice - long : Number of an already existing invoice.
     * @return - double : Sum of the total payments of the given invoice.
     */
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

    /**
     * Calculates the total earnings of the DataBase.
     * Iterates all the users in table of invoices and gets sums their total expenses.
     * @return - double : The total earnings registered in the DataBase.
     */
    public double totalEarnings(){
        double earnings = 0;
        Iterator<String> itrName = this.Table1.getKeyIterator();
        while(itrName.hasNext()){
            earnings += this.getTotalExpenses(itrName.next());
        }
        return earnings;
    }

    /**
     * Collects all the items and costs in each invoice of the user.
     * Iterates all the InvoiceNodes in the value of table of invoices with key(String:user).
     * Iterates all the expenses in the value of table of expenses in each invoice.
     * @param userName - String : Name of the already registered user.
     * @return - String : List of all the items and costs of the given user.
     */
     public String totalExpenses(String name){
         StringBuilder sb = new StringBuilder();
         Iterator<InvoiceNode> itrInvoice;
         Iterator<String> items;
         Iterator<Double> expenses;
         InvoiceNode invoiceN;
         sb.append(name + ": " + this.Table1.getValue(name) + "\n");
         itrInvoice = this.Table2.getValue(name).getItr();
         while(itrInvoice.hasNext()){
             invoiceN = itrInvoice.next();
             if(this.Table3.contains(invoiceN.invoice)){
                 items = this.Table3.getItemsItr(invoiceN.invoice);
                 expenses = this.Table3.getExpensesItr(invoiceN.invoice);
                 while(items.hasNext() && expenses.hasNext()){
                     sb.append("\t" + items.next() + " : $" + expenses.next() + "\n");
                 }
             }
         }
         return sb.toString();
     }

    /**
     * Collects all the invoices and total payments of the user in a listed string.
     * Iterates all the InvoiceNodes in the value of table of invoices with key(String:user).
     * @param name - String : Name of the already registered user.
     * @return - String : List of all the invoices and total payments for each node of the user.
     */
    public String totalPayments(String name){
        StringBuilder sb = new StringBuilder();
        Iterator<InvoiceNode> itrInvoice;
        InvoiceNode invoiceN;
        sb.append(name + ": " + this.Table1.getValue(name) + "\n");
        itrInvoice = this.Table2.getValue(name).getItr();
        while(itrInvoice.hasNext()){
            invoiceN = itrInvoice.next();
            sb.append("\t" + invoiceN.invoice + " : $" + invoiceN.expenses + "\n");
        }
        return sb.toString();
    }

    /**
     * Looks for all the connected users with the given user.
     * @param source - String : Name of the already registered user.
     * @return - String : The names of all the connected users with the given user.
     */
    public String getRelations(String source){
    	if(!Table1.contains(source)){
    		return "";
    	}
    	return this.Table4.breadthFirst(source);
    }

    /**
     * Looks for all values in each table and connects it in a leveled string.
     * @return - String : All the DataBase showed by levels.
     */

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
                if(this.Table3.contains(invoiceN.invoice)){
                    items = this.Table3.getItemsItr(invoiceN.invoice);
                    expenses = this.Table3.getExpensesItr(invoiceN.invoice);
                    while(items.hasNext() && expenses.hasNext()){
                        sb.append("\t\t" + items.next() + " : $" + expenses.next() + "\n");
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * InvoiceNode is used as a value for table of invoices.
     * Its purpose is to let the table of invoices store a root for an AVLTre so that it can store the connection of all the invoices related to that user.
     * @author JOSECARLOS
     *
     */
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
        db.newExpense("Jose", 431l, "At�n", 12.0);
        db.newExpense("Jose", 431l, "Huevo", 60.0);
        db.newExpense("Jose", 431l, "Coca", 23.0);
        db.newExpense("Jose", 431l, "Gansito", 13.0);
        db.newExpense("Jose", 5632l, "Overwatch", 1000.0 );

        System.out.println(db);

        db.removeLastExpense("Jose", 431l);

        System.out.println(db);
        System.out.println("--------------------");
        System.out.println(db.compareUsers("Jose", "Andres"));
        System.out.println(db.compareUsers("Miguel", "Andres"));
        System.out.println(db.compareUsers("Miguel", "Jose"));
        System.out.println(db.getRelations("Jose"));
        db.removeUser("Miguel");
        System.out.println(db.getRelations("Miguel"));
        System.out.println(db.compareUsers("Andres", "Jose"));
    }
}
