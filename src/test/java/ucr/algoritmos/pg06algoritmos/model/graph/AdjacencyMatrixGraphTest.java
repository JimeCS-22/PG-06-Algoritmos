package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    @Test
    public void testAdjacencyMatrixGraph() {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(10,false); //es no dirigido
        try {
            for (int i = 0; i <= 5; i++)
                graph.addVertex(i);

            //agregamos aristas con pesos
            graph.addEdgeAndWeight(1, 2, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(1, 3, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(2, 3, new Random().nextInt(5, 30));
            //grafo dirigido
            graph.addEdgeAndWeight(2, 1, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(2, 5, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(3, 4, new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(4, 5, new Random().nextInt(5, 30));


            System.out.println(graph);

            // Prueba de DFS
            System.out.println("\n=== PRUEBA DFS ===");
            System.out.println("Recorrido DFS: " + graph.dfs());

            // Prueba de BFS
            System.out.println("\n=== PRUEBA BFS ===");
            System.out.println("Recorrido BFS: " + graph.bfs());

            // Prueba de containsVertex
            System.out.println("\n=== PRUEBA containsVertex ===");
            System.out.println("¿Contiene vértice 3? " + graph.containsVertex(3));
            System.out.println("¿Contiene vértice 10? " + graph.containsVertex(10));

            // Prueba de containsEdge
            System.out.println("\n=== PRUEBA containsEdge ===");
            System.out.println("¿Existe arista entre 1 y 2? " + graph.containsEdge(1, 2));
            System.out.println("¿Existe arista entre 1 y 5? " + graph.containsEdge(1, 5));

            // Prueba de size
            System.out.println("\n=== PRUEBA size ===");
            System.out.println("Tamaño del grafo: " + graph.size());

            // Prueba de removeEdge
            System.out.println("\n=== PRUEBA removeEdge ===");
            System.out.println("Eliminando arista entre 2 y 5...");
            graph.removeEdge(2, 5);
            System.out.println("¿Existe arista entre 2 y 5? " + graph.containsEdge(2, 5));
            System.out.println("\nGrafo después de eliminar arista:");
            System.out.println(graph);

            // Prueba de removeVertex
            System.out.println("\n=== PRUEBA removeVertex ===");
            System.out.println("Eliminando vértice 1...");
            graph.removeVertex(1);
            System.out.println("Tamaño del grafo después de eliminar: " + graph.size());
            System.out.println("\nGrafo después de eliminar vértice:");
            System.out.println(graph);

            // Prueba de DFS después de cambios
            System.out.println("\n=== PRUEBA DFS (después de cambios) ===");
            System.out.println("Recorrido DFS: " + graph.dfs());

            // Prueba de BFS después de cambios
            System.out.println("\n=== PRUEBA BFS (después de cambios) ===");
            System.out.println("Recorrido BFS: " + graph.bfs());

        } catch (ListException | StackException e) {
            throw new RuntimeException(e);
        }
    }

}