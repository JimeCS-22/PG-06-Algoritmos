package ucr.algoritmos.pg06algoritmos.model.graph;

public class AdjacencyListGraph < T extends Comparable<T>> extends AdjacencyMatrixGraph{

    public AdjacencyListGraph(int n, boolean directed) {
        super(n,directed);
    }
}
