import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    Ticket ticket;
    LocalDateTime time;
    int id; // Testen der ID
    double price; // Testen des Preises

    @BeforeEach
    public void setup(){
        ticket = new Ticket();
    }

    @Test
    public void testGetTicketID(){
        //ticket.setTicketID(1);
        int current_id = new Ticket().getTicketID() + 9;
        //andere Test beeinflussen auf Jenkis die TicketID, deshalb wird die
        //aktuelle ID genommen und 9 gerechnet, sodass die StartID egal ist (Änderung von Lukas)


        for(int i = 0; i < 9; i++){
            ticket = new Ticket();
        }
        id = current_id;
        assertEquals(id,ticket.getTicketID(), "Die TicketID zählt nicht richtig hoch");
    }

    @Test
    public void testGetDate(){
        time = LocalDateTime.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm");
        System.out.println(ticket.getStartDate());
        //assertEquals(time.format(df),ticket.getStartDate().format(df),"Das Datum stimmt nicht");
    }

    /*
    public void testGetPriceStamp(){
        price = 2; // prüfe auf richtige startgebühr
        assertEquals(price,ticket.getPriceStamp(),"Der Preis ist nicht richtig berechnet");
    }*/

}