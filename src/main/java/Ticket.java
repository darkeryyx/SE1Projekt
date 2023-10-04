import java.time.LocalDateTime;

public class Ticket extends TicketAbs{

  //  private boolean FakeStatePayPenalty = false; //im Nachzahlstatus oder nicht, da es keinen echten Nachzahlstatus gibt
    //und already in Unpaid an der stelle schon vorher geändert wurde
    private static int counter; //statischer Zähler für die Tagestickets
    private final int ticketID; // Nummer der Tickets
    private boolean ticketPaid; // wurde das Ticket bezahlt
    private LocalDateTime startDate; // Datum und Uhrzeit bei Einfahrt
    private LocalDateTime endDate = null;// Datum und Uhrzeit bei bezahlung
    //private double currentPrice = 2; // aktueller Preis festgelegt vom Betreiber
    private boolean already = false; //wurde Ticket schonmal in den Automaten geführt?
    private TTState state;
    private double toPay;

    private int floor; // auf welcher Etage wurde geparkt

    public Ticket(){
        priceStamp = Administration.getCurrentPrice();
        toPay = 0;
        ticketPaid = false;
        ticketID = ++counter;
    }
    public void setState(TTState state){
        this.state = state;
    }
    public TTState getState(){
        return state;
    }
    public static void resetCounter(){counter = 0;}

    public int getTicketID() {
        return ticketID;
    } // Ausgabe der TicketID
    @Override
    public int getFloor() {
        return floor;
    }
    @Override
    public void setFloor(int floor){
        this.floor = floor;
    }
    public boolean getAlready(){return already;}
    public void setAlready(boolean v){already =v;}
    public void setTicketPaid(boolean ticketPaid) {
        this.ticketPaid = ticketPaid;
    }
    public boolean getTicketPaid(){
        return ticketPaid;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    } // ausgabe wann das Ticket bezahlt wurde
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public String getStateDescription() {
        return this.state.getStateDescription();
    }

  /*  public boolean isFakeStatePayPenalty() {
        return FakeStatePayPenalty;
    }
*/
  /*
    public void setFakeStatePayPenalty(boolean fakeStatePayPenalty) {
        FakeStatePayPenalty = fakeStatePayPenalty;
    }

   */
}