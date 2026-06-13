package ucr.algoritmos.pg06algoritmos.model.graph;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.Queue.QueueException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;
import ucr.algoritmos.pg06algoritmos.model.stack.StackException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LinkedGraphTest {

    @Test
    public void testLinkedGraph() {
        try {
            LinkedGraph<Integer> graph = new LinkedGraph<>(false); //es no dirigido
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
/*
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
*/
        } catch (ListException e) {
            throw new RuntimeException(e);

        } catch (QueueException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLinkedGraphOperations() throws GraphException {
        LinkedGraph<String> graph = new LinkedGraph<>(false);
        try {
            // A) Cree e instancie un objeto tipo LinkedListGraph

            String[] people = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"};
            String[] vertices = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

            // B) Para el grafo, agregue vértices
            for (String v : vertices) {
                graph.addVertex(v);
            }

            Random random = new Random();

            // C) Agregue como pesos nombres de personas (aristas con pesos)
            graph.addEdgeAndWeight("A", "B", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("A", "C", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("B", "D", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("C", "E", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("D", "F", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("E", "G", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("F", "H", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("G", "I", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("H", "J", people[random.nextInt(people.length)]);
            graph.addEdgeAndWeight("I", "J", people[random.nextInt(people.length)]);

            // D) Muestre el contenido del grafo por consola (vértices, aristas, pesos)
            System.out.println("Grafo original:");
            System.out.println(graph);

            // E) Pruebe los recorridos dfs(), bfs() y muestre los resultados por consola
            System.out.println("\n=== RECORRIDOS ===");
            System.out.println("DFS: " + graph.dfs());
            System.out.println("BFS: " + graph.bfs());

            // F) Suprima los vértices F, H, J (también deberá suprimir aristas y pesos)
            System.out.println("\n=== ELIMINANDO VÉRTICES F, H, J ===");
            graph.removeVertex("F");
            graph.removeVertex("H");
            graph.removeVertex("J");

            // G) Pruebe nuevamente los recorridos dfs(), bfs() y muestre los resultados por consola
            System.out.println("\nGrafo después de la eliminación:");
            System.out.println(graph);

            System.out.println("\n=== RECORRIDOS DESPUÉS DE ELIMINAR ===");
            System.out.println("DFS: " + graph.dfs());
            System.out.println("BFS: " + graph.bfs());

            // H) Muestre el contenido del grafo por consola (vértices, aristas, pesos)
            System.out.println("\nContenido final del grafo:");
            System.out.println(graph);

        } catch (ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }

}
