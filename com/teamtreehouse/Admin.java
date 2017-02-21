package com.teamtreehouse;
import com.teamtreehouse.model.*;

import java.util.*;
import java.io.*;


public class Admin{
	
	private BufferedReader mReader;
	private Map<String,String> mMenu;
	private Map<String,String> mPlayersMenu;
	private Map<String,List<Player>> mTeams;
	private List<Team> mTeamsList;
	private List<Player> mAvailablePlayers;
	private int mAvailablePlayersCount;
	

		
		
	public Admin(Player[] args){
		mMenu=new HashMap<String,String>();
		mMenu.put("1","Add a new team");
		mMenu.put("2","Choose a team");
		mMenu.put("3","League Balance Report");
		mMenu.put("9","Exit the program");
		
		mPlayersMenu=new HashMap<String,String>();
		mPlayersMenu.put("1","Add player");
		mPlayersMenu.put("2","Remove player");
		mPlayersMenu.put("3","Print players by height");
		mPlayersMenu.put("4","Print team roster");
		mPlayersMenu.put("9","Return to the main menu");
		
		mTeamsList = new  ArrayList<Team>();
		mTeams=new TreeMap<String,List<Player>>();
		mAvailablePlayers=new ArrayList<>(Arrays.asList(args));
		mAvailablePlayersCount=mAvailablePlayers.size();
		
		mReader=new BufferedReader(new InputStreamReader(System.in));
	}	
		


	
	private String promptAction(Map<String,String> menu) throws IOException{
		System.out.println();
		for(Map.Entry<String,String> options:menu.entrySet()){
			System.out.printf("%s. 	%s %n",options.getKey(),options.getValue());
		
		}
		System.out.println();
		System.out.print("What would you like to do: ");
		String choice=mReader.readLine();
		return choice.trim().toLowerCase();
		
	}
	
	
	public void run(){
		String choice="";
		do{
			try{
				System.out.println();
				System.out.println("MAIN MENU");
				choice =promptAction(mMenu);
				switch(choice){
					case "1":
						if(!isAvailablePlayers()||mTeamsList.size()>=mAvailablePlayersCount){
							System.out.println("The number of teams is greater than the number of available players!");
							
						}else {
							Team team=promptNewTeam();
							addTeam(team);
							System.out.printf("%n .....Team %s was added %n", team.getTeamName());
						}
						break;
					case "2":
						String teamChoice=promptTeam();
						if (!teamChoice.equals("")){
								runPlayers(teamChoice);
						}
						break;
					case "3":
						if (!mTeams.isEmpty()){
									leagueBalanceReport();
						}else{
							System.out.printf("No teams created or players added yet!");
						} 
						break;
					case "9":
						
						break;
					default:
						System.out.printf("Unknown choice. Try again %n%n%n");
						
				}
			
			}catch(IOException ioe){
				System.out.println("Problem with input!");
				ioe.printStackTrace();
			}
		
		}while (!choice.equals("9"));
	
	}
	
	
	private void addTeam(Team team){
		mTeamsList.add(team);
		String teamName=team.getTeamName();
		List<Player> players = new ArrayList<Player>();
		mTeams.put(teamName, players);
	}

	private Team promptNewTeam() throws IOException{
			System.out.print("Enter team name: ");
			String name=mReader.readLine();
			name=name.toUpperCase();
			System.out.print("Enter coach name: ");
	    String coach=mReader.readLine();
		
	return new Team(name,coach);
	}
	
	
	private String promptTeam() throws IOException{
		if (mTeamsList.size()==0){
			System.out.println("There are no teams created at this time");
			return "";
		}
		
		System.out.println("Teams:");
		
		List<String> teamNames=new ArrayList<>();
		for(Team team:mTeamsList){
			teamNames.add(team.getTeamName());
		}
		Collections.sort(teamNames);
		int index=promptForChoice(teamNames);
		return teamNames.get(index);
	}
	

	
	
