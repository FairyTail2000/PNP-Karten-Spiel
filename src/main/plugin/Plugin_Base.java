package main.plugin;

public abstract class Plugin_Base {

	public abstract void onMove();
	
	public abstract void onLoad();
	
	public abstract int ondamage() /*Returns modified Damage*/;
	
	public abstract void modHealth();

	public abstract String getPluginName();
	
	public abstract void onExit();
	
	public abstract void debug();
	
	public abstract void onMouseClick();
	
	public abstract void onFullscreenStart();
	
	public abstract void onSetupComplete();
}
