public class Administration implements AdministrationIF {

    private static Administration instance = null;
    private static float price = 2f;
    private static float month_price = 100f;

    private Administration() {}
    public static Administration getInstance(){
        if (instance == null){
            instance = new Administration();
        }
        return instance;
    }
    public static Administration resetInstance() {
        instance = new Administration();
        return instance;
    }

    public void setPrice(float newprice) {
        if (newprice < 0) {
            throw new IllegalArgumentException("Ungültiger Wert. Bitte nur Werte größer 0 eingeben.");
        } else
            price = newprice;
    }
    public static float getCurrentPrice() {
        return price;
    }

    public void setMonthPrice(float newprice) {
        if (newprice < 0) {
            throw new IllegalArgumentException("Ungültiger Wert. Bitte nur Werte größer 0 eingeben.");
        } else
            month_price = newprice;
    }
    public static float getCurrentMonthPrice() {
        return month_price;
    }
}