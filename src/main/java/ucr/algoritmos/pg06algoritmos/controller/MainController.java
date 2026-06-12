package ucr.algoritmos.pg06algoritmos.controller;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyListGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyMatrixGraph;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    //ATRIBUTOS TAB AdjacencyMatrixGraph - Jimena
    @javafx.fxml.FXML
    private Button generateGrafo;
    @javafx.fxml.FXML
    private ComboBox<Integer> numVertex;
    @javafx.fxml.FXML
    private Canvas canvasMatriz;
    @javafx.fxml.FXML
    private TextField infoAristas;
    @javafx.fxml.FXML
    private TabPane mainTabs;
    @javafx.fxml.FXML
    private CheckBox checkGrafo;
    @javafx.fxml.FXML
    private Canvas canvasGrafoMatriz;
    @javafx.fxml.FXML
    private Label lblTours;
    @javafx.fxml.FXML
    private Button Clean;
    @javafx.fxml.FXML
    private Label spaceInfo;
    @javafx.fxml.FXML
    private Button loadExamples;
    @javafx.fxml.FXML
    private ListView<String> listViewInfoMatriz;

    private AdjacencyMatrixGraph<String> graph;
    private final double NODE_RADIUS = 20;

    //ATRIBUTOS TAB AdjacencyListRGraph - Camila
    @javafx.fxml.FXML
    private Button btnLoadExamplesAdj;
    @javafx.fxml.FXML
    private Button btnGenerateGrafoAdj;
    @javafx.fxml.FXML
    private ComboBox<Integer> numVertexAdj;
    @javafx.fxml.FXML
    private TextField infoAristasAdj;
    @javafx.fxml.FXML
    private CheckBox checkGrafoAdj;
    @javafx.fxml.FXML
    private Button btnCleanAdj;
    @javafx.fxml.FXML
    private Label lblGraphInfo;
    private AdjacencyListGraph<String> graphAdj;
    @javafx.fxml.FXML
    private Canvas canvasAdj;
    @javafx.fxml.FXML
    private ListView listViewInfoAdj;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMatrixGraph();
        setupAdjListGraph();
        setupLinkedGraph();
    }

    // --------------Metódos para el tab Adjacency Matrix Graph - Jimena ---------------------
    private void setupMatrixGraph() {
        numVertex.setItems(FXCollections.observableArrayList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        numVertex.setValue(5);

        generateGrafo.setOnAction(e -> handleGenerate());
        loadExamples.setOnAction(e -> handleLoadCR());
        Clean.setOnAction(e -> handleClear());
    }

    private void handleGenerate() {
        try {
            int n = numVertex.getValue();
            boolean directed = checkGrafo.isSelected();
            graph = new AdjacencyMatrixGraph<>(n, directed);

            for (int i = 0; i < n; i++) {
                graph.addVertex(String.valueOf(i));
            }

            // Parsear aristas (u-v, x-y)
            String text = infoAristas.getText().trim();
            if (!text.isEmpty()) {
                String[] pairs = text.split(",");
                for (String pair : pairs) {
                    String[] parts = pair.trim().split("-");
                    if (parts.length == 2) {
                        graph.addEdge(parts[0], parts[1]);
                    }
                }
            }
            startDrawingAnimation();
        } catch (Exception e) {
            showError("Error al generar el grafo: " + e.getMessage());
        }
    }

    private void handleLoadCR() {
        boolean directed = checkGrafo.isSelected();
        graph = new AdjacencyMatrixGraph<>(10, directed);
        String[] provinces = {"SJO", "ALAJ", "HER", "CAR", "TURR", "LIM", "GUA", "PUN", "PZE", "SCA"};

        try {
            for (String p : provinces) graph.addVertex(p);

            graph.addEdge("SJO", "ALAJ");
            graph.addEdge("SJO", "HER");
            graph.addEdge("SJO", "CAR");
            graph.addEdge("ALAJ", "GUA");
            graph.addEdge("HER", "ALAJ");
            graph.addEdge("CAR", "TURR");
            graph.addEdge("TURR", "LIM");
            graph.addEdge("LIM", "GUA");
            graph.addEdge("PUN", "GUA");
            graph.addEdge("PUN", "SJO");
            graph.addEdge("PUN", "PZE");
            graph.addEdge("PZE", "SCA");

            startDrawingAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClear() {
        if (graph != null) graph.clear();
        canvasGrafoMatriz.getGraphicsContext2D().clearRect(0, 0, canvasGrafoMatriz.getWidth(), canvasGrafoMatriz.getHeight());
        canvasMatriz.getGraphicsContext2D().clearRect(0, 0, canvasMatriz.getWidth(), canvasMatriz.getHeight());
        listViewInfoMatriz.getItems().clear();
        infoAristas.clear();
    }

    private void startDrawingAnimation() {
        actualizarInformacion();
        drawGraphAnimated();
        drawMatrixVisual();
        spaceInfo.setText("Espacio: O(V²) = " + (graph.n * graph.n));
    }

    private void drawGraphAnimated() {
        GraphicsContext gc = canvasGrafoMatriz.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasGrafoMatriz.getWidth(), canvasGrafoMatriz.getHeight());

        double centerX = canvasGrafoMatriz.getWidth() / 2;
        double centerY = canvasGrafoMatriz.getHeight() / 2;
        double radius = Math.min(centerX, centerY) - 50;

        int size = graph.counter;
        double[][] positions = new double[size][2];
        for (int i = 0; i < size; i++) {
            double angle = 2 * Math.PI * i / size;
            positions[i][0] = centerX + radius * Math.cos(angle);
            positions[i][1] = centerY + radius * Math.sin(angle);
        }

        new AnimationTimer() {
            private long lastUpdate = 0;
            private int edgeIndex = 0;
            private int frameDelay = 15;

            @Override
            public void handle(long now) {
                if (now - lastUpdate < 50_000_000 * frameDelay) return;
                lastUpdate = now;

                gc.clearRect(0, 0, canvasGrafoMatriz.getWidth(), canvasGrafoMatriz.getHeight());

                for (int i = 0; i < size; i++) {
                    gc.setFill(Color.web("#1e293b"));
                    gc.fillOval(positions[i][0] - NODE_RADIUS, positions[i][1] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                    gc.setStroke(Color.web("#4ade80"));
                    gc.strokeOval(positions[i][0] - NODE_RADIUS, positions[i][1] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                    gc.setFill(Color.WHITE);
                    gc.fillText(graph.getVertexByIndex(i).data, positions[i][0] - 8, positions[i][1] + 5);
                }

                gc.setStroke(Color.web("#60a5fa"));
                gc.setLineWidth(2);
                int count = 0;
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        try {
                            if (graph.containsEdge(graph.getVertexByIndex(i).data, graph.getVertexByIndex(j).data)) {
                                if (count <= edgeIndex) {
                                    if (graph.directed) {
                                        drawArrow(gc, positions[i][0], positions[i][1], positions[j][0], positions[j][1]);
                                    } else {
                                        gc.strokeLine(positions[i][0], positions[i][1], positions[j][0], positions[j][1]);
                                    }
                                }
                                count++;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }

                edgeIndex++;
                if (edgeIndex >= count) stop();
            }
        }.start();
    }

    private void drawMatrixVisual() {
        GraphicsContext gc = canvasMatriz.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasMatriz.getWidth(), canvasMatriz.getHeight());

        int size = graph.counter;
        double cellSize = Math.min(canvasMatriz.getWidth() / (size + 1), canvasMatriz.getHeight() / (size + 1));

        gc.setFont(new Font(10));
        for (int i = 0; i < size; i++) {
            // Cabeceras
            gc.setFill(Color.web("#60a5fa"));
            gc.fillText(graph.getVertexByIndex(i).data, (i + 1) * cellSize + 5, cellSize - 5);
            gc.fillText(graph.getVertexByIndex(i).data, 5, (i + 1) * cellSize + cellSize / 2);

            for (int j = 0; j < size; j++) {
                try {
                    boolean edge = graph.containsEdge(graph.getVertexByIndex(i).data, graph.getVertexByIndex(j).data);

                    gc.setFill(edge ? Color.web("#064e3b") : Color.web("#0f172a"));
                    gc.fillRect((j + 1) * cellSize, (i + 1) * cellSize, cellSize - 2, cellSize - 2);

                    gc.setFill(edge ? Color.web("#4ade80") : Color.web("#475569"));
                    gc.fillText(edge ? "1" : "0", (j + 1) * cellSize + cellSize / 3, (i + 1) * cellSize + cellSize / 1.5);

                } catch (Exception ignored) {
                }
            }
        }
    }

    private void actualizarInformacion() {

        listViewInfoMatriz.getItems().clear();

        try {

            listViewInfoMatriz.getItems().add("════ MATRIZ DE ADYACENCIA ════");
            listViewInfoMatriz.getItems().add("");

            String encabezado = "     ";

            for (int i = 0; i < graph.counter; i++) {
                encabezado += graph.getVertexByIndex(i).data + " ";
            }

            listViewInfoMatriz.getItems().add(encabezado);

            for (int i = 0; i < graph.counter; i++) {

                String fila = graph.getVertexByIndex(i).data + "   ";

                for (int j = 0; j < graph.counter; j++) {

                    if (graph.containsEdge(
                            graph.getVertexByIndex(i).data,
                            graph.getVertexByIndex(j).data))
                        fila += "1 ";
                    else
                        fila += "0 ";

                }

                listViewInfoMatriz.getItems().add(fila);
            }

            listViewInfoMatriz.getItems().add("");
            listViewInfoMatriz.getItems().add("----- Estadísticas -----");
            listViewInfoMatriz.getItems().add("Vértices (V): " + graph.size());
            listViewInfoMatriz.getItems().add("Aristas (E): " + graph.totalEdges());

            double densidad;

            if (graph.directed)
                densidad = (double) graph.totalEdges() /
                        (graph.size() * (graph.size() - 1));
            else
                densidad = (double) (2 * graph.totalEdges()) /
                        (graph.size() * (graph.size() - 1));

            listViewInfoMatriz.getItems().add(
                    String.format("Densidad: %.1f%%", densidad * 100));

            listViewInfoMatriz.getItems().add(
                    "Grado del Grafo: " + graph.getGraphDegree());

            listViewInfoMatriz.getItems().add("");

            listViewInfoMatriz.getItems().add("----- Vecinos -----");

            for (int i = 0; i < graph.counter; i++) {

                String vecinos = graph.getVertexByIndex(i).data + ": ";

                boolean existe = false;

                for (int j = 0; j < graph.counter; j++) {

                    if (graph.containsEdge(
                            graph.getVertexByIndex(i).data,
                            graph.getVertexByIndex(j).data)) {

                        vecinos += graph.getVertexByIndex(j).data + " ";
                        existe = true;
                    }
                }

                if (!existe)
                    vecinos += "(sin vecinos)";

                listViewInfoMatriz.getItems().add(vecinos);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }

    /*
   ----------- Metódos para el tab AdjacencyListGraph - Camila----------------------
     */
    private void setupAdjListGraph() {
        numVertexAdj.setItems(FXCollections.observableArrayList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        numVertexAdj.setValue(5);

        btnGenerateGrafoAdj.setOnAction(e -> handleGenerateAdj());
        btnLoadExamplesAdj.setOnAction(e -> handleLoadCrAdj());
        btnCleanAdj.setOnAction(e -> handleClearAdj());
    }

    private void handleGenerateAdj() {
        try {
            int n = numVertexAdj.getValue();
            boolean directed = checkGrafoAdj.isSelected();
            graphAdj = new AdjacencyListGraph<>(n, directed);

            for (int i = 0; i < n; i++) {
                graphAdj.addVertex(String.valueOf(i));
            }

            // Parsear aristas (u-v, x-y)
            String text = infoAristasAdj.getText().trim();
            if (!text.isEmpty()) {
                String[] pairs = text.split(",");
                for (String pair : pairs) {
                    String[] parts = pair.trim().split("-");
                    if (parts.length == 2) {
                        graphAdj.addEdge(parts[0], parts[1]);
                    }
                }
            }
            startDrawingAnimationAdj();
        } catch (Exception e) {
            showError("Error al generar el grafo: " + e.getMessage());
        }
    }

    private void handleLoadCrAdj() {
        boolean directed = checkGrafoAdj.isSelected();
        graphAdj = new AdjacencyListGraph<>(10, directed);
        String[] provinces = {"SJO", "ALAJ", "HER", "CAR", "TURR", "LIM", "GUA", "PUN", "PZE", "SCA"};

        try {
            for (String p : provinces) graphAdj.addVertex(p);

            graphAdj.addEdge("SJO", "ALAJ");
            graphAdj.addEdge("SJO", "HER");
            graphAdj.addEdge("SJO", "CAR");
            graphAdj.addEdge("ALAJ", "GUA");
            graphAdj.addEdge("HER", "ALAJ");
            graphAdj.addEdge("CAR", "TURR");
            graphAdj.addEdge("TURR", "LIM");
            graphAdj.addEdge("LIM", "GUA");
            graphAdj.addEdge("PUN", "GUA");
            graphAdj.addEdge("PUN", "SJO");
            graphAdj.addEdge("PUN", "PZE");
            graphAdj.addEdge("PZE", "SCA");

            startDrawingAnimationAdj();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClearAdj() {
        if (graphAdj != null) graphAdj.clear();
        canvasAdj.getGraphicsContext2D().clearRect(0, 0, canvasAdj.getWidth(), canvasAdj.getHeight());
        listViewInfoAdj.getItems().clear();
        infoAristasAdj.clear();
        lblGraphInfo.setText("--");
    }

    private void startDrawingAnimationAdj() throws ListException {
        actualizarInformacionAdj();
        drawGraphAnimatedAdj();
        drawMatrixVisualAdj();
        lblGraphInfo.setText("Espacio: O(V²) = "
                + (graphAdj.counter + " + " + graphAdj.totalEdges()
                + " = " + (graphAdj.counter + graphAdj.totalEdges()) + " entradas "
                + "| existeArista: O (grado) | vecinos: O(1) | insertar: O(1)"));
    }

    private void drawGraphAnimatedAdj() {
        GraphicsContext gc = canvasAdj.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasAdj.getWidth(), canvasAdj.getHeight());

        double centerX = canvasAdj.getWidth() / 2;
        double centerY = canvasAdj.getHeight() / 2;
        double radius = Math.min(centerX, centerY) - 50;

        int size = graphAdj.counter;
        double[][] positions = new double[size][2];
        for (int i = 0; i < size; i++) {
            double angle = 2 * Math.PI * i / size;
            positions[i][0] = centerX + radius * Math.cos(angle);
            positions[i][1] = centerY + radius * Math.sin(angle);
        }

        new AnimationTimer() {
            private long lastUpdate = 0;
            private int edgeIndex = 0;
            private int frameDelay = 15;

            @Override
            public void handle(long now) {
                if (now - lastUpdate < 50_000_000 * frameDelay) return;
                lastUpdate = now;

                gc.clearRect(0, 0, canvasAdj.getWidth(), canvasAdj.getHeight());

                for (int i = 0; i < size; i++) {
                    gc.setFill(Color.web("#1e293b"));
                    gc.fillOval(positions[i][0] - NODE_RADIUS, positions[i][1] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                    gc.setStroke(Color.web("#4ade80"));
                    gc.strokeOval(positions[i][0] - NODE_RADIUS, positions[i][1] - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                    gc.setFill(Color.WHITE);
                    gc.fillText(graphAdj.getVertexByIndex(i).data, positions[i][0] - 8, positions[i][1] + 5);
                }

                gc.setStroke(Color.web("#60a5fa"));
                gc.setLineWidth(2);
                int count = 0;
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        try {
                            if (graphAdj.containsEdge(graphAdj.getVertexByIndex(i).data, graphAdj.getVertexByIndex(j).data)) {
                                if (count <= edgeIndex) {
                                    if (graphAdj.directed) {
                                        drawArrow(gc, positions[i][0], positions[i][1], positions[j][0], positions[j][1]);
                                    } else {
                                        gc.strokeLine(positions[i][0], positions[i][1], positions[j][0], positions[j][1]);
                                    }
                                }
                                count++;
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }

                edgeIndex++;
                if (edgeIndex >= count) stop();
            }
        }.start();
    }

    private void drawMatrixVisualAdj() {
        GraphicsContext gc = canvasAdj.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasAdj.getWidth(), canvasAdj.getHeight());

        int size = graphAdj.counter;
        double cellSize = Math.min(canvasAdj.getWidth() / (size + 1), canvasAdj.getHeight() / (size + 1));

        gc.setFont(new Font(10));
        for (int i = 0; i < size; i++) {
            // Cabeceras
            gc.setFill(Color.web("#60a5fa"));
            gc.fillText(graphAdj.getVertexByIndex(i).data, (i + 1) * cellSize + 5, cellSize - 5);
            gc.fillText(graphAdj.getVertexByIndex(i).data, 5, (i + 1) * cellSize + cellSize / 2);

            for (int j = 0; j < size; j++) {
                try {
                    boolean edge = graphAdj.containsEdge(graphAdj.getVertexByIndex(i).data, graphAdj.getVertexByIndex(j).data);

                    gc.setFill(edge ? Color.web("#064e3b") : Color.web("#0f172a"));
                    gc.fillRect((j + 1) * cellSize, (i + 1) * cellSize, cellSize - 2, cellSize - 2);

                    gc.setFill(edge ? Color.web("#4ade80") : Color.web("#475569"));
                    gc.fillText(edge ? "1" : "0", (j + 1) * cellSize + cellSize / 3, (i + 1) * cellSize + cellSize / 1.5);

                } catch (Exception ignored) {
                }
            }
        }
    }
    //metodo aux para los grafos dirigidos
    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        gc.strokeLine(x1, y1, x2, y2);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 10;
        double arrowAngle = Math.PI / 8;
        double xArrow1 = x2 - arrowLength * Math.cos(angle - arrowAngle);
        double yArrow1 = y2 - arrowLength * Math.sin(angle - arrowAngle);
        double xArrow2 = x2 - arrowLength * Math.cos(angle + arrowAngle);
        double yArrow2 = y2 - arrowLength * Math.sin(angle + arrowAngle);
        gc.strokeLine(x2, y2, xArrow1, yArrow1);
        gc.strokeLine(x2, y2, xArrow2, yArrow2);
    }

    private void actualizarInformacionAdj() throws ListException {
        listViewInfoAdj.getItems().clear();

        listViewInfoAdj.getItems().add("════ LISTA DE ADYACENCIA ════");
        listViewInfoAdj.getItems().add("Tipo de grafo: " + (graphAdj.directed ? "Dirigido" : "No Dirigido"));
        listViewInfoAdj.getItems().add("");

        // Mostrar lista de adyacencia
        for (int i = 0; i < graphAdj.size(); i++) {
            String vertice = graphAdj.getVertexByIndex(i).data;
            StringBuilder vecinos = new StringBuilder(vertice + " → Vecinos: ");

            // recorrer los vecinos del vértice
            boolean tieneVecinos = false;
            for (int j = 0; j < graphAdj.size(); j++) {
                String vecino = graphAdj.getVertexByIndex(j).data;
                if (graphAdj.containsEdge(vertice, vecino)) {
                    vecinos.append(vecino).append(" → ");
                    tieneVecinos = true;
                }
            }

            if (!tieneVecinos) {
                vecinos.append("(sin vecinos)");
            } else {
                vecinos.append("null");
            }

            listViewInfoAdj.getItems().add(vecinos.toString());
        }

        // Estadísticas
        listViewInfoAdj.getItems().add("");
        listViewInfoAdj.getItems().add("---- Estadísticas ----");
        listViewInfoAdj.getItems().add("Vértices (V): " + graphAdj.size());
        listViewInfoAdj.getItems().add("Aristas (E): " + graphAdj.totalEdges());
        listViewInfoAdj.getItems().add("");

        // Grados
        listViewInfoAdj.getItems().add("---- Grados ----");
        for (int i = 0; i < graphAdj.size(); i++) {
            String vertice = graphAdj.getVertexByIndex(i).data;
            int grado = graphAdj.getVertexDegree(vertice);
            listViewInfoAdj.getItems().add("grado(" + vertice + ") = " + grado);
        }
    }




    /*
    --------------Metódos para el tab Linked List - Alexander------------------------
    */

        private void setupLinkedGraph () {

        }

}