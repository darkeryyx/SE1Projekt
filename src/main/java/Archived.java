import javax.servlet.ServletContext;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Archived extends TTState{

    private final String message = "Das Ticket mit der ID " + ticket.getTicketID() + " ist bereits archiviert";

    public Archived(ServletContext context, Ticket ticket) {
        super(context, ticket);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException(message);
    }

    @Override
    public void assignParkingLotToTicket(Integer parkingLotLevel) {
        throw new IllegalArgumentException(message);
    }

    @Override
    public void removeTicketFromParkingLot() {
        throw new IllegalArgumentException(message);
    }

    @Override
    public void enter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new IllegalArgumentException(message);
    }

    @Override
    public void leave(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException(message);
    }

    @Override
    public String toString(){
        return "Archived";
    }

    @Override
    public String getStateDescription() {
        return "archiviert";
    }

}
