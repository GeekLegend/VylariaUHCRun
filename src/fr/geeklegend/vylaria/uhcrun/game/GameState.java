package fr.geeklegend.vylaria.uhcrun.game;

public enum GameState
{
	
	WAITING,
	PREGAME,
	GAME,
	FINISH;
	
	private GameState current;
	
	public GameState getState()
	{
		return current;
	}
	
	public boolean isState(GameState state)
	{
		return current == state;
	}
	
	public void setState(GameState state)
	{
		current = state;
	}

}
