import java.util.*;

public class SimPa {

    public static Map<String, String> funkcijePrijelaza = new HashMap<>();
    public static List<String> stog = new ArrayList<String>();
    public static List<String> trenutnaStanja = new ArrayList<String>();
    public static List<String> iducaStanja = new ArrayList<String>();
    public static String ispis = "";

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        String trenutniRed = new String();

        //ucitavanje prvog reda (ulazni nizovi)
        trenutniRed = s.nextLine();
        String[] ulazniNizovi = trenutniRed.split("\\|");


        //ucitavanje drugog reda (stanja)
        trenutniRed = s.nextLine();
        Set <String> stanja = new TreeSet <String>();
        String[] linija = trenutniRed.split("\\,") ;
        for (String str : linija) {
            stanja.add(str);
        }

        //ucitavanje treceg reda (ulazni znakovi)
        trenutniRed = s.nextLine();
        Set <String> ulazniZnakovi = new TreeSet <String>();
        String[] linija3 = trenutniRed.split("\\,") ;
        for (String str : linija3) {
            ulazniZnakovi.add(str);
        }

        //ucitavanje cetvrtog reda (znakovi stoga)
        trenutniRed = s.nextLine();
        Set <String> znakoviStoga = new TreeSet <String>();
        String[] linija4 = trenutniRed.split("\\,") ;
        for (String str : linija4) {
            znakoviStoga.add(str);
        }

        //ucitavanje petog reda (prihvatljiva stanja)
        trenutniRed = s.nextLine();
        Set <String> prihvatljivaStanja = new TreeSet <String>();
        String[] linija5 = trenutniRed.split("\\|") ;
        for (String str : linija5) {
            prihvatljivaStanja.add(str);
        }

        //ucitavanje sestog reda (pocetno stanje)
        String pocetnoStanje = s.nextLine();

        //ucitavanje sedmog reda (pocetni znak stoga)
        String pocetniZnakStoga = s.nextLine();

        //ucitavanje funkcija prijelaza
        while (s.hasNextLine() && !(trenutniRed = s.nextLine()).isEmpty()) {
            funkcijePrijelaza.put(trenutniRed.split("->")[0],
                    trenutniRed.split("->")[1]);
        }

        s.close();


        //iteracija po ulaznim nizovima
        for(int i = 0; i < ulazniNizovi.length; i++){

            stog.add(pocetniZnakStoga);
            String[] trenutniUlazniNiz = ulazniNizovi[i].split("\\,");
            //krece od pocetka tj od pocetnog stanja
            String trenutnoStanje = pocetnoStanje;
            boolean prihvatljiv = false;
            String redakIspisa = "";

            //iteracija po ulazima trenutnog ulaznog niza
            for(int j = 0; j < trenutniUlazniNiz.length; j++){

                //dodaj u ispis
                redakIspisa = redakIspisa + dodajIspis(trenutnoStanje);

                //provjeri epsilon prijelaze
                while(true) {
                    String kljucEpsilon = trenutnoStanje + "," + "$" + "," +
                            stog.get(stog.size() - 1);

                    String epsilonPrijelaz = provjeriEpsilon(kljucEpsilon);
                    if (!epsilonPrijelaz.isEmpty()) {
                        //dodaj u ispis
                        redakIspisa = redakIspisa + dodajIspis(epsilonPrijelaz);
                        //promijeni trenutno stanje
                        trenutnoStanje = epsilonPrijelaz;
                    } else{
                        break;
                    }
                }

                String vrijednost = "";

                String kljuc = trenutnoStanje + "," + trenutniUlazniNiz[j] + "," +
                        stog.get(stog.size()-1);


                //iteracija po mapi da se nade odgovarajuca funkcija prijelaza
                for (Map.Entry<String, String> entry : funkcijePrijelaza.entrySet()) {
                    if ( entry.getKey().equals(kljuc) ){
                        vrijednost = entry.getValue();
                    }
                }


                //ako je pronadena odgovarajuca funkcija
                if (!vrijednost.isEmpty()){
                    //hendlanje pronadene funkcije
                    //vrijednost = "novo stanje" + "," + "nizZnakovaStoga"
                    trenutnoStanje = vrijednost.split("\\,")[0];
                    addToStog(vrijednost.split("\\,")[1]);
                    if(j == trenutniUlazniNiz.length - 1){
                        redakIspisa = redakIspisa + dodajIspis(trenutnoStanje);
                    }
                } else{
                    //dodaj fail u ispis
                    redakIspisa = redakIspisa + "fail|";
                    //break;
                    j = trenutniUlazniNiz.length;
                }


                //provjeri je li prihvatljiv
                if( (j == trenutniUlazniNiz.length - 1) &&
                        prihvatljivaStanja.contains(trenutnoStanje)){
                    prihvatljiv = true;
                }

                if( (j == trenutniUlazniNiz.length - 1) && !prihvatljiv){
                    //provjeri epsilon prijelaze
                    while(true) {
                        String kljucEpsilon = trenutnoStanje + "," + "$" + "," +
                                stog.get(stog.size() - 1);

                        String epsilonPrijelaz = provjeriEpsilon(kljucEpsilon);
                        if (!epsilonPrijelaz.isEmpty()) {
                            //dodaj u ispis
                            redakIspisa = redakIspisa + dodajIspis(epsilonPrijelaz);
                            //promijeni trenutno stanje
                            trenutnoStanje = epsilonPrijelaz;
                            if(prihvatljivaStanja.contains(trenutnoStanje)){
                                prihvatljiv = true;
                                break;
                            }
                        } else{
                            if(prihvatljivaStanja.contains(trenutnoStanje)){
                                prihvatljiv = true;
                            }
                            break;
                        }
                    }
                }

            }

            //na kraju svake iteracije dodat u ispis je li niz prihvatljiv
            //ako sadrzi fail onda nije prihvatljiv
            if(!prihvatljiv){
                redakIspisa = redakIspisa + "0";
            } else{
                redakIspisa = redakIspisa + "1";
            }

            //ispis = ispis + redakIspisa + "\n";
            if( i != ulazniNizovi.length - 1){
                ispis = ispis + redakIspisa + "\n";
            } else {
                ispis = ispis + redakIspisa;
            }
            stog.clear();
        }

        System.out.print (ispis);

    }

    public static void addToStog (String zaStog){
        char[] djelovi = zaStog.toCharArray();
        if(djelovi[0]== '$'){
            stog.remove(stog.size()-1);
            if(stog.size() == 0){
                stog.add("$");
            }
        } else{
        stog.remove(stog.size()-1);

        for(int i = djelovi.length - 1; i >= 0; i--){
            stog.add(Character.toString(djelovi[i]));
        }}
    }

    public static String dodajIspis (String trStanje){
        String rjesenje = "";
        rjesenje = trStanje + "#";
        for(int i = stog.size()-1; i >= 0; i--) {
            rjesenje = rjesenje + stog.get(i);
        }
        rjesenje = rjesenje + "|";
        return rjesenje;
    }

    public static String provjeriEpsilon (String kljucEpsilon){
        String rjesenje = "";
        String vrijednost = "";
        for (Map.Entry<String, String> entry : funkcijePrijelaza.entrySet()) {
            if ( entry.getKey().equals(kljucEpsilon) ){
                vrijednost = entry.getValue();
            }
        }
        if(!vrijednost.isEmpty()){
            rjesenje = vrijednost.split("\\,")[0];
            addToStog(vrijednost.split("\\,")[1]);
        }
        return rjesenje;
    }

}
