package ucr.algoritmos.pg06algoritmos.model.graph;

import ucr.algoritmos.pg06algoritmos.model.Node;
import ucr.algoritmos.pg06algoritmos.model.Queue.LinkedQueue;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.LinkedList;
import ucr.algoritmos.pg06algoritmos.model.stack.LinkedStack;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;


import java.util.concurrent.ConcurrentMap;

public class LinkedGraph <T extends Comparable<T>> extends LinkedList<T> implements Graph<T> {
    //atributos para los recorridos dfs, bfs
    public final boolean directed;
    public LinkedStack<Integer> stack;
    public LinkedQueue<Integer> queue;


    public LinkedGraph(boolean directed) {
        super();
        this.directed = directed;
        stack = new LinkedStack<>();
        queue = new LinkedQueue<>();
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        return contains(element);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        Node<T> nodeA = getNode(a);
        Node<T> nodeB = null;

        if (!directed) {
            nodeB = getNode(b);

        }
        return !directed ? getNodeNeighbor(nodeA, b) != null && getNodeNeighbor(nodeB,a) != null
                   : getNodeNeighbor(nodeA,b)!= null;

    }
    private Node<T> getNodeNeighbor(Node<T> headNode, T element) {
        if(headNode.neighbor==null) return null;
        Node<T> aux = headNode.neighbor;
        while(aux!=null){
            if(aux.data.compareTo(element)==0) return aux;
            aux = aux.neighbor; //movemos aux al sgte nodo vecino
        }
        return null; //si llega aquí, no encontró el nodo
    }
    @Override
    public void addVertex(T element) throws GraphException, ListException {
        if (isEmpty()) add(element); //si la lista enlazada está vacia agregue el elemento
        else if (!contains(element)) add(element);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjancency List Graph Not Contains Vertex");
        if (!containsEdge(a,b)) {
            Node<T>nodeA = getNode(a);
            addNeighbor(nodeA,b,null); //null
            if(!directed){
                Node<T>nodeB = getNode(b);
                addNeighbor(nodeB,a,null); //null para el peso
            }
        }
    }

    private void addNeighbor(Node<T> headNode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if(headNode.neighbor == null)
            headNode.neighbor = node;
        else{
            Node<T> aux = headNode.neighbor;
            //me muevo por la lista hasta el ult nodo
            while(aux.neighbor != null)
                aux = aux.neighbor; //se mueve al sgte nodo vecino
            //se sale cuando aux.neighbor es nulo
            aux.neighbor = node; //entonces conectados el nodo al final
        }
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if (!containsEdge(a,b)) {
            Node<T>nodeA = getNode(a);
            //recuperamos la lista de vecinos del nodo A  para settear el peso
            getNodeNeighbor(nodeA,b).weight = weight; //null  para el peso
            if(!directed){
                Node<T>nodeB = getNode(b);
                getNodeNeighbor(nodeB,a).weight = weight; //null para el peso
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
           addNeighbor(nodeA, b, weight);
            if(!directed) {
                Node<T> nodeB = getNode(b);
                // a partir del node B contruya
                addNeighbor(nodeB, a, weight);
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if(!containsVertex(element))
            throw new GraphException("Linked Graph Not Contains Vertex");
        remove(element); //eliminamos el vertice del grafo
        //buscamos el rastro del vértice en las listas enlazadas de vecinos de los otros vértices
        int len = size();
        for (int i = 0; i < len; i++) {
            Node<T> node = getNodeByIndex(i);
            removeNeighbor(node, element);
        }
    }
    private void removeNeighbor(Node<T> headNode, T element) throws ListException {
        if(headNode == null)throw new ListException("Linked List in Graph is Empty");
        //Caso 1: el elemento a suprimir es el primero
        if (equals(headNode.neighbor.data,element)) {
            headNode = headNode.neighbor.neighbor; //queda apuntando al sgte nodo vecino
            //caso 2.El elemento a suprimir puede estar en medio o al final de la lista
        }else{
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
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            throw new GraphException("Linked Graph Not Contains Edge");
        }
        Node<T> nodeA = getNode(a);
        removeNeighbor(nodeA,b);
        if (!directed ) {
            Node<T> nodeB = getNode(b);
            removeNeighbor(nodeB,a);
        }
    }

    //TODO

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
        sb.append("|-------|  | Linked Graph |---------| |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("****** Graph Type: ").append(graphType).append("\n");

        sb.append(super.toString());//para mostrar la lista enlazada de vértices

        //mostramos todos los vértices
        try {
            int len = size(); //llamamos al método de la lista enlazada
            for (int i = 1; i <= len; i++) {

                sb.append("\n(").append(i).append(")-------Vertex [").append(getNodeByIndex(i).data).append("]");
                Node<T> aux = getNodeByIndex(i).neighbor;
                while(aux != null){
                    sb.append("\n*** Edge: \"").append(aux.data).append(", weight: ").append(aux.weight);
                    aux = aux.neighbor;
                }
            }

        } catch(ListException e){
            throw new RuntimeException(e);
        }
        return super.toString() + sb;
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
