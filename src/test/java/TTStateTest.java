import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import javax.servlet.http.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TTStateTest {

    HttpServletRequest request;
    HttpServletResponse response;
    ServletContext mockcontext;
    ParkhausServlet parkhaus;
    StringWriter stringWriter;
    PrintWriter writer;
    RequestDispatcher rd;
    Ticket ticket; //Testobjekt
    SingleTicketCreated stcreated; //Startzustand für Testobjekt
    HashMap<Integer,Ticket> ticketIdList;

    @BeforeEach
    void initParkhausServlet() {
        ticketIdList = new HashMap<>();
        mockcontext = mock(ServletContext.class); //Mock Servlet Context
        //mockcontext.setAttribute("TicketIDs", ticketIdList);
        request = mock(HttpServletRequest.class); //Mock Request
        response = mock(HttpServletResponse.class); //Mock Response
        rd = mock(RequestDispatcher.class);
        parkhaus = new ParkhausServlet(){
            public ServletContext getServletContext() {
                return mockcontext; // return the mock
            }
        };
        //Die Zeit im Parkhaus ist: 2023-05-04T09:00:00
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
    }
    @BeforeEach
    void initTTState() {
        ticket = new Ticket();
        when(mockcontext.getAttribute("sum")).thenReturn(0.0);
        stcreated = new SingleTicketCreated(mockcontext, ticket);
    }
    @Test
    void testNotNull(){
        assertNotNull(mockcontext);
        assertNotNull(request);
        assertNotNull(response);
        assertNotNull(parkhaus);
        assertNotNull(ticket);
        //assertNotNull(ticket.getState());
        assertNotNull(stcreated);
    }
    @Test
    void testEnter() throws IOException, ServletException {

        when(request.getParameter("zeitpunkt")).thenReturn("2023-05-04T09:00:00");
        when(mockcontext.getAttribute("TicketIDs")).thenReturn(ticketIdList);
        //when(response.getWriter()).thenReturn(writer);
        //doReturn(LocalDateTime.parse("2023-05-04T09:00:00")).when(mockcontext).getAttribute("zeit");
        ticket.setState(stcreated);
        doReturn(rd).when(mockcontext).getRequestDispatcher(anyString());
        doNothing().when(rd).include(request,response); //Das Weiterleiten an das Frontend findet nicht statt
        assertEquals("wird erstellt",ticket.getStateDescription()); //Ticket ist im Zustand 'SingleTicketCreated'
        ticket.getState().enter(request, response);
        assertEquals("nicht eingeparkt",ticket.getStateDescription()); //Ticket ist im Zustand 'SingleTicketNoParkingLot'
        //when(mock.doPost_Enter()).thenReturn(true);
    }
    @Test
    void testParkInParkingLot() throws ServletException, IOException {
        mockcontext.setAttribute("sum", 0.0);
        when(mockcontext.getAttribute("sum")).thenReturn(0.0);
        testEnter(); //Ticket ist jetzt in Zustand 'SingleTicketNoParkingLot'
        ticket.getState().assignParkingLotToTicket(0); //Ticket ist jetzt in Zustand 'Unpaid'
        assertEquals("Unpaid",ticket.getState().toString());
    }
    @Test
    void testPayTicketWithCard() throws ServletException, IOException {
        testParkInParkingLot(); //Das Ticket ist jetzt im Zustand 'Unpaid'
        //Vorbereitung Schritt 1
        when(request.getParameter("action")).thenReturn("pay");
        when(mockcontext.getAttribute("TicketIDs")).thenReturn(ticketIdList);
        when(request.getParameter("ID")).thenReturn(""+ticket.getTicketID()); //Gib die ID des Testtickets zurück
        when(request.getParameter("zeitpunkt")).thenReturn("2023-05-04T10:00:00");
        doReturn(LocalDateTime.parse("2023-05-04T09:00:00")).when(mockcontext).getAttribute("zeit");
        when(response.getWriter()).thenReturn(writer);
        doReturn(this.rd).when(mockcontext).getRequestDispatcher(anyString());
        doNothing().when(rd).include(request,response); //Das Weiterleiten an das Frontend findet nicht statt

        //Aufruf der Methode (Ticket wird das 1. Mal in den Automaten gesteckt, keine Nachzahlung)
        ticket.getState().handle(request, response);

        //Vorbereitung Schritt 2.1
        when(request.getParameter("action")).thenReturn("karte"); //Bezahlung mit Karte
        when(mockcontext.getAttribute("tmp")).thenReturn(""+ticket.getTicketID());

        //Aufruf der Methode (Wahl 'Kartenzahlung')
        ticket.getState().handle(request, response);

        //Vorbereitung Schritt 2.2
        when(request.getParameter("action")).thenReturn("in"); //Karte wird in den Automaten eingeführt
        mockcontext.setAttribute("income",0.0);
        when(mockcontext.getAttribute("income")).thenReturn(0.0);
        when(mockcontext.getAttribute("preis")).thenReturn(ticket.getPriceStamp());

        //Aufruf der Methode (Karte in den Automaten einführen)
        ticket.getState().handle(request, response);
        verify(mockcontext, atLeast(1)).getAttribute("income"); // to verify zeit was called
        verify(mockcontext, atLeast(1)).getAttribute("preis");
        assertEquals("SingleTicketInParkingLot", ticket.getState().toString()); //Ticket ist jetzt in Zustand 'SingleTicketInParkingLot'
    }
    /*
    @Test
    void testPayTicketBar() throws ServletException, IOException {
        testParkInParkingLot(); //Das Ticket ist jetzt im Zustand 'Unpaid'
        //Vorbereitung Schritt 1
        when(request.getParameter("action")).thenReturn("pay");
        when(mockcontext.getAttribute("TicketIDs")).thenReturn(ticketIdList);
        when(request.getParameter("ID")).thenReturn(""+ticket.getTicketID()); //Gib die ID des Testtickets zurück
        when(request.getParameter("zeitpunkt")).thenReturn("2023-05-04T10:00:00");
        doReturn(LocalDateTime.parse("2023-05-04T09:00:00")).when(mockcontext).getAttribute("zeit");
        when(response.getWriter()).thenReturn(writer);
        doReturn(this.rd).when(mockcontext).getRequestDispatcher(anyString());
        doNothing().when(rd).include(request,response); //Das Weiterleiten an das Frontend findet nicht statt

        //Aufruf der Methode (Ticket wird das 1. Mal in den Automaten gesteckt, keine Nachzahlung)
        ticket.getState().handle(request, response);

        //Vorbereitung Schritt 2.1
        when(request.getParameter("action")).thenReturn("bar"); //Barzahlung
        //when(Integer.parseInt(String.valueOf(mockcontext.getAttribute("tmp")))).thenReturn(ticket.getTicketID());
        when(mockcontext.getAttribute("tmp")).thenReturn(""+ticket.getTicketID());
        when(request.getParameter("counter2Euro")).thenReturn(""+1);
        when(request.getParameter("counter1Euro" )).thenReturn(""+0);

        System.out.println("preis"+mockcontext.getAttribute("preis"));

        //Aufruf der Methode (Wahl 'Barzahlung')
        ticket.getState().handle(request, response);
        assertEquals("SingleTicketInParkingLot", ticket.getState().toString()); //Ticket ist jetzt in Zustand 'SingleTicketPaid'
    }
     */
    @Test
    void testAusparken() throws ServletException, IOException {
        testPayTicketWithCard(); //Ticket ist jetzt in Zustand SingleTicketInParkingLot
        ticket.getState().removeTicketFromParkingLot();
        assertEquals("SingleTicketPaid", ticket.getState().toString());
    }
    @Test
    void testLeave() throws ServletException, IOException {
        testAusparken(); //Ticket ist jetzt in Zustand SingleTicketPaid
        when(request.getParameter("ticketID")).thenReturn(""+ticket.getTicketID());
        when(mockcontext.getAttribute("TicketIDs")).thenReturn(ticketIdList);
        doReturn(this.rd).when(mockcontext).getRequestDispatcher(anyString());
        doNothing().when(rd).include(request,response); //Das Weiterleiten an das Frontend findet nicht statt
        ticket.getState().leave(request,response);
        assertEquals("Archived", ticket.getState().toString());
    }
}