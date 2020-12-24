

public class Test {

    public static void main(String[] args) {
        UndirectedGraph g = new UndirectedGraph(0.5, 400727, 3200440, "input_files/Amazon0312.txt");
	
		
		FourVertexSubgraphCounter counter = new FourVertexSubgraphCounter(g.E(), 
            g.W, g.offset, g.adj, g.edgeTau);
			

        counter.threePathSampler(1000);
        counter.print();

        // 250, 1273 for medium txt

        // Directed graph (each unordered pair of nodes is saved once): Amazon0312.txt 
        // Amazon product co-purchaisng network from March 12 2003
        // Nodes: 400727, 3200440
        // FromNodeId	ToNodeId

	}
}
