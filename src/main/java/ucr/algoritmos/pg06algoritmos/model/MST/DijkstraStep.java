package ucr.algoritmos.pg06algoritmos.model.MST;

public class DijkstraStep {

    public int currentVertex;

    public int[] distance;

    public int[] previous;

    public boolean[] visited;

    public String description;

    public DijkstraStep(int currentVertex,
                        int[] distance,
                        int[] previous,
                        boolean[] visited,
                        String description){

        this.currentVertex = currentVertex;
        this.distance = distance.clone();
        this.previous = previous.clone();
        this.visited = visited.clone();
        this.description = description;
    }
}
