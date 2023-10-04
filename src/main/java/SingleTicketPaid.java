import StartConf.StartConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class SingleTicketPaid extends TTState {

    private static final long timeLimitMinutes = 15;

    public SingleTicketPaid(ServletContext context, Ticket ticket) {
        super(context, ticket);
    }

    @Override
    public void leave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Parameter
        LocalDateTime end = LocalDateTime.parse(request.getParameter("zeitpunkt"), ISO_LOCAL_DATE_TIME);
        String id_String = request.getParameter("ticketID");
        Integer id = Integer.parseInt(id_String);
        //Attribute aus dem Kontext
        HashMap<Integer, Ticket> ticketList = (HashMap<Integer, Ticket>) context.getAttribute("TicketIDs");

        //Zeit korrekt?
        //if(end.isBefore((LocalDateTime)(context.getAttribute("zeit")))){ //Erlaubter Zeitpunkt?
        //    throw new IllegalStateException("Raum-Zeit-Kontinuum verletzt");
        //}
        //Gibt es das Ticket?
        Ticket ticket = ticketList.get(id);
        String message; //Ausgabe für JSP

        //context.setAttribute("zeit", end); //Aktualisiere die Zeit im PH

        if (!withinTimeLimit(ticket, end)) {//Im Zeitlimit?
            message = "Ticket: " + ticket.getTicketID() + " Das Zeitlimit wurde verpasst. Nachzahlung fällig";
            /*
            TO-DO set auf Nachzahlungsstatus wenn er implementiert ist
             */
            ticket.setState(new SingleTicketPayPenalty(context, ticket));
        } else {
            Statistics stats = Statistics.getInstance(); // Instanz der Statistiken
            stats.setTicketList(id,ticketList.get(id)); //
            //Tickets bleiben jetzt standardmäßig erhalten, nur der State wird geändert
            ticket.setState(new Archived(context, ticket));
            message = "Ticket mit ID: " + id + " kann das Parkhaus verlassen";
            StartConfig.getInstance().inc(); //Anzahl der freien Parkplätze wird erhöht
        }
        context.setAttribute("message", message);
        RequestDispatcher rd = context.getRequestDispatcher("/leaveSingleTicket.jsp");
        rd.include(request, response);
    }

    @Override
    public void assignParkingLotToTicket(Integer parkingLotLevel) {
        throw new IllegalArgumentException("Das Ticket mit der ID " + this.ticket.getTicketID() + " hat schon ein und ausgeparkt. Ist nur 1x erlaubt");
    }

    @Override
    public void removeTicketFromParkingLot() {
        throw new IllegalArgumentException("Das Ticket mit der ID " + this.ticket.getTicketID() + " hat schon ausgeparkt");
    }

    @Override
    public void enter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new IllegalArgumentException();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
       throw new IllegalArgumentException("Das Ticket mit der ID " + this.ticket.getTicketID() + " wurde schon bezahlt");
    }

    @Override
    public String toString() {
        return "SingleTicketPaid";
    }

    @Override
    public String getStateDescription() {
        return "bezahlt und ausgeparkt";
    }

    private boolean withinTimeLimit(Ticket ticket, LocalDateTime actualCheckOut) {
        LocalDateTime paidAt = ticket.getEndDate();
        LocalDateTime checkOutUntil = paidAt.plusMinutes(timeLimitMinutes);
        return actualCheckOut.isBefore(checkOutUntil);
        /*
        PaidAt: Zeitpunkt zu dem das Ticket bezahlt wurde
        checkOutUntil: Zeitpunkt bis zu dem das Parkhaus verlassen werden muss,
                       setzt sich zusammen aus: Bezahlzeitpunkt + Zeitlimit zum Verlasssen
        actualCheckOUt: Zeitpunkt zu dem das Parkhaus tatsächlich verlassen werden soll
        */
    }
}