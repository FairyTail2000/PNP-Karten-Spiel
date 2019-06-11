package main.plugin;

public enum Actions {
	
	Attack(0),
	Health_Mod (1),
	Move (2),
	FullScreenStart (3),
	MouseClick (4),
	SetupDone (5);
	
	
	private final int ac;
	private Actions (int a) {
		this.ac = a;
	}
	
	public int getAction () {
		return this.ac;
	}
	
}
