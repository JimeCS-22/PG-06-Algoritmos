package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    @Test
    void adjacencyMatrixGraphTest() {
        AdjacencyMatrixGraph<Integer> graph = new AdjacencyMatrixGraph<>(10);

        try {
            for (int i = 1; i <= 5; i++) {

                graph.addVertex(i);

            }

            System.out.println(graph);

        }catch (GraphException | ListException e){
            throw new RuntimeException(e);
        }
    }

}