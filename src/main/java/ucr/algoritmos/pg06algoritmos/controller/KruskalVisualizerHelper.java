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

import ucr.algoritmos.pg06algoritmos.model.MST.Kruskal.KruskalAction;
import ucr.algoritmos.pg06algoritmos.model.MST.Kruskal.KruskalEdge;
import ucr.algoritmos.pg06algoritmos.model.MST.Kruskal.KruskalStep;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyListGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.AdjacencyMatrixGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.LinkedGraph;
import ucr.algoritmos.pg06algoritmos.model.graph.GraphException;
import ucr.algoritmos.pg06algoritmos.model.linkedList.ListException;

public class KruskalVisualizerHelper {

    private static final Color EDGE_DEFAULT = Color.LIGHTGRAY;
    private static final Color EDGE_MST = Color.LIMEGREEN;
    private static final Color EDGE_CURRENT = Color.ORANGE;
    private static final Color NODE_COLOR = Color.TEAL;
    private static final Color TEXT_COLOR = Color.WHITE;

    private static Timeline autoTimeline;

    private static final double MIN_SPEED_MS = 200;
    private static final double MAX_SPEED_MS = 2000;

    private KruskalVisualizerHelper(){}

    //====================================================
    // MATRIX GRAPH
    //====================================================

    public static void drawGraphMatrix(
            Canvas canvas,
            AdjacencyMatrixGraph<String> graph,
            KruskalStep step
    ) throws Exception {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );

        if(graph == null || graph.isEmpty())
            return;

        double[][] coords =
                getCoordinates(
                        graph.counter,
                        canvas.getWidth(),
                        canvas.getHeight()
                );

        drawEdgesMatrix(gc, graph, coords, step);

