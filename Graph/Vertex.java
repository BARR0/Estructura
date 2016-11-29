package Graph;

import java.util.LinkedList;
import java.util.List;

public class Vertex {
    public String name;
    public List<Edge> adjacency;
    public int check;
    public double distance;
    public Vertex prev;

    public Vertex(String name){
        this.name = name;
        this.adjacency = new LinkedList<>();
        this.check = 0;
        this.distance = Graph.INFINITY;
        this.prev = null;
    }
    public void restart(){
        this.check = 0;
        this.distance = Graph.INFINITY;
        this.prev = null;
    }
}
