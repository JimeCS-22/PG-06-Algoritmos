package ucr.algoritmos.pg06algoritmos.controller;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ucr.algoritmos.pg06algoritmos.model.Node;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyListGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyMatrixGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.GraphException;
import ucr.algoritmos.pg06algoritmos.model.graph.LinkedGraph;
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

    //ATRIBUTOS TAB LinkedList - Alexander
    @javafx.fxml.FXML
    private Label spaceInfoLinkedList;
    @javafx.fxml.FXML
    private Canvas canvasNodesLinkedList;
    @javafx.fxml.FXML
    private ComboBox numVertexLinkedList;
    @javafx.fxml.FXML
    private Button generateGrafoLinkedList;
    @javafx.fxml.FXML
    private Canvas canvasLinkedList;
    @javafx.fxml.FXML
    private TextField infoAristasLinkedList;
    @javafx.fxml.FXML
    private ListView listViewInfoLinkedList;
    @javafx.fxml.FXML
    private Button loadExamplesLinkedList;
    @javafx.fxml.FXML
    private CheckBox checkGrafoLinkedList;
    @javafx.fxml.FXML
    private Button CleanLinkedList;
    private LinkedGraph<String> graphLinkedList;

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

        try {
            spaceInfo.setText(
                    "Espacio: O(V²) = " + (graph.counter * graph.counter)
                            + " celdas | Grado del grafo: " + graph.getGraphDegree()
                            + " | Aristas: " + graph.totalEdges()
                            + " | existeArista(): O(1)"
                            + " | vecinos(): O(V)"
                            + " | insertarVértice(): O(1)"
                            + " | insertarArista(): O(1)"
            );
        } catch (GraphException | ListException e) {
            spaceInfo.setText("No se pudo calcular la información del grafo.");
        }
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
    private void setupLinkedGraph() {
        numVertexLinkedList.setItems(FXCollections.observableArrayList(2,3,4,5,6,7,8,9,10,11,12));
        numVertexLinkedList.setValue(5);
        generateGrafoLinkedList.setOnAction(e -> handleGenerateLinkedList());
        loadExamplesLinkedList.setOnAction(e -> handleLoadExampleLinkedList());
        CleanLinkedList.setOnAction(e -> handleClearLinkedList());
    }
    private void handleGenerateLinkedList() {
        try {
            int n = (int) numVertexLinkedList.getValue();
            boolean directed = checkGrafoLinkedList.isSelected();
            graphLinkedList = new LinkedGraph<>(directed);
            for(int i=0; i<n; i++) {
                graphLinkedList.addVertex(String.valueOf(i));
            }
            String text = infoAristasLinkedList.getText().trim();
            if(!text.isEmpty()) {
                String[] pairs = text.split(",");
                for(String pair : pairs) {
                    String[] parts = pair.trim().split("-");
                    if(parts.length == 2) {
                        graphLinkedList.addEdge(parts[0], parts[1]);
                    }
                }
            }
            ajustarTamanoCanvasNodos();
            ajustarAnchoCanvasNodos();
            actualizarInformacionLinkedList();
            drawGraphAnimatedLinkedList();
            drawLinkedListStructure();
            spaceInfoLinkedList.setText(
                    "Espacio: O(V+E) | Insertar arista O(V+E) | existeArista O(grado) | Control total de memoria"
            );
        } catch (Exception ex) {
            showError("Error al generar grafo Linked List: "+ex.getMessage());
        }
    }
    private void handleLoadExampleLinkedList() {
        try {
            boolean directed = checkGrafoLinkedList.isSelected();
            graphLinkedList = new LinkedGraph<>(directed);
            String[] provinces = {"SJO", "ALAJ", "HER", "CAR", "TURR", "LIM", "GUA", "PUN", "PZE", "SCA"};
            for(String p : provinces) graphLinkedList.addVertex(p);
            graphLinkedList.addEdge("SJO", "ALAJ");
            graphLinkedList.addEdge("SJO", "HER");
            graphLinkedList.addEdge("SJO", "CAR");
            graphLinkedList.addEdge("ALAJ", "GUA");
            graphLinkedList.addEdge("HER", "ALAJ");
            graphLinkedList.addEdge("CAR", "TURR");
            graphLinkedList.addEdge("TURR", "LIM");
            graphLinkedList.addEdge("LIM", "GUA");
            graphLinkedList.addEdge("PUN", "GUA");
            graphLinkedList.addEdge("PUN", "SJO");
            graphLinkedList.addEdge("PUN", "PZE");
            graphLinkedList.addEdge("PZE", "SCA");
            ajustarTamanoCanvasNodos();
            ajustarAnchoCanvasNodos();
            actualizarInformacionLinkedList();
            drawGraphAnimatedLinkedList();
            drawLinkedListStructure();
            spaceInfoLinkedList.setText(
                    "Espacio: O(V+E) | Insertar arista O(V+E) | existeArista O(grado) | Control total de memoria"
            );
        } catch (Exception ex) {
            showError("Error al cargar ejemplo Linked List: "+ex.getMessage());
        }
    }
    private void handleClearLinkedList() {
        if(graphLinkedList != null) graphLinkedList.clear();
        canvasLinkedList.getGraphicsContext2D().clearRect(0, 0, canvasLinkedList.getWidth(), canvasLinkedList.getHeight());
        canvasNodesLinkedList.getGraphicsContext2D().clearRect(0, 0, canvasNodesLinkedList.getWidth(), canvasNodesLinkedList.getHeight());
        listViewInfoLinkedList.getItems().clear();
        infoAristasLinkedList.clear();
        spaceInfoLinkedList.setText("");
    }
    private void ajustarAnchoCanvasNodos() {
        if (graphLinkedList == null) return;
        int maxVecinos = 0;
        try {
            for (int i = 1; i <= graphLinkedList.size(); i++) {
                int vecinosCount = 0;
                Node<String> aux = graphLinkedList.getNodeByIndex(i).neighbor;
                while (aux != null) {
                    vecinosCount++;
                    aux = aux.neighbor;
                }
                if (vecinosCount > maxVecinos) maxVecinos = vecinosCount;
            }
        } catch (Exception e) {
            // Ignora errores aquí
        }
        double nodeWidth = 60;
        double gapX = 25;
        double nuevoAncho = 20 + nodeWidth + (maxVecinos + 1) * (nodeWidth + gapX); // nodo + vecinos + null
        if (nuevoAncho < 300) nuevoAncho = 300; // ancho mínimo visible
        canvasNodesLinkedList.setWidth(nuevoAncho);
    }
    private void ajustarTamanoCanvasNodos() throws ListException {
        if (graphLinkedList == null) return;

        int maxVecinos = 0;
        int vertices = graphLinkedList.size();

        try {
            for (int i = 1; i <= vertices; i++) {
                int count = 0;
                Node<String> aux = graphLinkedList.getNodeByIndex(i).neighbor;
                while (aux != null) {
                    count++;
                    aux = aux.neighbor;
                }
                if (count > maxVecinos) maxVecinos = count;
            }
        } catch (Exception e) {}

        double nodeWidth = 60;
        double gapX = 25;
        double nodeHeight = 30;
        double gapY = 50;

        double ancho = 20 + nodeWidth + (maxVecinos + 1) * (nodeWidth + gapX);
        double alto = 40 + vertices * gapY;

        if (ancho < 300) ancho = 300;
        if (alto < 200) alto = 200;

        canvasNodesLinkedList.setWidth(ancho);
        canvasNodesLinkedList.setHeight(alto);
    }

    private void actualizarInformacionLinkedList() {
        listViewInfoLinkedList.getItems().clear();
        listViewInfoLinkedList.getItems().add("════ LINKED LIST GRAPH ════");
        listViewInfoLinkedList.getItems().add("Tipo de grafo: " + (graphLinkedList.directed ? "Dirigido" : "No Dirigido"));
        listViewInfoLinkedList.getItems().add("");
        try {
            for (int i = 1; i <= graphLinkedList.size(); i++) {
                String vertice = graphLinkedList.getNodeByIndex(i).data;
                StringBuilder vecinos = new StringBuilder(vertice + " → Vecinos: ");
                Node<String> aux = graphLinkedList.getNodeByIndex(i).neighbor;
                // Siempre agregar "null" al final, aunque no tenga vecinos
                while (aux != null) {
                    vecinos.append(aux.data).append(" → ");
                    aux = aux.neighbor;
                }
                vecinos.append("null");
                listViewInfoLinkedList.getItems().add(vecinos.toString());
            }
            listViewInfoLinkedList.getItems().add("");
            listViewInfoLinkedList.getItems().add("---- Estadísticas ----");
            listViewInfoLinkedList.getItems().add("Vértices (V): " + graphLinkedList.size());
            listViewInfoLinkedList.getItems().add("Aristas (E): " + graphLinkedList.totalEdges());
            listViewInfoLinkedList.getItems().add("");
            listViewInfoLinkedList.getItems().add("---- Grados ----");
            for (int i = 1; i <= graphLinkedList.size(); i++) {
                String vertice = graphLinkedList.getNodeByIndex(i).data;
                int grado = graphLinkedList.getVertexDegree(vertice);
                listViewInfoLinkedList.getItems().add("grado(" + vertice + ") = " + grado);
            }
        } catch (Exception e) {
            showError("Error al actualizar información Linked List: " + e.getMessage());
        }
    }

    private void drawGraphAnimatedLinkedList() throws ListException {
        GraphicsContext gc = canvasLinkedList.getGraphicsContext2D();
        gc.clearRect(0,0,canvasLinkedList.getWidth(),canvasLinkedList.getHeight());
        double centerX = canvasLinkedList.getWidth() / 2;
        double centerY = canvasLinkedList.getHeight() / 2;
        double radius = Math.min(centerX,centerY) - 50;
        int size = graphLinkedList.size();
        if(size == 0) return;
        double[][] positions = new double[size][2];
        for(int i=0;i<size;i++) {
            double angle = 2*Math.PI*i/size;
            positions[i][0] = centerX + radius * Math.cos(angle);
            positions[i][1] = centerY + radius * Math.sin(angle);
        }
        new AnimationTimer() {
            private long lastUpdate = 0;
            private int edgeIndex = 0;
            private final int frameDelay = 15;
            private int countEdges = countTotalEdges();
            private int countTotalEdges(){
                int edges = 0;
                try {
                    for(int i=1; i<=size; i++) {
                        Node<String> node = graphLinkedList.getNodeByIndex(i);
                        Node<String> aux = node.neighbor;
                        while(aux!=null){
                            edges++;
                            aux = aux.neighbor;
                        }
                    }
                } catch(Exception e) {}
                return edges;
            }
            @Override
            public void handle(long now) {
                if(now - lastUpdate < 50_000_000 * frameDelay) return;
                lastUpdate = now;
                gc.clearRect(0,0,canvasLinkedList.getWidth(),canvasLinkedList.getHeight());
                // Dibujar nodos
                for(int i=0; i<size; i++) {
                    gc.setFill(Color.web("#1e293b"));
                    gc.fillOval(positions[i][0]-NODE_RADIUS,positions[i][1]-NODE_RADIUS,NODE_RADIUS *2,NODE_RADIUS *2);
                    gc.setStroke(Color.web("#4ade80"));
                    gc.strokeOval(positions[i][0]-NODE_RADIUS,positions[i][1]-NODE_RADIUS,NODE_RADIUS *2,NODE_RADIUS *2);
                    gc.setFill(Color.WHITE);
                    try {
                        gc.fillText(graphLinkedList.getNodeByIndex(i+1).data, positions[i][0]-8, positions[i][1]+5);
                    } catch (Exception ignored){}
                }
                gc.setStroke(Color.web("#60a5fa"));
                gc.setLineWidth(2);
                int count = 0;
                outerLoop:
                for(int i=1; i<=size; i++) {
                    try {
                        Node<String> node = graphLinkedList.getNodeByIndex(i);
                        int fromIdx = i-1;
                        Node<String> aux = node.neighbor;
                        while(aux!=null){
                            if(count > edgeIndex) break outerLoop;
                            int toIdx = -1;
                            for(int j=1; j<=size;j++){
                                if(graphLinkedList.equals(aux.data, graphLinkedList.getNodeByIndex(j).data)){
                                    toIdx = j-1;
                                    break;
                                }
                            }
                            if(toIdx != -1){
                                if(graphLinkedList.directed){
                                    drawArrow(gc, positions[fromIdx][0], positions[fromIdx][1], positions[toIdx][0], positions[toIdx][1]);
                                } else {
                                    gc.strokeLine(positions[fromIdx][0], positions[fromIdx][1], positions[toIdx][0], positions[toIdx][1]);
                                }
                            }
                            aux = aux.neighbor;
                            count++;
                        }
                    } catch(Exception ignored){}
                }
                edgeIndex++;
                if(edgeIndex >= countEdges) stop();
            }
        }.start();
    }
    private void drawLinkedListStructure() throws ListException {
        GraphicsContext gc = canvasNodesLinkedList.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasNodesLinkedList.getWidth(), canvasNodesLinkedList.getHeight());

        int size;
        try {
            size = graphLinkedList.size();
        } catch (Exception e) {
            return;
        }

        double startX = 20;
        double startY = 40;
        double nodeWidth = 60;
        double nodeHeight = 30;
        double gapX = 25;
        double gapY = 50;
        double margin = 12;
        double arrowSize = 6;

        double maxWidth = canvasNodesLinkedList.getWidth() - 10;

        gc.setFont(Font.font(12));
        gc.setStroke(Color.GREENYELLOW);
        gc.setLineWidth(2);
        gc.setFill(Color.GREENYELLOW);

        // 1. Dibujar todas las líneas y flechas primero
        for (int i = 1; i <= size; i++) {
            Node<String> vertexNode = graphLinkedList.getNodeByIndex(i);
            double y = startY + (i - 1) * gapY;
            double x = startX;

            Node<String> aux = vertexNode.neighbor;
            double xNeighbor = x + nodeWidth + gapX;

            while (aux != null && xNeighbor + nodeWidth <= maxWidth) {
                double lineStartX = x + nodeWidth;
                double lineEndX = xNeighbor - margin;
                double lineY = y + nodeHeight / 2;

                // Línea
                gc.strokeLine(lineStartX, lineY, lineEndX, lineY);

                // Cabeza de flecha (triángulo)
                gc.fillPolygon(
                        new double[]{lineEndX, lineEndX - arrowSize, lineEndX - arrowSize},
                        new double[]{lineY, lineY - arrowSize, lineY + arrowSize},
                        3);

                xNeighbor += nodeWidth + gapX;
                aux = aux.neighbor;
            }
        }

        // 2. Dibujar todos los nodos y texto (vértices y vecinos)
        for (int i = 1; i <= size; i++) {
            try {
                Node<String> vertexNode = graphLinkedList.getNodeByIndex(i);
                double y = startY + (i - 1) * gapY;
                double x = startX;

                // Nodo principal
                gc.setFill(Color.web("#3b82f6"));
                gc.fillRect(x, y, nodeWidth, nodeHeight);
                gc.setFill(Color.WHITE);
                gc.fillText(vertexNode.data, x + 10, y + 20);

                Node<String> aux = vertexNode.neighbor;
                double xNeighbor = x + nodeWidth + gapX;

                while (aux != null) {
                    if (xNeighbor + nodeWidth > maxWidth) {
                        gc.setFill(Color.WHITE);
                        gc.fillText("...", xNeighbor, y + 20);
                        break;
                    }
                    gc.setFill(Color.web("#a855f7"));
                    gc.fillRect(xNeighbor, y, nodeWidth, nodeHeight);
                    gc.setFill(Color.WHITE);
                    gc.fillText(aux.data, xNeighbor + 10, y + 20);

                    xNeighbor += nodeWidth + gapX;
                    aux = aux.neighbor;
                }

                // Nodo null al final si hay espacio
                if (xNeighbor + nodeWidth <= maxWidth) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(xNeighbor, y, nodeWidth, nodeHeight);
                    gc.setFill(Color.BLACK);
                    gc.fillText("null", xNeighbor + 15, y + 20);
                }
                if (xNeighbor + nodeWidth <= maxWidth) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(xNeighbor, y, nodeWidth, nodeHeight);
                    gc.setFill(Color.BLACK);
                    gc.fillText("null", xNeighbor + 15, y + 20);
                }


            } catch (Exception e) {
                // Ignora excepciones para evitar corte en dibujo
            }
        }

    }

}