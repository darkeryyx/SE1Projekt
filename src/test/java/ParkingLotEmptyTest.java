import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingLotEmptyTest {
    ParkingLot parkingLot; //Testparkplatz
    HashMap<Integer,Ticket> singleTickets;
    Ticket singleTicket; //TestTagesTickets

    HashMap<MonthTicket,String> monthlyTickets;
    MonthTicket monthlyTicket;

    ServletContext mockcontext;
    @BeforeEach
    public void setUp() {
        parkingLot = new ParkingLot(1,'0');

        singleTicket = new Ticket();
        singleTickets = new HashMap<>();

        monthlyTicket = new MonthTicket(LocalDate.parse("2023-05-01"));

        mockcontext = mock(ServletContext.class);
        singleTickets.put(singleTicket.getTicketID(),singleTicket);
    }

    @AfterEach
    public void tearDown() {
        parkingLot = null;
        singleTicket = null;
        mockcontext = null;
        singleTickets = null;
    }
    @Test
    public void testLeave() {
        assertThrows(IllegalArgumentException.class, () -> parkingLot.leave(singleTicket));
    }

    @Test
    public void testOccupySingleTicket() {
        when(mockcontext.getAttribute("TicketIDs")).thenReturn(singleTickets);
        when(mockcontext.getAttribute("sum")).thenReturn(0.0);

        TTState state = new SingleTicketNoParkingLot(mockcontext, singleTicket);
        singleTicket.setState(state); //State setzen

        parkingLot.occupy(singleTicket);

        assertTrue(parkingLot.getState() instanceof ParkingLotOccupied);
        //Ist der Parkplatz jetzt vom State belegt?

        assertEquals(Integer.parseInt(parkingLot.getTicketId()), singleTicket.getTicketID());
        //Ist der Parkplatz von der richtigen TicketID belegt?

        assertTrue(parkingLot.isOccupied());
        //Parkplatz sollte jetzt besetz sein
    }


    @Test
    public void testOccupyMonthlyTicket() {
        assertTrue(parkingLot.getState() instanceof ParkingLotEmpty);
        //Sicherheitscheck

        assertFalse(monthlyTicket.getIsInPH());
        monthlyTicket.setIsInPH();
        assertTrue(monthlyTicket.getIsInPH());
        parkingLot.occupy(monthlyTicket);

        assertTrue(monthlyTicket.isCurrentlyInParkingLot(), "Ticket sollte geparkt sein");
        //ist das Ticket jetzt geparkt?

        assertEquals(monthlyTicket.getID(), parkingLot.getTicketId(), "Dem Parkplatz wurde eine false ID zugewiesen");
        //wurde dem Parkplatz die richtige ID zugewiesen?

        assertTrue(parkingLot.getState() instanceof ParkingLotOccupied, "State sollte gewechselt haben");
        //State hat jetzt gewechselt
    }


    @Test
    public void testOccupyMonthlyTicketNotInCarPark() {
        assertFalse(monthlyTicket.getIsInPH());
        assertThrows(IllegalArgumentException.class,
                () -> parkingLot.occupy(monthlyTicket), "Ticket befindet sich nicht im Parkhaus -> kein Parken möglich");
        //Ticket befindet sich nicht im Parkhaus -> kein Parken möglich
    }

    @Test
    public void testOccupyMonthlyTicketAlreadyParked() {
        monthlyTicket.setIsInPH();
        monthlyTicket.setCurrentlyInParkingLot(true);

        assertTrue(monthlyTicket.getIsInPH());
        assertTrue(monthlyTicket.isCurrentlyInParkingLot());
        //nur Sicherheitschecks

        assertThrows(IllegalArgumentException.class,
                () -> parkingLot.occupy(monthlyTicket), "Ticket bereits geparkt -> kein nochmaliges Parken möglich");
        //Ticket bereits geparkt -> kein nochmaliges Parken möglich
    }
    @Test
    public void testOccupied() {
        assertFalse(parkingLot.isOccupied());
    }
}
