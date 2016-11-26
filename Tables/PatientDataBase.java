import HashTable.*;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PatientDataBase{
    DictionaryInterface<String, PatientRecord> table;

    public PatientDataBase() {
        table = new MyHashTable<>();
    }
    public void add(String name, String date, String reason, String treatment){
        if(!table.contains(name)){
            table.add(name, new PatientRecord(date, reason, treatment));
        }
        else{
            PatientRecord tmp = table.getValue(name);
            PatientRecord toAdd = new PatientRecord(date, reason, treatment);
            toAdd.next = tmp.next;
            tmp.next = toAdd;
        }
    }
    public void remove(String name){
        table.remove(name);
    }
    public boolean contains(String name){
        return table.contains(name);
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

    private PatientRecord getRecord(String name, String date){
        if(name == null || date == null) throw new NoSuchElementException("Parameters can't be null.");
        PatientRecord tmp = table.getValue(name);
        while(tmp != null){
            if(date.equals(tmp.date))
                return tmp;
            tmp = tmp.next;
        }
        throw new NoSuchElementException("Date or name not found.");
    }
    public String getReason(String name, String date){
        return this.getRecord(name, date).reason;
    }
    public String getTreatment(String name, String date){
        return this.getRecord(name, date).treatment;
    }
    public List<String> getDates(String name){
        if(name == null) throw new NullPointerException("null argument");
        List<String> dates = new ArrayList<>();
        PatientRecord tmp = table.getValue(name);
        while(tmp != null){
            dates.add(tmp.date);
            tmp = tmp.next;
        }
        return dates;
    }

    private static class PatientRecord{
        public String date,
                      reason,
                      treatment;
        public PatientRecord next;

        public PatientRecord(String date, String reason, String treatment){
            this.date = date;
            this.reason = reason;
            this.treatment = treatment;
        }
    }

    /*public static void main(String[] args) {
        PatientDataBase pdb = new PatientDataBase();
        pdb.add("a", "a1", "a2", "a3");
        pdb.add("a", "a4", "a5", "a6");
        pdb.add("a", "a7", "a8", "a9");
        pdb.add("b", "b1", "b2", "b3");
        pdb.add("c", "c1", "c2", "c3");
        pdb.add("c", "c4", "c5", "c6");
        pdb.add("d", "d1", "d2", "d3");
        System.out.println(pdb.getReason("a", "a7") + " : " + pdb.getTreatment("a", "a7"));
        System.out.println(pdb.getReason("b", "b1") + " : " + pdb.getTreatment("b", "b1"));
        System.out.println(pdb.getReason("c", "c4") + " : " + pdb.getTreatment("c", "c4"));
        System.out.println(pdb.getReason("d", "d1") + " : " + pdb.getTreatment("d", "d1"));
        System.out.println();
        System.out.println(pdb.getDates("a"));
        System.out.println(pdb.getDates("b"));
        System.out.println(pdb.getDates("c"));
        System.out.println(pdb.getDates("t"));
    }*/
}