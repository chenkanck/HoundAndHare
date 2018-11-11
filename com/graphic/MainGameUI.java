package com.graphic;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.*;

import com.state.AlgorithmPruning;
import com.state.GameState;

public class MainGameUI extends JFrame{
	
	public MainGameUI (int l,int s)
	{
		level=l;
		side=s;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(400, 300);
		this.setResizable(false);
		this.setVisible(true);
		crtpos="";
		lastpos="";
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT );
		this.setTitle("Hare&Hounds");
		jpanel= new MyPanel(image);
		jpanel.setLayout(null);
			
		initButton();
		
		add(jpanel);
		turn = 0;
		if (side==1)
			this.startWithHound();
	}
	public void startWithHound ()
	{
		crtpos="5";
		lastpos="5";
		playGame();
		refreshBoard();
		lastpos="";
	}
	
	//intern Class
	public class ClickAction implements ActionListener {
		
		public void actionPerformed (ActionEvent event)
		{
			ImageButton srcButton;
			srcButton=((ImageButton)event.getSource());
			crtpos=srcButton.getText();
			if (lastpos=="")
			{
				System.out.println(crtpos);
				int crt=Integer.parseInt(crtpos);
				int cont=game.getNodes().get(crt).getContent();
				if (cont!=0)
				{
					lastpos=crtpos;
					if (cont==1)
						srcButton.changeContent(2);
					else
						srcButton.changeContent(-2);
					srcButton.updateUI();
				}
			}
			else
			{
				System.out.println(crtpos);
				playGame();
				playGame();
				refreshBoard();
				checkEnd();
				lastpos="";
			}
		}
	}
	
	public void playGame ()  
	{
		int src=Integer.parseInt(lastpos);
		int dst=Integer.parseInt(crtpos);
		game.showChessboard();
		if (turn==0) //hound's turn
		{
			if (side==1)
			{
				AlgorithmPruning algor= new AlgorithmPruning(game);
				ArrayList<Integer> nextStep= new ArrayList<Integer>();
				nextStep=algor.doAlphaBetaMax();
				src=nextStep.get(0);
				dst=nextStep.get(1);	
			}
			game.showChessboard();
			System.out.println("Hound:give me a move:");
			System.out.println(src+"->"+dst);
			if (game.getHoundNodes(src).contains(dst))
				{
					game.makeMove(src,dst);
					turn=1;
				}
			else
				System.out.println("Hound can't move to a unreachable node!");
		}
		else //hare's turn
		{
			if (side==0)
			{
				AlgorithmPruning algor= new AlgorithmPruning(game);
				ArrayList<Integer> nextStep= new ArrayList<Integer>();
				nextStep=algor.doAlphaBetaMin();
				src=nextStep.get(0);
				dst=nextStep.get(1);
			}
//			System.out.println("NextStep:"+dst);
			game.showChessboard();
			System.out.println("hare:give me a move:");
			System.out.println(src+"->"+dst);
			if (game.getHareNodes(src).contains(dst))
				{
					game.makeMove(src, dst);
					turn=0;
				}
			else
				System.out.println("hare can't move to a unreachable node!");
		}		
	}
	
	public void checkEnd ()
	{
		if (game.checkGoal()==1)
		{
			JOptionPane.showMessageDialog(null, "Hound win");
			System.out.println("Hound win");
			System.exit(0);
			
		}
		if (game.checkGoal()==-1) {
			JOptionPane.showMessageDialog(null, "Hare win");
			System.out.println("Hare win");
			System.exit(0);
		}
	}
	
	public void refreshBoard()
	{
		for (int i=0;i<11;i++)
		{
			jjb[i].changeContent(game.getNodes().get(i).getContent());
			jjb[i].updateUI();
		}
	}
	/**
	 * initial 11 Buttons
	 */
	private void initButton ()
	{
		jjb[0]=new ImageButton("0");
		jjb[0].setBounds(10, 162, 45, 45);
		jjb[0].setVisible(true);
		jpanel.add(jjb[0]);
		jjb[10]=new ImageButton("10");
		jjb[10].setBounds(10+141*4, 162, 45, 45);
		jjb[10].setVisible(true);
		jpanel.add(jjb[10]);
		for (int i=1;i<=7;i+=3)
		{
			jjb[i]=new ImageButton(""+i);
			jjb[i].setBounds(10+141*(i+2)/3, 51, 45, 45);
			jjb[i].setVisible(true);
			jpanel.add(jjb[i]);
		}
		for (int i=2;i<=8;i+=3)
		{
			jjb[i]=new ImageButton(""+i);
			jjb[i].setBounds(10+141*(i+1)/3, 162, 45, 45);
			jjb[i].setVisible(true);
			jpanel.add(jjb[i]);
		}
		for (int i=3;i<=9;i+=3)
		{
			jjb[i]=new ImageButton(""+i);
			jjb[i].setBounds(10+141*i/3, 273, 45, 45);
			jjb[i].setVisible(true);
			jpanel.add(jjb[i]);
		}
		//Create 11 buttons
		for (int i=0;i<11;i++)
			{
			jjb[i].addActionListener(new ClickAction());
			jjb[i].changeContent(game.getNodes().get(i).getContent());
			}
	}
	
	private ImageButton jjb[]= new ImageButton[11];
	private Image image = new ImageIcon("back1.png").getImage();
	private JPanel jpanel;
	private static final int DEFAULT_WIDTH = 640 ;
	private static final int DEFAULT_HEIGHT = 400 ;
	private String crtpos= new String();
	private String lastpos = new String();
	private static GameState game = new GameState();
	private int turn;
	
	private int level;
	private int side;
}


