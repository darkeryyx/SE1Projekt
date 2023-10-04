import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public abstract class TTState {

    protected ServletContext context;

    protected Ticket ticket;//Ticket zum der State gehört
    protected double sum;

        public TTState(ServletContext context, Ticket ticket) {
            this.context = context;
            this.ticket = ticket;
            this.sum = getSum();
        }

        public abstract void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

        public abstract void assignParkingLotToTicket(Integer parkingLotLevel);
        public abstract void removeTicketFromParkingLot();

        public abstract void enter(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException;
        public abstract void leave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

        protected double getSum() {
            if (context.getAttribute("sum") == null) {
                context.setAttribute("sum", 0.0d);
            }
            return (double) context.getAttribute("sum");
        }

        protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response, String jspPath) throws ServletException, IOException {
            RequestDispatcher rd = context.getRequestDispatcher(jspPath);
            rd.include(request, response);
        }

        public abstract String toString();

        public abstract String getStateDescription(); //Beschreibung des Status, für Ausgabe
    }