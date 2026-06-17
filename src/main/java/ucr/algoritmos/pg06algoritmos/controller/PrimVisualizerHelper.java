package ucr.algoritmos.pg06algoritmos.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import ucr.algoritmos.pg06algoritmos.model.MST.PrimStep;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyListGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyMatrixGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.LinkedGraph;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

public class PrimVisualizerHelper {

    private static final Color COLOR_NODO_DEFAULT = Color.rgb(100, 100, 200); // Azul
    private static final Color COLOR_NODO_MST = Color.rgb(50, 200, 50); // Verde
    private static final Color COLOR_NODO_ACTUAL = Color.rgb(255, 200, 0); // Amarillo/Naranja
    private static final Color COLOR_TEXTO_NODO = Color.WHITE;
    private static final Color COLOR_ARISTA_DEFAULT = Color.rgb(150, 150, 150);
    private static final Color COLOR_ARISTA_MST = Color.rgb(50, 200, 50); // Verde
    private static final Color COLOR_TEXTO_PESO = Color.BLACK;

    private static Timeline autoTimeline;
    private static final double MIN_SPEED_MS = 200;
    private static final double MAX_SPEED_MS = 2000;

    private PrimVisualizerHelper() {}

    // ========== MATRIZ DE ADYACENCIA ==========
    public static void drawGraphMatrix(
            Canvas canvas,
            AdjacencyMatrixGraph<String> graph,
            PrimStep step
    ) throws Exception {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (graph == null || graph.isEmpty())
            return;

        double[][] coords = getCoordinates(
                graph.counter,
                canvas.getWidth(),
                canvas.getHeight()
        );

        drawEdgesMatrix(gc, graph, coords, step);
        drawVerticesMatrix(gc, graph, coords, step);
    }

    // ========== LISTA DE ADYACENCIA ==========
    public static void drawGraphList(
            Canvas canvas,
            AdjacencyListGraph<String> graph,
            PrimStep step
    ) throws Exception {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (graph == null || graph.isEmpty())
            return;

        double[][] coords = getCoordinates(
                graph.counter,
                canvas.getWidth(),
                canvas.getHeight()
        );

        drawEdgesList(gc, graph, coords, step);
        drawVerticesList(gc, graph, coords, step);
    }

    // ========== LISTA ENLAZADA ==========
    public static void drawGraphLinked(
            Canvas canvas,
            LinkedGraph<String> graph,
            PrimStep step
    ) throws Exception {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (graph == null || graph.isEmpty())
            return;

        double[][] coords = getCoordinates(
                graph.size(),
                canvas.getWidth(),
                canvas.getHeight()
        );

        drawEdgesLinked(gc, graph, coords, step);
        drawVerticesLinked(gc, graph, coords, step);
    }

    // ========== INFORMACIÓN DEL PASO ==========
    public static void showCurrentStep(
            PrimStep step,
            AdjacencyMatrixGraph<String> graph,
            ListView<String> stepInfo,
            ListView<String> mstInfo
    ) throws Exception {

        if (step == null || graph == null) {
            return;
        }

        stepInfo.getItems().clear();
        stepInfo.getItems().add("Paso: " + step.description);
        stepInfo.getItems().add("Peso acumulado: " + step.totalWeight);

        mstInfo.getItems().clear();
        mstInfo.getItems().add("Nodos en MST:");
        mstInfo.getItems().add("------------------------");

        for (int i = 0; i < graph.counter; i++) {
            if (step.inMST[i]) {
                String parent = (step.parent[i] == -1) ?
                        "raíz" :
                        graph.getVertexByIndex(step.parent[i]).data;

                mstInfo.getItems().add(
                        graph.getVertexByIndex(i).data + " (padre: " + parent + ")"
                );
            }
        }
    }

