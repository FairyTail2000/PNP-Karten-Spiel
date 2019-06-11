package main.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jamlParser.main.JAMLParser;
import main.plugin.EventConstants;

public class Download {

	// Why did I choose to Comment my code?

	//Don't ask..
	private static transient int u;
	
	/**
	 * Here the Monster definitions get Downloaded
	 */
	public static void Download_Monster() {
		try {
			File folder = new File("Monster");
			if (!folder.exists()) {
				folder.mkdir();
			}

			if (folder.listFiles().length >= 229) {
				return;
			}
			
			Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
			for (int i = 1; i < 230; i++) {
				Thread.sleep(400);
				for (int j = 0; j < 10 && i < 230; i++, j++) {
					u = i;
					//LMAO this sould be really interesting, bye bye CPU
					Thread t = new Thread (new Runnable() {
						
						@Override
						public void run() {
							int i = u;
							try {
								URLConnection connection = null;
								InputStream instream = null;
								BufferedReader reader = null;
								String readString;
								
								connection = new URL(EventConstants.monsterBaseUrl + i + EventConstants.monsterDefi).openConnection();
								connection.addRequestProperty("User-Agent", "Mozilla/5.0");
								connection.setConnectTimeout(10000);
								connection.setReadTimeout(10000);
								connection.connect();

								//Checks if Server sended ok, if not continue
								if (Integer.valueOf(connection.getHeaderField(0).split(" ")[1]) != HttpURLConnection.HTTP_OK) {
									System.err.println("Error while downloading " + i);
									return;
								}

								reader = new BufferedReader(new InputStreamReader(instream));
								//The definition is only one line because of shitty encoding, this is why i need to do the replace thing and unicode escapes conversion
								readString = reader.readLine();

								readString = readString.substring(1, readString.length() - 1).replaceAll("\\\\r\\\\n", "")
										.replaceAll("\\\\n", "\n").replaceAll("\\\\\"", "\"");

								// Begin of Stackoverflow
								Matcher m = p.matcher(readString);
								StringBuffer buf = new StringBuffer(readString.length());
								while (m.find()) {
									String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
									m.appendReplacement(buf, Matcher.quoteReplacement(ch));
								}
								m.appendTail(buf);
								// end of StackoverFlow
								String reString = buf.toString();

								//Write it to a file and close the Stream
								//Could have used the Monster class, but it is not necessary here
								PrintWriter printWriter = new PrintWriter(new File("Monster/" + new JAMLParser(Arrays.asList(reString.split("\\n"))).getValue("Name") + ".monster"));

								printWriter.write(reString);
								printWriter.flush();
								printWriter.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							
							
							
						}
					});
					t.start();
				}
			}
		} catch (InterruptedException e) {
			//Why would i ever get this?
			//Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}

	public static void Download_Atacken() {
		//Check if the Attacks are ok
		//TODO: Read base from Config file
		boolean check_suceeded = check();
		if (!check_suceeded) {
			System.err.println("Check detected problems");
		} else {
			//Nothing to do, I go away bye
			System.out.println("Check succeded");
			return;
		}
		//The attack ids have are ordered in directories with and int as index number
		int[] attacken_ids = new int[150];
		int curr = 0;
		try {
			File atk_ids = new File("attacken_ids");
			//Check if the File which we use for saving the index number is there
			if (!atk_ids.exists()) {
				atk_ids.createNewFile();
			}
			//If it wasn't there, we don't care, if it's still not there, i have a problems
			//Hello Windows Defender my old friend
			URL id_Url = new URL("http://vstein.pythonanywhere.com/json_api/monster_attacken_ids.json");
			URLConnection con = id_Url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/5.0");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = br.readLine();
			String[] parts = line.substring(1, line.length() - 1).split(", ");
			for (int i = 0; i < parts.length; i++) {
				attacken_ids[i] = Integer.parseInt(parts[i]);
			}
			//The id file
			PrintWriter pw = new PrintWriter(atk_ids);

			for (int a : attacken_ids) {
				pw.println(a);
			}
			pw.flush();
			pw.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Cannot download Monster attacken map file...");
			e.printStackTrace();
			return;
		}

		//The dir for the Attack definitions
		File dir = new File("Attacken");
		if (!dir.exists()) {
			dir.mkdir();
		} else if (dir.isFile()) {
			dir.delete();
			dir.mkdir();
		}

		try {
			//Lets start the Actual downloading
			URL dowload_url;
			URLConnection conn;
			File act_file = null;
			BufferedReader br;
			PrintWriter pr;
			Instant before;
			Pattern p = Pattern.compile("\\\\u(\\p{XDigit}{4})");
			for (int i : attacken_ids) {
				if (i == 0) {
					break;
				}
				//Just to measure the the Time i need
				before = Instant.now();

				curr = i;
				//We name the Attack after its Number, may not the best idea
				act_file = new File(dir, i + ".jaml");
				act_file.delete();
				if (act_file.exists()) {
					continue;
				} else {
					act_file.createNewFile();
				}
				dowload_url = new URL(EventConstants.AttackBaseUrl + i + EventConstants.AttackDefi);
				conn = dowload_url.openConnection();
				conn.addRequestProperty("UserAgent", "Mozilla");
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				String read = br.readLine();
				read = read.substring(1, read.length() - 1);
				read = read.replaceAll("\\\\n", "\n");
				read = read.replaceFirst("\\n", "").replaceAll(" \\\\\"", "\"").replaceAll("\\\\\"", "\"");

				// Begin of Stackoverflow
				Matcher m = p.matcher(read);
				StringBuffer buf = new StringBuffer(read.length());
				while (m.find()) {
					String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
					m.appendReplacement(buf, Matcher.quoteReplacement(ch));
				}
				m.appendTail(buf);
				// end of StackoverFlow
				read = buf.toString();

				pr = new PrintWriter(act_file);
				pr.print(read);
				pr.flush();
				pr.close();
				System.out.println("Downloaded: " + i);
				try {
					System.out.println("Took: " + Duration.between(before, Instant.now()).toMillis() + "ms");
				} catch (ArithmeticException e) {
					System.err.println("Wtf went wrong?");
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error while Downloading Attack definition with id: " + curr);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return the Attack consisty
	 */
	private static boolean check() {
		File dir = new File("Attacken");
		File def = new File("attacken_ids");
		if (!def.exists()) {
			System.err.println("Unable to check completeness of attack definitions");
			return false;
		}
		List<String> einträge = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(def));) {
			Iterator<String> it = br.lines().iterator();
			while (it.hasNext()) {
				einträge.add(it.next().replace("\n", ""));
			}
			File[] f = dir.listFiles();
			if (f == null || f.length == 0) {
				System.err.println("No Files in \"Attacken\" or not existing");
				return false;
			}
			for (int i = 0; i < f.length; i++) {
				if (!f[i].getName().equals(einträge.get(i).replace(".jaml", ""))) {
					return false;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		return true;
	}
}
