package main.plugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;

import main.ZipClassLoader;

public class FindAndCall {

	@SuppressWarnings("unchecked")
	public static void start() {
		File pluginFolder = new File("Plugin");
		if (pluginFolder.exists()) {
			pluginFolder.mkdir();
		}
		
		File[] plugins = pluginFolder.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(".jar")) {
					return true;
				}
				return false;
			}
		});
		if (plugins.length == 0) {
			return;
		}
		
		try {
			//TODO: alle plugins lesen
			//TODO: es einfach mal machen
			ZipClassLoader zipClassLoader = new ZipClassLoader(plugins[0].getAbsolutePath());
			Class<Plugin_Base> mainPluginClass = (Class<Plugin_Base>) zipClassLoader.loadClass("Plugin");
			Plugin_Base plugin_instance = mainPluginClass.newInstance();
			PluginList.addPlugin(plugin_instance);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
