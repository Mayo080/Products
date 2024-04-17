package FrameTest1;

import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.*;

/**
 * @author Mayo080
 *
 */
public class MyFrameTest extends JFrame{
	Label aLabel1;
	
	public MyFrameTest() {
		this.setSize(300,200);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});
		this.addMouseListener(new MyMouseAdapter());
		aLabel1=new Label();
		this.getContentPane().add(aLabel1,BorderLayout.NORTH);
		this.setVisible(true);
	}
	
	class MyMouseAdapter extends MouseAdapter{
		public void mouseClicked(MouseEvent ev) {
			int x=ev.getX();
			int y=ev.getY();
			int n=ev.getClickCount();
			String str ="X:"+Integer.toString(x);
			str +="Y:"+Integer.toString(y);
			str +="click:"+Integer.toString(n);
			aLabel1.setText(str);
			
			Graphics g2=getGraphics();
			g2.setColor(Color.red);
			g2.fillOval(x, y, 20, 20);
			g2.drawRect(x,y,20,20);
		}
	}

	public static void main(String[] args) {
		System.out.println("Message from MyFrameTest\n");
		MyFrameTest f=new MyFrameTest();
	}

}
