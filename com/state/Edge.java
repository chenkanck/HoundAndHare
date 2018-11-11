package com.state;

public class Edge {
	private Node n_one;
	private Node n_other;
	
	public Edge (Node a, Node b)
	{
		this.n_one=a;
		this.n_other=b;
	}
	
	@Override
	public boolean equals(Object obj) {
		Edge e = (Edge) obj;
		if ((n_one.equals(e.n_one)&&n_other.equals(e.n_other))
				||(n_one.equals(e.n_other)&&n_other.equals(e.n_one))
				)
				return true;
			else 
				return false;
	}
	
	
	//is this node is a part of the edge?
	public boolean matchNode (Node n)
	{
		if (n_one.equals(n) || n_other.equals(n))
			return true;
		else 
			return false;
	}
	
//	public Node getPeerNode (Node one)
//	{
//		if (matchNode(one))
//		{
//			if (one.equals(n_one))
//				return n_other;
//			else
//				return n_one;
//		}
//		else
//		{
//			System.out.println("ERROR:This node doesn't match the edge!");
//			return one;
//	
//		}
//	}
	public int getPeerNodeId (Node one)
	{
		if (matchNode(one))
		{
			if (one.equals(n_one))
				return n_other.getID();
			else
				return n_one.getID();
		}
		else
		{
			System.out.println("ERROR:This node doesn't match the edge!");
			return -1;
	
		}
	}
	
	public Node getOne(){
		return n_one;
	}
	public Node getOther(){
		return n_other;
	
	}
}
