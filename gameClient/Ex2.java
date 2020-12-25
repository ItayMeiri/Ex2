package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Ex2 implements Runnable{
	private MyFrame _win;
	private Arena _ar;
	private static int scenario_num = 23;
	private static long id = 208387001;
	public static void main(String[] a) throws InterruptedException {
		if(a.length != 0)
		{
			id = Long.parseLong(a[0]);
			scenario_num = Integer.parseInt(a[1]);
		}
		Thread client = new Thread(new Ex2());
		client.start();
	}

	@Override
	public void run() {
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
		game.login(id);
		String pks = game.getPokemons();
		directed_weighted_graph gg = null;
		try {
			gg = new DWGraph_DS(game.getGraph());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.init(game, gg);
		dw_graph_algorithms dga = new DW_Graph_Algo();
		dga.init(gg);


		//_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());

		long dt=1;
		long lastTime = Long.MAX_VALUE;
		long currentTime = 0;
		System.out.println(game.getAgents());
		game.startGame();
		while(game.isRunning()) {
			currentTime = game.timeToEnd();

			if(lastTime - currentTime  > dt)
			{
				moveAgants(game, dga);
				lastTime = currentTime;
			}
			_win.repaint();
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);

	}


	/** 
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private  void moveAgants(game_service game, dw_graph_algorithms dga) {

		directed_weighted_graph gg = dga.getGraph();
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		if(lg == null)
		{
			return;
		}
		_ar.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		List<CL_Pokemon> ffsCopy =new ArrayList<>(ffs);
		_ar.setPokemons(ffs);

		for(int i = 0; i < _ar.getPokemons().size(); i++)
		{
			Arena.updateEdge(_ar.getPokemons().get(i), gg );
		}


//
//		List<CL_Agent> logCopy = new ArrayList<>(log);

//		double minimum = Double.MAX_VALUE;
//		for(CL_Agent agent : log)
//		{
//			for(CL_Pokemon p: ffs)
//			{
//				double w = nextNodeDist(dga, agent.getSrcNode(), p.get_edge().getDest());
//				if(agent.getShortestWeight() > w  + 0.0001);
//				{
//					agent.setShortestWeight(w);
//				}
//			}
//		}

		List<CL_Agent> logCopy = new ArrayList<>(log);
		Collections.sort(logCopy);


		//Best one so far

		double min = Double.MAX_VALUE;
		int minSrc = 0;
		int src = 0, dest = 0;
		double distance = Double.MAX_VALUE;
		CL_Pokemon minP = null;
		for(CL_Agent ag: logCopy)
		{
			min = Double.MAX_VALUE;
			minSrc = 0;
			minP = null;
			for(CL_Pokemon p : ffsCopy)
			{

					int type = p.getType();
					if(type == -1) // This means that the Pokemon sits on the edge max[p.src, p.dest] -> min[p.src, p.dest]
					{
						//We want to travel to the MAXIMUM first
						dest = max(p.get_edge().getSrc(), p.get_edge().getDest());
					}
					else //this means that the Pokemon sits on the edge min[p.src, p.dest] -> max[p.src, p.dest]

					{
						//We want to travel to the MINIMUM first
						dest = min(p.get_edge().getSrc(), p.get_edge().getDest());
					}

					//If this is true, the agent is not traveling! So we need to find the shortest path from agentSOURCE -> pokemonDEST
					if(!ag.isMoving())
					{
						src = ag.getSrcNode();
					}
					else{ // The agent is traveling, so we need to calculate from  agentDEST -> pokemonDEST

						src = ag.getNextNode();
					}


					//If dest and source are the same, that means we're on the SOURCE of the edge where the Pokemon lies
					//We must go towards its DEST/SRC depending on type!
					if(src == dest)
					{
						if(type == -1)
						{
							//if the type is -1, the edge starts at max[p.src, p.dest], thus we want to travel to the minimum!
							dest = min(p.get_edge().getSrc(), p.get_edge().getDest());
						}
						else
						{
							dest = max(p.get_edge().getSrc(), p.get_edge().getDest());

						}
					}
					distance = nextNodeDist(dga, src, dest);
					if(distance < min)
					{
						min = distance;
						minSrc = dest;
						minP = p;
					}



			}
			ffsCopy.remove(minP);
			game.chooseNextEdge(ag.getID(), nextNode(dga, ag.getSrcNode(), minSrc));
		}


		//






//		double value = Double.MAX_VALUE;
//		double maxValue = 0;
//		int maxSrc = 0;
//		int src = 0, dest = 0;
//		CL_Pokemon maxP = null;
//
//		for(CL_Agent ag: log)
//		{
//			maxValue = 0;
//			maxSrc = 0;
//			ArrayList<CL_Pokemon> removeList = new ArrayList<>();
//			for(CL_Pokemon p : ffsCopy)
//			{
//				src = ag.getSrcNode();
//				dest = p.get_edge().getDest();
//				if(src == dest)
//				{
//					dest = p.get_edge().getSrc();
//				}
//
//				if(value > maxValue)
//				{
//					maxValue = value;
//					maxP = p;
//					maxSrc = dest;
//				}
//			}
//			ffsCopy.remove(maxP);
//			game.chooseNextEdge(ag.getID(), nextNode(dga, ag.getSrcNode(), maxSrc));
//		}


//		int counter = 0;
//		for(int i = 0; i < _ar.getPokemons().size(); i++)
//		{
//			Arena2.updateEdge(_ar.getPokemons().get(i), gg );
//		}

//		for(int i=0;i<log.size();i++) {
//			if(counter >= ffs.size())
//			{
//				counter = 0;
//			}
//			CL_Agent ag = log.get(i);
//			int id = ag.getID();
//			int dest = ffs.get(counter).get_edge().getDest();
//			int src = ag.getSrcNode();
//			double v = ag.getValue();
//			if(ag.getNextNode()==-1) {
//
//
//				dest = nextNode(dga, src, dest);
//
//				if(src == dest)
//				{
//					dest = ffs.get(counter).get_edge().getSrc();
//					dest = nextNode(dga, src, dest);
//				}
//				game.chooseNextEdge(ag.getID(), dest);
//				System.out.println("Agent: "+id+", src: "+ag.getSrcNode()+"   turned to node: "+dest);
//			}
//			counter++;
//		}
	}

	private int max(int dest, int src) {
		if(dest >=src)
		{
			return dest;
		}
		return src;
	}

	private int min(int dest, int src) {

		if(dest >= src)
		{
			return src;
		}
		else
		{
			return dest;
		}
	}

	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private int nextNode(dw_graph_algorithms dga, int src, int dest) {
		if(src == dest)
		{
			return dga.shortestPath(src,dest).get(0).getKey();
		}
		return dga.shortestPath(src,dest).get(1).getKey();
	}
	private int nextNode(dw_graph_algorithms dga, int src, int dest, List<CL_Pokemon> list) {
		if(src == dest)
		{
			return dga.shortestPath(src,dest).get(0).getKey();

		}
		return dga.shortestPath(src,dest).get(1).getKey();
	}

	private double nextNodeDist(dw_graph_algorithms dga, int src, int dest)
	{
		return dga.shortestPathDist(src, dest);
	}


	private void init(game_service game, directed_weighted_graph gg) {
		String fs = game.getPokemons();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);

	
		_win.show();
		String info = game.toString();
		JSONObject line;
		try {

			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			ArrayList<CL_Pokemon> cl_fsCopy =new ArrayList<>(cl_fs);

			for (CL_Pokemon cl_f : cl_fs) {
				Arena.updateEdge(cl_f, gg);
			}


			for(int a = 0;a<rs;a++) {
				double max = 0;
				int maxIndex = 0;
				for(int b = 0; b < cl_fsCopy.size(); b++)
				{
					if(cl_fsCopy.get(b).getValue() > max)
					{
						max = cl_fsCopy.get(b).getValue();
						maxIndex = b;
					}
				}
				CL_Pokemon c = cl_fsCopy.get(maxIndex);
				cl_fsCopy.remove(maxIndex);

				int nn;
				if(c.getType() == 1)
				{
					nn = min(c.get_edge().getSrc(), c.get_edge().getDest());
				}
				else{
					nn = max(c.get_edge().getSrc(), c.get_edge().getDest());
				}
				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}



	}
}
