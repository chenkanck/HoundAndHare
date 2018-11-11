package com.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class GameState implements Cloneable{
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private static ArrayList<Edge> edges = new ArrayList<Edge>();
	private static ArrayList<Integer> nodesEval= new ArrayList<Integer>();
//	private ArrayList<Node> tempNodes = new ArrayList<Node>();
//	private static int count=0;
	private int verticMove=0;
	
	public GameState ()
	{
		//construct the game table information
		//create nodes
		int i,j;
		nodes.clear();
		nodes.add(new Node(1,1,0));
		int n=1;
		for (i=2;i<=4;i++)
		{
			for (j=1;j<=3;j++)
			{
				nodes.add(new Node(i,0,n));
				n++;
			}
		}
		nodes.add(new Node(5,-1,n));
		nodes.get(1).changeContent(1);
		nodes.get(3).changeContent(1);
		
		//create edges
		initEdges();
		initNodeEval();
		}
	
	public GameState (GameState s)  // Create a copy of State
	{
		this.verticMove=s.verticMove;
		nodes.clear();
		int col,con,id;
		for (int i=0;i<s.getNodes().size();i++)
		{
			col=s.getNodes().get(i).getColumn();
			con=s.getNodes().get(i).getContent();
			id=s.getNodes().get(i).getID();
			nodes.add(new Node(col,con,id));
		}
		
	}

	/**
	 * find all valid nodes for the hare
	 * @param crt_node
	 * @return a list of nodeID
	 */
	public ArrayList<Integer> getHareNodes (int crtNodeId)
	{
		int i;
		int matchedID;
		ArrayList<Integer> nextStep = new ArrayList<Integer>();
		if (nodes.get(crtNodeId).getContent()!=-1)
		{
			System.out.println("FindNodeERROR:This node is not a hare!");
			return nextStep;
		}
		for (i=0;i< edges.size(); i++)
		{
			
			if (edges.get(i).matchNode(nodes.get(crtNodeId)))		
			{
				matchedID=edges.get(i).getPeerNodeId(nodes.get(crtNodeId));
				if (nodes.get(matchedID).getContent()==0)
//				System.out.println(crt_node.get_id()+"-->>>>"+edges.get(i).get_another(crt_node).get_id());;
				nextStep.add(matchedID);
			}
		}	
		return nextStep;
	}
	/**
	 * find all valid nodes for a hound
	 * @param crt_node
	 * @return a list of nodeID
	 */
	public ArrayList<Integer> getHoundNodes (int crtNodeId)
	{
		int i;
		int otherNodeId;
		ArrayList<Integer> nextStep = new ArrayList<Integer>();
		if (nodes.get(crtNodeId).getContent()!=1)
		{
			System.out.println("FindNodeERROR:This node is not a hound!");
			return nextStep;
		}
		for (i=0;i< edges.size(); i++)
		{
			if (edges.get(i).matchNode(nodes.get(crtNodeId)))
			//find a useful edge
			{
				otherNodeId=edges.get(i).getPeerNodeId(nodes.get(crtNodeId));
				if ((nodes.get(otherNodeId).getContent()==0)&&(nodes.get(crtNodeId).getColumn()<=nodes.get(otherNodeId).getColumn()))
				{
					//System.out.println(crt_node.getID()+"<can only to>"+otherNode.getID());;
					nextStep.add(otherNodeId);
				}
			}
		}	
		return nextStep;
	}
	/**
	 * Move from src to dst
	 * @param src
	 * @param dst
	 * @return Whether moved successfully
	 */
	public void makeMove (int src,int dst)
	{
		
		if (nodes.get(src).getContent()!=0&&nodes.get(dst).getContent()==0)
			if (edges.contains(new Edge(nodes.get(src),nodes.get(dst))) || edges.contains(new Edge(nodes.get(dst),nodes.get(src))))
			{
				if (nodes.get(src).getContent()==1)
				{
					if(nodes.get(src).getColumn()==nodes.get(dst).getColumn()) //hound move vertically
						verticMove++;
					else
						verticMove=0;
				}
				nodes.get(dst).changeContent(nodes.get(src).getContent());
				nodes.get(src).changeContent(0);
				
//				System.out.println("Moved!  "+src+">>>>>"+dst+"\tCount:"+count+"\t vertic move:"+verticMove);
//				showChessboard();
//				count++;
			}
			else 
				{
					System.out.println("MakeMove_ERROR: No way from source:"+src+" to dest:"+dst);
					
				}
		else
			{
				System.out.println("MakeMove_ERROR: Source:"+src+" is empty or Dest:"+dst+" is full!");	
				
			}
		
		
	}
	/**
	 * check the state
	 * @return 1:hound win
	 * @return -1:hare win
	 * @return 0:nothing
	 */
	public int checkGoal()
	{
		int i;
		int hare_nodeID=-1; // just initial, the number doesn't matter
		int min_col=5;
		if (verticMove==10)	//hound move vertically 10 times
		{
			System.out.println("Move vertically 10times!");
			return -1;          
		}
		for (i=0;i<nodes.size();i++)
		{	
			if (nodes.get(i).getContent()==1&&nodes.get(i).getColumn() < min_col)  //find a hound node 
				min_col=nodes.get(i).getColumn();
			if (nodes.get(i).getContent()==-1) //find hare node
				hare_nodeID=i;
		}
		
		if (getHareNodes(hare_nodeID).isEmpty())
			return 1;               //hound win
		if (nodes.get(hare_nodeID).getColumn() <= min_col)
			return -1;			//hare win
		return 0;               //can't tell the result
	}
	public double eval()
	{
		if (checkGoal()!=0)
			return checkGoal();
		else 
			return 0.5;
	}
	
	public int calcOpposition ()
	{
		int sum=0;
		for (int i=0;i<nodes.size();i++)
		{
			if (nodes.get(i).getContent()!=0)
				sum+=nodesEval.get(i);
		}
		sum=sum%3;
		return sum;
					
	}
	
	public void playGame () throws IOException 
	{
		showChessboard();
		String s= new String();
		String[] sn;
		int src=-1,dst=-1;
		
		int turn=0;
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		while(checkGoal()==0)
		{
			if (turn==0) //hound's turn
			{

				
//				long time;
//				time= System.currentTimeMillis();
//				
//				AlgorithmPruning algor= new AlgorithmPruning(this);
//				ArrayList<Integer> nextStep= new ArrayList<Integer>();
//				nextStep=algor.doAlphaBetaMax();
//				src=nextStep.get(0);
//				dst=nextStep.get(1);
//				
//				time= System.currentTimeMillis()-time;
//				System.out.println("Time used:"+time);
				
				showChessboard();
				System.out.println("Hound:give me a move:");
				try {
					s=br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				sn = s.split("-");
				src=new Integer(sn[0]);
				dst=new Integer(sn[1]);
				
				System.out.println(src+"->"+dst);
				if (getHoundNodes(src).contains(dst))
					{
						makeMove(src,dst);
						turn=1;
					}
				else
					System.out.println("Hound can't move to a unreachable node!");
			}
			else //hare's turn
			{
				
				long time;
				time= System.currentTimeMillis();
//				
				AlgorithmPruning algor= new AlgorithmPruning(this);
				ArrayList<Integer> nextStep= new ArrayList<Integer>();
				nextStep=algor.doAlphaBetaMin();
				src=nextStep.get(0);
				dst=nextStep.get(1);
				System.out.println("NextStep:"+dst);
				time= System.currentTimeMillis()-time;
				System.out.println("Time used:"+time);
				

				showChessboard();
//				System.out.println("hare:give me a move:");
//				try {
//					s=br.readLine();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				sn = s.split("-");
//				src=new Integer(sn[0]);
//				dst=new Integer(sn[1]);

				System.out.println(src+"->"+dst);
				if (getHareNodes(src).contains(dst))
					{
						makeMove(src, dst);
						turn=0;
					}
				else
					System.out.println("hare can't move to a unreachable node!");
			}
		}
		if (checkGoal()==1)
			System.out.println("Hound win");
		else 
			System.out.println("Hare win");
	}
	
	public void showEdges ()
	{
		int i;
		System.out.println("Edges Size:"+edges.size());
		for (i=0;i<edges.size();i++)
			System.out.println("edge:"+edges.get(i).getOne().getID()+"---"+edges.get(i).getOther().getID());		
	}
	
	public void showNodes ()
	{
		int i;
		System.out.println("Nodes Size:"+nodes.size());
		for (i=0; i<nodes.size();i++)
			System.out.println("id:"+nodes.get(i).getID()+"-->>"+nodes.get(i).getContent()+"  col:"+nodes.get(i).getColumn());
	}
	
	public void showChessboard ()
	{
		int i;
		for (i=1;i<=7;i+=3)
			System.out.printf("\t"+nodes.get(i).getContent());
		System.out.printf("\n"+nodes.get(0).getContent());		
		for (i=2;i<=8;i+=3)
			System.out.printf("\t"+nodes.get(i).getContent());
		System.out.printf("\t"+nodes.get(10).getContent()+"\n");	
		for (i=3;i<=9;i+=3)
			System.out.printf("\t"+nodes.get(i).getContent());
		System.out.printf("\n");
	}
	
	public ArrayList<Node> getNodes ()
	{
		return nodes;
	}
	
	public static ArrayList<Edge> getEdges ()
	{
		return edges;
	}
	public static ArrayList<Integer> getNodesEval()
	{
		return nodesEval;
	}
	private void initEdges ()
	{
		int i;
		if (edges.isEmpty())
		{
			for (i=1;i<=3;i++)
			{
				//
				edges.add(new Edge(nodes.get(0),nodes.get(i)));
				edges.add(new Edge(nodes.get(i),nodes.get(i+3)));
				edges.add(new Edge(nodes.get(i+3),nodes.get(i+6)));
				edges.add(new Edge(nodes.get(i+6),nodes.get(10)));
				//
				edges.add(new Edge(nodes.get(3*i-1),nodes.get(3*i)));
				edges.add(new Edge(nodes.get(3*i-2),nodes.get(3*i-1)));
			}
			for (i=1;i<=9;i+=2)
			{
				if (nodes.get(i).equals(nodes.get(5)) == false)
					edges.add(new Edge(nodes.get(5),nodes.get(i)));
			}
		}
	}
	private void initNodeEval()
	{
		for (int i=0;i<=10;i++)
			nodesEval.add(-1);
		for (int i=1;i<=9;i+=2)
			nodesEval.set(i, 0);
		for (int i=2;i<=8;i+=2)
			nodesEval.set(i, 1);
		for (int i=0;i<=10;i+=5)
			nodesEval.set(i, 2);
	}
}
