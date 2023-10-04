/*
import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.util.HashMap;

public interface ExitIF {

    //void setTimeLimit(long minutes) throws IllegalArgumentException;
    String enter(LocalDateTime start);
    String leave(LocalDateTime end,Integer id);
    void setCon(ServletContext sc);
    void setTicketList(HashMap<Integer, Ticket> ticketList);
    static Exit getInstance(){return null;}
}
*/