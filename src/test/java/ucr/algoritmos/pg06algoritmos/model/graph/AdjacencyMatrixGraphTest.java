package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

import static org.junit.jupiter.api.Assertions.*;
class AdjacencyMatrixGraphTest {


    @Test
    public void testAdjacencyMatrixGraph() {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(10);
        try {
            for (int i = 0; i <= 5; i++)
                graph.addVertex(i);


            //agregamos aristas
            graph.addEdge(1,2);
            graph.addEdge(1,3);
            graph.addEdge(2,3);
            graph.addEdge(2,5);
            graph.addEdge(3,4);
            graph.addEdge(4,5);

        }catch (ListException e) {
            throw new RuntimeException(e);
        }
        System.out.println(graph);
    }
}