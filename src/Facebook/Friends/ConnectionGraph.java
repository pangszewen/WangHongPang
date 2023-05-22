package Facebook.Friends;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionGraph<T extends Comparable<String>> {
    String graphFile = "graphFile.csv";
    Vertex<String> head;
    int size;

    public ConnectionGraph() {
        head = null;
        size = 0;
    }

    public ConnectionGraph<String> getGraph(ConnectionGraph<String> graph){
        try (BufferedReader reader = new BufferedReader(new FileReader(graphFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",(?![^\\[]*\\])");
                if(rowData.length==2){
                    graph.addVertex(graph, rowData[0]);
                    String[] edgeData = rowData[1].substring(2, rowData[1].length()-2).split(",");
                    for(String edge : edgeData){
                        graph.addEdge(graph, rowData[0], edge);
                        graph.addEdge(graph, edge, rowData[0]);
                    }
                }else{
                    graph.addVertex(graph, line);
                }
            }
            return graph;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Failed to get graph.");
        return graph;
    }

    public ConnectionGraph<String> clear(ConnectionGraph<String> graph) {
        graph.head = null;
        graph.size = 0;
        return graph;
    }

    public int getSize(ConnectionGraph<String> graph) {
        return graph.size;
    }

    public int getIndeg(ConnectionGraph<String> graph, String v) {
        if (graph.hasVertex(graph,v)) {
            Vertex<String> temp = graph.head;
            while (temp != null) {
                if (temp.vertexInfo.compareTo(v) == 0)
                    return temp.indeg;
                temp = temp.nextVertex;
            }
        }
        return -1;
    }

    public int getOutdeg(ConnectionGraph<String> graph, String v) {
        if (graph.hasVertex(graph, v)) {
            Vertex<String> temp = graph.head;
            while (temp != null) {
                if (temp.vertexInfo.compareTo(v) == 0)
                    return temp.outdeg;
                temp = temp.nextVertex;
            }
        }
        return -1;
    }

    public boolean hasVertex(ConnectionGraph<String> graph, String v) {
        if (graph.head == null)
            return false;
        Vertex<String> temp = graph.head;
        while (temp != null) {
            if (temp.vertexInfo.compareTo(v) == 0)
                return true;
            temp = temp.nextVertex;
        }
        return false;
    }

    public boolean addVertex(ConnectionGraph<String> graph, String v){
        if(hasVertex(graph, v)==false){
            Vertex<String> temp = head;
            Vertex<String> newVertex = new Vertex<>(v, null);
            if(graph.head==null)
                graph.head = newVertex;
            else{
                Vertex<String> previous = graph.head;
                while(temp!=null){
                    previous = temp;
                    temp = temp.nextVertex;
                }
                previous.nextVertex = newVertex;
            }
            graph.size++;
            return true;
        }else
            return false;
    }


    public ConnectionGraph<String> registerVertex(ConnectionGraph<String> graph,String v) {
        boolean status = addVertex(graph, v);
        if(status){
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(graphFile, true))){
                writer.write(v);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else
            System.out.println("Failed to add vertex");
        return graph;
    }

    public int getIndex(ConnectionGraph<String> graph, String v) {
        Vertex<String> temp = graph.head;
        int pos = 0;
        while (temp != null) {
            if (temp.vertexInfo.compareTo(v) == 0)
                return pos;
            temp = temp.nextVertex;
            pos += 1;
        }
        return -1;
    }

    public ArrayList<String> getAllVertexObjects(ConnectionGraph<String> graph) {
        ArrayList<String> list = new ArrayList<>();
        Vertex<String> temp = graph.head;
        while (temp != null) {
            list.add(temp.vertexInfo);
            temp = temp.nextVertex;
        }
        return list;
    }

    public ArrayList<String> getAllVertices(ConnectionGraph<String> graph) {
        ArrayList<String> list = new ArrayList<>();
        Vertex<String> temp = graph.head;
        while (temp != null) {
            list.add(temp.vertexInfo);
            temp = temp.nextVertex;
        }
        return list;
    }

    public String getVertex(ConnectionGraph<String> graph, String v) {
        if (graph.head == null)
            return null;
        Vertex<String> temp = graph.head;
        while (temp != null) {
            if (temp.vertexInfo.compareTo(v) == 0)
                return temp.vertexInfo;
            temp = temp.nextVertex;
        }
        return null;
    }

    public ConnectionGraph<String> addEdge(ConnectionGraph<String> graph, String source, String destination) {
        if (graph.head == null) 
            return graph;
        if (!hasVertex(graph, source) || !hasVertex(graph, destination)) 
            return graph;
        Vertex<String> sourceVertex = graph.head;
        while (sourceVertex != null) {
            if (sourceVertex.vertexInfo.compareTo(source) == 0) {
                // Reached source vertex, look for destination now
                Vertex<String> destinationVertex = graph.head;
                while (destinationVertex != null) {
                    if (destinationVertex.vertexInfo.compareTo(destination) == 0) {
                        // Reached destination vertex, add edge here
                        Edge<String> currentEdge = sourceVertex.firstEdge;
                        Edge<String> newEdge = new Edge<>(destinationVertex, currentEdge);
                        sourceVertex.firstEdge = newEdge;
                        sourceVertex.outdeg++;
                        destinationVertex.indeg++;
                        return graph;
                    }
                    destinationVertex = destinationVertex.nextVertex;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        System.out.println("Failed to add edge");
        return graph;
    }
    public ConnectionGraph<String> addUndirectedEdge(ConnectionGraph<String> graph, String v1, String v2) {
        ConnectionGraph<String> temp = graph.addEdge(graph, v1, v2);
        temp = temp.addEdge(temp, v2, v1);
        writeGraphFile(temp);
        return temp;
    }

    public boolean hasEdge(ConnectionGraph<String> graph, String source, String destination) {
        if (graph.head == null)
            return false;
        if (!hasVertex(graph, source) || !hasVertex(graph, destination))
            return false;
        Vertex<String> sourceVertex = graph.head;
        while (sourceVertex != null) {
            if (sourceVertex.vertexInfo.compareTo(source) == 0) {
                // Reached source vertex, look for destination now
                Edge<String> currentEdge = sourceVertex.firstEdge;
                while (currentEdge != null) {
                    // destination vertex found
                    if (currentEdge.toVertex.vertexInfo.compareTo(destination) == 0)
                        return true;
                    currentEdge = currentEdge.nextEdge;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        return false;
    }

    public ConnectionGraph<String> removeEdge(ConnectionGraph<String> graph, String source, String destination) {
        if (graph.head == null)
            return graph;
        if (!(hasVertex(graph, source) && hasVertex(graph, destination)))
            return graph;
        Vertex<String> sourceVertex = graph.head;
        while (sourceVertex != null) {
            if (sourceVertex.vertexInfo.compareTo(source) == 0) {
                // Reached source vertex, look for destination now
                Edge<String> currentEdge = sourceVertex.firstEdge;
                Edge<String> tempEdge = new Edge<>();
                while (currentEdge != null) {
                    // destination vertex found
                    if (currentEdge.toVertex.vertexInfo.compareTo(destination) == 0) {
                        if(currentEdge.equals(sourceVertex.firstEdge)){
                            sourceVertex.firstEdge = currentEdge.nextEdge;
                        }else
                            tempEdge.nextEdge = currentEdge.nextEdge;
                        return graph;
                    }
                    tempEdge = currentEdge;
                    currentEdge = currentEdge.nextEdge;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        return graph;
    }
    public ConnectionGraph<String> removeUndirectedEdge(ConnectionGraph<String> graph, String v1, String v2) {
        ConnectionGraph<String> temp = graph.removeEdge(graph, v1, v2);
        temp = temp.removeEdge(temp, v2, v1);
        writeGraphFile(temp);
        return temp;
    }

    public ArrayList<String> getNeighbours(ConnectionGraph<String> graph, String v) {
        if (!hasVertex(graph, v))
            return null;
        ArrayList<String> list = new ArrayList<>();
        Vertex<String> temp = graph.head;
        while (temp != null) {
            if (temp.vertexInfo.compareTo(v) == 0) {
                // Reached vertex, look for destination now
                Edge<String> currentEdge = temp.firstEdge;
                while (currentEdge != null) {
                    list.add(currentEdge.toVertex.vertexInfo);
                    currentEdge = currentEdge.nextEdge;
                }
            }
            temp = temp.nextVertex;
        }
        return list;
    }

    public void printEdges(ConnectionGraph<String> graph) {
        Vertex<String> temp = graph.head;
        while (temp != null) {
            System.out.print("# " + temp.vertexInfo + " : ");
            Edge<String> currentEdge = temp.firstEdge;
            while (currentEdge != null) {
                System.out.print("[" + temp.vertexInfo + "," + currentEdge.toVertex.vertexInfo + "] ");
                currentEdge = currentEdge.nextEdge;
            }
            System.out.println();
            temp = temp.nextVertex;
        }
    }

    public void writeGraphFile(ConnectionGraph<String> graph){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(graphFile))) {
            List<String> lines = new ArrayList<>();
            Vertex<String> temp = graph.head;
            while(temp!=null){
                ArrayList<String> edges = graph.getNeighbours(graph, temp.vertexInfo);
                lines.add(String.format("%s,\"%s\"", temp.vertexInfo, edges));
                temp = temp.nextVertex;
            }
            for (String rowData : lines) {
                String line = String.join(",", rowData);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Vertex<T extends Comparable<T>> {
    T vertexInfo;
    int indeg;
    int outdeg;
    Vertex<T> nextVertex;
    Edge<T> firstEdge;

    public Vertex() {
        vertexInfo = null;
        indeg = 0;
        outdeg = 0;
        nextVertex = null;
        firstEdge = null;
    }

    public Vertex(T vInfo, Vertex<T> next) {
        vertexInfo = vInfo;
        indeg = 0;
        outdeg = 0;
        nextVertex = next;
        firstEdge = null;
    }
}

class Edge<T extends Comparable<T>> {
	Vertex<T> toVertex;
	Edge<T> nextEdge;
	
	public Edge()	{
		toVertex = null;
		nextEdge = null;
	}
	
	public Edge(Vertex<T> destination, Edge<T> a)	{
		toVertex = destination;
		nextEdge = a;
	}

}
