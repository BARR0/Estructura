package Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Graph {
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String, Vertex> vertices;

    public Graph(){
        this.vertices = new HashMap<>();
    }
    private Vertex getVertex(String name){
        if(this.vertices.containsKey(name)){
            return this.vertices.get(name);
        }
        Vertex tmp = new Vertex(name);
        this.vertices.put(name, tmp);
        return tmp;
    }
    public void addEdge(String origin, String end, double cost){
        Vertex v = this.getVertex(origin),
               w = this.getVertex(end);
        v.adjacency.add(new Edge(w, cost));
    }
    public void resetAll(){
        for(Vertex v:this.vertices.values()){
            v.restart();
        }
    }
    public String breadthFirst(String origin){
        StringBuilder st = new StringBuilder();
        st.append(origin + "\n");
        this.resetAll();
        Vertex s = this.getVertex(origin);
        s.check = 1;
        s.distance = 0.0;
        s.prev = null;
        Queue<Vertex> Q = new LinkedList<>();
        Q.add(s);
        Vertex u;
        while(!Q.isEmpty()){
            u = Q.poll();
            for(Edge v:u.adjacency){
                if(v.end.check == 0){
                    st.append(v.end.name + "\n");
                    v.end.check = 1;
                    v.end.distance = u.distance + 1;
                    Q.add(v.end);
                }
            }
            u.check = 2;
        }
        return st.toString();
    }
    public String depthFirst(String origin){
        this.resetAll();
        StringBuilder tmp = new StringBuilder();
        this.depthFirst(this.getVertex(origin), 0, tmp);
        return tmp.toString();
    }
    private int depthFirst(Vertex origin, int time, StringBuilder st){
        st.append(origin.name + "\n");
        origin.distance = ++time;
        origin.check = 1;
        for(Edge v:origin.adjacency){
            if(v.end.check == 0){
                v.end.prev = origin;
                time = this.depthFirst(v.end, time, st);
            }
        }
        origin.check = 2;
        ++time;
        //f?
        return time;
    }
    public void addEdgeUndirected(String a, String b, double cost){
        this.addEdge(a, b, cost);
        this.addEdge(b, a, cost);
    }
    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdgeUndirected("s", "a", 0);
        g.addEdgeUndirected("s", "b", 0);
        g.addEdgeUndirected("s", "c", 0);
        g.addEdgeUndirected("a", "d", 0);
        g.addEdgeUndirected("b", "d", 0);
        g.addEdgeUndirected("c", "d", 0);
        g.addEdgeUndirected("0", "1", 0);
        System.out.println(g.depthFirst("a"));
        System.out.println(g.breadthFirst("s"));
    }
}
