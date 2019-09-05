package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Properties extends java.util.Properties {
	private static final long serialVersionUID = -3592929020435250622L;
	private List<Prop> properties = new ArrayList<Prop>();
	class Prop {
		public Prop(String k, String property) {
			this.key = k;
			this.value = property;
		}

		String key, value;
	}
	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		super.load(inStream);
		String[] keys = keySet().toArray(new String[0]);
		for (String k : keys) {
			properties.add(new Prop(k, getProperty(k)));
		}
	}
	
	@Override
	public synchronized void load(Reader reader) throws IOException {
		super.load(reader);
		String[] keys = (String[]) keySet().toArray();
		for (String k : keys) {
			properties.add(new Prop(k, getProperty(k)));
		}
		
	}
	
	public synchronized String getProp (String key) {
		if (key == null || key.isEmpty()) {
			return null;
		}
		for (Prop p : properties) {
			if (p.key.equals(key)) {
				return p.value;
			}
		}
		return null;
	}
}
