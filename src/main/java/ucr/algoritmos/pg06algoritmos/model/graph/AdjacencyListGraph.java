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
        if (!containsVertex(element))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        //ahora que sabemos que el vèrtice existe, procedemos a buscarlo para eliminarlo
        int index = indexOf(element);//devuelve el indice del vértice a eliminar
        //si el vértice existe en la lista de vértices
        if (index != -1) {//devuelve el indidce del vertice a eliminar
            for (int i = index; i < counter - 1; i++) {
                vertexList[i] = vertexList[i + 1];
            }
            counter--;//lo debemos decrementar por el vértice suprimido
        }

        //ahora debemos buscar el rastro del vértice suprimido en las listas enlazadas
        //de vecinos de los otros vértices
        for (int i = 0; i < counter; i++) {
            Vertex<T> vertex = vertexList[i];
            vertex.headNode = removeNeighbor(vertex.headNode, element);
        }
    }

    private Node<T> removeNeighbor(Node<T> headNode, T element) throws ListException {
        if(headNode == null)throw new ListException("Linked List in Graph is Empty");
        //Caso 1: el elemento a suprimir es el primero
        if (equals(headNode.data,element)) {
            headNode = headNode.neighbor; //queda apuntando al sgte nodo vecino
            //caso 2.El elemento a suprimir puede estar en medio o al final de la lista
        }else{
            //vamos a brincar el nodo
            Node<T> prev = headNode;
            while(prev.neighbor != null){
                if (equals(prev.neighbor.data, element)) {
                    Node<T>removed = prev.neighbor;//es el nodo a eliminar
                    //desenlaza el nodo
                    prev.neighbor = removed.neighbor;
                }
                prev = prev.neighbor; //lo movemos al sgte vecino
            }
        }
        return headNode;//modificado sin el elemento eliminado
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");

        if (!containsEdge(a,b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Edge");

        if (containsEdge(a,b)) {

            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = removeNeighbor(vertexA.headNode, b);

            if (!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode = removeNeighbor(vertexB.headNode, a);
            }

        }
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

    //TODO
    @Override
    public String dfs() throws GraphException, StackException, ListException {

        if(isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");

        setVisited(false);

        StringBuilder sb = new StringBuilder();

        stack.clear();

        vertexList[0].setVisited(true);
        sb.append(vertexList[0].data).append(", ");

        stack.push(0);

        while(!stack.isEmpty()){

            int current = (int) stack.top();

            int neighbor = adjacentVertexNotVisited(current);

            if(neighbor == -1){
                stack.pop();
            }else{
                vertexList[neighbor].setVisited(true);
                sb.append(vertexList[neighbor].data).append(", ");
                stack.push(neighbor);
            }
        }

        return sb.toString();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {

        if(isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");

        setVisited(false);

        StringBuilder sb = new StringBuilder();

        queue.clear();

        vertexList[0].setVisited(true);
        sb.append(vertexList[0].data).append(", ");

        queue.enQueue(0);

        while(!queue.isEmpty()){

            int current = (int) queue.deQueue();

            Node<T> aux = vertexList[current].headNode;

            while(aux != null){

                int neighbor = indexOf(aux.data);

                if(neighbor != -1 && !vertexList[neighbor].isVisited()){

                    vertexList[neighbor].setVisited(true);
                    sb.append(vertexList[neighbor].data).append(", ");

                    queue.enQueue(neighbor);
                }

                aux = aux.neighbor;
            }
        }

        return sb.toString();
    }

    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value); //value==true o false
        }//for
    }

    private int adjacentVertexNotVisited(int indexVertex) throws ListException {

        Node<T> aux = vertexList[indexVertex].headNode;

        while(aux != null){

            int indexNeighbor = indexOf(aux.data);

            if(indexNeighbor != -1 && !vertexList[indexNeighbor].isVisited()){
                return indexNeighbor;
            }

            aux = aux.neighbor;
        }

        return -1;
    }

    /**
     * Metodo que devuelve el grado del vertice del elemento dado, de un grafo dirgido grado de salida + grado de entrada
     *
     **/

    public int getVertexDegree(T element) throws GraphException, ListException {

        if (isEmpty()) throw new ListException("Adjacency List Graph is Empty");

        if (!containsVertex(element)) throw new ListException("Adjacency List Graph Not Contains Vertex");

        int degree = 0; //El va a devolver el grado del vertice

        Vertex<T> vertex = getVertex(element);

        if (!directed) {

            Node<T> aux = vertex.headNode;

            while (aux != null) {
                degree++;
                aux = aux.neighbor;
            }

        } else {

            // Grado de salida
            Node<T> aux = vertex.headNode;

            while (aux != null) {
                degree++;
                aux = aux.neighbor;
            }

            // Grado de entrada
            for (int i = 0; i < counter; i++) {

                aux = vertexList[i].headNode;

                while (aux != null) {

                    if (equals(aux.data, element))
                        degree++;

                    aux = aux.neighbor;
                }
            }
        }

        return degree;
    }

    /**
     *Metodo que devuelva el grado del grafo
     **/
    public int getGraphDegree() throws GraphException, ListException {

        if (isEmpty()) throw new ListException("Adjacency List Graph is Empty");

        int maxDegree = 0;

        for (int i = 0; i < counter; i++) {

            int degree = getVertexDegree(vertexList[i].data);

            if (degree > maxDegree) maxDegree = degree;
        }

        return maxDegree;

    }

    /**
     * Metodo que devuelva el número total de aristas existentes en el grafo
     **/
    public int totalEdges() throws GraphException, ListException {

        if(isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");

        int edges = 0;

        for(int i = 0; i < counter; i++) {

            Node<T> aux = vertexList[i].headNode;

            while(aux != null) {
                edges++;
                aux = aux.neighbor;
            }
        }

        return directed ? edges : edges / 2;
    }

}