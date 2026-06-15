package ucr.algoritmos.pg06algoritmos.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import ucr.algoritmos.pg06algoritmos.model.MST.DijkstraStep;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyMatrixGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.GraphException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

import java.util.ArrayList;

public class DijkstraVisualizerHelper {

    private static final Color COLOR_NODO_DEFAULT = Color.rgb(20, 160, 140); // Turquesa
    private static final Color COLOR_NODO_VISITADO = Color.DARKGRAY;
    private static final Color COLOR_NODO_ACTUAL = Color.rgb(220, 160, 0); // Amarillo/Naranja
    private static final Color COLOR_TEXTO_NODO = Color.WHITE;
    private static final Color COLOR_ARISTA_DEFAULT = Color.rgb(60, 70, 90);
    private static final Color COLOR_ARISTA_PATH = Color.rgb(100, 100, 240); // Azul claro
    private static final Color COLOR_TEXTO_PESO = Color.rgb(220, 160, 0); // Amarillo/Naranja

    private static Timeline autoTimeline;
    private static final double MIN_SPEED_MS = 200;
    private static final double MAX_SPEED_MS = 2000;

    /**
     * Dibuja el grafo en el canvas según el estado actual de Dijkstra.
     */
    public static void drawGraph(Canvas canvas, AdjacencyMatrixGraph<String> graph, DijkstraStep step)
            throws GraphException {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        gc.clearRect(0, 0, w, h);

        if (graph == null || graph.isEmpty()) return;

        double radius = 25.0;
        double[][] coords = {
                {w / 2, h / 5},        // A (Top center)
                {w * 3 / 4, h * 1 / 3}, // B (Mid right)
                {w * 3 / 4, h * 2 / 3}, // C (Bottom right)
                {w / 2, h * 4 / 5},    // D (Bottom center)
                {w / 4, h * 2 / 3},    // E (Bottom left)
                {w / 4, h * 1 / 3}     // F (Mid left)
        };

        gc.setLineWidth(1.0);
        gc.setFont(new Font("System", 12));
        int n = graph.counter;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                try {
                    if (graph.containsEdge(graph.getVertexByIndex(i).data, graph.getVertexByIndex(j).data)) {
                        int weight = graph.getWeight(graph.getVertexByIndex(i).data, graph.getVertexByIndex(j).data);

                        if (i < j) {
                            Color edgeColor = COLOR_ARISTA_DEFAULT;
                            double lineWidth = 1.0;

                            if (step != null && step.previous != null) {
                                if (step.previous[j] == i || step.previous[i] == j) {
                                    edgeColor = COLOR_ARISTA_PATH;
                                    lineWidth = 3.0;
                                }
                            }

                            double x1 = coords[i % 6][0];
                            double y1 = coords[i % 6][1];
                            double x2 = coords[j % 6][0];
                            double y2 = coords[j % 6][1];

                            gc.setStroke(edgeColor);
                            gc.setLineWidth(lineWidth);
                            gc.strokeLine(x1, y1, x2, y2);

                            gc.setFill(COLOR_TEXTO_PESO);
                            double midX = (x1 + x2) / 2;
                            double midY = (y1 + y2) / 2;
                            gc.fillText(String.valueOf(weight), midX, midY - 5);
                        }
                    }
                } catch (ListException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        gc.setFont(new Font("System Bold", 16));
        for (int i = 0; i < n; i++) {
            double x = coords[i % 6][0] - radius;
            double y = coords[i % 6][1] - radius;

            // Determinar color del nodo
            Color nodeColor = COLOR_NODO_DEFAULT;
            if (step != null) {
                if (i == step.currentVertex) {
                    nodeColor = COLOR_NODO_ACTUAL;
                } else if (step.visited[i]) {
                    nodeColor = COLOR_NODO_VISITADO;
                }
            }

            gc.setFill(nodeColor);
            gc.fillOval(x, y, radius * 2, radius * 2);

            gc.setFill(COLOR_TEXTO_NODO);
            String vertexData = graph.getVertexByIndex(i).data;
            double textWidth = gc.getFont().getSize() * vertexData.length() * 0.6;
            gc.fillText(vertexData, x + radius - textWidth / 2, y + radius + 6);

            if (step != null && step.distance[i] != Integer.MAX_VALUE) {
                gc.setFill(COLOR_TEXTO_PESO);
                gc.fillText(String.valueOf(step.distance[i]), x + radius - 10, y - radius - 5);
            }
        }
    }

    /**
     * Muestra la ruta mínima completa en el Label y resalta el camino en el grafo.
     */
    public static void showRouteAndColor(
            Label lblRute,
            Canvas canvas,
            AdjacencyMatrixGraph<String> graph,
            DijkstraStep finalStep,
            String targetVertex) throws GraphException {

        lblRute.setText("");
        drawGraph(canvas, graph, finalStep);

        if (finalStep == null || graph == null || targetVertex == null) return;

        int targetIdx = -1;
        for (int i = 0; i < graph.counter; i++) {
            if (graph.getVertexByIndex(i).data.equals(targetVertex)) {
                targetIdx = i;
                break;
            }
        }

        if (targetIdx == -1) return;

        int dist = finalStep.distance[targetIdx];
        if (dist == Integer.MAX_VALUE) {
            lblRute.setText("Ruta: No hay camino accesible a " + targetVertex);
            return;
        }

        StringBuilder routeBuilder = new StringBuilder();
        int current = targetIdx;
        while (current != -1) {
            routeBuilder.insert(0, graph.getVertexByIndex(current).data);
            if (finalStep.previous[current] != -1) {
                routeBuilder.insert(0, " → ");
            }
            current = finalStep.previous[current];
        }

        lblRute.setText("Ruta:\n" + routeBuilder.toString() + " (costo: " + dist + ")");

        highlightFinalPath(canvas, graph, finalStep, targetIdx);
    }

    /**
     * Dibuja el grafo pero solo resalta la ruta final hacia un destino.
     */
    private static void highlightFinalPath(
            Canvas canvas,
            AdjacencyMatrixGraph<String> graph,
            DijkstraStep step,
            int targetIdx) throws GraphException {

        drawGraph(canvas, graph, step);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(5.0);
        gc.setStroke(COLOR_ARISTA_PATH);

        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double[][] coords = { {w/2,h/5}, {w*3/4,h/3}, {w*3/4,h*2/3}, {w/2,h*4/5}, {w/4,h*2/3}, {w/4,h/3} };

        int current = targetIdx;
        while (current != -1 && step.previous[current] != -1) {
            int prev = step.previous[current];

            double x1 = coords[prev % 6][0];
            double y1 = coords[prev % 6][1];
            double x2 = coords[current % 6][0];
            double y2 = coords[current % 6][1];

            gc.strokeLine(x1, y1, x2, y2);

            current = prev;
        }
    }

    /**
     * Muestra el paso actual: actualiza el log, la lista de distancias y el canvas.
     */
    public static void showCurrentStep(
            DijkstraStep step,
            AdjacencyMatrixGraph<String> graph,
            ListView<String> listStepsLog,
            ListView<String> listViewDistances,
            Canvas canvas) throws GraphException {

        if (step == null || graph == null) return;

        listStepsLog.getItems().clear();
        listStepsLog.getItems().add(step.description);
        listStepsLog.scrollTo(listStepsLog.getItems().size() - 1);

        listViewDistances.getItems().clear();
        String startVertexData = graph.getVertexByIndex(findStartIndex(step, graph)).data;

        listViewDistances.getItems().add("Distancias mínimas desde "+startVertexData+":");
        listViewDistances.getItems().add("--------------------------");

        int n = graph.counter;
        for (int i = 0; i < n; i++) {
            String vertexData = graph.getVertexByIndex(i).data;
            String distValue = (step.distance[i] == Integer.MAX_VALUE) ? "∞" : String.valueOf(step.distance[i]);

            String pathStr = "";
            if (i != findStartIndex(step, graph) && step.distance[i] != Integer.MAX_VALUE) {
                pathStr = " [";
                ArrayList<String> path = new ArrayList<>();
                int currPath = i;
                while (currPath != -1) {
                    path.add(0, graph.getVertexByIndex(currPath).data);
                    currPath = step.previous[currPath];
                }
                pathStr += String.join("→", path);
                pathStr += "]";
            }

            listViewDistances.getItems().add(
                    startVertexData + " → " + vertexData + " = " + distValue + pathStr
            );
        }

        drawGraph(canvas, graph, step);
    }

    private static int findStartIndex(DijkstraStep step, AdjacencyMatrixGraph<String> graph) {
        for(int i=0; i<step.distance.length; i++) {
            if(step.distance[i] == 0) return i;
        }
        return 0; // Fallback
    }

    /**
     * Maneja el Timeline de la ejecución automática.
     */
    public static Timeline getAutoTimeline(Slider slider, Runnable onTick){

        if(autoTimeline != null){
            autoTimeline.stop();
        }

        double msDelay = MAX_SPEED_MS -
                (slider.getValue()/100.0)*(MAX_SPEED_MS-MIN_SPEED_MS);

        autoTimeline = new Timeline(
                new KeyFrame(Duration.millis(msDelay), e -> onTick.run())
        );

        autoTimeline.setCycleCount(Timeline.INDEFINITE);

        return autoTimeline;
    }
}
