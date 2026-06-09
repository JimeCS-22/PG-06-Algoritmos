package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Queue.LinkedQueue;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.LinkedStack;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentMap;

public class LinkedGraph <T extends Comparable<T>> extends LinkedList<T> implements Graph<T> {
    //atributos para los recorridos dfs, bfs
    public LinkedStack<Integer> stack;
    public LinkedQueue<Integer> queue;

    public LinkedGraph(boolean directed){
        super();
        stack = new LinkedStack<>();
        queue = new LinkedQueue<>();
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
        if(isEmpty()) add(element); //si la lista enlazada está vacia agregue el elemento
        else if (!contains(element))add(element);
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
