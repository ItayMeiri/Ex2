package api;

import java.io.*;
import java.util.*;

import api.DWGraph_DS.NodeData;


public class DW_Graph_Algo implements dw_graph_algorithms, Serializable {

    //instance variables

    private DWGraph_DS graph;

    public DW_Graph_Algo()
    {
        this.init(null);

    }


    @Override
    public void init(directed_weighted_graph g) {
        if(g == null)
        {
            graph = new DWGraph_DS();
            return;
        }
        graph = (DWGraph_DS)g;

    }

    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    @Override
    public directed_weighted_graph copy() {

        directed_weighted_graph g = new DWGraph_DS(); // The new graph we'll return

        for(node_data nd : graph.getV()) { // Going over all nodes in graph

            //Making sure it wasn't added in the inner loop
            if(g.getNode(nd.getKey()) == null) {

                g.addNode(new NodeData(nd.getKey(), nd.getTag(), nd.getInfo(), nd.getLocation(), nd.getWeight()));
            }

            for(edge_data ed : graph.getE(nd.getKey())) {
                node_data ni = graph.getNode(ed.getDest());

                if(g.getNode(ni.getKey()) == null) {
                    g.addNode(new NodeData(ni.getKey(), ni.getTag(), ni.getInfo(), new GeoLocation(ni.getLocation()), ni.getWeight()));
                }
                g.connect(nd.getKey(), ni.getKey(), graph.getEdge(nd.getKey(), ni.getKey()).getWeight());
            }
        }

        return g;
    }

    @Override
    public boolean isConnected() {


        for(node_data nd: graph.getV())
        {
            for(node_data ne: graph.getV()) {
                if(nd == ne)
                {
                    continue;
                }
                if (!pathExists(nd, ne)) {

                    return false;
                }

            }
        }
        return true;
    }

    /** Checks if theres a pathway between nd to ne
     * @param nd - the src node
     * @param ne - the dest node
     * @return true if dest can be reached from src
     */
    private boolean pathExists(node_data nd, node_data ne) {

        //DFS
        for(node_data n: graph.getV())
        {
            n.setTag(0); // set unvisited
        }
        Queue<node_data> queue = new LinkedList<>();
        nd.setTag(1);
        queue.add(nd);

        while(!queue.isEmpty())
        {
            node_data v = queue.remove();
            if(v == ne)
            {
                return true;
            }
            for(edge_data ed: graph.getE(v.getKey()))
            {
                node_data neighbor = graph.getNode(ed.getDest());
                if(neighbor.getTag() != 1)
                {
                    neighbor.setTag(1);
                    queue.add(neighbor);
                }
            }
        }

        return false;
    }


    @Override
    public double shortestPathDist(int src, int dest) {


        if(graph.getNode(src) == null || graph.getNode(dest) == null)
        {
            return -1;
        }
        Queue<node_data> que = new LinkedList<node_data>();
        double alt = 0;

        for(node_data ni: graph.getV())
        {
            ni.setTag(0); // set unvisited
            this.graph.setDist(ni.getKey(), Double.MAX_VALUE); // Sets distance from source to infinity
            que.add(ni);
        }

        this.graph.setDist(src, 0);
        while(!que.isEmpty())
        {
            node_data min = this.minimum(que);

            que.remove(min);

            for(edge_data neighbor : graph.getE(min.getKey()))
            {
                node_data nd = graph.getNode(neighbor.getDest());
                if(!que.contains(nd))
                {
                    continue;
                }
                alt = graph.getDist(min.getKey()) + graph.getEdge(min.getKey(), nd.getKey()).getWeight();

                if(alt < graph.getDist(nd.getKey()))
                {
                    graph.setDist(nd.getKey(), alt);
                }
            }
        }

        return graph.getDist(dest);
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(graph.getNode(src) == null || graph.getNode(dest) == null)
        {
            return null;
        }

        //Adapted Dijkstra's algorithm: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

        Queue<node_data> que = new LinkedList<node_data>();
        double alt = 0;

        HashMap<Integer, node_data> prevHash = new HashMap<Integer, node_data>();
        for(node_data ni: graph.getV())
        {
            ni.setTag(0); // set unvisited
            prevHash.put(ni.getKey(), null);
            this.graph.setDist(ni.getKey(), Double.MAX_VALUE); // Sets distance from source to infinity
            que.add(ni);
        }

        this.graph.setDist(src, 0);
        while(!que.isEmpty())
        {
            node_data min = this.minimum(que);

            que.remove(min);

            for(edge_data neighbor : graph.getE(min.getKey()))
            {
                node_data nd = graph.getNode(neighbor.getDest());
                if(!que.contains(nd))
                {
                    continue;
                }
                alt = graph.getDist(min.getKey()) + graph.getEdge(min.getKey(), nd.getKey()).getWeight();

                if(alt < graph.getDist(nd.getKey()))
                {
                    graph.setDist(nd.getKey(), alt);
                    prevHash.put(nd.getKey(), min);
                }
            }
        }

        List<node_data> toRet = new ArrayList<>();
        node_data currentNode = graph.getNode(dest);

        while(currentNode.getKey() != src)
        {
            toRet.add(currentNode);
            currentNode = prevHash.get(currentNode.getKey());
            // testing
            if(currentNode == null)
            {
                return null;
            }
        }

        toRet.add(graph.getNode(src));
        Collections.reverse(toRet);

        return toRet;
    }


    private node_data minimum(Queue<node_data> que) {
        int minNodeKey = que.peek().getKey();
        double minimum = graph.getDist(minNodeKey);

        for(node_data ni: que)
        {
            if( minimum > this.graph.getDist(ni.getKey()) )
            {
                minimum = this.graph.getDist(ni.getKey());
                minNodeKey = ni.getKey();
            }
        }

        return graph.getNode(minNodeKey);

    }


    //Save and load have been adapted from: https://mkyong.com/java/how-to-read-and-write-java-object-to-a-file/ and https://github.com/simon-pikalov/Ariel_OOP_2020/blob/master/Classes/week_03/class3/src/class3/Points3D.java

    @Override
    public boolean save(String file) {

        boolean result = false;

        try{
            FileOutputStream f = new FileOutputStream(new File(file));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(graph);
            o.close();
            f.close();
            result = true;

        }catch(FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean load(String file) {

        boolean result = false;
        try{
            FileInputStream f = new FileInputStream(new File(file));
            ObjectInputStream o = new ObjectInputStream(f);
            DWGraph_DS readGraphDS = (DWGraph_DS)o.readObject();
            this.graph = readGraphDS;

            f.close();
            o.close();
            result = true;

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
