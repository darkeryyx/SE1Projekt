abstract class TicketAbs implements TicketIF {

    protected double priceStamp; //Der für das Ticket gezahlte Preis
    public TicketAbs(){}
    public double getPriceStamp(){
        return priceStamp;
    }
    @Override
    public void openBarrier(){
        System.out.println("Schranke öffnet");
    }
}
