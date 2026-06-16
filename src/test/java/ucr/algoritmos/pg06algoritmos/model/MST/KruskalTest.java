package ucr.algoritmos.pg06algoritmos.model.MST;

import org.junit.jupiter.api.Test;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyListGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyMatrixGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.LinkedGraph;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KruskalTest {
    @Test
    void test() {
        try {
            Kruskal kruskal= new Kruskal();
            AdjacencyMatrixGraph<String> graphMatrix = new AdjacencyMatrixGraph<>(10,false);

            graphMatrix.addVertex("A");
            graphMatrix.addVertex("B");
            graphMatrix.addVertex("C");
            graphMatrix.addVertex("D");

            AdjacencyListGraph<String> graphListGraph = new AdjacencyListGraph<>(10,false);
            graphListGraph.addVertex("A");
            graphListGraph.addVertex("B");
            graphListGraph.addVertex("C");
            graphListGraph.addVertex("D");

            LinkedGraph<String> linkedGraph = new LinkedGraph<>(false);
            linkedGraph.addVertex("A");
            linkedGraph.addVertex("B");
            linkedGraph.addVertex("C");
            linkedGraph.addVertex("D");


        ArrayList<Kruskal.KruskalEdge> edgeList = new ArrayList<>();

        edgeList.add(new Kruskal.KruskalEdge("A", "D", 5));
        edgeList.add(new Kruskal.KruskalEdge("A", "B", 7));
        edgeList.add(new Kruskal.KruskalEdge("B", "E", 7));
        edgeList.add(new Kruskal.KruskalEdge("B", "D", 9));
        edgeList.add(new Kruskal.KruskalEdge("B", "C", 8));
        edgeList.add(new Kruskal.KruskalEdge("D", "F", 6));
        edgeList.add(new Kruskal.KruskalEdge("D", "E", 15));
        edgeList.add(new Kruskal.KruskalEdge("C", "E", 5));
        edgeList.add(new Kruskal.KruskalEdge("E", "G", 9));


             kruskal.executeMatrixGraph(graphMatrix, edgeList);
            kruskal.executeListGraph(graphListGraph, edgeList);
            kruskal.executeLinkedGraph(linkedGraph, edgeList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}