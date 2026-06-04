package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

import java.io.PrintStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    @Test
    public void testAdjacencyMatrixGraph() {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(10,false); //es no dirigido
        try {
            for (int i = 1; i <= 5; i++)
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

            //probamos los recorridos
            System.out.println("DFS: " + graph.dfs());
            System.out.println("BFS: " + graph.bfs() );

            // Prueba de removeVertex
            System.out.println("\n=== PRUEBA removeVertex ===");
            System.out.println("Eliminando vértice 1...");
            graph.removeVertex(1);
            System.out.println("Eliminando vértice 2...");
            graph.removeVertex(2);
            System.out.println("Eliminando vértice 3...");
            graph.removeVertex(3);

            graph.addVertex(6);
            graph.addVertex(7);
            graph.addEdgeAndWeight(4,5,new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(6,7,new Random().nextInt(5, 30));

            // Prueba de removeEdge
            System.out.println("\n=== PRUEBA removeEdge ===");
            System.out.println("Eliminando arista entre 4 y 5...");
            graph.removeEdge(4, 5);
            System.out.println("¿Existe arista entre 4 y 5? " + graph.containsEdge(4, 5));
            System.out.println("\nGrafo después de eliminar arista:");
            System.out.println(graph);

            System.out.println("==Prueba==");
            graph.addVertex(6); graph.addVertex(7);
            graph.addEdgeAndWeight(4,7,new Random().nextInt(5, 30));
            graph.addEdgeAndWeight(5,6,new Random().nextInt(5, 30));
            System.out.println(graph.printMatrix());
            System.out.println(graph);

            //probamos los recorridos
            System.out.println("DFS: " + graph.dfs());
            System.out.println("BFS: " + graph.bfs() );

        } catch (ListException | StackException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

}