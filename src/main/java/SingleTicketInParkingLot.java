import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SingleTicketInParkingLot extends TTState {
    //State in dem noch nicht ausgeparkt wurde
    public SingleTicketInParkingLot(ServletContext context, Ticket ticket) {
        super(context, ticket);
    }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Das Ticket mit der ID " +  this.ticket.getTicketID() + " wurde schon bezahlt");
    }

    @Override
    public void assignParkingLotToTicket(Integer parkingLotLevel) {
        throw new IllegalArgumentException("Das Ticket mit der ID " +  this.ticket.getTicketID() + " wurde schon eingeparkt");
    }

    public void removeTicketFromParkingLot() {
        System.out.println("Ticket ausgeparkt " + ticket.getTicketID());
        this.ticket.setState(new SingleTicketPaid(context,ticket));
        //state weiter schieben auf Bezahlt -> rausfahren ist jetzt möglich
    }

    @Override
    public void enter(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Das Ticket mit der ID " +  this.ticket.getTicketID() + " befindet sich schon im Parkhaus");
    }

    @Override
    public void leave(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Das Ticket mit der ID " +  this.ticket.getTicketID() + " muss vor dem Verlassen noch ausgeparkt werden");
    }
    @Override
    public String toString() {
        return "SingleTicketInParkingLot";
    }

    @Override
    public String getStateDescription() {
        return "bezahlt und eingeparkt";
    }
}
