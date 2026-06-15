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

    // Colores de la interfaz (según la imagen)
    private static final Color COLOR_NODO_DEFAULT = Color.rgb(20, 160, 140); // Turquesa
    private static final Color COLOR_NODO_VISITADO = Color.DARKGRAY;
    private static final Color COLOR_NODO_ACTUAL = Color.rgb(220, 160, 0); // Amarillo/Naranja
    private static final Color COLOR_TEXTO_NODO = Color.WHITE;
    private static final Color COLOR_ARISTA_DEFAULT = Color.rgb(60, 70, 90);
    private static final Color COLOR_ARISTA_PATH = Color.rgb(100, 100, 240); // Azul claro
    private static final Color COLOR_TEXTO_PESO = Color.rgb(220, 160, 0); // Amarillo/Naranja

    // Variables de control de la simulación
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
        gc.clearRect(0, 0, w, h); // Limpiar canvas

        if (graph == null || graph.isEmpty()) return;

        double radius = 25.0; // Radio del nodo
        // Coordenadas fijas para un layout pentagonal como en la imagen para 5 nodos.
        // Si hay más, se debería usar un layout circular o dinámico.
        double[][] coords = {
                {w / 2, h / 5},        // A (Top center)
                {w * 3 / 4, h * 1 / 3}, // B (Mid right)
                {w * 3 / 4, h * 2 / 3}, // C (Bottom right)
                {w / 2, h * 4 / 5},    // D (Bottom center)
                {w / 4, h * 2 / 3},    // E (Bottom left)
                {w / 4, h * 1 / 3}     // F (Mid left)
        };

        // 1. Dibujar Aristas (y pesos)
        gc.setLineWidth(1.0);
        gc.setFont(new Font("System", 12));
        int n = graph.counter;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                try {
                    if (graph.containsEdge(graph.getVertexByIndex(i).data, graph.getVertexByIndex(j).data)) {
                        int weight = graph.getWeight(graph.getVertexByIndex(i).data, graph.getVertexByIndex(j).data);

                        // Solo dibujamos la arista una vez (suponiendo grafo no dirigido)
                        if (i < j) {
                            // Determinar el color de la arista
                            Color edgeColor = COLOR_ARISTA_DEFAULT;
                            double lineWidth = 1.0;

                            if (step != null && step.previous != null) {
                                // Si j es el nodo 'v' que acabamos de relajar y i es su predecesor 'u'
                                if (step.previous[j] == i || step.previous[i] == j) {
                                    edgeColor = COLOR_ARISTA_PATH;
                                    lineWidth = 3.0;
                                }
                            }

                            // Calcular puntos de conexión (centros de nodos)
                            double x1 = coords[i % 6][0];
                            double y1 = coords[i % 6][1];
                            double x2 = coords[j % 6][0];
                            double y2 = coords[j % 6][1];

                            gc.setStroke(edgeColor);
                            gc.setLineWidth(lineWidth);
                            gc.strokeLine(x1, y1, x2, y2);

                            // Dibujar el peso en el medio de la línea
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

        // 2. Dibujar Nodos (y distancias)
        gc.setFont(new Font("System Bold", 16));
        for (int i = 0; i < n; i++) {
            double x = coords[i % 6][0] - radius;
            double y = coords[i % 6][1] - radius;

            // Determinar color del nodo
            Color nodeColor = COLOR_NODO_DEFAULT;
            if (step != null) {
                if (i == step.currentVertex) {
                    nodeColor = COLOR_NODO_ACTUAL; // El nodo extraído de la cola
                } else if (step.visited[i]) {
                    nodeColor = COLOR_NODO_VISITADO; // Nodo ya procesado
                }
            }

            // Dibujar el círculo
            gc.setFill(nodeColor);
            gc.fillOval(x, y, radius * 2, radius * 2);

            // Dibujar el dato del vértice (letra)
            gc.setFill(COLOR_TEXTO_NODO);
            String vertexData = graph.getVertexByIndex(i).data;
            // Centrar texto
            double textWidth = gc.getFont().getSize() * vertexData.length() * 0.6;
            gc.fillText(vertexData, x + radius - textWidth / 2, y + radius + 6);

            // Dibujar la distancia por encima del nodo si ya se ha calculado
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

        // Limpiar el label y redibujar el grafo normal
        lblRute.setText("");
        drawGraph(canvas, graph, finalStep);

        if (finalStep == null || graph == null || targetVertex == null) return;

        // 1. Encontrar el índice del vértice destino
        int targetIdx = -1;
        for (int i = 0; i < graph.counter; i++) {
            if (graph.getVertexByIndex(i).data.equals(targetVertex)) {
                targetIdx = i;
                break;
            }
        }

        if (targetIdx == -1) return;

        // 2. Construir la ruta hacia atrás usando 'finalStep.previous'
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

        // Mostrar texto de la ruta
        lblRute.setText("Ruta:\n" + routeBuilder.toString() + " (costo: " + dist + ")");

        // 3. Resaltar el camino en el grafo
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

        // Volvemos a dibujar las aristas normales
        drawGraph(canvas, graph, step);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(5.0); // Más gruesa
        gc.setStroke(COLOR_ARISTA_PATH);

        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double[][] coords = { {w/2,h/5}, {w*3/4,h/3}, {w*3/4,h*2/3}, {w/2,h*4/5}, {w/4,h*2/3}, {w/4,h/3} };

        // Trazar el camino hacia atrás
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

        // 1. Mostrar Log de Pasos (reemplazando contenido o añadiendo, según prefieras)
        listStepsLog.getItems().clear(); // Si quieres que solo se vea el paso actual
        listStepsLog.getItems().add(step.description);
        listStepsLog.scrollTo(listStepsLog.getItems().size() - 1); // Scroll al final

        // 2. Mostrar Tabla de Distancias Mínimas desde el origen
        listViewDistances.getItems().clear();
        String startVertexData = graph.getVertexByIndex(findStartIndex(step, graph)).data;

        // Título del panel de distancias
        listViewDistances.getItems().add("Distancias mínimas desde "+startVertexData+":");
        listViewDistances.getItems().add("--------------------------");

        int n = graph.counter;
        for (int i = 0; i < n; i++) {
            String vertexData = graph.getVertexByIndex(i).data;
            String distValue = (step.distance[i] == Integer.MAX_VALUE) ? "∞" : String.valueOf(step.distance[i]);

            // Construir el string del predecesor "A → B = 7 [A→B]"
            String pathStr = "";
            if (i != findStartIndex(step, graph) && step.distance[i] != Integer.MAX_VALUE) {
                pathStr = " [";
                // Rastrear la ruta hasta el origen para mostrarla en el panel
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

        // 3. Dibujar el Grafo en el Canvas
        drawGraph(canvas, graph, step);
    }

    private static int findStartIndex(DijkstraStep step, AdjacencyMatrixGraph<String> graph) {
        // En el primer paso del log, la distancia del origen es 0. Buscamos ese índice.
        // Si no, podríamos pasar el índice de inicio como parámetro a todas las funciones.
        for(int i=0; i<step.distance.length; i++) {
            if(step.distance[i] == 0) return i;
        }
        return 0; // Fallback
    }

    /**
     * Maneja el Timeline de la ejecución automática.
     */
    public static Timeline getAutoTimeline(Slider sliderVelo, Runnable onTick) {
        if (autoTimeline != null) {
            autoTimeline.stop();
        }

        // Crear una nueva línea de tiempo
        autoTimeline = new Timeline();
        autoTimeline.setCycleCount(Timeline.INDEFINITE); // Repetir hasta que lo detengamos

        // El primer KeyFrame dura 0, para que el primer tick sea instantáneo.
        autoTimeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, e -> onTick.run()));

        // El segundo KeyFrame define la duración del delay, basado en el slider.
        updateTimelineSpeed(sliderVelo);

        return autoTimeline;
    }

    /**
     * Actualiza la velocidad del Timeline basándose en el valor del Slider.
     */
    public static void updateTimelineSpeed(Slider sliderVelo) {
        if (autoTimeline == null || autoTimeline.getKeyFrames().size() < 2) return;

        // Mapear el valor del slider (0-100) al rango de milisegundos (2000 - 200)
        // Menos valor del slider = más ms (más lento). Más valor = menos ms (más rápido).
        double sliderValue = sliderVelo.getValue();
        double msDelay = MAX_SPEED_MS - (sliderValue / 100.0) * (MAX_SPEED_MS - MIN_SPEED_MS);

        // Actualizar la duración del segundo KeyFrame
        KeyFrame existingDelayFrame = autoTimeline.getKeyFrames().get(1);
        autoTimeline.getKeyFrames().set(1, new KeyFrame(Duration.millis(msDelay)));
    }
}
