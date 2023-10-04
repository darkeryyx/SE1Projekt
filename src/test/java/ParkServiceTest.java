import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkServiceTest {

    private ParkService parkService;
    private MonthTicket monthlyTicket;
    private Ticket singleTicket1;
    private Ticket singleTicket2;
    private Ticket singleTicket3;
    private Ticket singleTicket4;
    private Ticket singleTicket5;
    private Ticket singleTicket6;
    private Ticket singleTicket7;
    private Ticket singleTicket8;
    private Ticket singleTicket9;
    private Ticket singleTicket10;

    private HashMap<Integer,Ticket> singleTickets;
    private HashMap<String, MonthTicket> monthlyTickets;
    private ServletContext mockcontext;
    @BeforeEach
    public void setUp() {
        mockcontext = mock(ServletContext.class);
        parkService = ParkService.resetInstance();

        monthlyTickets = new HashMap<>();
        monthlyTicket = new MonthTicket(LocalDate.parse("2023-05-01"));
        monthlyTicket.setIsInPH();
        monthlyTickets.put(monthlyTicket.getID(),monthlyTicket);

        when(mockcontext.getAttribute("sum")).thenReturn(0.0);

        singleTicket1 = new Ticket();
        TTState state1 = new SingleTicketNoParkingLot(mockcontext, singleTicket1);
        singleTicket1.setState(state1); //State setzen

        singleTicket2 = new Ticket();
        TTState state2 = new SingleTicketNoParkingLot(mockcontext, singleTicket2);
        singleTicket2.setState(state2); //State setzen

        singleTicket3 = new Ticket();
        TTState state3 = new SingleTicketNoParkingLot(mockcontext, singleTicket3);
        singleTicket3.setState(state3); //State setzen

        singleTicket4 = new Ticket();
        TTState state4 = new SingleTicketNoParkingLot(mockcontext, singleTicket4);
        singleTicket4.setState(state4); //State setzen

        singleTicket5 = new Ticket();
        TTState state5 = new SingleTicketNoParkingLot(mockcontext, singleTicket5);
        singleTicket5.setState(state5); //State setzen

        singleTicket6 = new Ticket();
        TTState state6 = new SingleTicketNoParkingLot(mockcontext, singleTicket6);
        singleTicket6.setState(state6); //State setzen

        singleTicket7 = new Ticket();
        TTState state7 = new SingleTicketNoParkingLot(mockcontext, singleTicket7);
        singleTicket7.setState(state7); //State setzen

        singleTicket8 = new Ticket();
        TTState state8 = new SingleTicketNoParkingLot(mockcontext, singleTicket8);
        singleTicket8.setState(state8); //State setzen

        singleTicket9 = new Ticket();
        TTState state9 = new SingleTicketNoParkingLot(mockcontext, singleTicket9);
        singleTicket9.setState(state9); //State setzen

        singleTicket10 = new Ticket();
        TTState state10 = new SingleTicketNoParkingLot(mockcontext, singleTicket10);
        singleTicket10.setState(state10); //State setzen

        singleTickets = new HashMap<>();
        singleTickets.put(singleTicket1.getTicketID(), singleTicket1);
        singleTickets.put(singleTicket2.getTicketID(), singleTicket2);
        singleTickets.put(singleTicket3.getTicketID(), singleTicket3);
        singleTickets.put(singleTicket4.getTicketID(), singleTicket4);
        singleTickets.put(singleTicket5.getTicketID(), singleTicket5);
        singleTickets.put(singleTicket6.getTicketID(), singleTicket6);
        singleTickets.put(singleTicket7.getTicketID(), singleTicket7);
        singleTickets.put(singleTicket8.getTicketID(), singleTicket8);
        singleTickets.put(singleTicket9.getTicketID(), singleTicket9);
        singleTickets.put(singleTicket10.getTicketID(), singleTicket10);
    }

    private void setUpSingleTicketInParkingLot(Ticket singleTicket, String parkingLotId) {
        parkService.occupy(parkingLotId,
                ((Integer)singleTicket.getTicketID()).toString(),
                "",
                singleTickets,
                monthlyTickets);
        when(mockcontext.getAttribute("sum")).thenReturn(0.0);
        TTState state = new SingleTicketInParkingLot(mockcontext, singleTicket);
        singleTicket.setState(state); //State setzen
    }

    private void setUpMonthlyTicketInParkingLot(MonthTicket monthlyTicket) {
        parkService.occupy("0A",
                "",
                monthlyTicket.getID(),
                singleTickets,
                monthlyTickets);

    }



    @AfterEach
    public void tearDown() {
       parkService = null;
       monthlyTicket = null;
       singleTicket1 = null;
       singleTickets = null;
       monthlyTickets = null;
    }

    @Test
    public void testGetInstance() {
        assertSame(parkService, ParkService.getInstance());
    }
    @Test
    public void testGetParkingLotById() {
        ParkingLot p0 = parkService.getParkingLotById("0A");
        assertEquals("0A", p0.getId());

        ParkingLot p1 = parkService.getParkingLotById("1A");
        assertEquals("1A", p1.getId());

        ParkingLot p2 = parkService.getParkingLotById("1E");
        assertEquals("1E", p2.getId());
    }

    @Test
    public void testGetParkingLotByIdWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> parkService.getParkingLotById("-1A"));
        assertThrows(IllegalArgumentException.class, () -> parkService.getParkingLotById("0F"));
        assertThrows(IllegalArgumentException.class, () -> parkService.getParkingLotById("2A"));
    }
