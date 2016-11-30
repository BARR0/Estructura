package DBase;

public class UserInterface{
    private DataBase db;
    private long nextInvoice;

    public UserInterface(){
        this.db = new DataBase();
        this.nextInvoice = 0l;
    }
    public void addUser(String name, String address){
        if(name == null || address == null || this.db.containsUser(name)){
            throw new IllegalArgumentException("Null parameters or user already exists.");
        }
        this.db.newUser(name, address);
    }
    public long addInvoice(String name){
        if(name == null || !this.db.containsUser(name)) throw new IllegalArgumentException("Null parameters or user doesn't exists.");
        this.db.newInvoice(name, this.nextInvoice);
        return this.nextInvoice++;
    }
    public void addExpense(String name, long invoice, String item, double expense){
        if(name == null || item == null || expense < 0.0 || !this.db.containsInvoice(name, invoice)){
            throw new IllegalArgumentException("Null parameters, negative expense, or invoice doesn't exists.");
        }
        this.db.newExpense(name, invoice, item, expense);
    }
    public void removeLastExpense(String name, long invoice){
        if(name == null || !this.db.containsInvoice(name, invoice))
            throw new IllegalArgumentException("Null parameters, or invoice doesn't exists.");
        this.db.removeLastExpense(name, invoice);
    }
    public void removeInvoice(String name, long invoice){
        if(name == null || !this.db.containsInvoice(name, invoice))
            throw new IllegalArgumentException("Null parameters, or invoice doesn't exists.");
        this.db.removeInvoice(name, invoice);
    }
    public void removeUser(String name){
        if(name == null || !this.db.containsUser(name))
            throw new IllegalArgumentException("Null parameters or user doesn't exists.");
        this.db.removeUser(name);
    }
    public String totalExpenses(String name){
        if(name == null || !this.db.containsUser(name))
            throw new IllegalArgumentException("Null parameters or user doesn't exists.");
        return this.db.totalExpenses(name);
    }
    public String totalPayments(String name){
        if(name == null || !this.db.containsUser(name))
            throw new IllegalArgumentException("Null parameters or user doesn't exists.");
        return this.db.totalPayments(name);
    }
    public double totalEarning(){
        return this.db.totalEarning();
    }
    public double compareUsers(String name1, String name2){
        if(name1 == null || name2 == null || !this.db.containsUser(name1) || !this.db.containsUser(name2))
            throw new IllegalArgumentException("Null parameters or a user doesn't exists.");
        return this.db.compareUsers(name1, name2);
    }
    public static void main(String[] args) {
        
    }
}
