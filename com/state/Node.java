package com.state;

public class Node {
	private int column_num;
	private int content;   //1 is hound, -1 is hare 0 is null
	private int node_id;
	
	public Node ()
	{
		
	}
	public Node (int col, int content, int id)
	{
		this.column_num=col;
		this.content=content;
		this.node_id=id;
	}
	
//	public boolean equals(node n){
//		
//	}
	
	@Override
	public boolean equals(Object obj) {
		Node n= (Node)obj;
		if (this.node_id==n.node_id)
			return true;
		else
			return false;
		} 
	
	public void changeContent(int new_cont)
	{
		content= new_cont;
	}
	
	public int getID(){
		return node_id;
	}
	public int getColumn ()
	{
		return column_num;
	}
	
	public int getContent ()
	{
		return content;
	}
}
