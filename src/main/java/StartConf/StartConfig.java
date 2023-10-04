package StartConf;

public class StartConfig {
    private int frei;
    private static StartConfig instance = null;

    public int getFrei(){
        return frei;
    }

    public void dec() {
        this.frei--;
    }

    public void inc() {
        this.frei++;
    }

    private StartConfig(){
        frei = 10;
    }
    //es gibt nur 10 Parkplätze -> einfacher zu testen, was passiert wenn
    //es voll ist -> Zahl angepasst

    public static StartConfig getInstance(){
        if (instance == null){
            instance = new StartConfig();
        }
        return instance;
    }

    public static StartConfig resetInstance() {
        instance = new StartConfig();
        return instance;
    }
}
