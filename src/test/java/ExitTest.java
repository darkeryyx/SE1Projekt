import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
/**
public class ExitTest {
    Ticket unpaid;
    Ticket paidMissedTimeLimit;
    Ticket paid;
    HashMap<Integer, Ticket> ticketList_empty;
    HashMap<Integer, Ticket> ticketList_full;
    LocalDateTime checkIn;
    LocalDateTime paidAt;

    LocalDateTime checkOut;
    LocalDateTime checkOutMissedTimeLimit;

    Exit lv;

    @BeforeEach
    public void setUp() {
      //  lv = new Exit();
        ticketList_empty = new HashMap<>();
        ticketList_full = new HashMap<>();

        checkIn = LocalDateTime.of( 2023,05,04,9,30,00);
        paidAt = LocalDateTime.of( 2023,05,04,12,35,00);
        checkOut = LocalDateTime.of( 2023,05,04,12,41,00);
        checkOutMissedTimeLimit = LocalDateTime.of(2023,05,04,12,50,00);

        unpaid = new Ticket();
        unpaid.setTicketPaid(false);
        unpaid.setStartDate(checkIn);
        unpaid.setEndDate(paidAt);
        ticketList_full.put(1,unpaid);

        paidMissedTimeLimit = new Ticket();
        paidMissedTimeLimit.setTicketPaid(true);
        paidMissedTimeLimit.setStartDate(checkIn);
        paidMissedTimeLimit.setEndDate(paidAt);
        ticketList_full.put(2,paidMissedTimeLimit);

        paid = new Ticket();
        paid.setTicketPaid(true);
        paid.setStartDate(checkIn);
        paid.setEndDate(paidAt);
        ticketList_full.put(3,paidMissedTimeLimit);
    }

    @AfterEach
    public void tearDown() {
        unpaid = null;
        paidMissedTimeLimit = null;
        paid = null;
    }

    @Test
    @DisplayName("Test: Mit unbezahltem Ticket, sollte man das Parkhaus nicht verlassen können")
    public void testUnPaidTicket() {
        assertEquals(3, ticketList_full.size());

        LocalDateTime checkOut = (LocalDateTime.of( 2023,05,04,12,41,00));
      //  assertEquals("Ticket wurde 1 wurde noch nicht bezahlt", lv.leave(checkOut,ticketList_full, 1 ));

        assertEquals(3, ticketList_full.size(), "Größe unverändert");
    }

    @Test
    @DisplayName("Nach Ablauf des Zeitlimits, sollte man das Parkhaus nicht verlassen können -> leave=false")
    public void testPaidTicketButMissedTimeLimit() {
        assertEquals(3, ticketList_full.size());

        LocalDateTime checkOut = (LocalDateTime.of( 2023,05,04,12,50,00));
       // assertEquals("Ticket 2 Zeitlimit wurde überschritten. Bitte am Automaten nachzahlen.", lv.leave(checkOutMissedTimeLimit,ticketList_full, 2));

        assertEquals(3, ticketList_full.size(), "Größe unverändert");
    }

    @Test
    @DisplayName("Bezahlt+Zeitlimit eingehalte, sollte Parkhaus verlassen können -> leave=false")
    public void testPaidTicket() {
        assertEquals(3, ticketList_full.size());

       // assertEquals("Ticket 3 kann das Parkhaus verlassen", lv.leave(checkOut,ticketList_full,3));

        assertEquals(2, ticketList_full.size(), "Größe - 1");
    }

    @Test
    @DisplayName("Für ein negatives Zeitlimit soll eine Exception geworfen werden -> leave=false")
    public void testNegativeTimeLimit() {
        assertThrows(IllegalArgumentException.class, ()-> lv.setTimeLimit(-1), "Zeitlimit darf nicht negativ sein");
    }

    @Test
    public void enter() {
        assertEquals(0, ticketList_empty.size());
        for(int i = 1; i < 5; i++) {
       //     lv.enter(checkIn, ticketList_empty);
            assertEquals(i, ticketList_empty.size());
        }
    }
}
**/