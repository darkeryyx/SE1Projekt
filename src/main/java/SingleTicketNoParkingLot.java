import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class SingleTicketNoParkingLot extends TTState{
    //State in dem noch nicht eingeparkt wurde
    public SingleTicketNoParkingLot(ServletContext context, Ticket ticket) {
        super(context, ticket);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response)  {
        throw new IllegalArgumentException("Ticket ID: " + ticket.getTicketID() + " vor dem Bezahlen bitte vorher einparken");
    }

    public void assignParkingLotToTicket(Integer parkingLotLevel) {
       ticket.setFloor(parkingLotLevel);
       //schreibt Etage auf das Ticket
       ticket.setState(new Unpaid(context, (HashMap<Integer,Ticket>)context.getAttribute("TicketIDs"), ticket));
       //nächster State: unbezahlt -> man muss bezahlen
    }

    @Override
    public void removeTicketFromParkingLot() {
        throw new IllegalArgumentException( "Das Ticket " +  ticket.getTicketID() + " wurde noch nicht eingeparkt");
    }

    @Override
    public void enter(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Das Ticket " +  ticket.getTicketID() + "  befindet sich schon im Parkhaus");
    }

    @Override
    public void leave(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("TicketID: "+ ticket.getTicketID() + ": Vor dem Verlassen noch einparken, bezahlen und ausparken");
    }
    @Override
    public String toString() {
        return "SingleTicketNoParkingLot";
    }

    @Override
    public String getStateDescription() {
        return "nicht eingeparkt";
    }
}
