package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Node;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

public class AdjacencyListGraph<T extends Comparable<T>> extends AdjacencyMatrixGraph<T> {

    public AdjacencyListGraph(int n, boolean directed) {
        super(n, directed);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");
        Vertex<T> vertexA = getVertex(a);
        boolean getVertexA = getNodeNeighbor(vertexA.headNode, b)!=null;
        boolean getVertexB = false;
        if(!directed) {
            Vertex<T> vertexB = getVertex(b);
            getVertexB = getNodeNeighbor(vertexB.headNode, a) != null;
        }
        return !directed ? getVertexA && getVertexB : getVertexA;
    }

    private Node<T> getNodeNeighbor(Node<T> headNode, T element) {
        if(headNode==null) return null;
        Node<T> aux = headNode;
        while(aux!=null){
            if(aux.data.compareTo(element)==0) return aux;
            aux = aux.neighbor; //movemos aux al sgte nodo vecino
        }
        return null; //si llega aquí, no encontró el nodo
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = addNeighbor(vertexA.headNode, b, null);
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode =  addNeighbor(vertexB.headNode, a, null);
            }
        }
    }

    private Node<T> addNeighbor(Node<T> headNode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if(headNode == null)
            headNode = node;
        else{
            Node<T> aux = headNode;
            //me muevo por la lista hasta el ult nodo
            while(aux.neighbor != null)
                aux = aux.neighbor; //se mueve al sgte nodo vecino
            //se sale cuando aux.neighbor es nulo
            aux.neighbor = node; //entonces conectados el nodo al final
        }
        return headNode; //si llegó nulo, lo devuelve con un nodo
    }

    private Vertex<T> getVertex(T element) {
        for (int i = 0; i < counter; i++)
            if(equals(vertexList[i].data, element)) return vertexList[i];
        return null; //no existe el vértice
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        super.addWeight(a, b, weight);
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = addNeighbor(vertexA.headNode, b, weight);
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode =  addNeighbor(vertexB.headNode, a, weight);
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        super.removeVertex(element);
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        super.removeEdge(a, b);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|——————| |Adjacency List Graph|——————| |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("※※※※※※Graph Type: ").append(graphType).append("\n");
        //mostramos todos los vértices
        for (int i = 0; i < counter; i++) {
            sb.append("\nThe vertex in position [").append(i).append("] is: ")
                    .append(vertexList[i].data);
        }
        //mostramos la información de aristas y pesos
        for (int i = 0; i < counter; i++) {
            sb.append("\n( ").append(i).append(" )----Vertex [ ").append(getVertexByIndex(i).data).append(" ]");
            Node<T> aux = getVertexByIndex(i).headNode; //llamanos al inicio de la lista enlazada del nodo
            while(aux!=null){
                sb.append("\n※※※ Edge: ").append(aux.data).append(", weight: ").append(aux.weight);
                aux = aux.neighbor; //lo movemos al sgte vecino con quien tiene una arista
            }
        }
        return sb.toString();
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        return super.dfs();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        return super.bfs();
    }

    public Vertex<T> getVertexByIndex(int index){
        for (int i = 0; i < counter; i++) {
            if (i == index) return this.vertexList[i];
        }
        return null;
    }
}
