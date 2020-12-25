This projects implements a weight, directional graph and allows users to find the shortest paths and store information in nodes.



Interfaces used:

-node_data
-directed_weighted_graph
-dw_graph_algorithms
-edge_data

node_data represents the set of operations applicable on a node in the graph.
directed_weighted_graph represents the aforementioned graph
weighted_graph_algorithm represents operations on the graph
edge_data represents an edge on the graph


node_data functions:
getKey(int key) - returns an integer for the Node key
getInfo() - returns a string of metadata
setInfo(String s) - sets the metadata 
getTag() - returns an integer tag(used for algorithm marking)
setTag(int tag) - sets an integer tag

NodeData implementation of node_data

Beyond the above, NodeData has a dist and prev field, which are used for algorithms, the constructor takes an key to assign a key to node_data or creates one on its own.



edge_data functions:
getSrc() - returns the source from which the edge begins
getDest() - returns the destination from which the edge ends
getWeight() - returns the weight of the edge
getInfo() - allows getting meta-data
setInfo(String s) - allows setting meta-data 
getTag() - returns the tag of the edge, mainly used for algorithms
setTag(int t) - set the tag of the edge, mainly used for algorithms. 



directed_weighted_graph functions:

getNode(int key) - returns the node_data with requested key
hasEdge(int node1, node2) - returns whether node1 is a neighbor of node2
getEdge(int node1, int node2) - returns the edge
addNode(node_data node) - adds a node to the graph 
connect(node1, node2) - adds node2 as a neighbor to node1
getV() - returns a collection of the Nodes in the graph
getE(int node_id) - returns a collection of the neighbors for a Node in the graph
removeNode(int key) - removes a Node from the graph
removeEdge(int node1, node2) - Removes node1 as a neighbor for node2
nodeSize() - returns the amount of nodes in the graph
edgeSize() - returns the amount of connected nodes in the graph
getMC() - Used for testing changes in the graph

DWGraph_DS implementation:
The graph is implemented as a weighted, directional graph, which means that the operation connect(node1, node2), for example - adds an edge between node1 to node2. It doesn't add an edge for node2->node1.

Nodes and edges are stored in a HashMap.

the ModeCounter is incremented every time a new node/edge is added/removed. MC won't change if there hasn't been an actual change(such as trying to remove an edge that doesn't exist)


dw_graph_algorithms functions:
init(weighted_graph g) - initializes weighted_graph g
getGraph() - returns the directed_weighted_graph initialized
copy() - performs a deep copy of the graph, and returns it.
isConnected() - returns true if the graph is connected
shortestPathDist(int src, int dest) - returns an integer for the shortest distance from source to dest. 
shortestPath(int src, int dest) - returns a list of Nodes from source to destination. 


DWGraph_Algo implementation:
init(graph g) initializes a graph, and allows the operations of weighted_graph_algorithms on graph g. If g is null, it creates a new DWGraph_DS. 

When creating a new instance of DWGraph_Algo - it calls the init function with a null value and is thus a substitution for init(null).
DWGraph_Algo(weighted_graph g) calls init(g) which can be used to initialize a specific graph without explicitly calling init. 

Copy() runs a deep copy - it copies everything including the keys - the MC is not copied.

shortestPathDist uses Dijkstra's algorithm to return the distance from src to dest

shortestPath implements Dijkstra, and returns the shortest path. There may be other paths of identical lengths, this isn't guarenteed to return the same path each time as the graphs change and
the HashMap collection changes. 




Example usage:

    directed_weighted_graph graph;
    dw_graph_algorithms wga;


        graph = new DWGraph_DS();

        wga = new DWGraph_Algo();
        wga.init(graph);
        for(int i = 0; i < 10; i++)
        {
            graph.addNode(i); // Creating some nodes
        }
        graph.connect(0, 1);
        graph.connect(1, 5);
        wga = new WGraph_Algo();
        wga1.init(g1);

        System.out.println("Is this graph connected?" + wga.isConnected()); // This would be false, since only 2 nodes are connected right now







