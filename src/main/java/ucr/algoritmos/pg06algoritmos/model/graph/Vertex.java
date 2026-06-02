package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Node;

public class Vertex<T> {
    public T data;
    public Node<T> headNode; //Sirve para crear la lista enlazada de aristas y pesos
    public boolean visited; // Sirve para los recorridos DFS y BFS

    public Vertex(T data) {
        this.data = data;
        this.headNode = null;
    }
}
