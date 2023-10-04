import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingLotOccupiedTest {
    private ParkingLot parkingLotMonthlyTicket; //Testparkplatz mit Monatsticket
    private ParkingLot parkingLotSingleTicket; //Testparkplatz mit Tagesticket
    private HashMap<Integer,Ticket> singleTickets;
    private Ticket singleTicket; //TestTagesTickets
    private MonthTicket monthlyTicket;
    private ServletContext mockcontext;
    @BeforeEach
    public void setUp() {
        //verschiedenen Parkplätze initialisieren
        parkingLotMonthlyTicket = new ParkingLot(1,'A');
        parkingLotSingleTicket = new ParkingLot(1,'B');

        //Einzelticket + Liste
        singleTicket = new Ticket();
        singleTickets = new HashMap<>();
        singleTickets.put(singleTicket.getTicketID(),singleTicket);

        //Mockkontext, um State zu setzen
        mockcontext = mock(ServletContext.class);
        when(mockcontext.getAttribute("TicketIDs")).thenReturn(singleTickets);
        when(mockcontext.getAttribute("sum")).thenReturn(0.0);

        //State setzen, sodass geparkt werden kann
        TTState state = new SingleTicketNoParkingLot(mockcontext, singleTicket);
        singleTicket.setState(state);

        //Monatsticket, setIsInPh auf true, sodass geparkt werden kann
        monthlyTicket = new MonthTicket(LocalDate.parse("2023-05-01"));
        monthlyTicket.setIsInPH();

        //Monatsticket und Einzelticket parken
        parkingLotMonthlyTicket.occupy(monthlyTicket);
        parkingLotSingleTicket.occupy(singleTicket);

        //State weitersetzen, sodass ohne Bezahlen ausgeparkt werden kann
        state = new SingleTicketInParkingLot(mockcontext, singleTicket);
        singleTicket.setState(state);
    }

    @AfterEach
    public void tearDown() {
        parkingLotMonthlyTicket = null;
        parkingLotSingleTicket = null;

        singleTicket = null;
        singleTickets = null;
        monthlyTicket = null;

        mockcontext = null;
    }

    @Test
    public void testOccupy() {
        assertTrue(parkingLotSingleTicket.getState() instanceof ParkingLotOccupied);
        assertTrue(parkingLotMonthlyTicket.getState() instanceof ParkingLotOccupied);
        //nur Sicherheitstests

        assertThrows(IllegalStateException.class, () -> parkingLotMonthlyTicket.occupy(monthlyTicket));
        assertThrows(IllegalStateException.class, () -> parkingLotSingleTicket.occupy(monthlyTicket));
    }

    @Test
    public void leaveSingleTicket() {
           assertTrue(parkingLotSingleTicket.getState() instanceof ParkingLotOccupied);
           parkingLotSingleTicket.leave(singleTicket);
           //neuer State
           assertTrue(parkingLotSingleTicket.getState() instanceof ParkingLotEmpty, "State-Wechsel hat nicht funktioniert");
           //boolean nicht mehr belegt
           assertFalse(parkingLotSingleTicket.isOccupied(),"sollte nicht mehr belegt sein");
           //TicketId, die dem Parkplatz zugeordnet ist sollte null sein
           assertNull(parkingLotSingleTicket.getTicketId(),"TicketID wird nicht entfernt");
    }

    @Test
    public void leaveMonthlyTicket() {
        assertTrue(parkingLotMonthlyTicket.getState() instanceof ParkingLotOccupied);
        parkingLotSingleTicket.leave(monthlyTicket);
        //neuer State
        assertTrue(parkingLotSingleTicket.getState() instanceof ParkingLotEmpty, "State-Wechsel hat nicht funktioniert");
        //boolean nicht mehr belegt
        assertFalse(parkingLotSingleTicket.isOccupied(),"sollte nicht mehr belegt sein");
        //TicketId, die dem Parkplatz zugeordnet ist sollte null sein
        assertNull(parkingLotSingleTicket.getTicketId(),"TicketID wird nicht entfernt");
    }

    @Test
    public void testIsOccupied() {
        assertTrue(parkingLotSingleTicket.isOccupied(), "boolean is occupied nicht angepasst");
    }
}
