package com.teamtreehouse.model;
import java.util.List;

public class Team{
	private String mTeamName;
	private String mCoach;
	private List<Player> mPlayers;
	
	
	public Team(String name,String coach){
			mTeamName=name;
			mCoach=coach;	
	
	}
	

	
	public int getPlayersCount(){
		return mPlayers.size();
	}
	
	
	public String getTeamName(){
		return mTeamName;
	}
	
	public String getCoach(){
		return mCoach;
	}

	
	
}