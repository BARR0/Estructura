package Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Graph {
    public static final double INFINITY = Double.MAX_VALUE;
    private Map<String, Vertex> vertices;

    public Graph(){
        this.vertices = new HashMap<>();
    }
    private Vertex getVertex(String name){
    	if(name == null){
    		throw new NullPointerException("Can´t create a null vertex");
    	}
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
    
    public boolean contains(String vertex){
    	return this.getVertex(vertex) != null;
    }
    
    public boolean isAdjacent(String vertex1, String vertex2){
    	if(this.contains(vertex1) && this.contains(vertex2)){
    		for (Edge e : this.getVertex(vertex1).adjacency) {
				if(e.end == this.getVertex(vertex2)){
					return true;
				}
			}
    		return false;
    	}
    	throw new NoSuchElementException("One of the vertex not found");
    }
    
    public double getCost(String vertex1, String vertex2){
    	for (Edge e : this.getVertex(vertex1).adjacency) {
			if(e.end == this.getVertex(vertex2)){
				return e.cost;
			}
		}
    	return -1.0;
    }
    
    private double removeEdge(Vertex vertex1, Vertex vertex2){
    	List<Edge> adjVertex1 = vertex1.adjacency;
    	for (Edge e : adjVertex1) {
			if(e.end == vertex2){
				double cost = e.cost;
				adjVertex1.remove(e);
				return cost;
			}
		}
		return -1.0;
    }
    
    public void removeEdgeUndirected(String vertex1, String vertex2){
    	Vertex Vertex1 = this.getVertex(vertex1);
    	Vertex Vertex2 = this.getVertex(vertex2);
    	this.removeEdge(Vertex1, Vertex2);
    	this.removeEdge(Vertex2, Vertex1);
    }
    
    public void removeVertexUndirected(String vertex){
    	Vertex Vertex1 = this.getVertex(vertex);
    	for(Edge e : Vertex1.adjacency){
    		this.removeEdge(Vertex1, e.end);
    		this.removeEdge(e.end, Vertex1);
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
