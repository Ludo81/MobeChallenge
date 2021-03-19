package level_page.model;

public class Chrono {

    private long tempsDepart=0;
    private long tempsFin=0;
    private long duree=0;

    public void start()
    {
        tempsDepart=System.currentTimeMillis();
        tempsFin=0;
        duree=0;
    }


    public void stop()
    {
        //tempsDepart=0;
        //tempsFin=0;
        duree=0;
    }

    public long getDuree(){
        tempsFin=System.currentTimeMillis();
        return (tempsFin-tempsDepart) / 1000;
    }

    public static String timeToHMS(long tempsS) {

        // IN : (long) temps en secondes
        // OUT : (String) temps au format texte : "1 h 26 min 3 s"

        int h = (int) (tempsS / 3600);
        int m = (int) ((tempsS % 3600) / 60);
        int s = (int) (tempsS % 60);

        String r="";

        if(h>0) {r+=h+" h ";}
        if(m>0) {r+=m+" min ";}
        if(s>0) {r+=s+" s";}
        if(h<=0 && m<=0 && s<=0) {r="0 s";}

        return r;
    }

} // class Chrono