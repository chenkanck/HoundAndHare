package com.graphic;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

public class SetGame extends JFrame{
	
	private static final int DEFAULT_WIDTH = 320 ;
	private static final int DEFAULT_HEIGHT = 200 ;
	
	public SetGame()
	{
		this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT );
		this.setTitle("Hare&Hounds Set");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(500, 400);
		this.setResizable(false);
		this.setVisible(true);
		
		JPanel cpanel = new JPanel();
		JPanel title_panel = new JPanel();
		JPanel go_panel = new JPanel();
		
		ArrayList<String> level = new ArrayList<String>();
		level.add("Easy");
		level.add("Medium");
		level.add("Hard");
		ArrayList<String> side = new ArrayList<String>();
		side.add("Hound");
		side.add("hare");
		JComboBox levelBox = new JComboBox(new DefaultComboBoxModel(level.toArray()));
		JComboBox sideBox = new JComboBox(new DefaultComboBoxModel(side.toArray()));
		JButton goButton = new JButton("Go");
		JLabel title = new JLabel("Hounds and Hare Logic Game");
		
		title_panel.add(title);
		title_panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		cpanel.setLayout(new GridLayout(2,2));
		cpanel.add(new JLabel("Level"));
		cpanel.add(levelBox);
		cpanel.add(new JLabel("Side"));
		cpanel.add(sideBox);
		cpanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));;
		go_panel.add(goButton);
		go_panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		add(cpanel, BorderLayout.CENTER);
		add(go_panel, BorderLayout.SOUTH);
		add(title_panel,BorderLayout.NORTH);
		goButton.addActionListener(new GoAction(this,levelBox,sideBox));
	}
	
	//intern class
	public class GoAction implements ActionListener
	{
		private JFrame jframe;
		private JComboBox levelBox;
		private JComboBox sideBox;
		public GoAction (JFrame jf,JComboBox lb,JComboBox sb)
		{
			jframe=jf;
			levelBox=lb;
			sideBox=sb;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(levelBox.getSelectedItem());
			System.out.println(sideBox.getSelectedItem());
			System.out.println(levelBox.getSelectedIndex());
			System.out.println(sideBox.getSelectedIndex());
			jframe.dispose();
			MainGameUI newGame = new MainGameUI(levelBox.getSelectedIndex(),sideBox.getSelectedIndex());
		}
		
	}
	public static void main (String[] args) throws IOException
	{
		
		
		EventQueue.invokeLater (new Runnable() {
			public void run()
			{
				SetGame frame =new SetGame();
				
			}
		});
				
	}
}
