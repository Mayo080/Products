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
		// 使える手札からランダムに提出
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
		
		// 場に提出
		putCard(card);
		
		// メッセージを作る
		message = "を出します";
	}
	
	/*
	 * テスト用メイン
	 */
	public static void main(String[] args) throws Exception {
		// プレイヤーを作成して初期化
		Player p = new Player0124();
		// 15回手を出す
		for(int i = 0; i < 15; ++i) {
			p.play(15, true);
			System.out.println( p.getCard() + " " + p.say());
		}
	}
}