	private void runPlayers(String teamName){
		

		String choice="";
		int playersNumber;
		
	
		do{
			try{
					System.out.println();
					System.out.printf("Team:%-20s %n ",teamName);
					System.out.println();
					System.out.println("PLAYERS MENU");
					choice =promptAction(mPlayersMenu);
					List<Player> plyersByTeam=getPlayersForTeam(teamName);
					if (plyersByTeam!=null){
						playersNumber=plyersByTeam.size();
					}else {
						System.out.println("No players on this team yet ...........");
						playersNumber=0;
					}
				
				switch(choice){
					case "1":
						if(isAvailablePlayers()){
						 if(playersNumber<11){
							System.out.printf("Adding player....... %n%n ");
							System.out.println("Available players:");
							int playerIndex=promptForIndex(printPlayers(sortPlayersByName(mAvailablePlayers)));
							addPlayerToTeam(teamName,mAvailablePlayers.get(playerIndex));
							mAvailablePlayers.remove(playerIndex);	
						 }else {
							System.out.printf("This team already has 11 players %n");
						 }
						}else {
							System.out.printf("There are no more available players %n");
						}
						break;

					case "2":
						if(playersNumber>0){
							System.out.printf("Removing player %n ");					
							int playerIndexR=promptForIndex(printPlayers(plyersByTeam));
							Player player=plyersByTeam.get(playerIndexR);		
							removePlayerFromTeam(teamName,player);
						}else {
							System.out.printf("There are no players on this team  %n");
						
						}
					break;

					case "3": 
						if (playersNumber==0){
							   System.out.println("No players on this team yet ...........");
							}else {
							  System.out.printf("Team %s players by height:  %n",teamName);
								System.out.println();
								printPlayersGroupedByHeight(ByHeight(teamName));
							}
							break;
					case "4":
							if (playersNumber==0){
							   System.out.println("No players on this team yet ...........");
							}else {		  
								System.out.printf("Team %s roster:  %n",teamName);
							  int i=printPlayers(plyersByTeam);
							}
							break;
					case "9":
							break;
					default:
						System.out.printf("Unknown choice. Try again %n%n%n");
						
				}
			
			}catch(IOException ioe){
				System.out.println("Problem with input!");
				ioe.printStackTrace();
			}
		
		}while (!choice.equals("9"));
	}
	

	

	
	
	private void addPlayerToTeam(String teamName,Player player){
		List<Player> players;
		if(mTeams.containsKey(teamName)){
			players = mTeams.get(teamName);
    	players.add(player);
		} else {
			players = new ArrayList<Player>();
			players.add(player);
    	mTeams.put(teamName, players);
		}
	}
	
	private void removePlayerFromTeam(String teamName,Player player){
		List<Player> players = new ArrayList<>(mTeams.get(teamName));

		if(players.remove(player)){
			List<Player> pl=mTeams.replace(teamName,players);		
			mAvailablePlayers.add(player); 
		}
	}
	
	

	
  private List<Player> sortPlayersbyH(List<Player> players){
		
			players.sort(new Comparator<Player>(){
			@Override 
			public int compare(Player player1,Player player2){
				if(player1.equals(player2)){
					return 0;
				}
				return player1.getHeightInInches()-player2.getHeightInInches();
			}
		}  );
		
		return players;

	}
	
	
	
	
	
	
	
	public List<Player> getPlayersForTeam(String teamName){
		if(mTeams.containsKey(teamName)){
			List<Player> players= sortPlayersByName(mTeams.get(teamName));
			return players;
		}else {
			return null;
		}
	}
			
	
	
	
		private List<Player> sortPlayersByName(List<Player> players){
		
			players.sort(new Comparator<Player>(){
			@Override 
			public int compare(Player player1,Player player2){
				if(player1.equals(player2)){
					return 0;
				}
				return player1.getLastName().compareTo(player2.getLastName());
			}
		}  );
		
		return players;

	}
	
	
	public List<Player> ByHeight(String teamName){
		if(mTeams.containsKey(teamName)){
			List<Player> players= mTeams.get(teamName);
			players.sort(new Comparator<Player>(){
			@Override 
			public int compare(Player player1,Player player2){
				if(player1.equals(player2)){
					return 0;
				}
				return player1.getHeightInInches()-player2.getHeightInInches();
			}
		}  );
		
		return players;
		}else {
			return null;
		}
	}
	
	
	