    // ========== MOSTRAR MST FINAL ==========
    public static void showMSTAndColor(
            Label lblMST,
            Canvas canvas,
            AdjacencyMatrixGraph<String> graph,
            PrimStep finalStep
    ) throws Exception {

        lblMST.setText("");

        if (finalStep == null || graph == null)
            return;

        drawGraphMatrix(canvas, graph, finalStep);

        StringBuilder sb = new StringBuilder();
        sb.append("Árbol de Expansión Mínima (Prim)\n");
        sb.append("--------------------------------\n");

        for (int i = 0; i < graph.counter; i++) {
            if (finalStep.parent[i] != -1) {
                try {
                    int weight = graph.getWeight(
                            graph.getVertexByIndex(finalStep.parent[i]).data,
                            graph.getVertexByIndex(i).data
                    );

                    sb.append(graph.getVertexByIndex(finalStep.parent[i]).data)
                            .append(" - ")
                            .append(graph.getVertexByIndex(i).data)
                            .append(" (")
                            .append(weight)
                            .append(")\n");
                } catch (ListException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        sb.append("\nPeso total: ").append(finalStep.totalWeight);
        lblMST.setText(sb.toString());
    }

    // ========== TIMELINE AUTOMÁTICO ==========
    public static Timeline getAutoTimeline(
            Slider slider,
            Runnable onTick
    ) {

        if (autoTimeline != null)
            autoTimeline.stop();

        double msDelay = MAX_SPEED_MS -
                (slider.getValue() / 100.0) * (MAX_SPEED_MS - MIN_SPEED_MS);

        autoTimeline = new Timeline(
                new KeyFrame(Duration.millis(msDelay), e -> onTick.run())
        );

        autoTimeline.setCycleCount(Timeline.INDEFINITE);

        return autoTimeline;
    }

    // ========== MÉTODOS PRIVADOS ==========

    private static double[][] getCoordinates(int n, double width, double height) {
        double[][] coords = new double[n][2];
        double centerX = width / 2;
        double centerY = height / 2;
        double radius = Math.min(width, height) / 3;

        for (int i = 0; i < n; i++) {
            double angle = (2 * Math.PI * i) / n;
            coords[i][0] = centerX + radius * Math.cos(angle);
            coords[i][1] = centerY + radius * Math.sin(angle);
        }

        return coords;
    }

    private static void drawEdgesMatrix(
            GraphicsContext gc,
            AdjacencyMatrixGraph<String> graph,
            double[][] coords,
            PrimStep step
    ) throws Exception {

        for (int i = 0; i < graph.counter; i++) {
            for (int j = i + 1; j < graph.counter; j++) {

                if (graph.containsEdge(
                        graph.getVertexByIndex(i).data,
                        graph.getVertexByIndex(j).data)) {

                    int weight = graph.getWeight(
                            graph.getVertexByIndex(i).data,
                            graph.getVertexByIndex(j).data
                    );

                    drawEdge(gc, coords[i][0], coords[i][1],
                            coords[j][0], coords[j][1],
                            weight, i, j, step);
                }
            }
        }
    }

    private static void drawEdgesList(
            GraphicsContext gc,
            AdjacencyListGraph<String> graph,
            double[][] coords,
            PrimStep step
    ) throws Exception {

        for (int i = 0; i < graph.counter; i++) {
            for (int j = i + 1; j < graph.counter; j++) {

                if (graph.containsEdge(
                        graph.getVertexByIndex(i).data,
                        graph.getVertexByIndex(j).data)) {

                    int weight = graph.getWeight(
                            graph.getVertexByIndex(i).data,
                            graph.getVertexByIndex(j).data
                    );

                    drawEdge(gc, coords[i][0], coords[i][1],
                            coords[j][0], coords[j][1],
                            weight, i, j, step);
                }
            }
        }
    }

    private static void drawEdgesLinked(
            GraphicsContext gc,
            LinkedGraph<String> graph,
            double[][] coords,
            PrimStep step
    ) throws Exception {

        int n = graph.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                if (graph.containsEdge(
                        graph.getVertexByIndex(i).data,
                        graph.getVertexByIndex(j).data)) {

                    int weight = graph.getWeight(
                            graph.getVertexByIndex(i).data,
                            graph.getVertexByIndex(j).data
                    );

                    drawEdge(gc, coords[i][0], coords[i][1],
                            coords[j][0], coords[j][1],
                            weight, i, j, step);
                }
            }
        }
    }

    private static void drawEdge(
            GraphicsContext gc,
            double x1, double y1,
            double x2, double y2,
            int weight,
            int i, int j,
            PrimStep step
    ) {

        Color color = COLOR_ARISTA_DEFAULT;
        double lineWidth = 1.5;

        // Si la arista es parte del MST, colorearla de verde
        if (step != null && step.inMST[i] && step.inMST[j]) {
            if ((step.parent[j] == i) || (step.parent[i] == j)) {
                color = COLOR_ARISTA_MST;
                lineWidth = 3.0;
            }
        }

        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(x1, y1, x2, y2);

        // Mostrar peso
        double midX = (x1 + x2) / 2;
        double midY = (y1 + y2) / 2;

        gc.setFill(COLOR_TEXTO_PESO);
        gc.setFont(Font.font(14));
        gc.fillText(String.valueOf(weight), midX, midY - 5);
    }

    private static void drawVerticesMatrix(
            GraphicsContext gc,
            AdjacencyMatrixGraph<String> graph,
            double[][] coords,
            PrimStep step
    ) throws Exception {

        drawVertices(gc, coords, graph.counter,
                i -> graph.getVertexByIndex(i).data, step);
    }

    private static void drawVerticesList(
            GraphicsContext gc,
            AdjacencyListGraph<String> graph,
            double[][] coords,
            PrimStep step
    ) throws Exception {

        drawVertices(gc, coords, graph.counter,
                i -> graph.getVertexByIndex(i).data, step);
    }

    private static void drawVerticesLinked(
            GraphicsContext gc,
            LinkedGraph<String> graph,
            double[][] coords,
            PrimStep step
    ) throws Exception {

        drawVertices(gc, coords, graph.size(),
                i -> graph.getVertexByIndex(i).data, step);
    }

    @FunctionalInterface
    private interface LabelProvider {
        String get(int index) throws Exception;
    }

    private static void drawVertices(
            GraphicsContext gc,
            double[][] coords,
            int n,
            LabelProvider provider,
            PrimStep step
    ) throws Exception {

        gc.setFont(Font.font(16));

        for (int i = 0; i < n; i++) {

            double x = coords[i][0];
            double y = coords[i][1];

            Color nodeColor = COLOR_NODO_DEFAULT;

            if (step != null) {
                if (i == step.currentVertex) {
                    nodeColor = COLOR_NODO_ACTUAL;
                } else if (step.inMST[i]) {
                    nodeColor = COLOR_NODO_MST;
                }
            }

            gc.setFill(nodeColor);
            gc.fillOval(x - 20, y - 20, 40, 40);

            gc.setFill(COLOR_TEXTO_NODO);
            String label = provider.get(i);
            gc.fillText(label, x - 5, y + 5);

            if (step != null && step.key[i] != Integer.MAX_VALUE) {
                gc.setFill(COLOR_TEXTO_PESO);
                gc.setFont(Font.font(12));
                gc.fillText("k:" + step.key[i], x - 15, y - 30);
            }
        }
    }
}