        drawVerticesMatrix(gc, graph, coords);
    }

    //====================================================
    // LIST GRAPH
    //====================================================

    public static void drawGraphList(
            Canvas canvas,
            AdjacencyListGraph<String> graph,
            KruskalStep step
    ) throws Exception {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );

        if(graph == null || graph.isEmpty())
            return;

        double[][] coords =
                getCoordinates(
                        graph.counter,
                        canvas.getWidth(),
                        canvas.getHeight()
                );

        drawEdgesList(gc, graph, coords, step);

        drawVerticesList(gc, graph, coords);
    }

    //====================================================
    // LINKED GRAPH
    //====================================================

    public static void drawGraphLinked(
            Canvas canvas,
            LinkedGraph<String> graph,
            KruskalStep step
    ) throws Exception {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );

        if(graph == null || graph.isEmpty())
            return;

        double[][] coords =
                getCoordinates(
                        graph.size(),
                        canvas.getWidth(),
                        canvas.getHeight()
                );

        drawEdgesLinked(gc, graph, coords, step);

        drawVerticesLinked(gc, graph, coords);
    }

    //====================================================
    // STEP INFO
    //====================================================

    public static void showCurrentStep(
            KruskalStep step,
            ListView<String> stepInfo,
            ListView<String> mstInfo
    ) throws Exception {

        stepInfo.getItems().clear();

        stepInfo.getItems().add(
                "Arista: "
                        + step.getCurrentEdge()
        );

        stepInfo.getItems().add(
                "Estado: "
                        + step.getAction()
        );

        stepInfo.getItems().add(
                "Peso acumulado: "
                        + step.getCurrentWeight()
        );

        mstInfo.getItems().clear();

        mstInfo.getItems().add(
                "Aristas MST"
        );

        for(int i=1;i<=step.getMstEdges().size();i++){

            mstInfo.getItems().add(
                    step.getMstEdges().get(i).toString()
            );
        }
    }

    //====================================================
    // TIMELINE
    //====================================================

    public static Timeline getAutoTimeline(
            Slider slider,
            Runnable onTick
    ){

        if(autoTimeline != null)
            autoTimeline.stop();

        double msDelay =
                MAX_SPEED_MS -
                        (slider.getValue()/100.0)
                                * (MAX_SPEED_MS-MIN_SPEED_MS);

        autoTimeline =
                new Timeline(
                        new KeyFrame(
                                Duration.millis(msDelay),
                                e -> onTick.run()
                        )
                );

        autoTimeline.setCycleCount(
                Timeline.INDEFINITE
        );

        return autoTimeline;
    }

    //====================================================
    // COORDINATES
    //====================================================

    private static double[][] getCoordinates(
            int n,
            double width,
            double height
    ){

        double[][] coords =
                new double[n][2];

        double centerX = width / 2;
        double centerY = height / 2;

        double radius =
                Math.min(width,height) / 3;

        for(int i=0;i<n;i++){

            double angle =
                    (2*Math.PI*i)/n;

            coords[i][0] =
                    centerX
                            + radius*Math.cos(angle);

            coords[i][1] =
                    centerY
                            + radius*Math.sin(angle);
        }

        return coords;
    }

    //====================================================
    // MATRIX EDGES
    //====================================================

    private static void drawEdgesMatrix(
            GraphicsContext gc,
            AdjacencyMatrixGraph<String> graph,
            double[][] coords,
            KruskalStep step
    ) throws Exception {

        for(int i=0;i<graph.counter;i++){

            for(int j=i+1;j<graph.counter;j++){

                if(graph.containsEdge(
                        graph.getVertexByIndex(i).data,
                        graph.getVertexByIndex(i).data
                )){

                    drawEdge(
                            gc,
                            coords[i][0],
                            coords[i][1],
                            coords[j][0],
                            coords[j][1],
                            graph.getVertexByIndex(i).data,
                            graph.getVertexByIndex(j).data,
                            step, graph.getWeight(graph.getVertexByIndex(i).data,graph.getVertexByIndex(i).data)
                    );
                }
            }
        }
    }

    //====================================================
    // LIST EDGES
    //====================================================

    private static void drawEdgesList(
            GraphicsContext gc,
            AdjacencyListGraph<String> graph,
            double[][] coords,
            KruskalStep step
    ) throws Exception {

        drawEdgesMatrix(gc, graph, coords, step);
    }

    //====================================================
    // LINKED EDGES
    //====================================================

    private static void drawEdgesLinked(
            GraphicsContext gc,
            LinkedGraph<String> graph,
            double[][] coords,
            KruskalStep step
    ) throws Exception {

        int n = graph.size();

        for(int i=0;i<n;i++){

            for(int j=i+1;j<n;j++){

                String a =
                        graph.getVertexByIndex(i).data;

                String b =
                        graph.getVertexByIndex(j).data;

                if(graph.containsEdge(a,b)){

                    drawEdge(
                            gc,
                            coords[i][0],
                            coords[i][1],
                            coords[j][0],
                            coords[j][1],
                            a,
                            b,
                            step, graph.getWeight(a,b)
                    );
                }
            }
        }
    }

    //====================================================
    // DRAW EDGE
    //====================================================

    private static void drawEdge(
            GraphicsContext gc,
            double x1,
            double y1,
            double x2,
            double y2,
            String source,
            String destination,
            KruskalStep step,
            double weight
    ) throws Exception {

        Color color = EDGE_DEFAULT;

        if(step != null){

            for(int i=1;
                i<=step.getMstEdges().size();
                i++){

                KruskalEdge edge =
                        step.getMstEdges().get(i);

                if(matches(edge,
                        source,
                        destination)){

                    color = EDGE_MST;
                }
            }

            if(matches(
                    step.getCurrentEdge(),
                    source,
                    destination
            )){
                color = EDGE_CURRENT;
            }
        }

        gc.setStroke(color);
        gc.setLineWidth(3);
        gc.strokeLine(x1,y1,x2,y2);
        // punto medio
        double mx = (x1 + x2) / 2;
        double my = (y1 + y2) / 2;

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(17));