	private void leagueBalanceReport() throws IOException{
		System.out.println();
		System.out.println("LEAGUE BALANCE REPORT");
		System.out.println("----------------------");
		System.out.println();
		
		for(Map.Entry<String,List<Player>> options:mTeams.entrySet()){
			String teamName=options.getKey();
			List<Player> players=options.getValue();
			if (players.size()>0){ 
						int experience=0;
						for(Player player:players){
							if(player.isPreviousExperience()) {
							experience++;
							}
						}
					System.out.printf("TEAM: %s %n%n",teamName);
					System.out.printf("Experienced players: %d %n",experience);
					System.out.printf("Inexperienced players: %d %n",players.size()-experience);
				
					double eLevel=experience*100/players.size();
					System.out.printf("Experience level: %.2f%% %n%n",eLevel);
					System.out.printf("Players by height: %n%n");
					printPlayersByHeight(ByHeight(teamName));
					System.out.println("--------------------");
					System.out.println();
			}else{
						System.out.printf("TEAM: %s - no players on this team yet %n%n",teamName);
			}
		}

}
	
	private boolean isAvailablePlayers(){
				return (mAvailablePlayers.size()>0);
	}	
	
	
	
	private int promptForIndex(int numberOfElements) throws IOException{ 			 
	boolean isAcceptible=false;
	int choice=0;
		
		while (!isAcceptible){ 
			try{
				System.out.print("Your choice: ");
				String optionAsString=mReader.readLine();
				choice=Integer.parseInt(optionAsString.trim());
				isAcceptible=true;
			
			} catch (NumberFormatException ex ){ 
				System.out.println("Please enter a number");
				
			}
			if (isAcceptible&&((choice<1)||(choice>numberOfElements))) {
					isAcceptible=!isAcceptible;
					System.out.println("Please enter a valid number");
			}
		}
		
		return choice-1;
	}
	
	
	
	
	private int promptForChoice(List<String> options) throws IOException{ 			 
	int counter=0;
		
			for(String option:options){
					++counter;
					System.out.printf("%3d. %s %n",counter,option);
			}
		return promptForIndex(counter);
				
	}
			

	
		private int printPlayers(List<Player> players){
		int counter=0;

			String[] st={"number","last name","first name","height","experience"};
			System.out.println();
			System.out.printf("%7s  %-15s %-15s %7s %15s %n",st[0],st[1],st[2],st[3],st[4]);
			System.out.println("--------------------------------------------------------------------------");
		
			for(Player player:players){
					String s;
					if (player.isPreviousExperience()){
						s="experienced";
					}else {
						s="inexperienced";
					}
					++counter;
		  		System.out.printf("%7d.  %-15s %-15s %5d %20s %n",counter,player.getLastName(),player.getFirstName(),player.getHeightInInches(),s);
					
			}
	
			return counter;
		
	}	
	
	
	private void printPlayersGroupedByHeight(List<Player> players){
		int height=0;
					
		for(Player player:players){
			if (height!=player.getHeightInInches()){
				System.out.println();
				height=player.getHeightInInches();
			}			
			System.out.printf("%-15s %-15s %5d %n",player.getLastName(),player.getFirstName(),player.getHeightInInches());
		}
	
	
}
	
	
	private void printPlayersByHeight(List<Player> players){
		int height=0;
		int count=0;

			for(Player player:players){
			
			if(player.getHeightInInches()==height){
					count++;
			}else{
				if(height!=0){
					System.out.printf("%5d\"- %d player(s) %n",height,count);
				}
					height=player.getHeightInInches();	
					count=1;
				}
			}
				System.out.printf("%5d\"- %d player(s) %n",height,count);
		}

}	

	

	
	
	
	
	
	
	
	
