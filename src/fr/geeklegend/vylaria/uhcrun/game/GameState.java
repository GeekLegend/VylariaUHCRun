package fr.geeklegend.vylaria.uhcrun.game;

public enum GameState
{
	
	WAITING,
	PREGAME,
	GAME,
	FINISH;
	
	private static GameState current;
	
	public static GameState getState()
	{
		return current;
	}
	
	public static boolean isState(GameState state)
	{
		return current == state;
	}
	
	public static void setState(GameState state)
	{
		current = state;
	}

}
