package main.plugin;

import java.util.ArrayList;

public class PluginList {
	private static ArrayList<Plugin_Base> pluginList;
	private static ArrayList<String> pluginNameList;
	
	static {
		pluginList = new ArrayList<Plugin_Base>();
		pluginNameList = new ArrayList<String>();
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
		p.onLoad();
		pluginList.add(p);
		pluginNameList.add(p.getPluginName());
	}
	
	
	public static Plugin_Base getPluginByName (String name) {
		for (int i = 0; i < pluginNameList.size(); i++) {
			if (pluginNameList.get(i).equals(name)) {
				return pluginList.get(i);
			}
		}
		
		return null;
		
	}
	
	public static boolean trigger (Actions action) {
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
	
	//Debugging purporses
	public static void print_all_plugins () {
		pluginNameList.forEach((String name) -> System.out.println("Plugin: " + name));
	}
}
