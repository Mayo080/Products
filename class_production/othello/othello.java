package othello;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class Othello extends JFrame implements ActionListener {

	Board board;

	JLabel title, status;
	JButton[][] buttonGrid;
	JButton sButton;
	JRadioButton bHumanButton;
	JRadioButton bRandomButton;
	JRadioButton wHumanButton;
	JRadioButton wRandomButton;
	Label aLabel0, aLabel1;

	int player = Board.black;
	Strategy bStrategy, wStrategy;
	SynchronizedFlag runflag = new SynchronizedFlag();
	SynchronizedFlag inputEnabled = new SynchronizedFlag();
	SynchronizedFlag inputDone = new SynchronizedFlag();
	int inputMove;

	Othello() {
		board = new Board();

		bStrategy = new StrategyHuman();
		wStrategy = new StrategyHuman();

		this.setSize(400, 700);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});

		getContentPane().setLayout(new BorderLayout());

		buttonGrid = new JButton[Board.boardSize][Board.boardSize];
		JPanel backpanel = new JPanel(new GridLayout(Board.boardSize, Board.boardSize));
		for (int i = 0; i < Board.boardSize; i++)
			for (int j = 0; j < Board.boardSize; j++) {
				buttonGrid[i][j] = new JButton();
				buttonGrid[i][j].addActionListener(this);
				backpanel.add(buttonGrid[i][j]);
			}

		getContentPane().add(backpanel, SwingConstants.CENTER);

		JPanel topPane = new JPanel();
		topPane.setLayout(new BoxLayout(topPane, BoxLayout.PAGE_AXIS));

		title = new JLabel("Othello", SwingConstants.CENTER);
		topPane.add(title);
		topPane.add(Box.createRigidArea(new Dimension(0, 5)));

		status = new JLabel(board.getScore(), SwingConstants.RIGHT);

		topPane.add(status);
		getContentPane().add(topPane, BorderLayout.NORTH);

		displayBoard();

		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.PAGE_AXIS));
		bottomPane.setPreferredSize(new Dimension(400, 200));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

		sButton = new JButton("Play");
		sButton.setBounds(0, 0, 80, 30);
		sButton.addActionListener(this);

		JLabel bRLabel = new JLabel("黒の戦略", SwingConstants.LEFT);
		bHumanButton = new JRadioButton("人間");
		bHumanButton.addActionListener(this);
		bHumanButton.setSelected(true);

		bRandomButton = new JRadioButton("ランダム");
		bRandomButton.addActionListener(this);

		ButtonGroup bButton = new ButtonGroup();
		bButton.add(bHumanButton);
		bButton.add(bRandomButton);

		JLabel wRLabel = new JLabel("白の戦略", SwingConstants.LEFT);
		wHumanButton = new JRadioButton("人間");
		wHumanButton.addActionListener(this);
		wHumanButton.setSelected(true);

		wRandomButton = new JRadioButton("ランダム");
		wRandomButton.addActionListener(this);

		ButtonGroup wButton = new ButtonGroup();
		wButton.add(wHumanButton);
		wButton.add(wRandomButton);

		aLabel0 = new Label();
		aLabel1 = new Label();

		JPanel bRadioPanel = new JPanel(new GridLayout(0, 1));
		bRadioPanel.add(bRLabel);
		bRadioPanel.add(bHumanButton);
		bRadioPanel.add(bRandomButton);

		JPanel wRadioPanel = new JPanel(new GridLayout(0, 1));
		wRadioPanel.add(wRLabel);
		wRadioPanel.add(wHumanButton);
		wRadioPanel.add(wRandomButton);

		panel.add(bRadioPanel);
		panel.add(wRadioPanel);
		panel.add(sButton);
		bottomPane.add(aLabel0);
		bottomPane.add(panel);
		bottomPane.add(aLabel1);
		this.getContentPane().add(bottomPane, BorderLayout.SOUTH);

		this.setVisible(true);

	}

	public void displayBoard() {
		buttonGrid = board.display(buttonGrid);
		status.setText(board.getScore());
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(sButton)) {
			runflag.set();
		} else if (e.getSource().equals(bHumanButton)) {
			bStrategy = new StrategyHuman();
		} else if (e.getSource().equals(bRandomButton)) {
			bStrategy = new StrategyRandom();
		} else if (e.getSource().equals(wHumanButton)) {
			wStrategy = new StrategyHuman();
		} else if (e.getSource().equals(wRandomButton)) {
			wStrategy = new StrategyRandom();
		} else if (inputEnabled.test() && !(inputDone.test())) {
			for (int i = 0; i < Board.boardSize; i++) {
				for (int j = 0; j < Board.boardSize; j++) {
					if (e.getSource().equals(buttonGrid[i][j])) {
						int move = (i + 1) * 10 + j + 1;
						if (!board.legalMove(move, player)) {
							aLabel1.setText("そこには置けません");
							return;
						} else {
							inputMove = move;
							inputEnabled.unset();
							inputDone.set();
						}
					}
				}
			}
		}
	}

	public void run() {
		while (true) {
			aLabel0.setText("戦略を選択してPlayを押してください");
			while (!(runflag.test())){
			}

			board.initialState();
			displayBoard();
			aLabel1.setText("");
			System.out.println("Play start ...");
			for (player = Board.black; player > 0; player = board.nextPlayer(player)) {
				System.out.println("Player : " + player);
				System.out.println("B:" + board.count(Board.black) + " W:" + board.count(Board.white));
				Strategy currentstrategy;
				String turntext;
				if(player == Board.black) {
					currentstrategy = bStrategy;
					turntext = "黒の手番です";
				}else {
					currentstrategy = wStrategy;
					turntext = "白の手番です";
				}
				aLabel0.setText(turntext);
				if(currentstrategy.isHuman()){
					inputEnabled.set();
					inputDone.unset();
					while (inputEnabled.test() || !(inputDone.test())) {
					}
					System.out.println("InputMove: " + inputMove);
				}else {	
					inputMove = currentstrategy.getMove(player, board);
				}
				board.makeMove(inputMove, player);
				displayBoard();
			}
			int score = board.count(Board.black) - board.count(Board.white);
			if (score > 0) {
				aLabel1.setText("ゲーム終了: 結果 黒の勝ち[" + score + "]");
			} else if (score < 0) {
				aLabel1.setText("ゲーム終了: 結果 白の勝ち[" + -score + "]");
			} else {
				aLabel1.setText("ゲーム終了: 引き分け");
			}
			runflag.unset();
		}
	}

	public static void main(String[] args) {
		System.out.println("Let's play Othello Game\n");
		Othello othelloGame = new Othello();
		othelloGame.run();
	}

	class SynchronizedFlag {
		private boolean flag = false;

		public synchronized void set() {
			flag = true;
		}

		public synchronized void unset() {
			flag = false;
		}

		public synchronized boolean test() {
			return flag;
		}
	}



}
