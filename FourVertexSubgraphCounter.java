import java.util.ArrayList;
import java.util.Random;

public class FourVertexSubgraphCounter {
	public static final int[] A = {0, 1, 2, 4, 6, 12};
	/**
	 * motif counts
	 * [0] 3-star
	 * [1] 3-path
	 * [2] tailed-triangle
	 * [3] 4-cycle
	 * [4] chordal-4-cycle
	 * [5] 4-clique
	 * 
	 * for both the count and the motifs array
	 * 
	 */
	public int[] count = {0, 0, 0, 0, 0, 0};
	public double[] motifs = {0, 0, 0, 0, 0, 0};

	public ArrayList<ArrayList<Integer>> adj;
	long[][] edgeTau;
	public int E;
	public long W;
	public long offset;


	public FourVertexSubgraphCounter(int E, long W, long offset, ArrayList<ArrayList<Integer>> adj, long[][] edgeTau) {
		this.adj = adj;
		this.edgeTau = edgeTau;
		this.E = E;
		this.W = W;
		this.offset = offset;
	}

    /** sampler algorithm to obtain a set of edges that compose a 3-path */
	public int[][] sampler() {
		int[][] setOfEdges = new int[3][2];
		int[] middleEdge = new int[2];
		int[] uPrime = new int[2];
		int[] vPrime = new int[2];

		// pick middle edge e = (u,v) with probability p_e = T_e/W
		Random rand = new Random();
		long x = randomLong();

		for (int i = 0; i < 2*E; i++) {
			if (x < edgeTau[i][2]) {
				if (i == 0) {
					middleEdge[0] = (int) edgeTau[i][0];
					middleEdge[1] = (int) edgeTau[i][1];
				}else {
					if (x >= edgeTau[i-1][2]) {
						middleEdge[0] = (int) edgeTau[i][0];
						middleEdge[1] = (int) edgeTau[i][1];
					}
				}
			}
		}


		/** selects the neighbors of the middle edge vertex */
		int u = middleEdge[0];
		int v = middleEdge[1];
		// select a random neighbor uPrime different than u
		int y = rand.nextInt(adj.get(u).size());


		if (adj.get(u).get(y) == v) {
			if (y == adj.get(u).size() -1) {
				y = 0;
			}else {
				y++;
			}
		}
		
		uPrime[0] = adj.get(u).get(y);
		uPrime[1] = u;



		// select a random neighbor vPrime different than v
		int z = rand.nextInt(adj.get(v).size());
		
		if (adj.get(v).get(z) == u) {
			if (z == adj.get(v).size() -1) {
				z = 0;
			}else {
				z++;
			}
		}
		
		vPrime[0] = v;
		vPrime[1] = adj.get(v).get(z);

		setOfEdges[0] = uPrime; setOfEdges[1] = middleEdge; setOfEdges[2] = vPrime;
		
		return setOfEdges;
	}

	public long randomLong() {
		long leftLimit = 0;
		long rightLimit = offset;
		long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
		return generatedLong;
	}


	/**
	 * Determines the motif induced by given set of edges
	 * Increments the proper motif count according to
	 * the configuration of the edges
	 * @param edges
	 */
	private void determineMotif(int[][] edges) {
		int u = edges[1][0]; // u from middle edge
		int v = edges[1][1]; // v from middle edge
		int uP = edges[0][0]; // u prime
		int vP = edges[2][1]; // v prime
		
		if (vP == uP) { // inproper motif, triangular
			return ;
		}

		boolean e0 = edgeExists(u, vP);
		boolean e1 = edgeExists(v, uP);
		boolean e2 = edgeExists(uP, vP);

		if (e2 && e1 && e0) { // 4-clique
			count[5]++;
			return;
		} else if (e2 && (e1 || e0)) { // chordal-4-cycle
			count[4]++;
			return;
		} else if (e2) { // 4-cycle
			count[3]++;
			return;
		} else if (e1 || e0) { // tailed-triangle
			count[2]++; 
			return;
		} else { // a 3-path
			count[1]++; 
			return;
		}


	}

	private boolean edgeExists(int u, int v) {
		for (int vertex : adj.get(u)) {
			if (vertex == v) {
				return true;
			}
		}
		return false;
	}

	public void threePathSampler(int k) {
		int[][][] setOfEdges = new int[k][3][2];

		/** 
		 * 
		 * Run the sampler algorithm to get k set of edges 
		 * 
		 */
		for (int i = 0; i < k; i++) {
			setOfEdges[i] = sampler();
		}


		/**
		 * 
		 * Determine the motif induced by S_l (setOfEdges[i])
		 * with the help of determineMotif method
		 * 
		 */
		for (int i = 0; i < k; i++) {
			determineMotif(setOfEdges[i]);
		}
		
		
		for (int m = 1; m < 6; m++) {
			motifs[m] = ((double)count[m]/k)*((double)W/A[m]);
		}

		long n1 = calculateN();
		//System.out.println(n1);
		motifs[0] = n1 - motifs[2] - 2*motifs[4] - 4*motifs[5];


	}

	/**
	 * Calculates the N_1 value which is composed of
	 * summation of all vertex's degree's combination with 3
	 * @return
	 */
	private long calculateN() {
		long sum = 0;
		for (int i = 0; i < adj.size(); i++) {
			int degree = adj.get(i).size();
			if (degree >= 3) {
				sum += combination(degree, 3);
			}
		}
		return sum;
	}

	public long combination(int n, int r) {
		long numerator = 1;
		long denumerator = 6;

		for (int i = n; i > n-r; i--) {
			numerator *= i;
		}

		return numerator/denumerator;
	}


	public int getDegree(int v) {
		int degree = 0;
		degree += adj.get(v).size();
	
		return degree;
	}

	public void print() {
		int c = 1;
		for (int i : count) {
			System.out.println("count " + c + " is " + i);
			c++;
		}
		c = 1;
		for (double i : motifs) {
			System.out.println("Motif " + c + " is " + i);
			c++;
		}
		
	}
	
}