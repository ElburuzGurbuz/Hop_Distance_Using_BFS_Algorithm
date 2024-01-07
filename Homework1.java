
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Homework1
{
	static BufferedReader br;
	static ArrayList<String> laptops = new ArrayList<String>();
	static ArrayList<Integer> Array = new ArrayList<Integer>();

	public static void main(String[] args) throws IOException
	{
		Map<Integer, LinkedList<Edge>> g = new HashMap<>();
		
		String thisLine = null;
		
		int numbOfLaptops;
		double x, y, r;
		
		String file = "";
	    if(args.length > 0) 
	    {
	    	file += args[0];
	    } 
	    else
	    {
	    	
	      System.out.println("Wrong file name.");
	      System.exit(0);
	    }
	    
		br = new BufferedReader(new FileReader(file));

		while ((thisLine = br.readLine()) != null)
		{
			if (thisLine.contains("##"))
			{
				continue;
			}
			laptops.add(thisLine);
		}
		numbOfLaptops = Integer.parseInt(laptops.get(0));
		String array;
		Node[] Nodes = new Node[numbOfLaptops];

		for (int a = 1; a < laptops.size(); a++)
		{
			array = laptops.get(a);
			String split[] = array.split("\\s+");
			x = Double.parseDouble(split[0]);
			y = Double.parseDouble(split[1]);
			r = Double.parseDouble(split[2]);
			Nodes[a - 1] = new Node(x, y, r, a-1);
		}

		boolean check;
		int control = 0;

		for (int i = 0; i < Nodes.length; i++)
		{
			for (int j = 0; j < Nodes.length; j++)
			{
				check = checkNeighbours(Nodes[i].getX(), Nodes[j].getX(), 
										Nodes[i].getY(), Nodes[j].getY(),
										Nodes[i].getR(), Nodes[j].getR());
				if (check)
				{
					addEdge(g, Nodes[i].getID(), Nodes[j].getID());
				}
			}
		}
		
		Array.add(0);
		int source = 0, dest = 1;
		for (int i = 0; i < g.size(); i++)
		{
			printShortestDistance(g, source, dest, g.size());
			dest++;
		}
		
		FileWriter f0 = new FileWriter("output.txt");

		String newLine = System.getProperty("line.separator");

		for (int i = 0; i < Array.size()-1; i++) 
		{
			f0.write(Array.get(i)+newLine);
		}
		
		f0.close();
	}
	
	static class Node
	{
		private double x;
		private double y;
		private double r;
		private int ID;

		public Node(double x, double y, double r, int ID)
		{
			this.x = x;
			this.y = y;
			this.r = r;
			this.ID = ID;
		}

		public double getX()
		{
			return x;
		}

		public double getY()
		{
			return y;
		}

		public double getR()
		{
			return r;
		}

		public int getID()
		{
			return ID;
		}

	}

	static class Edge
	{
		int endVertex;
		

		Edge(int b)
		{
			this.endVertex = b;
			
		}
	}

	public static boolean checkNeighbours(double x1, double x2, double y1, double y2, double r1, double r2)
	{
		double distSq = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
		double radSumSq = (r1 + r2) * (r1 + r2);

		if (distSq == radSumSq || distSq < radSumSq)
			return true;
		else
			return false;
	}

	private static void printShortestDistance(Map<Integer, LinkedList<Edge>> g, int source, int dest, int size)
	{
		int[] dist = new int[size];
		boolean[] visited = new boolean[size];
		
		Queue<Integer> queue = new LinkedList<>(); //
		Arrays.fill(dist, -1);
		Arrays.fill(visited, false);

		visited[source] = true;
		dist[source] = 0;

		queue.add(source);
		while (!queue.isEmpty())
		{
			Integer vertex = queue.poll();
			for (Edge e : g.get(vertex)) 
			{
				if (!visited[e.endVertex])
				{
					dist[e.endVertex] = dist[vertex] + 1;
					visited[e.endVertex] = true;
					queue.add(e.endVertex);

					if (e.endVertex == dest)
					{
						Array.add(dist[dest]);
						return;
					}
				}
			}
		}
		
		Array.add(0);
	}

	private static void addEdge(Map<Integer, LinkedList<Edge>> g, int src, int dest)
	{
		LinkedList<Edge> val;
		if (g.containsKey(src))
		{
			val = g.get(src);
		}
		else
		{
			val = new LinkedList<>();
		}
		val.add(new Edge(dest));
		g.put(src, val);

		LinkedList<Edge> val2;
		if (g.containsKey(dest))
		{
			val2 = g.get(dest);
		}
		else
		{
			val2 = new LinkedList<>();
		}
		val2.add(new Edge(src));
		g.put(dest, val2);
	}
}