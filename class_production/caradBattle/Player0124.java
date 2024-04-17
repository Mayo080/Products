package cardBattle1;
/**
 * @author Mayo080
 *
 */
import java.util.*;

public class Player0124 extends Player {
	private Random rnd;
	
	public Player0124() {
		super();
		this.rnd = new Random();
		this.name = "0124" + rnd.nextInt(5); 
	}
	
	@Override
	public void play(int point, boolean first) throws Exception {
		int card = 0;
		do {
			if(point<=-1){
				card=rnd.nextInt(5)+8;
			}else if(point<=8 && point>=6) {
				card=rnd.nextInt(3)+13;
			}else{
				card=rnd.nextInt(7)+1;
			}
		} while (!isInHand(card));
		putCard(card);
		message = "を出します";
	}
	public static void main(String[] args) throws Exception {
		Player p = new Player0124();
		for(int i = 0; i < 15; ++i) {
			p.play(15, true);
			System.out.println( p.getCard() + " " + p.say());
		}
	}
}
