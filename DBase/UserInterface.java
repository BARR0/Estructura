package DBase;

import java.util.Scanner;

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
    public double totalEarnings(){
        return this.db.totalEarnings();
    }
    public double compareUsers(String name1, String name2){
        if(name1 == null || name2 == null || !this.db.containsUser(name1) || !this.db.containsUser(name2))
            throw new IllegalArgumentException("Null parameters or a user doesn't exists.");
        return this.db.compareUsers(name1, name2);
    }
    public String toString(){
        return this.db.toString();
    }
    public static void main(String[] args) {
        final String menu =
            "Type:\n"
            + "\tadduser (name) (address)\n"
            + "\taddinvoice (name)\n"
            + "\taddexpense (name) (invoice) (item) (expense)\n"
            + "\tremoveuser (name)\n"
            + "\tremoveinvoice (name) (invoice)\n"
            + "\tremovelastexpense (name) (invoice)\n"
            + "\ttotalexpenses (name)\n"
            + "\ttotalpayments (name)\n"
            + "\ttotalearnings\n"
            + "\tcompareusers (name1) (name2)\n"
            + "\tprint\n"
            + "\texit\n"
        ;
        Scanner sc = new Scanner(System.in);
        String[] input = {"-1"};
        UserInterface db = new UserInterface();
        while(!input[0].equals("exit")){
            System.out.println(menu);
            input = sc.nextLine().split(" ");
            System.out.println();
            try{
                switch(input[0]){
                    case "adduser":
                        db.addUser(input[1], input[2]);
                        break;
                    case "addinvoice":
                        System.out.println("New invoice: " + db.addInvoice(input[1]));
                        break;
                    case "addexpense":
                        db.addExpense(input[1], Long.parseLong(input[2]), input[3], Double.parseDouble(input[4]));
                        break;
                    case "removeuser":
                        db.removeUser(input[1]);
                        break;
                    case "removeinvoice":
                        db.removeInvoice(input[1], Long.parseLong(input[2]));
                        break;
                    case "removelastexpense":
                        db.removeLastExpense(input[1], Long.parseLong(input[2]));
                        break;
                    case "totalexpenses":
                        System.out.println(db.totalExpenses(input[1]));
                        break;
                    case "totalpayments":
                        System.out.println(db.totalPayments(input[1]));
                        break;
                    case "totalearnings":
                        System.out.println(db.totalEarnings());
                        break;
                    case "compareusers":
                        System.out.println(db.compareUsers(input[1], input[2]));
                        break;
                    case "print":
                        System.out.println(db);
                        break;
                    case "exit":
                        break;
                    default:
                        System.out.println("Option not valid");
                        break;
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Option not recognized.\n");
            }
        }
    }
}
