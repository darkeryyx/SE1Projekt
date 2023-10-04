import StartConf.StartConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class SingleTicketCreated extends TTState {
    public SingleTicketCreated(ServletContext context, Ticket ticket) {
        super(context, ticket);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Das Ticket mit der ID " + this.ticket.getTicketID() + " ist noch beim CheckIn");
    }

    @Override
    public void assignParkingLotToTicket(Integer parkingLotLevel) {
        throw new IllegalArgumentException("Das Ticket mit der ID " + this.ticket.getTicketID() + " ist noch beim CheckIn");
    }

    @Override
    public void removeTicketFromParkingLot() {
        throw new IllegalArgumentException("Das Ticket mit der ID " + this.ticket.getTicketID() + " ist noch beim CheckIn");
    }

    @Override
    public void enter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LocalDateTime ticketStartTime = LocalDateTime.parse(request.getParameter("zeitpunkt"), ISO_LOCAL_DATE_TIME);
        HashMap<Integer, Ticket> ticketIdList = (HashMap<Integer,Ticket>)context.getAttribute("TicketIDs");

        //globale Zeit auf Zeit des Tickets setzen
        context.setAttribute("zeit", ticketStartTime);

        ticketIdList.put(ticket.getTicketID(), this.ticket);
        ticket.setStartDate(ticketStartTime);//Zeistempel fürs reinfahren setzen

        ticket.setState(new SingleTicketNoParkingLot(context,ticket));
        //nächste State: es muss eingeparkt werden

        String message = "Das Ticket mit der ID " + ticket.getTicketID() + " wurde erzeugt";
        request.setAttribute("message", message);
        request.setAttribute("ID",ticket.getTicketID());
        StartConfig.getInstance().dec(); //Anzahl der freien Parkpätze verringern

        forwardToJSP(request, response, "/enterSingleTicket.jsp");
    }

    @Override
    public void leave(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Das Ticket mit der ID " + this.ticket.getTicketID() + " ist noch beim CheckIn");
    }

    @Override
    public String toString() {
        return "UnregisteredSingleTicket";
    }

    @Override
    public String getStateDescription() {
        return "wird erstellt";
    }
}

