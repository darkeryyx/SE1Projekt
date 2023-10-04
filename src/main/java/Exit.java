/*
import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.util.HashMap;
public class Exit implements ExitIF {
    private HashMap<Integer, Ticket> ticketList;
    private static Exit instance;
    private ServletContext sc;
    public Exit(){}
    private static final long timeLimitMinutes = 15;
    public void setTicketList(HashMap<Integer, Ticket> ticketList){
        this.ticketList=ticketList;
    }
    //Methode setzt Startzeitpunkt für das Ticket und setzt es auf die TicketListe
    public String enter(LocalDateTime start) {
        Ticket ticket =  new Ticket();
        ticket.setState(new Unpaid(sc, ticketList));
        ticket.setStartDate(start);
        ticketList.put(ticket.getTicketID(), ticket);
        return "Ticket erstellt ID: " + ticket.getTicketID() + " Start: " + start.toString();
    }

    //Methode prüft, ob das Zeitlimit zwischen Beazahlen und Verlassen des Parkhauses
    //eingehalten wurde
    private boolean withinTimeLimit(Ticket ticket, LocalDateTime actualCheckOut) {
        LocalDateTime paidAt = ticket.getEndDate();
        LocalDateTime checkOutUntil = paidAt.plusMinutes(timeLimitMinutes);
        return actualCheckOut.isBefore(checkOutUntil);
        /*
        PaidAt: Zeitpunkt zu dem das Ticket bezahlt wurde
        checkOutUntil: Zeitpunkt bis zu dem das Parkhaus verlassen werden muss,
                       setzt sich zusammen aus: Bezahlzeitpunkt + Zeitlimit zum Verlasssen
        actualCheckOUt: Zeitpunkt zu dem das Parkhaus tatsächlich verlassen werden soll
         *//*
    }

    public String leave(LocalDateTime end,Integer id) {

        Ticket ticket = ticketList.get(id);
        if (ticket == null) {
            return "Ticket mit " + id  + "  existiert nicht";
        }
        if(!ticket.getTicketPaid()){
            return "Ticket wurde " + id  + " wurde noch nicht bezahlt";
        }
        if(!withinTimeLimit(ticket, end)) {
            return "Ticket " + id + " Zeitlimit wurde überschritten. Bitte am Automaten nachzahlen.";
        }
        ticketList.remove(id);
        ticket.setState(new Archived(sc, ticketList));
        return "Ticket " + id  + " kann das Parkhaus verlassen";
    }

    public void setCon(ServletContext sc){
        this.sc =sc;
    }


    public static Exit getInstance() {
        if(instance == null) {
            instance = new Exit();
        }
        return instance;
    }
*/
    /**never used methoden */
     /*   public void changeState(Ticket t, String state, HashMap<Integer, Ticket> ticketList){
        if(state.equals("unpaid")){
            t.setState(new Archived(sc, ticketList));
        }
    }*/
    /* public void setTimeLimit(long minutes) throws IllegalArgumentException {
        if(minutes <= 0) {
            throw new IllegalArgumentException("TimeLimit kann nicht negativ sein");
        }
        timeLimitMinutes = minutes;
    }*/
/*
}
 */
