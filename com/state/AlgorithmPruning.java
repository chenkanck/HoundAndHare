package com.state;

import java.util.ArrayList;

public class AlgorithmPruning {
	private GameState crtState;
//	private int count;
//	private Edge nextStep;
	
	public AlgorithmPruning(GameState comingSate) {
		this.crtState=comingSate;
//		count=0;
	} 
	
	public ArrayList<Integer> doAlphaBetaMax ()
	{
		ArrayList<Integer> nextStep=new ArrayList<Integer>();
		nextStep.add(-1);
		nextStep.add(-1);
		GameState state = new GameState(crtState);
//		if (state.checkGoal()!=0)
//			return state.checkGoal();
		double v=Double.NEGATIVE_INFINITY;
		int[] houndPos= new int[3];
		int i=0,j;
		int count=0;
		double a=Double.NEGATIVE_INFINITY;
		double b=Double.POSITIVE_INFINITY;
		ArrayList<Integer> houndMove=new ArrayList<Integer>();
		
		for (Node n: state.getNodes())
			if (n.getContent()==1)
			{
				houndPos[i]=n.getID();
				i++;
				if (i==3)
					break;
			}
		for (i=0;i<3;i++)               //only one step to win?
		{	
			houndMove.clear();
			houndMove=state.getHoundNodes(houndPos[i]); //store the valid moves of a hound
			sortRandom(houndMove);
			for (j=0;j<houndMove.size();j++)
			{
				count++;
				GameState nextState= new GameState(state);
				int dstId=houndMove.get(j);
				nextState.makeMove(houndPos[i], dstId);
				if (nextState.checkGoal()==1)
				{
					nextStep.set(0, houndPos[i]);
					nextStep.set(1, houndMove.get(j));
					return nextStep;
				}
				
			}
		}	
		for (i=0;i<3;i++)
		{	
			houndMove.clear();
			houndMove=state.getHoundNodes(houndPos[i]); //store the valid moves of a hound
			sortRandom(houndMove);
			for (j=0;j<houndMove.size();j++)
			{
				count++;
				GameState nextState= new GameState(state);
				int dstId=houndMove.get(j);
				nextState.makeMove(houndPos[i], dstId);
				if (nextState.checkGoal()==1)
				{
					nextStep.set(0, houndPos[i]);
					nextStep.set(1, houndMove.get(j));
					return nextStep;
				}
				double temp=computeMIN_Value(nextState, a, b,1);
				if (temp>v)
					{
					v=temp;
					nextStep.set(0, houndPos[i]);
					nextStep.set(1, houndMove.get(j));
					}
				if (v==1)
					return nextStep;
				if (v>=b)
					return nextStep;
				a=max(a,v);
			}
		}	
		return nextStep;
	}
	public ArrayList<Integer> doAlphaBetaMin()
	{
		ArrayList<Integer> nextStep=new ArrayList<Integer>();
		nextStep.add(-1);
		nextStep.add(-1);
		GameState state = new GameState(crtState);
//		if (state.checkGoal()!=0)
//			return state.checkGoal();
		double v=Double.POSITIVE_INFINITY;
		int i;
		double a=Double.NEGATIVE_INFINITY;
		double b=Double.POSITIVE_INFINITY;
		int harePos=-1; //just initial
		ArrayList<Integer> hareMove= new ArrayList<Integer>();
		
		for (Node n:state.getNodes())
			if (n.getContent()==-1)
			{
				harePos=n.getID();
				break;
			}
		hareMove.clear();
		hareMove=state.getHareNodes(harePos);   //store the valid nodes for hare
		sortRandom(hareMove);
		for (i=0;i<hareMove.size();i++)			//only one step to win
		{
			GameState nextState =new GameState(state);
			int dstId=hareMove.get(i);
			nextState.makeMove(harePos, dstId);
			if (nextState.checkGoal()==-1)
			{
				nextStep.set(0, harePos);
				nextStep.set(1, hareMove.get(i));
				return nextStep;
			}
		}
		for (i=0;i<hareMove.size();i++)
		{
			GameState nextState =new GameState(state);
			int dstId=hareMove.get(i);
			nextState.makeMove(harePos, dstId);
			if (computeMAX_Value(nextState, a, b,1)<v)
				{
				v=computeMAX_Value(nextState, a, b,1);
				nextStep.set(0, harePos);
				nextStep.set(1, hareMove.get(i));
				}
			if (v==-1)
				return nextStep;
			if (v<=a)
				return nextStep;
			b=min(b,v);
		}
	
		return nextStep;
	}
	/**
	 * Hound's strategy
	 * @param state
	 * @param a
	 * @param b
	 * @return
	 */
	public double computeMAX_Value (GameState state,double a,double b,int depth) 
	{
		if (state.checkGoal()!=0)
			return state.checkGoal();
		if (depth>=9)
			{
				if (state.calcOpposition()==0)
					return -0.75;
				else
					return 0;
			}
		double v=Double.NEGATIVE_INFINITY;
		int[] houndPos= new int[3];
		int i=0,j;
		int count=0;
		ArrayList<Integer> houndMove=new ArrayList<Integer>();
		for (Node n: state.getNodes())
			if (n.getContent()==1)
			{
				houndPos[i]=n.getID();
				i++;
				if (i==3)
					break;
			}
		for (i=0;i<3;i++)
		{	
			houndMove.clear();
			houndMove=state.getHoundNodes(houndPos[i]); //store the valid moves of a hound
			sortRandom(houndMove);
			for (j=0;j<houndMove.size();j++)
			{
				count++;
				GameState nextState= new GameState(state);
				int dstId=houndMove.get(j);
				nextState.makeMove(houndPos[i], dstId);
				v=max(v,computeMIN_Value(nextState, a, b,depth+1));
				if (v==1)
					return v;
				if (v>=b)
					return v;
				a=max(a,v);
				
			}
		}	
		return v;
	}
	/**
	 * Hare's strategy
	 * @param state
	 * @param a
	 * @param b
	 * @return
	 */
	public double computeMIN_Value (GameState state,double a,double b, int depth) 
	{
		
		if (state.checkGoal()!=0)
			return state.checkGoal();
		if (depth>=9)
		{
			if (state.calcOpposition()==0)
				return 0.75;
			else
				return 0;
		}
		double v=Double.POSITIVE_INFINITY;
		int i;
		int count=0;
		int harePos=-1; //just initial
		ArrayList<Integer> hareMove= new ArrayList<Integer>();
		for (Node n:state.getNodes())
			if (n.getContent()==-1)
			{
				harePos=n.getID();
				break;
			}
		hareMove.clear();
		hareMove=state.getHareNodes(harePos);   //store the valid nodes for hare
		sortRandom(hareMove);
		for (i=0;i<hareMove.size();i++)
		{
			count++;
//			System.out.println("Current Search Tree Depth:"+depth+"\t Leaf:"+count);
			GameState nextState =new GameState(state);
			int dstId=hareMove.get(i);
			nextState.makeMove(harePos, dstId);
			v=min(v,computeMAX_Value(nextState, a, b,depth+1));
			if (v==-1)
				return v;
			if (v<=a)
				return v;
			b=min(b,v);
			
		}
//		count++;
//		System.out.println(count);
	
		return v;
	}
	public static void wait1 ()
	{
		try
		{
		Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
			System.out.println(e);
		}
	}
	
	public static double max (double... values )
	{
		double largest= Double.NEGATIVE_INFINITY;
		for (double v : values)
			if (v>largest)
				largest=v;
		return largest;
	}
	
	public static double min (double... values)
	{
		double minimum = Double.POSITIVE_INFINITY;
		for (double v : values)
			if (v<minimum)
				minimum=v;
		return minimum;
	}
	
	public void sortRandom (ArrayList<Integer> array)
	{
		int i;
		int r;
		int tempN;
		for (i=0;i<array.size();i++)
		{
			r=(int)(array.size()*Math.random());
			tempN=array.get(i);
			array.set(i,array.get(r));
			array.set(r,tempN);
		}
	}
	
}
