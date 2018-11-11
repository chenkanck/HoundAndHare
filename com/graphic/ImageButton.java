package com.graphic;
import java.awt.*;
import javax.swing.*;

public class ImageButton extends JButton{
	Image image1=new ImageIcon("dog.png").getImage();
	Image image2= new ImageIcon("hare.png").getImage();
	Image image3= new ImageIcon("blank.png").getImage();
	Image image4= new ImageIcon("dog2.png").getImage();
	Image image5= new ImageIcon("hare2.png").getImage();
	int content=0;
	public ImageButton (String text)
	{
		super(text);
		setOpaque(false);
		setFocusable(false);
		setBorderPainted(false);
		this.setPreferredSize(new Dimension(image1.getWidth(this),image1.getHeight(this)));
		
		
	}
	
	protected void paintComponent (Graphics g) {
		if (content==1)		//draw dog
			g.drawImage(image1, 0, 0, null);
		if (content==2)		//draw dog2
			g.drawImage(image4, 0, 0, null);
		if (content==-1)	//draw hare
			g.drawImage(image2, 0, 0, null);
		if (content==-2)	//draw hare2
			g.drawImage(image5, 0, 0, null);
		if (content==0)		//draw blank
			g.drawImage(image3, 0, 0, null);
	}
	
	public void changeContent(int newC)
	{
		content=newC;
	}
}
