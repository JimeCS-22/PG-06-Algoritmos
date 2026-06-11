package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.LinkedList;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {
    /*
    Test AdjacencyListGraph<T> Punto 2 de la PG-6
    -Camila Montoya
    Nota: correr cada test por separado
     */
    @Test
    void testAdjacencyListGraph1() {
        //a. Cree e instancie un objeto tipo AdjacencyListGraph llamado “graph”.
        AdjacencyListGraph<Integer> graph = new AdjacencyListGraph<>(30, false); //es no dirigido
        try {
            // b. Agregue 30 nuevos vértices con números al alzar entre 10 y 50.
            while (graph.counter < 30) {
                int value = new Random().nextInt(10, 50);
                if (graph.isEmpty()) {
                    graph.addVertex(value);
                } else if (!graph.containsVertex(value)) {
                    graph.addVertex(value);
                }
            }


            //c.Cree un método que permita conectar entre sí por medio de aristas,
            System.out.println("-----Conectar vértices pares con pares e impares con impares-----\n");
            connectEvenAndOddVertices(graph);
            //d.mostrar por consola el contenido del grafo
            System.out.println(graph);

            //f. Pruebe los recorridos dfs(), bfs() y muestre los resultados por consola
            System.out.println("BFS Transversal Tour: " + graph.bfs());
            System.out.println("DFS Transversal Tour: " + graph.dfs());
            System.out.println("---------------------------");

            //g. Suprima 5 vértices al azar (también deberá suprimir aristas y pesos)
            // uso de una lista para que sean exactamente 5 vértices los eliminados y no repetidos
            System.out.println("-------Prueba Remove--------\n");
            LinkedList<Integer> vertices = new LinkedList<>();

            for (int i = 0; i < graph.size(); i++) {
                vertices.add(graph.getVertexByIndex(i).data);
            }
            for (int i = 0; i < 5 && !vertices.isEmpty(); i++) {
                int index = new Random().nextInt(vertices.size()) + 1;
                Integer value = vertices.get(index);
                System.out.println("Eliminando vértice: " + value);
                graph.removeVertex(value);
                // para evitar volver a escoger el mismo vértice
                vertices.remove(value);
            }
            System.out.println("Graph size: " + graph.size());
            //h. Muestre el contenido del grafo por consola (vértices, aristas, pesos)
            System.out.println(graph);


        } catch (ListException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testPuntoE() {
        AdjacencyListGraph<Integer> graph = new AdjacencyListGraph<>(30, false); //es no dirigido
        try {
            while (graph.counter < 30) {
                int value = new Random().nextInt(10, 60);
                if (graph.isEmpty()) {
                    graph.addVertex(value);
                } else if (!graph.containsVertex(value)) {
                    graph.addVertex(value);
                }
            }

            //e. Conecte en forma aleatoria 10 vértices pares existentes en el grafo con 10 vértices impares.
            System.out.println("-----Conectar vértices pares con impares-------\n");
            connectRandomEvenOddVertices(graph);
            System.out.println(graph);

        }catch (ListException e) {
        throw new RuntimeException(e);
    }
    }


    /*c. Cree un método que permita conectar entre sí por medio de aristas,
           todos los vértices del grafo con números pares y todos
           los vértices con números impares. Para los pesos utilice
           valores entre 1 y 40. d. Muestre el contenido del grafo
           por consola (vértices, aristas, pesos)*/
    private void connectEvenAndOddVertices(AdjacencyListGraph<Integer> graph)
            throws GraphException, ListException {

        Random random = new Random();

        LinkedList<Integer> evenVertices = new LinkedList<>();
        LinkedList<Integer> oddVertices = new LinkedList<>();

        // Separar vértices
        for (int i = 1; i < graph.size(); i++) { // si existe size()
            int value = graph.getVertexByIndex(i).data;

            if (value % 2 == 0)
                evenVertices.add(value);
            else
                oddVertices.add(value);
        }

        // Conectar pares entre sí
        for (int i = 1; i < evenVertices.size(); i++) {
            for (int j = i + 1; j <= evenVertices.size(); j++) {

                int weight = random.nextInt(40) + 1;

                graph.addEdgeAndWeight(
                        evenVertices.get(i),
                        evenVertices.get(j),
                        weight
                );
            }
        }

        // Conectar impares entre sí
        for (int i = 1; i <= oddVertices.size(); i++) {
            for (int j = i + 1; j <= oddVertices.size(); j++) {

                int weight = random.nextInt(40) + 1;

                graph.addEdgeAndWeight(
                        oddVertices.get(i),
                        oddVertices.get(j),
                        weight
                );
            }
        }
    }
// Método para punto e
// e. Conecte en forma aleatoria 10 vértices pares existentes en el grafo con 10 vértices impares.
    private void connectRandomEvenOddVertices(AdjacencyListGraph<Integer> graph)
            throws GraphException, ListException {

        Random random = new Random();

        LinkedList<Integer> evenVertices = new LinkedList<>();
        LinkedList<Integer> oddVertices = new LinkedList<>();

        // Separar pares e impares
        for (int i = 1; i <= graph.size(); i++) {

            Vertex<Integer> vertex = graph.getVertexByIndex(i);
            if (vertex == null) continue; // saltar índices vacíos

            int value = vertex.data;
            if (value % 2 == 0)
                evenVertices.add(value);
            else
                oddVertices.add(value);
        }

        int limit = Math.min(10, Math.min(evenVertices.size(), oddVertices.size()));

        for (int i = 1; i <= limit; i++) {

            // Escoger un par aleatorio
            int evenIndex = random.nextInt(10, evenVertices.size()+1);
            int even = evenVertices.get(evenIndex);
            evenVertices.remove(evenIndex);

            // Escoger un impar aleatorio
            int oddIndex = random.nextInt(10, oddVertices.size()+1);
            int odd = oddVertices.get(oddIndex);
            oddVertices.remove(oddIndex);

            int weight = random.nextInt(40) + 1;

            if (!graph.containsEdge(even, odd)) {
                graph.addEdgeAndWeight(even, odd, weight);
            }
        }
    }

}
