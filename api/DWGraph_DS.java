package api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph, Serializable{


    //private classes
    public static class NodeData implements node_data, Serializable
    {
        static int global = 0;
        private int key;

        private String info;
        private int tag;
        private geo_location location;
        private double weight;
        private double dist;

        public NodeData() {
            this.key = global++;
            this.info = "";
            this.tag = 0;
            this.weight = 0;
            this.location = null;
            dist = 0;

        }

        public NodeData(int key)
        {
            this.key = key;
            this.info = "";
            this.tag = 0;
            this.weight = 0;

            //Maybe don't null?
            this.location = new GeoLocation();
        }

        public NodeData(int key, int tag, String info, geo_location location, double weight)
        {
            this.key = key;
            this.info = info;
            this.tag = tag;
            this.weight = weight;
            this.location = new GeoLocation(location);
        }

        @Override
        public int getKey() {
            return this.key;
        }

        @Override
        public geo_location getLocation() {
            return location;
        }

        @Override
        public void setLocation(geo_location location) {
            this.location = location;
        }

        @Override
        public double getWeight() {
            return this.weight;
        }

        @Override
        public void setWeight(double w) {
            this.weight = w;

        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int tag) {
            this.tag = tag;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String info) {
            this.info = info;
        }

        public double getDist()
        {
            return dist;
        }
        public void setDist(double dist)
        {
            this.dist = dist;
        }


    }


    //
    private class EdgeData implements edge_data, edge_location{


        private int src, dest;
        private double weight;
        private String info;
        private int tag;
        private geo_location srcLocation;
        private geo_location destLocation;

//        public EdgeData(double w)
//        {
//            src = 0;
//            dest = 0;
//            weight = w;
//            info = "";
//            tag = 0;
//        }

        public EdgeData(int src, int dest, double w)
        {
            this.src = src;
            this.dest = dest;
            this.weight = w;
            this.info = "";
            tag = 0;
        }


        @Override
        public int getSrc() {
            return src;
        }

        @Override
        public int getDest() {
            return dest;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {

            this.info = s;
        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;

        }

        public void setSrc(int src)
        {
            this.src = src;
        }
        public void setDest(int dest)
        {
            this.dest = dest;
        }


        @Override
        public edge_data getEdge() {
            return this;
        }

        @Override
        public double getRatio() {
            return 0;
        }
    }


    //instance variables
    private HashMap<Integer, node_data> nodes;
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;
    private int edgeSize;
    private int MC;

    //Functions
    public DWGraph_DS()
    {
        nodes = new HashMap<Integer, node_data>();
        edges = new HashMap<Integer, HashMap<Integer, edge_data>>();
        edgeSize = 0;
    }

    public DWGraph_DS(String JSON) throws JSONException {
        nodes = new HashMap<Integer, node_data>();
        edges = new HashMap<Integer, HashMap<Integer, edge_data>>();
        edgeSize = 0;


        JSONObject jo = new JSONObject(JSON);

        JSONArray jEdges = jo.getJSONArray("Edges");
        JSONArray jNodes = jo.getJSONArray("Nodes");


        for(int i = 0; i < jNodes.length(); i++)
        {
            JSONObject ob = jNodes.getJSONObject(i);

            String pos[] = ob.get("pos").toString().split(",");
            int key = Integer.parseInt(ob.get("id").toString());
            double x = Double.parseDouble(pos[0]);
            double y = Double.parseDouble(pos[1]);
            double z = Double.parseDouble(pos[2]);
            this.addNode(new NodeData(key, 0, "", new GeoLocation(x, y, z), 0));

        }
        for(int i = 0; i <jEdges.length();i++)
        {
            JSONObject ob = jEdges.getJSONObject(i);

            int src = Integer.parseInt(ob.get("src").toString());
            int dest = Integer.parseInt(ob.get("dest").toString());
            double weight = Double.parseDouble(ob.get("src").toString());
            this.connect(src, dest, weight);
        }


    }



    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        //TODO make sure these exist
        return edges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data n) {
        //TODO make sure not null
        nodes.put(n.getKey(), n);
        edges.put(n.getKey(), new HashMap<Integer, edge_data>());
        MC++;

    }

    @Override
    public void connect(int src, int dest, double w) {
        //TODO security checks, make sure src/dest exist, make sure w >=0
        if(nodes.get(src) == null || nodes.get(dest) == null)
        {
            return;
        }

        if (edges.get(src) == null)
        {
            edges.put(src, new HashMap<Integer, edge_data>());
        }
        edges.get(src).put(dest, new EdgeData(src, dest, w));

        //Add more weight to the node
        nodes.get(src).setWeight(nodes.get(src).getWeight() + w);
        //Create the edge at src->dest with the data of such edge
        this.edgeSize++;
        MC++;

    }

    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        //TODO MAKE SURE NOT NULL
        if(edges.get(node_id) == null)
        {
            return null;
        }
        return edges.get(node_id).values();

    }

    @Override
    public node_data removeNode(int key) {
        //TODO: MAKE NULL CHECKS AND EXISTENCE CHECKS
        edgeSize-=edges.get(key).size();
        edges.remove(key);

        MC++;

        return nodes.remove(key);

    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        MC++;
        edgeSize--;
        return edges.get(src).remove(dest);
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return MC;
    }


    public double getDist(int node_id)
    {
        return ((NodeData)(nodes.get(node_id))).getDist();
    }
    public void setDist(int node_id, double dist)
    {
        ((NodeData)(nodes.get(node_id))).setDist(dist);
    }

}
