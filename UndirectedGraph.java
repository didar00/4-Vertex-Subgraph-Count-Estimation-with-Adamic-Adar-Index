import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class UndirectedGraph {

	private final int V;
    private int E;
	public long W;
	public ArrayList<ArrayList<Integer>> adj;
	public long[][] edgeTau;

	public long offset;
	public double k;
	public int edgeTauSize;



	public UndirectedGraph(double k, int V, String file) {
		if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
		offset = 0;
		this.k = k;
		
		adj = new ArrayList<ArrayList<Integer>>(V);
		// initializes the adjacency list
		for (int i = 0; i < V; i++) 
			adj.add(new ArrayList<Integer>()); 


		/**
         * 
         * reads text file with edge information in it,
         * adds each edge to the graph
         * 
         */
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int u;
			int v;
			while ((line = br.readLine()) != null) {
				String[] vertices = line.split("\t");
				u = Integer.parseInt(vertices[0]);
				v = Integer.parseInt(vertices[1]);
				if(!adj.get(u).contains(v)) {
					addEdge(u, v);
				}
				
			}

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i< adj.size(); i++)
		{
			E += adj.get(i).size();
		}
		E = E/2;

		edgeTau = new long[2*E][3];
		W = W(k);
	}


	/**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
     */
    public int E() {
        return E;
	}


	public void addEdge(int u, int v) {
		adj.get(u).add(v);
		adj.get(v).add(u);
	}


	private long W(double k) {
		long W = 0;
		int index = 0;
		

		for (int u = 0; u < V; u++) {
			int degree1 = adj.get(u).size();
			// for the other vertices that compose an edge with u
			for (int v : adj.get(u)) {
				int degree2 = adj.get(v).size();
				if (checkAdamicAdarIndex(k, u, v)) {
					int tau = (degree1-1)*(degree2-1);
					W += tau;
					offset += tau;
					// adds as edge-tau value pairs
					edgeTau[index][0] = u;
					edgeTau[index][1] = v;
					edgeTau[index][2] = offset;
					index++;
				}

			}
		}
		edgeTauSize = index;
		return W/2;
	}

	private boolean checkAdamicAdarIndex(double k, int u, int v) {
		double adamicAdarIndex = 0;
		ArrayList<Integer> intersection = intersection(adj.get(u), adj.get(v));

		for (int x : intersection) {
			adamicAdarIndex += (double) 1/Math.log((double)adj.get(x).size());
		}
		
		if (adamicAdarIndex > k) {
			return true;
		}
		return false;
	}

	private ArrayList<Integer> intersection(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		
		ArrayList<Integer> list = new ArrayList<Integer>();

		for (int i : list1) {
			if(list2.contains(i)) {
				list.add(i);
			}
		}

		return list;
	}


}
