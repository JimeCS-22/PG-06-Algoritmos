package ucr.algoritmos.pg06algoritmos.model.graph;

import javafx.beans.binding.StringBinding;
import ucr.algoritmos.pg06algoritmos.model.Queue.ArrayQueue;
import ucr.algoritmos.pg06algoritmos.model.Queue.LinkedQueue;
import ucr.algoritmos.pg06algoritmos.model.Queue.MyQueue;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

public class AdjacencyMatrixGraph<T extends Comparable<T>> implements Graph<T> {
    public int n; //tam máximo de la matriz
    public Vertex<T>[] vertexList;//arreglo estático de objetos tipo Vertex
    private T[][] adjancencyMatrix; //areglo multidimensional tipo matriz
    public int counter;

    public AdjacencyMatrixGraph(int n){
        if (n <= 0) {
            System.exit(1);
        }
        this.n = n;
        this.vertexList = new Vertex[n];
        this.adjancencyMatrix = (T[][]) new Comparable[n][n];
        this.counter = 0;
        initMatrix();
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
        return counter;
    }

    @Override
    public void clear() {
        this.vertexList = new Vertex[n];
        this.adjancencyMatrix = (T[][]) new Comparable[n][n];
        this.counter = 0;
        initMatrix();
    }

    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        if(isEmpty())throw new GraphException("Adjacency Matrix Graph is Empty");
        for (int i = 0; i < counter; i++) {
            if(element.equals(vertexList[i].data))return true;
        }
        return false; //No lo encontró
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        if(isEmpty())throw new GraphException("Adjacency Matrix Graph is Empty");
        return !equals(adjancencyMatrix[indexOf(a)][indexOf(b)],(T)Integer.valueOf(0));
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
        if(!containsEdge(a,b)) {
            adjancencyMatrix[indexOf(a)][indexOf(b)] = (T) Integer.valueOf(1);
            //grafo no dirigido
            adjancencyMatrix[indexOf(b)][indexOf(a)] = (T) Integer.valueOf(1);
        }
    }

    private int indexOf(T element) {
        for (int i = 0; i < counter; i++) {
            if(element.equals(vertexList[i].data))return i;
        }return -1;//no encontró la data del vértice
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)) throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");
        if(containsEdge(a,b)) {
            adjancencyMatrix[indexOf(a)][indexOf(b)] = weight;
            //grafo no dirigido
            adjancencyMatrix[indexOf(b)][indexOf(a)] = weight;
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)) throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");
        if(!containsEdge(a,b)) {
            adjancencyMatrix[indexOf(a)][indexOf(b)] = weight;
            //grafo no dirigido
            adjancencyMatrix[indexOf(b)][indexOf(a)] = weight;
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");

        int index = indexOf(element);
        if(index != -1) throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");

        //Se desplaza los vértices hacia la izquierda
        for (int i = index; i < counter; i++) {
            vertexList[i] = vertexList[i+1];
        }

        //Se desplaza las filas en la matriz de adyacencia
        for (int i = index; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                adjancencyMatrix[i][j] = adjancencyMatrix[i+1][j];
            }
        }

        //Desplaza columnas en la matriz de adyacencia
        for (int i = 0; i < counter; i++) {
            for (int j = index; j < counter; j++) {
                adjancencyMatrix[i][j] = adjancencyMatrix[i][j+1];
            }
        }

        vertexList[counter - 1] = null; // Limpiar la última posición

        counter--;

    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if (isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");

        if (!containsVertex(a) || !containsVertex(b)) throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");

        if (!containsEdge(a,b)) {

            //Grafo dirigido
            adjancencyMatrix[indexOf(a)][indexOf(b)] = (T) Integer.valueOf(0);
            // Grafo no dirigido
            adjancencyMatrix[indexOf(b)][indexOf(a)] = (T) Integer.valueOf(0);
        }

    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        if(isEmpty())
            throw new GraphException("Adjacency Matrix Graph is Empty");

        StringBuilder sb = new StringBuilder();
        boolean[] visited = new boolean[counter];

        for(int i = 0; i < counter; i++) {
            if(!visited[i]) {
                dfsProceso(i, visited, sb);
            }
        }

        return sb.toString().trim();
    }

    private void dfsProceso(int index, boolean[] visited, StringBuilder sb) {
        visited[index] = true;
        sb.append(vertexList[index].data).append(" ");

        for(int j = 0; j < counter; j++) {
            if(!visited[j] && adjancencyMatrix[index][j] != null
                    && !adjancencyMatrix[index][j].equals((T)Integer.valueOf(0))) {
                dfsProceso(j, visited, sb);
            }
        }
    }


    @Override
    public String bfs() throws GraphException, ListException {
        if(isEmpty())
            throw new GraphException("Adjacency Matrix Graph is Empty");

        StringBuilder sb = new StringBuilder();
        boolean[] visited = new boolean[counter];
        int[] queue = new int[counter];
        int front = 0, rear = 0;

        for(int i = 0; i < counter; i++) {
            if(!visited[i]) {
                queue[rear++] = i;
                visited[i] = true;

                while(front < rear) {
                    int current = queue[front++];
                    sb.append(vertexList[current].data).append(" ");

                    for(int j = 0; j < counter; j++) {
                        if(!visited[j] && adjancencyMatrix[current][j] != null
                                && !adjancencyMatrix[current][j].equals((T)Integer.valueOf(0))) {
                            queue[rear++] = j;
                            visited[j] = true;
                        }
                    }
                }
                front = 0;
                rear = 0;
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency Matrix Graph\n");
        for (int i = 0; i < counter; i++) {
            sb.append("\nThe vertex in position [").append(i).append("] is:")
                    .append(vertexList[i].data + "\n");

        }
        //mostramos todas las aristas de cada vértice
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if(!adjancencyMatrix[i][j].equals(0)){
                    sb.append("\nThere is edge between the vertexes: ")
                            .append(vertexList[i].data)
                            .append("...................").append(vertexList[j].data);
                    //Valido que tenga pesos, si es el caso que se muestran
                    if(!adjancencyMatrix[j][i].equals(1))
                        sb.append(" .weight: ").append(adjancencyMatrix[j][i]);
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
