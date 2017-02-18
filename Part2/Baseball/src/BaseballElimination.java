import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	private int numberOfTeams;

	private String[] teams;

	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	private Map<String, Integer> m;
	// private FlowNetwork flowNetwork;

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		processFile(filename);
	}

	private void processFile(String filename) {
		m = new HashMap<>();

		In in = new In(filename);
		numberOfTeams = in.readInt();

		in.readLine();

		teams = new String[numberOfTeams];
		w = new int[numberOfTeams];
		l = new int[numberOfTeams];
		r = new int[numberOfTeams];
		g = new int[numberOfTeams][];

		String str;
		for (int i = 0; i < numberOfTeams; i++) {
			str = in.readLine();
			
			String[] details = str.trim().split("\\s+");

			teams[i] = details[0];
			m.put(details[0], i);

			w[i] = Integer.valueOf(details[1]);
			l[i] = Integer.valueOf(details[2]);
			r[i] = Integer.valueOf(details[3]);
			g[i] = new int[numberOfTeams];

			for (int j = 0; j < numberOfTeams; j++) {
				g[i][j] = Integer.valueOf(details[4 + j]);
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return this.numberOfTeams;
	}

	// all teams
	public Iterable<String> teams() {
		Stack<String> stack = new Stack<>();
		for (String s : teams) {
			stack.push(s);
		}
		return stack;
	}

	// number of wins for given team
	public int wins(String team) {
		if (m.containsKey(team)) {
			return w[m.get(team)];
		} else {
			throw new java.lang.IllegalArgumentException();
		}
	}

	// number of losses for given team
	public int losses(String team) {
		if (m.containsKey(team)) {
			return l[m.get(team)];
		} else {
			throw new java.lang.IllegalArgumentException();
		}
	}

	// number of remaining games for given team
	public int remaining(String team) {
		if (m.containsKey(team)) {
			return r[m.get(team)];
		} else {
			throw new java.lang.IllegalArgumentException();
		}
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		if (m.containsKey(team1) && m.containsKey(team2)) {
			return g[m.get(team1)][m.get(team2)];
		} else {
			throw new java.lang.IllegalArgumentException();
		}
	}

	private int trivialEliminated(String team){
		int teamId;
		if(m.containsKey(team)){
			teamId = m.get(team);
		}else{
			throw new java.lang.IllegalArgumentException();
		}
		 
		for(int i=0;i<numberOfTeams;i++){
			if(teamId != i && w[teamId] + r[teamId] < w[i]){
				return i;
			}
		}
		return -1;
	}
	
	private FlowNetwork prepareFlowNetwork(String team) {
		int teamId;

		if (m.containsKey(team)) {
			teamId = m.get(team);
		} else {
			throw new java.lang.IllegalArgumentException();
		}

		int numberOfGames = numberOfTeams * numberOfTeams  ;

		int V = numberOfGames + this.numberOfTeams + 2;
		int s = numberOfGames + this.numberOfTeams;
		int t = numberOfGames + this.numberOfTeams + 1;

		FlowNetwork flowNetwork = new FlowNetwork(V);

		for (int i = 0; i < this.numberOfTeams; i++) {

			for (int j = i + 1; j < this.numberOfTeams; j++) {
				if (i == teamId || j == teamId) {
					continue;
				}
				
				FlowEdge e = new FlowEdge(s, i * this.numberOfTeams + j, g[i][j]);
				flowNetwork.addEdge(e);
			}
		}

		for (int i = 0; i < this.numberOfTeams; i++) {
			for (int j = i + 1; j < this.numberOfTeams; j++) {
				if (i == teamId || j == teamId) {
					continue;
				}

				int v = numberOfGames + i;
				int w = numberOfGames + j;

				FlowEdge e1 = new FlowEdge(i * this.numberOfTeams + j, v, Integer.MAX_VALUE);
				flowNetwork.addEdge(e1);

				FlowEdge e2 = new FlowEdge(i * this.numberOfTeams + j, w, Integer.MAX_VALUE);
				flowNetwork.addEdge(e2);
			}
		}

		for (int i = 0; i < this.numberOfTeams; i++) {
			if (i == teamId) {
				continue;
			}

			FlowEdge e = new FlowEdge(numberOfGames + i, t, w[teamId] + r[teamId] - w[i]);
			flowNetwork.addEdge(e);
		}
		return flowNetwork;
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		if(trivialEliminated(team)>=0){
			return true;
		}
		
		int numberOfGames = numberOfTeams * numberOfTeams;

		int s = numberOfGames + this.numberOfTeams;
		int t = numberOfGames + this.numberOfTeams + 1;
		FlowNetwork G = prepareFlowNetwork(team);

		FordFulkerson fordFulkerson = new FordFulkerson(G, s, t);

		for (FlowEdge e : G.adj(s)) {
			if (e.capacity() != e.residualCapacityTo(e.from())) {
				return true;
			}
		}

		return false;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		Stack<String> stack = new Stack<>();
		
		if(!isEliminated(team)){
			return null;
		}
		
		int trivial = trivialEliminated(team);
		if(trivial >= 0 ){
			stack.push(this.teams[trivial]);
			return stack;
		}
		
		int numberOfGames = numberOfTeams * numberOfTeams;

		int s = numberOfGames + this.numberOfTeams;
		int t = numberOfGames + this.numberOfTeams + 1;
		FlowNetwork G = prepareFlowNetwork(team);

		FordFulkerson fordFulkerson = new FordFulkerson(G, s, t);

		boolean eliminated = false;
		for (FlowEdge e : G.adj(s)) {
			if (e.capacity() != e.residualCapacityTo(e.from())) {
				eliminated = true;
				break;
			}
		}

		if(!eliminated){
			return null;
		}
		for(int i=0;i<this.numberOfTeams;i++){
			if(fordFulkerson.inCut(numberOfGames + i)){
				stack.push(this.teams[i]);
			}
			
		}
				
		return stack;
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination("src/baseball/teams10.txt");

		//BaseballElimination division = new BaseballElimination(args[0]);

		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}

	}
}
