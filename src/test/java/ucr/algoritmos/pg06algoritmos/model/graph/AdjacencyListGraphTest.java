package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {

    @Test
    void testAdjacencyListGraph() {
        AdjacencyListGraph<Integer> graph = new AdjacencyListGraph<>(10, false); //es no dirigido
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

            //TODO fix this to study
            System.out.println("BFS Transversal Tour: " + graph.bfs());
            System.out.println("DFS Transversal Tour: " + graph.dfs());
            System.out.println(graph);

        } catch (ListException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        }

    }
}