// pequeño fondo para que se lea mejor
        gc.fillText(String.valueOf(weight), mx, my);
    }

    //====================================================
    // VERTICES
    //====================================================

    private static void drawVerticesMatrix(
            GraphicsContext gc,
            AdjacencyMatrixGraph<String> graph,
            double[][] coords
    ) throws Exception {

        drawVertices(
                gc,
                coords,
                graph.counter,
                i -> graph.getVertexByIndex(i).data
        );
    }

    private static void drawVerticesList(
            GraphicsContext gc,
            AdjacencyListGraph<String> graph,
            double[][] coords
    ) throws Exception {

        drawVertices(
                gc,
                coords,
                graph.counter,
                i -> graph.getVertexByIndex(i).data
        );
    }

    private static void drawVerticesLinked(
            GraphicsContext gc,
            LinkedGraph<String> graph,
            double[][] coords
    ) throws Exception {

        drawVertices(
                gc,
                coords,
                graph.size(),
                i -> graph.getVertexByIndex(i).data
        );
    }

    private interface LabelProvider{
        String get(int index) throws Exception;
    }

    private static void drawVertices(
            GraphicsContext gc,
            double[][] coords,
            int n,
            LabelProvider provider
    ) throws Exception {

        gc.setFont(
                Font.font(16)
        );

        for(int i=0;i<n;i++){

            double x =
                    coords[i][0];

            double y =
                    coords[i][1];

            gc.setFill(NODE_COLOR);

            gc.fillOval(
                    x-20,
                    y-20,
                    40,
                    40
            );

            gc.setFill(TEXT_COLOR);

            gc.fillText(
                    provider.get(i),
                    x-5,
                    y+5
            );
        }
    }

    //====================================================
    // HELPERS
    //====================================================

    private static boolean matches(
            KruskalEdge edge,
            String a,
            String b
    ){

        return
                (edge.getSource().equals(a)
                        &&
                        edge.getDestination().equals(b))
                        ||
                        (edge.getSource().equals(b)
                                &&
                                edge.getDestination().equals(a));
    }

    /**
     * Muestra el MST final y resalta todas las aristas
     * aceptadas por Kruskal.
     */
    public static void showMSTAndColor(
            Label lblMST,
            Canvas canvas,
            AdjacencyMatrixGraph<String> graph,
            KruskalStep finalStep
    ) throws Exception {

        lblMST.setText("");

        if(finalStep == null || graph == null)
            return;

        drawGraphMatrix(canvas, graph, finalStep);

        StringBuilder sb = new StringBuilder();

        sb.append("Árbol de Expansión Mínima\n");
        sb.append("------------------------\n");

        for(int i = 1;
            i <= finalStep.getMstEdges().size();
            i++) {

            KruskalEdge edge =
                    finalStep.getMstEdges().get(i);

            sb.append(edge.getSource())
                    .append(" - ")
                    .append(edge.getDestination())
                    .append(" (")
                    .append(edge.getWeight())
                    .append(")\n");
        }

        sb.append("\nPeso total: ")
                .append(finalStep.getCurrentWeight());

        lblMST.setText(sb.toString());

        highlightFinalMST(
                canvas,
                graph,
                finalStep
        );
    }


    private static void highlightFinalMST(
            Canvas canvas,
            AdjacencyMatrixGraph<String> graph,
            KruskalStep step
    ) throws Exception {

        drawGraphMatrix(canvas, graph, step);

        GraphicsContext gc =
                canvas.getGraphicsContext2D();

        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(5);

        double w = canvas.getWidth();
        double h = canvas.getHeight();

        double[][] coords = {
                {w/2,h/5},
                {w*3/4,h/3},
                {w*3/4,h*2/3},
                {w/2,h*4/5},
                {w/4,h*2/3},
                {w/4,h/3}
        };

        for(int k = 1;
            k <= step.getMstEdges().size();
            k++) {

            KruskalEdge edge =
                    step.getMstEdges().get(k);

            int source = -1;
            int destination = -1;

            for(int i=0;i<graph.counter;i++){

                if(graph.getVertexByIndex(i)
                        .data.equals(edge.getSource()))
                    source = i;

                if(graph.getVertexByIndex(i)
                        .data.equals(edge.getDestination()))
                    destination = i;
            }

            if(source != -1 && destination != -1){

                gc.strokeLine(
                        coords[source][0],
                        coords[source][1],
                        coords[destination][0],
                        coords[destination][1]
                );
            }
        }
    }
}
