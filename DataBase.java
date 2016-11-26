
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

	public void newInvoice(String name, Long invoice){
		Table2.getValue(name).insert(new InvoiceNode(invoice, 0));
	}

	public void newExpense(String name, Long invoice, String item, double expense){
		Table2.getValue(name).get(new InvoiceNode(invoice, 0)).expenses += expense; //TODO hacerlo bonito
		Table3.add(invoice, item, expense);
	}

	private class InvoiceNode implements Comparable<InvoiceNode>{
		public Long invoice;
		public double expenses;

		public InvoiceNode(Long invoice, double expenses){
			this.invoice = invoice;
			this.expenses = expenses;
		}

		@Override
		public int compareTo(InvoiceNode node) {
			return (int) (this.invoice - node.invoice);
		}
	}

    public void removeLastExpense(String name, Long invoice){

    }

    public void removeInvoice(String name, Long invoice){
        Table2.getValue(name).remove(/*---->*/new InvoiceNode(invoice,0)/*<----*/);//Esto essta asqueroso
        Table3.remove(invoice);
    }

    public void removeUser(String name){
        Table1.remove(name);
        Table2.remove(name);
        //Remover cada invpoice de la tabla3 que pertenezca a este name
    }

	public static void main(String[] args) {
	}
}
