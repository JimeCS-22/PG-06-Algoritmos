package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Queue.MyQueue;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

public class AdjacencyMatrixGraph<T extends Comparable<T>> implements Graph<T> {
    @Override
    public int size() throws ListException {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        return false;
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        return false;
    }

    @Override
    public void addVertex(T element) throws GraphException, ListException {

    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {

    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {

    }

    @Override
    public void addEdgeAndWeight(T a, T b, T c) throws GraphException, ListException {

    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {

    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {

    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        return "";
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        return "";
    }
}