@Test

    public void testOccupyInvalidEntry() {
         assertThrows(IllegalArgumentException.class, () ->
                parkService.occupy("0A",
                        "-1",
                        "",
                        singleTickets,
                        monthlyTickets));

        assertThrows(IllegalArgumentException.class, () ->
                parkService.occupy("0A",
                        "",
                        "",
                        singleTickets,
                        monthlyTickets));

        assertThrows(IllegalArgumentException.class, () ->
                parkService.occupy("0A",
                        "",
                        "MT-1",
                        singleTickets,
                        monthlyTickets));
    }


    @Test
    public void occupySingleTicket() {
        parkService.occupy("0A",
                ((Integer)singleTicket1.getTicketID()).toString(),
                "",
                singleTickets,
                monthlyTickets);

        assertTrue(singleTicket1.getState() instanceof Unpaid);
        //Hat das Ticket den State gewechselt?

        assertEquals(((Integer)singleTicket1.getTicketID()).toString(), parkService.getParkingLotById("0A").getTicketId());
        //Wurde die korrekte ID zugewiesen

        assertTrue(parkService.getParkingLotById("0A").getState() instanceof ParkingLotOccupied);
    }

    @Test
    public void occupyMonthlyTicket() {
        parkService.occupy("0A",
                "",
                monthlyTicket.getID(),
                singleTickets,
                monthlyTickets);

        assertTrue(monthlyTicket.isCurrentlyInParkingLot());
        //Hat das Ticket den State gewechselt?

        assertEquals(monthlyTicket.getID(), parkService.getParkingLotById("0A").getTicketId());
        //Wurde die korrekte ID zugewiesen

        assertTrue(parkService.getParkingLotById("0A").getState() instanceof ParkingLotOccupied);
    }

    @Test
    public void testLeaveSingleTicket() {
        setUpSingleTicketInParkingLot(singleTicket1, "0A");
        parkService.leave("0A",
                singleTickets,
                monthlyTickets);

        assertTrue(singleTicket1.getState() instanceof SingleTicketPaid);
        //Hat das Ticket den State gewechselt?

        assertNull( parkService.getParkingLotById("0A").getTicketId());
        //Wurde die korrekte ID zugewiesen

        assertTrue(parkService.getParkingLotById("0A").getState() instanceof ParkingLotEmpty);
    }

    @Test
    public void testLeaveMonthlyTicket() {
        setUpMonthlyTicketInParkingLot(monthlyTicket);

        parkService.leave("0A",
                singleTickets,
                monthlyTickets);

        assertFalse(monthlyTicket.isCurrentlyInParkingLot());
        //Hat das Ticket den State gewechselt?

        assertNull(parkService.getParkingLotById("0A").getTicketId());
        //Wurde die korrekte ID zugewiesen

        assertTrue(parkService.getParkingLotById("0A").getState() instanceof ParkingLotEmpty);
    }

    @Test
    public void testGetFirstEmptyParkingLot() {
        assertEquals("0A",parkService.GetFirstEmptyParkingLot().getId());

        setUpSingleTicketInParkingLot(singleTicket1, "0A");
        assertEquals("0B",parkService.GetFirstEmptyParkingLot().getId());

        setUpSingleTicketInParkingLot(singleTicket2, "0C");
        assertEquals("0B",parkService.GetFirstEmptyParkingLot().getId());

        setUpSingleTicketInParkingLot(singleTicket3, "0B");
        setUpSingleTicketInParkingLot(singleTicket4, "0D");
        setUpSingleTicketInParkingLot(singleTicket5, "0E");
        assertEquals("1A",parkService.GetFirstEmptyParkingLot().getId());
        setUpSingleTicketInParkingLot(singleTicket6, "1B");
        setUpSingleTicketInParkingLot(singleTicket7, "1C");
        setUpSingleTicketInParkingLot(singleTicket8, "1D");
        setUpSingleTicketInParkingLot(singleTicket9, "1E");
        assertEquals("1A",parkService.GetFirstEmptyParkingLot().getId());
        setUpSingleTicketInParkingLot(singleTicket10, "1A");
        assertNull(parkService.GetFirstEmptyParkingLot());

        parkService.leave("1A", singleTickets, monthlyTickets);
        assertEquals("1A",parkService.GetFirstEmptyParkingLot().getId());

        parkService.leave("0A", singleTickets, monthlyTickets);
        assertEquals("0A",parkService.GetFirstEmptyParkingLot().getId());
    }

    @Test
    public void testGetLevelsAsList() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        assertEquals(list, parkService.getLevelsAsList());
    }

    @Test
    public void testGetParkingLotsPerLevel() {
        List<ParkingLot> list1 = new ArrayList<>();
        list1.add(parkService.getParkingLotById("0A"));
        list1.add(parkService.getParkingLotById("0B"));
        list1.add(parkService.getParkingLotById("0C"));
        list1.add(parkService.getParkingLotById("0D"));
        list1.add(parkService.getParkingLotById("0E"));

        List<ParkingLot> list2 = new ArrayList<>();
        list2.add(parkService.getParkingLotById("1A"));
        list2.add(parkService.getParkingLotById("1B"));
        list2.add(parkService.getParkingLotById("1C"));
        list2.add(parkService.getParkingLotById("1D"));
        list2.add(parkService.getParkingLotById("1E"));

        assertEquals(list1, parkService.getParkingLotsByLevel("0"));
        assertEquals(list2, parkService.getParkingLotsByLevel("1"));
    }

    @Test
    public void testOccupiedParkingLots() {
        setUpSingleTicketInParkingLot(singleTicket1, "0A");
        setUpSingleTicketInParkingLot(singleTicket2, "0B");
        setUpSingleTicketInParkingLot(singleTicket5, "0E");
        setUpSingleTicketInParkingLot(singleTicket7, "1C");
        setUpSingleTicketInParkingLot(singleTicket8, "1D");

        List<ParkingLot> occupiedParkingLots = new ArrayList<>();
        occupiedParkingLots.add(parkService.getParkingLotById("0A"));
        occupiedParkingLots.add(parkService.getParkingLotById("0B"));
        occupiedParkingLots.add(parkService.getParkingLotById("0E"));
        occupiedParkingLots.add(parkService.getParkingLotById("1C"));
        occupiedParkingLots.add(parkService.getParkingLotById("1D"));

        assertEquals(occupiedParkingLots, parkService.occupiedParkingLots());
    }
    @Test
    public void testGetParkingLotList() {
        List<ParkingLot> parkingLotList = new ArrayList<>();
        parkingLotList.add(parkService.getParkingLotById("0A"));
        parkingLotList.add(parkService.getParkingLotById("0B"));
        parkingLotList.add(parkService.getParkingLotById("0C"));
        parkingLotList.add(parkService.getParkingLotById("0D"));
        parkingLotList.add(parkService.getParkingLotById("0E"));
        parkingLotList.add(parkService.getParkingLotById("1A"));
        parkingLotList.add(parkService.getParkingLotById("1B"));
        parkingLotList.add(parkService.getParkingLotById("1C"));
        parkingLotList.add(parkService.getParkingLotById("1D"));
        parkingLotList.add(parkService.getParkingLotById("1E"));
        assertEquals(parkingLotList, parkService.getParkingLotList());
    }
}
