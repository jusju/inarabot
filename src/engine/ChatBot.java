package engine;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.simple.*;
import org.json.simple.parser.*;

// Made according to https://github.com/rubenlagus/TelegramBots/blob/master/TelegramBots.wiki/Getting-Started.md

public class ChatBot extends TelegramLongPollingBot {
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			String kokoteksti = update.getMessage().getText();
			String[] solut = kokoteksti.split(" ");
			String viestinAlku = solut[0];
			if(solut.length == 2) {
				System.out.println("JUKKA" + solut[1]);	
			}
			String sanotaanTakaisin = sanoTakaisin(viestinAlku);
			SendMessage message = new SendMessage() // Create a SendMessage
													// object with mandatory
													// fields
					.setChatId(update.getMessage().getChatId()).setText(sanotaanTakaisin);
			try {
				execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	public String sanoTakaisin(String botilleSanottua) {
		System.out.println(botilleSanottua);
		String sanotaanTakaisin = "";
		if (botilleSanottua.equals("/i love you")) {
			sanotaanTakaisin = "I love you too.";
		}
		else if (botilleSanottua.equals("/who will fetch pauline today?")) {
			sanotaanTakaisin = "I can tell you that when that feature is implemented.";
		}
		else if (botilleSanottua.equals("/when did jukka pay?")) {
			sanotaanTakaisin = "I can tell you that when that feature is implemented.";
		}
		else if (botilleSanottua.equals("/weather")) {
			sanotaanTakaisin = etsiSaa();
		}
		else if (botilleSanottua.equals("/foodmenu")) {
			sanotaanTakaisin = etsiRuokalista();
		} 
		else if (botilleSanottua.equals("/jukkapaid")) {
			sanotaanTakaisin = etsiJukanMaksut();
		}
		
		return sanotaanTakaisin;
	}
	
	public String etsiJukanMaksut() {
		String sanotaanTakaisin = "Jukka has paid a lot";
		return sanotaanTakaisin;
	}
	

	public String etsiRuokalista() {
		LocalDate tanaan = LocalDate.now();
		int vuosi = tanaan.getYear();
		int kuukausi = tanaan.getMonthValue();
		int paiva = tanaan.getDayOfMonth();
		DayOfWeek viikonpaivaOlio = tanaan.getDayOfWeek();
		String viikonpaiva = viikonpaivaOlio.toString();
		String paivaTanaan = paiva + "." + kuukausi + "." + vuosi;
		String inline = "";
		String ruokainfo = viikonpaiva + " " + paivaTanaan + " ";
		try {
			URL url = new URL("https://hhapp.info/api/amica/pasila/fi");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if (responsecode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responsecode);
			} else {
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					inline += sc.nextLine();
				}
				sc.close();
				System.out.println("\nJSON data in string format");
				// System.out.println(inline);
				JSONParser parse = new JSONParser();

				JSONObject result = (JSONObject) parse.parse(inline);
				JSONArray menus = (JSONArray) result.get("LunchMenus");
		        Iterator i = menus.iterator();

		        System.out.println("TAPANI: ");

		        while (i.hasNext()) {
		            JSONObject weekinfo = (JSONObject) i.next();
		            String dayOfWeek = (String)weekinfo.get("DayOfWeek");
		            System.out.println(dayOfWeek);
		            String date = (String)weekinfo.get("Date");
		            System.out.println(date);
		            JSONArray linjastot = (JSONArray)weekinfo.get("SetMenus");
		            Iterator j = linjastot.iterator();
		            while (j.hasNext()) {
		            	JSONObject mealInfo = (JSONObject) j.next();
		            	JSONArray setmenus = (JSONArray)mealInfo.get("SetMenus");
		         
			            JSONArray meals = (JSONArray)mealInfo.get("Meals");
			            Iterator k = meals.iterator();
			            while(k.hasNext()) {
				            JSONObject dayInfo = (JSONObject) k.next();
				            String name = (String)dayInfo.get("Name");
				            System.out.println(name);
				           
				            if(date.equals(paivaTanaan)) {
				            	ruokainfo = ruokainfo + " " + name;
				            }
			            }
		            }
		        }
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return ruokainfo;
	}
	
	public String etsiSaa() {
		String inline = "";
		try {
			URL url = new URL(
					"http://api.openweathermap.org/data/2.5/weather?q=Helsinki&APPID=a8720cf3a65bd981b2fecc6381cd729e&units=metric");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.connect();
			int responsecode = conn.getResponseCode();
			if (responsecode != 200) {
				throw new RuntimeException("HttpResponseCode: " + responsecode);
			} else {
				Scanner sc = new Scanner(url.openStream());
				while (sc.hasNext()) {
					inline += sc.nextLine();
				}
				sc.close();
				System.out.println("\nJSON data in string format");
				System.out.println(inline);
				JSONParser parse = new JSONParser();

				JSONObject result = (JSONObject) parse.parse(inline);
				JSONObject main = (JSONObject) result.get("main");
				double temp = (Double) main.get("temp");

				JSONObject wind = (JSONObject) result.get("wind");
				double speed = (Double) wind.get("speed");
				System.out.println("wind speed");
				
				LocalDate tanaan = LocalDate.now();
				int vuosi = tanaan.getYear();
				int kuukausi = tanaan.getMonthValue();
				int paiva = tanaan.getDayOfMonth();
				DayOfWeek viikonpaivaOlio = tanaan.getDayOfWeek();
				String viikonpaiva = viikonpaivaOlio.toString();

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				String aika = dtf.format(now);

				System.out.println("JUKKA " + temp);
				inline = "Outside at Helsinki it is now " + temp + "C. Wind speed is: " + speed + "m/s. Today is " + viikonpaiva + " " +
						paiva + "." + kuukausi + "." + vuosi + " at " + aika + ".";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return inline;
	}

	public String getBotUsername() {
		return "inarabot";
	}

	@Override
	public String getBotToken() {
		return "944529343:AAEYPDWipfk6YnUQcIzw90r-U4RIuup0Gio";
	}

	public static void main(String[] args) {

		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();

		try {
			botsApi.registerBot(new ChatBot());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}