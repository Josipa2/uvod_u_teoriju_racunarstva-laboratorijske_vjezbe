

import java.util.*;

public class MinDka {
    //uklanjanje nedohvatljivih stanja i uklanjanje
    //istovjetnih stanja
    public static Map<String[], String> funkcijePrijelaza = new LinkedHashMap<>();
    public static String[] svaStanja;
    public static Set<String> skupStanja = new TreeSet<>();
    public static Set<String> skupDostiznihStanja = new TreeSet<>();
    public static Set<String> prihvatljivaStanja = new TreeSet<>();
    public static Set<String> nedostiznaStanja = new TreeSet<>();
    public static String  pocetnoStanje = new String();
    public static String[] simboliAbecede;

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        String trenutniRed = new String();

        //ucitavanje prvog reda (skup stanja)
        trenutniRed = s.nextLine();
        svaStanja = trenutniRed.split(",");


        //ucitavanje drugog reda (simboli abecede)
        trenutniRed = s.nextLine();
        simboliAbecede = trenutniRed.split(",");

        //ucitavanje treceg reda (prihvatljiva stanja)
        trenutniRed = s.nextLine();
        String[] linija3 = trenutniRed.split(",") ;
        for(String s1 : linija3)
            prihvatljivaStanja.add(s1);


        //ucitavanje cetvrtog reda (pocetno stanje)
        pocetnoStanje = s.nextLine();

        //ucitavanje funkcija prijelaza
        while (s.hasNextLine() && !(trenutniRed = s.nextLine()).isEmpty()) {
            String trenutnoStanje = trenutniRed.split(",")[0];
            String ostatak = trenutniRed.split(",", 2)[1];
            String simbolAbecede = ostatak.split("->")[0];
            String[] lijevo = new String[2];
            lijevo[0] = trenutnoStanje;
            lijevo[1] = simbolAbecede;
            String iduceStanje = trenutniRed.split("->")[1];
            funkcijePrijelaza.put(lijevo,iduceStanje);
            skupStanja.add(iduceStanje);
        }


        izbaciNedostizna();

        s.close();


        ispisRezultata();

    }

    public static void ispisRezultata(){
        int brojac = 0;


        for(String s1 : skupDostiznihStanja){
            System.out.print(s1);
            if(brojac < skupDostiznihStanja.size() - 1)
                System.out.print(",");
            brojac ++;
        }

        System.out.println();
        brojac = 0;

        for(String s1 : simboliAbecede){
            System.out.print(s1);
            if(brojac < simboliAbecede.length - 1)
                System.out.print(",");
            brojac ++;
        }

        System.out.println();
        brojac = 0;

        for(String s1 : prihvatljivaStanja){
            System.out.print(s1);
            if(brojac < prihvatljivaStanja.size() -1)
                System.out.print(",");
            brojac ++;
        }

        System.out.println();

        System.out.println(pocetnoStanje);

        for (Map.Entry<String[], String> entry : funkcijePrijelaza.entrySet()) {
            String[] iterat = entry.getKey();
            String iduceStanje = entry.getValue();
            System.out.println(iterat[0] + "," + iterat[1] + "->" + iduceStanje);
        }
    }

    public static void odvojiDostiznaINedostizna(){
        List<String> stog = new ArrayList<>();
        List<String> vecProdeni = new ArrayList<>();
        skupDostiznihStanja.add(pocetnoStanje);
        stog.add(pocetnoStanje);
        boolean pomogni = true;
        //pronalazak dostiznih stanja
        while(pomogni){
            if(stog.size() != 0) {
                for (Map.Entry<String[], String> entry : funkcijePrijelaza.entrySet()) {
                    String[] iterat = entry.getKey();
                    String iduceStanje = entry.getValue();
                    if (stog.get(0).equals(iterat[0])){
                        if(!skupDostiznihStanja.contains(iduceStanje))
                            skupDostiznihStanja.add(iduceStanje);
                        if(!stog.contains(iduceStanje) && !vecProdeni.contains(iduceStanje))
                            stog.add(iduceStanje);
                    }
                }
                vecProdeni.add(stog.get(0));
                stog.remove(0);
            } else{
                pomogni = false;
            }
        }

        for(String s : svaStanja){
            if(!skupDostiznihStanja.contains(s))
                nedostiznaStanja.add(s);
        }


    }

    public static void izbaciNedostizna(){
        //podijeli stanja na dostizna i nedostizna
        odvojiDostiznaINedostizna();

        //izbaci funkcije s nedostiznim stanjima
        Set<String[]> zaMaknuti = new HashSet<>();
        for (Map.Entry<String[], String> entry : funkcijePrijelaza.entrySet()) {
            String[] iterat = entry.getKey();
            if(nedostiznaStanja.contains(iterat[0])) {
                zaMaknuti.add(iterat);
            }
        }
        for(String[] s : zaMaknuti){
            funkcijePrijelaza.remove(s);
        }

        //izbaci nedostizna stanja iz prihvatljivih stanja
        for(String s : nedostiznaStanja){
            if(prihvatljivaStanja.contains(s))
                prihvatljivaStanja.remove(s);
        }

    }


}
