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
        AdjacencyMatrixGraph<String> graph = new AdjacencyMatrixGraph<>(10,false); //es no dirigido
        try {
           String [] colors = {"Red", "Blue", "Green" , "Yellow", "Purple", "Orange" , "Pink" , "Black", "White"};
           Random  random = new Random();

            String[] vertices = {"P", "T", "K", "D", "S", "M", "H", "A", "E", "Q", "G", "R", "B", "J"};
            for (String v : vertices) {
                graph.addVertex(v);
            }

            //agregamos aristas con pesos
            graph.addEdgeAndWeight("P", "T", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("P", "K", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("P", "D", colors[random.nextInt(colors.length)]);

            graph.addEdgeAndWeight("T", "S", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("S", "A", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("A", "G", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("G", "B", colors[random.nextInt(colors.length)]);

            graph.addEdgeAndWeight("K", "M", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("M", "E", colors[random.nextInt(colors.length)]);

            // Conexiones de la rama D
            graph.addEdgeAndWeight("D", "H", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("H", "Q", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("Q", "R", colors[random.nextInt(colors.length)]);
            graph.addEdgeAndWeight("R", "J", colors[random.nextInt(colors.length)]);

            System.out.println("Grafo original");
            System.out.println(graph);

            System.out.println("=== RECORRIDOS ===");
            System.out.println("DFS: " + graph.dfs());
            System.out.println("BFS: " + graph.bfs());

            System.out.println("\n=== ELIMINANDO VÉRTICES T, K, H ===");
            graph.removeVertex("T");
            graph.removeVertex("K");
            graph.removeVertex("H");

            System.out.println("\n=== GRAFO DESPUÉS DE LA ELIMINACIÓN ===");
            System.out.println(graph);

        } catch (ListException | StackException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
    }

}