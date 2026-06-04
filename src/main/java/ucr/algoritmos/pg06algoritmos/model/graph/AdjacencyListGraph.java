package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

public class AdjacencyListGraph<T extends Comparable<T>> extends AdjacencyMatrixGraph<T> {

    public AdjacencyListGraph(int n, boolean directed){
    super(n,directed);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        return super.containsEdge(a, b);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        super.addEdge(a, b);
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        super.addWeight(a, b, weight);
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");
        if(!containsEdge(a,b)) {
            Vertex<T> vertexA = getVertex(a);
            Vertex<T> vertexB = getVertex(b);

        }
    }

    private Vertex<T> getVertex(T element) {
        for (int i = 0; i < counter; i++) {
            if (equals(vertexList[i].data, element)) {
                return vertexList[i];
            }
        }
        return null;//no existe el vértice
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        super.removeEdge(a, b);
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        super.removeVertex(element);
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        return super.dfs();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        return super.bfs();
    }
}
