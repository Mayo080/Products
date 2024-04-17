package cardBattle1;
/**
 * 
 * @author Mayo080
 *
 */

import java.util.*;

public class Player implements Comparable<Player> {
	private ArrayList<Integer> hand;
	protected int playCard;
	protected int score;
	protected ArrayList<Integer> lastCards;
	protected String message;
	protected String name;
	protected boolean duplicate;
	private Random rnd;
	
	public Player() {
		rnd = new Random();
		
		hand = new ArrayList<Integer>();
		for (int i = 0; i < 15; ++i) 
			hand.add(i+1);
		
		lastCards = new ArrayList();
		message = "";
		name = "継承元" + rnd.nextInt(10);
		duplicate = false;
		score = 0;
		playCard = 0;
	}

	protected ArrayList<Integer> getHand() {
		return this.hand;
	}

	protected boolean isInHand(int card) {
		for(int n: hand) 
			if(n == card) return true;
		return false;
	}
	
	private void removeFromHand(int card) {
		for(int i = 0; i < hand.size(); ++i)
			if(hand.get(i) == card) {
				hand.remove(i);
				return;
			}
	}
		
	public final void putCard(int card) throws Exception {
		if (!isInHand(card)) 
			throw new Exception();
		
		playCard = card;
		
		removeFromHand(card);
	}
	
	public int getCard() {
		return playCard;
	}
	
	public void addScore(int s) {
		score += s;
	}
	
	public final int getScore() {
		return score;
	}
	
	public final String getName() {
		return name;
	}
	
	public final void setLastCards(ArrayList<Integer> list) {
		lastCards = (ArrayList<Integer>) list.clone();
	}
	public String say() {
		return message;
	}
	
	public void setDuplicate(boolean a) {
		duplicate = a;
	}
	public boolean getDuplicate() {
		return duplicate;
	}
	
	@Override
	public int compareTo(Player other) {
		return this.score - other.score;
	}
	
	public void play(int point, boolean first) throws Exception {
		int card = 0;
		do {
			card = rnd.nextInt(15) + 1;
		} while (!isInHand(card));
		
		putCard(card);
		
		String[] tail = {"します", "せば", "すぜ！", "すのかよ", "そう", "すよ"};
		message = "を出"
			+ tail[rnd.nextInt(tail.length)];
	}

	public static void main(String[] args) throws Exception {
		Player p = new Player();
		for(int i = 0; i < 15; ++i) {
			p.play(15, true);
			System.out.println( p.getCard() + " " + p.say());
		}
	}

}