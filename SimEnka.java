

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class SimEnka {
	
	public static Map<String[], String[]> funkcije_prijelaza = new HashMap<>();
	public static List<String> trenutna_stanja = new ArrayList<String>();
	public static List<String> iduca_stanja = new ArrayList<String>();

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String trenutni_red = new String();
		
		//ucitavanje prvog reda (ulazni nizovi)
		trenutni_red = s.nextLine();
		String[] ulazni_nizovi = trenutni_red.split("\\|") ;
		 
		
		//ucitavanje drugog reda (stanja)
		trenutni_red = s.nextLine();
		Set <String> stanja = new TreeSet <String>();
		String[] linija = trenutni_red.split("\\|") ;
		for (String str : linija) {
			stanja.add(str);
		} 
		
		//ucitavanje treceg reda (abeceda)
		trenutni_red = s.nextLine();
		Set <String> abeceda = new TreeSet <String>();
		String[] linija3 = trenutni_red.split("\\|") ;
		for (String str : linija3) {
			abeceda.add(str);
		} 
		
		//ucitavanje cetvrtog reda (prihvatljiva stanja)
		trenutni_red = s.nextLine();
		Set <String> prihvatljiva_stanja = new TreeSet <String>();
		String[] linija4 = trenutni_red.split("\\|") ;
		for (String str : linija4) {
			prihvatljiva_stanja.add(str);
		} 
		
		//ucitavanje petog reda (pocetnog stanja)
		String pocetno_stanje = s.nextLine();	
		
		//ucitavanje funkcija prijelaza
		while (s.hasNextLine() && !(trenutni_red = s.nextLine()).isEmpty()) {
			String trenutnoStanje = trenutni_red.split(",")[0];
			String prijelaz = trenutni_red.split(",", 2)[1];
			String simbolAbecede = prijelaz.split("->")[0];
			//System.out.println(simbolAbecede);
			String[] lijevo = new String[2];
			lijevo[0] = trenutnoStanje;
			lijevo[1] = simbolAbecede;
			//System.out.println(lijevo[0] + lijevo[1]);
			String ostatak = trenutni_red.split("->")[1];
			String[] desno = ostatak.split(",");
			funkcije_prijelaza.put(lijevo,desno);
				}
		
		s.close();
		
		String ispis = new String();

		//array za trazenje kljuca u mapi funkcije_prijelaza
		String[] stanje_prijelaz = new String[2];
		String[] stanje_prijelaz_epsilon = new String[2];
		stanje_prijelaz_epsilon[1] = "$";
		
		//prvo iteracija po ulazima (izmedu |)
		//na kraju je potrebno do sad skupljeno stavit u ispis i otic u novi red
		for(int brojac = 0; brojac < ulazni_nizovi.length ; brojac ++) {
			String ulazi = ulazni_nizovi[brojac];
			//ulaz sadrzi sve 
			String[] ulaz = ulazi.split(",");
			//red_ispis ce se dodat u ispis nakon kraja trenutnog ulaznog niza
			String red_ispis = new String();
			
			// svaki skup ulaza treba poceti od pocetnog stanja
			trenutna_stanja.add(pocetno_stanje);
			
			
			//dalje treba iterirat po ulazima odjeljenima zarezom 
			//treba dodat | izmedu stanja kroz koja je prosao 1 ulaz (osim za zavrsna stanja)
			//nakon svake te iteracije treba dodat trenutna stanja kroz koja se je  proslo u ispis (nakon petlje za ovaj skup ulaza)
			//na kraju petlje takoder treba prebaciti iz iducih u trenutno stanje da se za iduci ulaz ide u iduca stanja
			for(int brojac_ulaza = 0; brojac_ulaza < ulaz.length; brojac_ulaza++) {
				
				//postavit trenutni ulaz u kljuc za pretrazivanje funkcija stanja
				stanje_prijelaz[1] = ulaz[brojac_ulaza];
				
				
				//sad treba ic iteracija po stanjima za trenutni ulaz
				//treba proc sva stanja na stogu, kasnije ocistit stog
				//na kraju (nakon petlje) dodat sva ta stanja u red_ispis (sa zarezom izmedu njih)
				
				for(int i = 0; i < trenutna_stanja.size(); i++) {
					//staviti trenutno stanje u kljuc za pretragu funkcija prijelaza
					stanje_prijelaz[0] = trenutna_stanja.get(i);
					stanje_prijelaz_epsilon[0] = trenutna_stanja.get(i);
					//pretrazit jel postoji takav kljuc u funkciji
					if(postojiKljuc(stanje_prijelaz) && postojiKljuc(stanje_prijelaz_epsilon)) {
						String[] st = dohvatiStanja(stanje_prijelaz);
						for (String sta : st)
							if (!vecSadProdenStringIduca(sta))
								iduca_stanja.add(sta);
						String[] epsilon_stanja = dohvatiStanja(stanje_prijelaz_epsilon);
						for (String es : epsilon_stanja)
							if(!vecSadProdenStringTrenutna(es))
								trenutna_stanja.add(es);
					}else if (postojiKljuc(stanje_prijelaz)) {
						//ak postoji prijelaz, stanja u koje ide taj prijelaz dodati u iduca stanja
						String[] st = dohvatiStanja(stanje_prijelaz);
						for (String sta : st)
							if (!vecSadProdenStringIduca(sta))
								iduca_stanja.add(sta);
					}else if (postojiKljuc(stanje_prijelaz_epsilon)) {
						//ak postoji epsilon prijelaz, stanja u koje ide taj prijelaz dodati u trenutna stanja
						String[] epsilon_stanja = dohvatiStanja(stanje_prijelaz_epsilon);
						for (String es : epsilon_stanja)
							if(!vecSadProdenStringTrenutna(es))
								trenutna_stanja.add(es);
					}else  {
						if (iduca_stanja.isEmpty())
						//ako ne postoji taj prijelaz, iduce stanje je #
							iduca_stanja.add("#");
					}
					
				}
				//stavljanje stanja koja su prosla u string za ispis reda
				Collections.sort(trenutna_stanja);
				for(String zaispis : trenutna_stanja)
					if(!(zaispis.equals("#") && trenutna_stanja.size()>1))
						red_ispis += zaispis + ",";
				
				//maknut zadnji zarez iz reda za ispis prije neg kaj se doda |
				if(red_ispis.endsWith(","))
					red_ispis = red_ispis.substring(0, red_ispis.lastIndexOf(","));
				
				
				//ak nije zadnji prolaz za ovaj skup ulaza, dodaj |
				if(brojac_ulaza < ulaz.length - 1)
					red_ispis += "|";
				
				//prebacit iduca stanja u trenutna stanja za iducu iteraciju (za iduci ulaz)
				trenutna_stanja.clear();
				for(String stringoni : iduca_stanja)
					trenutna_stanja.add(stringoni);
				iduca_stanja.clear();
				
			}
			
			//od 148 do 198
			
			 	//sad ponovit za zadnji ulaz 
				//postavit trenutni ulaz u kljuc za pretrazivanje funkcija stanja
				stanje_prijelaz[1] = ulaz[ulaz.length -1];
				
				
				//sad treba ic iteracija po stanjima za trenutni ulaz
				//treba proc sva stanja na stogu, kasnije ocistit stog
				//na kraju (nakon petlje) dodat sva ta stanja u red_ispis (sa zarezom izmedu njih)
				
				for(int i = 0; i < trenutna_stanja.size(); i++) {
					//staviti trenutno stanje u kljuc za pretragu funkcija prijelaza
					stanje_prijelaz[0] = trenutna_stanja.get(i);
					stanje_prijelaz_epsilon[0] = trenutna_stanja.get(i);
					//pretrazit jel postoji takav kljuc u funkciji
					if(postojiKljuc(stanje_prijelaz) && postojiKljuc(stanje_prijelaz_epsilon)) {
						String[] st = dohvatiStanja(stanje_prijelaz);
						for (String sta : st)
							if (!vecSadProdenStringIduca(sta))
								iduca_stanja.add(sta);
						String[] epsilon_stanja = dohvatiStanja(stanje_prijelaz_epsilon);
						for (String es : epsilon_stanja)
							if(!vecSadProdenStringTrenutna(es))
								trenutna_stanja.add(es);
					}else if (postojiKljuc(stanje_prijelaz)) {
						//ak postoji prijelaz, stanja u koje ide taj prijelaz dodati u iduca stanja
						String[] st = dohvatiStanja(stanje_prijelaz);
						for (String sta : st)
							if (!vecSadProdenStringIduca(sta))
								iduca_stanja.add(sta);
					}else if (postojiKljuc(stanje_prijelaz_epsilon)) {
						//ak postoji epsilon prijelaz, stanja u koje ide taj prijelaz dodati u trenutna stanja
						String[] epsilon_stanja = dohvatiStanja(stanje_prijelaz_epsilon);
						for (String es : epsilon_stanja)
							if (!vecSadProdenStringTrenutna(es))
								trenutna_stanja.add(es);
					}else  {
						//ako ne postoji taj prijelaz, iduce stanje je #
						iduca_stanja.add("#");
					}
				}
				//stavljanje stanja koja su prosla u string za ispis reda
				Collections.sort(trenutna_stanja);
				red_ispis += "|";
				for(String zaispis : trenutna_stanja)
					if(!(zaispis.equals("#") && trenutna_stanja.size()>1))
						red_ispis += zaispis + ",";
				
				//maknut zadnji zarez iz reda za ispis prije neg kaj se doda |
				if(red_ispis.endsWith(","))
					red_ispis = red_ispis.substring(0, red_ispis.lastIndexOf(","));
				
				
				
				//prebacit iduca stanja u trenutna stanja za iducu iteraciju (za iduci ulaz)
				trenutna_stanja.clear();
				for(String stringoni : iduca_stanja)
					//if(stringoni.equals("#") && iduca_stanja.size()>1)
						trenutna_stanja.add(stringoni);
				iduca_stanja.clear();
			 
			
			
			//dodat do sad skupljeno u ispis
			ispis += red_ispis;
			//ako nije zadnji ulaz, dodati i novi red
			if(brojac < ulazni_nizovi.length)
				ispis += "\n";
			trenutna_stanja.clear();
			iduca_stanja.clear();
		}
		
		//kad se rijese sve te petlje ispisat sve kaj treba
		System.out.print(ispis);
		
	}
	
	
	//funkcija za provjeru postoji li kljuc
	private static boolean postojiKljuc(String[] stanje_ulaz) {
	
		for(String[] stringic : funkcije_prijelaza.keySet()) 
			if(stanje_ulaz.length == 2 && stringic.length == 2)
			if(stanje_ulaz[0].equals(stringic[0]) && stanje_ulaz[1].equals(stringic[1]))
				return true;
		
		return false;
	}
	
	//funkcija za vracanje iducih stanja
	private static String[] dohvatiStanja(String[] stanje_ulaz) {
		String[] iduci = null;
		for(String[] stringic : funkcije_prijelaza.keySet()) 
			if(stanje_ulaz.length == 2 && stringic.length == 2)
			if(stanje_ulaz[0].equals(stringic[0]) && stanje_ulaz[1].equals(stringic[1]))
				iduci = funkcije_prijelaza.get(stringic);
			
		return iduci;
		}
	
	//funkcija za provjeru postoji li kljuc
	private static boolean vecSadProdenStringTrenutna (String stanje) {
		
		for(String stringic : trenutna_stanja) 
			if(stanje.equals(stringic) )
				return true;
			
		return false;
		}
	
	//funkcija za provjeru postoji li kljuc
		private static boolean vecSadProdenStringIduca (String stanje) {
			
			for(String stringic : iduca_stanja) 
				if(stanje.equals(stringic) )
					return true;
				
			return false;
			}
}
