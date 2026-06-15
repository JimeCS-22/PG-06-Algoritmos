package ucr.algoritmos.pg06algoritmos.model.MST;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyMatrixGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.GraphException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraTest {

    @Test
    void test() {
        try {
            try {
                // 1. Crear el grafo con capacidad para 5 vértices
                AdjacencyMatrixGraph<String> grafo = new AdjacencyMatrixGraph<>(5, false);

                // 2. Agregar los vértices (índices 0 al 4)
                grafo.addVertex("A");
                grafo.addVertex("B");
                grafo.addVertex("C");
                grafo.addVertex("D");
                grafo.addVertex("E");

                // 3. Agregar aristas (peso, origen, destino)
                grafo.addEdge("A", "B");
                grafo.addEdge("A", "C");
                grafo.addEdge("B", "C");
                grafo.addEdge("B", "D");
                grafo.addEdge("C", "E");
                grafo.addEdge("E", "D");

                Dijkstra dijkstra = new Dijkstra();
                dijkstra.execute(grafo, 0);

                System.out.println("--- Ejecución del Algoritmo de Dijkstra ---");

                var steps = dijkstra.getSteps();
                for (int i = 1; i <= steps.size(); i++) {
                    DijkstraStep step = steps.getNode(i).data;
                    System.out.println(step.description);
                }

                System.out.println("--- Proceso Finalizado ---");

            } catch (GraphException | ListException e) {
                System.err.println("Ocurrió un error en la ejecución: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}