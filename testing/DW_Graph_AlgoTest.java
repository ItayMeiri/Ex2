package testing;

import api.DWGraph_DS;
import api.DW_Graph_Algo;
import api.directed_weighted_graph;
import api.node_data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DW_Graph_AlgoTest {


    DWGraph_DS graph;
    DW_Graph_Algo dga;
    double EPS = 0.001;
    @BeforeEach
    void init()
    {
        graph = new DWGraph_DS();
        dga = new DW_Graph_Algo();

        for(int i = 0; i < 10; i++)
        {
            graph.addNode(new DWGraph_DS.NodeData(i));
        }
        dga.init(graph);

    }

    @Test
    void getGraph() {

        assertEquals(dga.getGraph(), graph);

    }

    @Test
    void copy() {

        directed_weighted_graph copyGraph = dga.copy();

        for(node_data nd: graph.getV())
        {
            node_data copyNode = copyGraph.getNode(nd.getKey());
            assertEquals(copyNode.getInfo(), nd.getInfo());
            assertEquals(copyNode.getTag(), nd.getTag());
            assertEquals(copyNode.getWeight(), nd.getWeight());
            assertEquals(copyNode.getLocation(), nd.getLocation());
        }
    }

    @Test
    void isConnected() {

        graph.connect(0, 1, 1);
        graph.connect(1, 0, 1);
        graph.connect(1, 2, 1);
        graph.connect(2, 3, 3);
        graph.connect(3, 4, 1);
        graph.connect(4, 5, 1);
        graph.connect(5, 6, 1);
        graph.connect(6, 7, 1);
        graph.connect(7, 8, 1);
        graph.connect(8, 9, 1);
        graph.connect(9, 8, 1);
        graph.connect(8, 7, 3);
        graph.connect(7, 6, 1);
        graph.connect(6, 5, 1);
        graph.connect(5, 4, 1);
        graph.connect(4, 3, 1);
        graph.connect(3, 2, 1);
        graph.connect(2, 1, 1);
        graph.connect(1, 9, 1);

        assertTrue(dga.isConnected());



    }

    @Test
    void shortestPathDist() {

        graph.connect(0, 1, 2);
        graph.connect(1, 2, 1);
        graph.connect(0, 2, 5);

        assertEquals(dga.shortestPathDist(0, 2), 3, EPS);

    }

    @Test
    void shortestPath() {
        graph.connect(0, 1, 2);
        graph.connect(1, 2, 1);
        graph.connect(0, 2, 5);

        assertEquals(dga.shortestPath(0, 2).size(), 3);
    }

    @Test
    void save() {
    }

    @Test
    void load() {
    }
}