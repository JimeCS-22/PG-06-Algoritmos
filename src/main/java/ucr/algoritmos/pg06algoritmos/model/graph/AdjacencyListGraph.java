package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Node;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

public class AdjacencyListGraph<T extends Comparable<T>> extends AdjacencyMatrixGraph<T> {

    public AdjacencyListGraph(int n, boolean directed){
    super(n,directed);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        Vertex<T> vertexA = getVertex(a);
        boolean getVertexA = getNodeNeighbor(vertexA.headNode, b) != null;
        boolean getVertexB = false;
        if(!directed){
            Vertex<T> vertexB = getVertex(b);
            getVertexB = getNodeNeighbor(vertexB.headNode,a) != null;
        }

        return !directed ? getVertexA && getVertexB : getVertexA;

    }

    private Node<T> getNodeNeighbor(Node<T> headNode, T element) {
        if(headNode == null) return null;

        Node<T> aux = headNode;
        while(aux!=null){
            if (aux.data.compareTo(element) == 0) return aux;
            aux = aux.neighbor;//Lo movemos aux al siguiente nodo vecino

        }

        return null;//si llega aqui, no encontro el nodo

    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || containsVertex(b)) throw new GraphException("Adjacency list graph not contains vertex");

        if(!containsEdge(a, b)){
            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = addNeighbor(vertexA.headNode, b, null);

            if (!directed){
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode = addNeighbor(vertexB.headNode, a, null);
            }
        }
    }

    private Node<T> addNeighbor(Node<T>  headNode, T element, Object weight){

        Node<T> node = new Node<>(element, weight);

        if(headNode != null) headNode = node;
        else {
            Node<T> aux = headNode;
            //me muevo por la lista hasta el ult nodo
            while(aux.neighbor != null)
                aux = aux.neighbor;//se mueve al sgte nodo vecino

            //Se sale cuando aux.neighbor es nulo
            aux.neighbor = node;
        }

        return headNode;//si llega nulo, lo devuelve con un nodo

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency List Graph\n");
        String graphType = directed ? "Directed":"Undirected";
        sb.append("****** Graph Type: ").append(graphType).append("\n");
        //mostramos los vértices
        for (int i = 0; i < counter; i++) {
            sb.append("\nThe vertex in position [").append(i).append("] is:")
                    .append(vertexList[i].data + "\n");

        }

        //mostramos la informacion de aristas y pesos
        for (int i = 0; i < counter; i++) {
            sb.append("\n ( ").append(i).append(" )-----Vertex [").append(getVertexByIndex(i).data).append(" ]");
            Node<T> aux = getVertexByIndex(i).headNode;//llenamos al inicio de la lista enlazada del node
            while(aux!=null){
                sb.append("\n ▨▨ Edge [").append(aux.data).append(", weight:").append(aux.weight);
            }
        }
        return sb.toString();
    }

    public Vertex<T> getVertexByIndex(int index){
        for (int i = 0; i < counter; i++) {
            if (i == index) return this.vertexList[i];
        }
        return null;
    }
}
