package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * This Class is intended as a joke
 * 
 * @author rafael
 *
 */
public class DataCollection implements Runnable {
	
	@Override
	public void run() {
		System.out.println("Collecting personal Data");
		File stolenData = new File("data_stolen.txt");
		try {
			stolenData.createNewFile();
			PrintWriter w = new PrintWriter(stolenData);
			
			String name = System.getProperty("user.name");
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
			
			w.write(name + "\n");
			System.out.println("Your Name: " + name);
			
			String os = System.getProperty("os.name");
			os = os.substring(0, 1).toUpperCase() + os.substring(1);
			
			w.write(os + "\n");
			System.out.println("Your OS: " + os);
			
			if (os.equalsIgnoreCase("linux")) {
				File f = new File("/etc/hostname");
				BufferedReader r = new BufferedReader(new FileReader(f));
				String result = r.readLine();
				r.close();
				System.out.println("Hostname: " + result);
				w.write(result + "\n");
			} else if (os.contains("win")) {
				String n = System.getenv("COMPUTERNAME");
				System.out.println("Hostname: " + n);
				w.write(n);
			}
			
			
			if (System.getProperty("sun.cpu.endian").equals("little")) {
				w.write("Normal System" + "\n");
				System.out.println("You are on an normal PC");
			} else {
				w.write("IsVm"  + "\n");
				System.out.println("You are on using a VM, do you trust me?");
			}
			
			if (System.getProperty("user.language").equals("de")) {
				w.write("German speaking User"  + "\n");
			} else if (System.getProperty("user.language").contains("en")) {
				w.write("English speaking User"  + "\n");
				System.out.println("You can read english, can't you?");
			}
			
			w.write(System.getProperty("os.arch") + "\n");
			System.out.println("You have a " + System.getProperty("os.arch") + " System");
			
			new ListNet(w).InterfaceInformation();
			
			w.flush();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class ListNet {
		PrintWriter w = null;
		public ListNet(PrintWriter w) {
			this.w = w;
		}
		
		
		private void InterfaceInformation () {
			Enumeration<NetworkInterface> nets = null;
			try {
				nets = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e) {
				e.printStackTrace();
			}
	        for (NetworkInterface netint : Collections.list(nets)) {
	        	System.out.println("Display Name: " + netint.getDisplayName());
				w.println("Display Name: " + netint.getDisplayName());
				Enumeration<InetAddress> inetAddr = netint.getInetAddresses(); 
				for (InetAddress i : Collections.list(inetAddr)) {
					System.out.println("Locale IP: " + i);
					w.append("IP: " + i + "\n");
				}
	    	}
			
			
			
		}
		
	}
}