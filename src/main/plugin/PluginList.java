package main.plugin;

import java.util.ArrayList;

public class PluginList {
	private static ArrayList<Plugin_Base> pluginList = new ArrayList<Plugin_Base>();
	private static ArrayList<String> pluginNameList = new ArrayList<String>();
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				for (Plugin_Base p : pluginList) {
					p.onExit();
				}
			}
		}));
	}
	
	public static void addPlugin (Plugin_Base p) {
		if (p == null) {
			return;
		}
		p.onLoad();
		pluginList.add(p);
		pluginNameList.add(p.getPluginName());
	}
	
	
	public static Plugin_Base getPluginByName (String name) {
		if (pluginList.size() == 0 || name == null) {
			return null;
		}
		
		for (int i = 0; i < pluginNameList.size(); i++) {
			if (pluginNameList.get(i).equals(name)) {
				return pluginList.get(i);
			}
		}
		
		return null;
		
	}
	
	public static boolean trigger (Actions action) {
		if (pluginList.size() == 0 || action == null) {
			return false;
		}
		switch (action.getAction()) {
		case 0:
			for (Plugin_Base plugin : pluginList) {
				plugin.ondamage();
			}
			break;
		case 1:
			for (Plugin_Base plugin : pluginList) {
				plugin.onMove();
			}
			
			break;
			
		case 2:
			for (Plugin_Base plugin : pluginList) {
				plugin.onMove();
			}
			
			break;
		case 3:
			for (Plugin_Base plugin : pluginList) {
				plugin.onFullscreenStart();
			}
			break;
			
		case 4:
			for (Plugin_Base plugin : pluginList) {
				plugin.onMouseClick();
			}
			break;
			
		case 5:
			for (Plugin_Base plugin : pluginList) {
				plugin.onSetupComplete();
			}
			break;	
			
		default:
			break;
		}
		
		return true;
	}
	
	private String returnable = "";
	@Override
	public String toString() {
		pluginNameList.forEach((String name) -> returnable += "Plugin: " + name);
		return returnable;
	}
}
