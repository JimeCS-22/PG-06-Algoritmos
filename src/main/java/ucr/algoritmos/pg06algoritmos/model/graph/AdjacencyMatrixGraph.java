package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Queue.MyQueue;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

import java.util.Arrays;

public class AdjacencyMatrixGraph<T extends Comparable<T>> implements Graph<T> {
    public int n; //tam máximo de la matriz
    public Vertex<T>[] vertexList;//arreglo estático de objetos tipo Vertex
    private T[][] adjancencyMatrix; //areglo multidimensional tipo matriz
    public int counter;

    public AdjacencyMatrixGraph(int n){
        if (n <= 0) {
            System.exit(1);
            this.n = n;
            this.vertexList = new Vertex[n];
            this.adjancencyMatrix = (T[][]) new Comparable[n][n];
            this.counter = 0;
            initMatrix();
        }
    }

    private void initMatrix() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjancencyMatrix[i][j] = (T)Integer.valueOf(0);
            }
        }
    }

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
        if (isEmpty()) throw new GraphException("Adjacency Matrix is empty");
        for (int i = 0; i < counter; i++) {
            if(element.equals(this.vertexList[i].data)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        return false;
    }

    @Override
    public void addVertex(T element) throws GraphException, ListException {
        if(counter >= vertexList.length)
            throw new GraphException("Adjacency Matrix Graph is full");
        vertexList[counter++] = new Vertex<>(element);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
                throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");
        adjancencyMatrix[indexOf(a)][indexOf(b)] = (T) Integer.valueOf(1);
            //grafo no dirigido
            adjancencyMatrix[indexOf(b)][indexOf(a)] = (T) Integer.valueOf(1);
        
    }

    private int indexOf(T element) {
        for (int i = 0; i < counter; i++) {
            if(element.equals(vertexList[i].data))return i;
        }return -1;//no encontró la data del vértice
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency Matrix Graph\n");

        //mostramos todos los vertices
        for (int i = 0; i < counter; i++) {

            sb.append(" \n The vertex in position [").append(i).append(" ] is:").
                    append(vertexList[i].data). append("\n");
        }

        //mostramos todos las aristas
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if (!adjancencyMatrix[i][j].equals(0)) {

                    sb.append("\n There is edge between this vertexes: ").
                            append(vertexList[i].data).append("............").
                            append(vertexList[j].data);

                    //Valida que tenga pesos, si es el caso se muestran
                    if(!adjancencyMatrix[i][j].equals(1)) {
                        sb.append("___weight: ").append(adjancencyMatrix[j][i]);
                    }
                }
            }
        }

        return sb.toString();
    }

    /**Metodos de ayuda**/
    public boolean equals(T a, T b)  {
        return a==null ? b==null : a.equals(b);
    }

    //metodo generico de comparacion
    public int compareElement(T a, T b) {
        return a.compareTo(b);
    }
}
