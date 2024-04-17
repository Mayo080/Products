package cardBattle1;
/**
 * 
 * @author Mayo080
 *
 */
import java.util.*;

public class Dealer {
	private ArrayList<Player> players;
	private ArrayList<Integer> deck;
	private ArrayList<Integer> cards;
	private int turn;
	private Random rnd;

	public Dealer(ArrayList<Player> allPlayers) {
		players = allPlayers;
		turn = 0;
		rnd = new Random();
		cards = new ArrayList<Integer>();
		for(int i = 0; i < players.size(); ++i) 
			cards.add(0);
		deck = new ArrayList<Integer>();
		for(int i = -5; i < 11; ++i) 
			if(i != 0) deck.add(i);
		for(int i = 0; i < 100; ++i) {
			int j = rnd.nextInt(deck.size());
			int k = rnd.nextInt(deck.size());
			int a = deck.get(j);
			deck.set(j, deck.get(k));
			deck.set(k, a);
		}
	}
	
	public void game() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("=====================");
		System.out.println("== cardBattle1   ====");
		System.out.println("== ハゲタカのえじき対戦 ==");
		System.out.println("=====================");
		
		while (!deck.isEmpty()) {
			++turn;
			System.out.println("\n------ 第"+turn+"回戦 ------");
			
			int layout = deck.remove(0);
			System.out.print("場札は　「"+ layout +"」");
			if(layout<0)
				System.out.print("\t**危険！マイナス札登場**");
			System.out.println("\n");
			
			for(int i = 0; i < players.size(); ++i) {
				Player p = players.get(i);
				try {
					p.play(layout, (turn==1));
					System.out.println("プレイヤー:" + p.getName() + " (" + p.getScore() + "点)");
					System.out.println(" 「" + p.getCard() + p.say() + "」");
				} catch (Exception e) {
					System.out.println("プレイヤー"+p.getName()+"はエラーで退場");
					players.remove(p);
				}
			}
			
			for(Player p: players) {
				p.setDuplicate(false);
				for(Player q: players) {
					if(p.equals(q)) continue;
					if(p.getCard() == q.getCard()) {
						p.setDuplicate(true);
						System.out.println("プレイヤー" + p.getName() + " 重複で失格");
					}
				}
			}
			
			System.out.println("\n（チェック中）");
			Player winPlay = null;
			int sign = 1;
			int minmax = 0;
			if(layout < 0) {
				sign = -1;
				minmax = 16;
			}
			for(Player p: players) {
				if(p.getDuplicate()) continue;
				if(minmax*sign < p.getCard()*sign) {
					winPlay = p;
					minmax = p.getCard();
				}
			}
			if(winPlay == null)
				System.out.println("今回は獲得者なし！");
			else {
				winPlay.addScore(layout);
				System.out.println("プレイヤー "+winPlay.getName() +"が，場札の"
						+ layout + "ポイントを獲得して， "
						+ winPlay.getScore() + "点になりました！");
			}
			
			cards.clear();
			for(Player p: players)
				cards.add(p.getCard());
			for(Player p: players)
				p.setLastCards(cards);
			
			System.out.print(">");
		}
		System.out.println("\n======== ゲーム終了 =========");
		if(players.isEmpty()) {
			System.out.println("全員退場で勝者なし...");
			return;
		}
		Collections.sort(players, Collections.reverseOrder());
		for(Player p: players)
			System.out.println(p.getName() + " (" + p.getScore() + "　点)");
		System.out.print("\n勝者は");
		System.out.println(players.get(0).getName() + "さんです．");
		System.out.println("おめでとう！");
	}
	
	public static void main(String[] args) {
		ArrayList<Player> players = new ArrayList<>();
		
		players.add(new Player0124());
		players.add(new Player());
		
		Dealer d = new Dealer(players);
		d.game();
		
	}

}
