/**
 * 
 */
package QueenBoard;

/**
 * @author Mayo080
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QueenBoard extends JFrame implements ActionListener {

	int boardSize=8;
	JLabel title;
	JButton[][] buttonGrid;
	int[][] state;
	ImageIcon queenOn;
	ImageIcon queenChk;
	ImageIcon queenOff;
	boolean trapChk=true;
	JButton iButton;
	JRadioButton dButton;
	Label aLabel1;
	int queenNum=0;

	public QueenBoard()
	{
		this.setSize(400,400);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(0);
			}
		}); 

		getContentPane().setLayout(new BorderLayout());
		queenOn = new ImageIcon("C:\\Users\\aisat\\eclipse-workspace\\EightQueenBoard\\src\\QueenBoard\\queen-on.gif" ); // 黒
		queenOff = new ImageIcon("C:\\Users\\aisat\\eclipse-workspace\\EightQueenBoard\\src\\QueenBoard\\queen-off.gif" ); // 空白
		queenChk = new ImageIcon("C:\\Users\\aisat\\eclipse-workspace\\EightQueenBoard\\src\\QueenBoard\\queen-chk.gif" ); // 赤

		state = new int[boardSize][boardSize];

		buttonGrid = new JButton[boardSize][boardSize];
		JPanel backpanel = new JPanel(new GridLayout(boardSize, boardSize));
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++) {
				buttonGrid[i][j] = new JButton( ) ;
				buttonGrid[i][j].addActionListener(this);
				buttonGrid[i][j].setIcon(queenOff);
				state[i][j]= 0;
				backpanel.add( buttonGrid[i][j] );
			}
		}

		getContentPane().add(backpanel, SwingConstants.CENTER);
		title =new JLabel(boardSize + "-Queen ", SwingConstants.CENTER);
		getContentPane().add(title, BorderLayout.NORTH);
		
		iButton = new JButton("Init");
		iButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(iButton))
					gridInit();
			}
		});
		JRadioButton dButton = new JRadioButton("利き筋を表示", true);
				trapChk = true;
		dButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.DESELECTED)
					trapChk = false;
				else trapChk = true;
				drawCell();
			}
		});
		aLabel1 = new Label();
		JPanel southpan = new JPanel(new GridLayout(0,1));
		southpan.add(iButton);
		southpan.add(dButton);
		southpan.add(aLabel1);
		getContentPane().add(southpan, BorderLayout.SOUTH);
		queenNum=0;
		aLabel1.setText(Integer.toString(boardSize));

		this.setVisible(true);
	}
	
	public void gridInit(){
		for(int i=0; i<boardSize; i++)
			for(int j=0; j<boardSize; j++) {
				state[i][j]= 0;
				buttonGrid[i][j].setBackground(Color.WHITE);buttonGrid[i][j].setIcon(queenOff);
			}
		queenNum=0;
		aLabel1.setText(Integer.toString(boardSize));
	}
	
	private boolean legal(int x, int y) {
		return (x >= 0 && x < boardSize && y>=0 && y<boardSize);
	}
	
	public void checkState(int ip, int it){
		for(int ix=0; ix<boardSize; ix++)
			state[ix][it]= -1;
		for(int iy=0; iy<boardSize; iy++)
			state[ip][iy]= -1;
		for (int i=1; legal(ip+i,it+i); i++)
			state[ip+i][it+i]= -1;
		for (int i=1; legal(ip+i,it-i); i++)
			state[ip+i][it-i]= -1;
		for (int i=1; legal(ip-i,it+i); i++)
			state[ip-i][it+i]= -1;
		for (int i=1; legal(ip-i,it-i); i++)
			state[ip-i][it-i]= -1;
	}
	
	public void drawCell() {
		for(int i=0; i<boardSize; i++)
			for(int j=0; j<boardSize; j++) {
				if((state[i][j]== -1)||(state[i][j]== 1)){
					buttonGrid[i][j].setBackground(Color.ORANGE);
					buttonGrid[i][j].setOpaque(true);
				}
			}
	}

	public void actionPerformed(ActionEvent e) {
		for(int ip=0; ip<boardSize; ip++){
			for(int it=0; it<boardSize; it++){
				if (e.getSource().equals(buttonGrid[ip][it])){
					if(state[ip][it]==-1 || state[ip][it]==1) {
						buttonGrid[ip][it].setIcon(queenChk);
						aLabel1.setText(Integer.toString(boardSize-queenNum)+" Game Over");
						break;
					}
					else {
						buttonGrid[ip][it].setIcon(queenOn);
						state[ip][it]=1;
						checkState(ip, it);
						if(trapChk==true) drawCell();
						queenNum++;
						aLabel1.setText(Integer.toString(boardSize-queenNum));
						break;
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("Lets start QueenBoard¥n");
		QueenBoard aQueenBoard= new QueenBoard();
	}
}
