package testing;

import api.DWGraph_DS;
import api.node_data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    DWGraph_DS graph;
    double EPS = 0.001;


    @BeforeEach
    void init()
    {
        graph = new DWGraph_DS();

        for(int i = 0; i < 10; i++)
        {
            graph.addNode(new DWGraph_DS.NodeData(i));
        }

    }
    @Test
    void getNode() {

        assertNotNull(graph.getNode(5));
        assertNotEquals(graph.getNode(5), graph.getNode(2));

        assertNull(graph.getNode(50));


    }

    @Test
    void getEdge() {

        graph.connect(5, 3, 1);
        assertNotNull(graph.getEdge(5, 3));
        assertNull(graph.getEdge(3, 5));
    }

    @Test
    void addNode() {

        graph.addNode(new DWGraph_DS.NodeData(13));
        assertNotNull(graph.getNode(13));
    }

    @Test
    void connect() {

        graph.connect(5, 3, 2);
        assertEquals(graph.getEdge(5, 3).getWeight(), 2);
    }

    @Test
    void getV() {
        Collection<node_data> ndList = graph.getV();
        assertEquals(ndList.size(), 10);


    }

    @Test
    void getE() {
        graph.connect(5, 3, 2);
        assertNotNull(graph.getE(5));
    }

    @Test
    void removeNode() {
        assertEquals(graph.getV().size(), 10);
        graph.connect(5, 3, 2);
        graph.removeNode(5);
        assertEquals(graph.getV().size(), 9);
        assertNull(graph.getE(5));



    }

    @Test
    void removeEdge() {

        assertEquals(graph.getE(5).size(), 0);
        graph.connect(5, 3, 2);
        assertEquals(graph.getE(5).size(), 1);
        graph.removeEdge(5, 3);
        assertEquals(graph.getE(5).size(), 0);


    }

    @Test
    void nodeSize() {

        assertEquals(graph.getV().size(), 10);
    }

    @Test
    void edgeSize() {
        assertEquals(graph.edgeSize(), 0);
        graph.connect(5, 3, 2);
        assertEquals(graph.edgeSize(), 1);
        graph.removeEdge(5, 3);
        assertEquals(graph.edgeSize(), 0);



    }


    @Test
    void getDist() {

        graph.setDist(5, 3);
        assertEquals(graph.getDist(5), 3);
    }

}
