

public class Test {

    public static void main(String[] args) {
        UndirectedGraph g = new UndirectedGraph(0.5, 410236, "input_files/Amazon0505.txt");
	
		
		FourVertexSubgraphCounter counter = new FourVertexSubgraphCounter(g.edgeTauSize, g.E(), 
            g.W, g.offset, g.adj, g.edgeTau);
			

        //System.out.println("Edgegs " + g.E());
        counter.threePathSampler(200000);
        counter.print();
        System.out.println("index is 0.5");

        // 250, 1273 for medium txt

        // Directed graph (each unordered pair of nodes is saved once): Amazon0312.txt 
        // Amazon product co-purchaisng network from March 12 2003
        // Nodes: 400727, 3200440
        // FromNodeId	ToNodeId

	}
}
