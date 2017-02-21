import com.teamtreehouse.*;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;

public class LeagueManager {

  public static void main(String[] args) {
    Player[] players = Players.load();
    
		System.out.println();
		System.out.println();
		System.out.println("===================");
		System.out.println("LEAGUE MANAGER");
		System.out.println("===================");
		System.out.printf(" There are currently %d registered players.%n%n%n", players.length);
		
		Admin admin=new Admin(players);
		admin.run();
		
  }

}
