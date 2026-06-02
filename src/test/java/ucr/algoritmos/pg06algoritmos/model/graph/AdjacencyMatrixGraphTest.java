package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
class AdjacencyMatrixGraphTest {


    @Test
    public void testAdjacencyMatrixGraph() {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(10);
        try {
            for (int i = 0; i <= 5; i++)
                graph.addVertex(i);


            //agregamos aristas
            graph.addEdgeAndWeight(1,2, new Random().nextInt(1,30));
            graph.addEdgeAndWeight(1,3, new Random().nextInt(1,30));
            graph.addEdgeAndWeight(2,3, new Random().nextInt(1,30));
            graph.addEdgeAndWeight(2,5, new Random().nextInt(1,30));
            graph.addEdgeAndWeight(3,4, new Random().nextInt(1,30));
            graph.addEdgeAndWeight(4,5, new Random().nextInt(1,30));

        }catch (ListException e) {
            throw new RuntimeException(e);
        }
        System.out.println(graph);
    }
}