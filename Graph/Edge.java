package Graph;

public class Edge {
    public Vertex end;
    public double cost;

    public Edge(Vertex v, double c){
        this.end = v;
        this.cost = c;
    }
    
    public boolean equals(Object o){
    	return this == o;
    }
}
