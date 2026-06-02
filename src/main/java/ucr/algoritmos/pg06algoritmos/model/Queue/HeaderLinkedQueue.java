package ucr.algoritmos.pg06algoritmos.model.Queue;

import ucr.algoritmos.pg06algoritmos.model.Node;

/**
* Esta es la implementación del TDA utilizando un nodo cabecera vacio
* */

public class HeaderLinkedQueue<T> implements MyQueue<T> {
    private Node<T> front; //anterior
    private Node<T> rear; //posterior
    private int size; //control de elementos encolados

    public HeaderLinkedQueue() {
        front = rear = new Node<T>();
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        front = rear = new Node<T>();
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return front == rear;
    }

    @Override
    public int indexOf(T element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        HeaderLinkedQueue<T> aux = new HeaderLinkedQueue<>();
        int index = 1;
        int pos = -1;
        while (!isEmpty()) {

            if (equals(front(), element)){
                pos = index;
            }

            aux.enQueue(deQueue());
            index++;
        }

        while (!aux.isEmpty()) {

            enQueue(aux.deQueue());
        }
        return pos;
    }

    @Override
    public void enQueue(T element) throws QueueException {
        Node<T> newNode = new Node<>(element);
        rear.next = newNode;
        rear = newNode;
        size++; //Actualizo el contador de elementos encolados
    }

    @Override
    public T deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Header Linked Queue is empty");

        T element = front.next.data;
        //Caso 1. Cuando solo hay un elemento
        if(front.next == rear) clear();

        //Caso 2. Cuando hay más de un elemento
        else {
            front.next = front.next.next;
        }
        size--;
        return element;
    }

    @Override
    public void enQueue(T element, Integer priority) throws QueueException {
        if (priority == null)
            throw new QueueException("Priority cannot be null");
        enQueue(element);
    }

    @Override
    public boolean contains(T element) throws QueueException {
        if (isEmpty()) throw new QueueException("Header Linked Queue is empty");
        HeaderLinkedQueue<T> aux = new HeaderLinkedQueue<>();
        boolean finded = false;
        while (!isEmpty()) {
            if(equals(front(), element)) {
                finded = true;
            }
            aux.enQueue(deQueue());
        }
        //Al final dejamos el tda cola en su estado original
        while(!aux.isEmpty()) {
            enQueue(aux.deQueue());
        }
        return finded;
    }

    //Peek y Front son lo mismo
    @Override
    public T peek() throws QueueException {
        if (isEmpty()) throw new QueueException("Header Linked Queue is empty");
        return front.next.data;
    }

    @Override
    public T front() throws QueueException {
        if (isEmpty()) throw new QueueException("Header Linked Queue is empty");
        return front.next.data;
    }

    @Override
    public String toString(){
        if(isEmpty()) return "Header Linked Queue is empty";
        StringBuilder sb = new StringBuilder("FRONT ➡️ [] ➡️ ");
        HeaderLinkedQueue<T> auxQueue = new HeaderLinkedQueue<>();
        try {
            while(!isEmpty()) {
                sb.append("[").append(peek()).append("] ");
                auxQueue.enQueue(deQueue());
                if (!isEmpty()) sb.append(" ➡️ ");
            }

            //Al final dejamos la cola en su estado original
            while(!auxQueue.isEmpty()) {
                enQueue(auxQueue.deQueue());
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
        sb.append(" ➡️ REAR");
        return sb.toString();
    }
    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }
}
