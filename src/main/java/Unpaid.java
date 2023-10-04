import javax.servlet.ServletContext;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class Unpaid extends TTState {
    public Unpaid(ServletContext context, HashMap<Integer, Ticket> ticketIdList, Ticket ticket) {
        super(context, ticket);
    } //zusätzlicher Konstruktor um Zugriff auf das Ticket und die TicketId für die Exceptions zu erhalten


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("UnpaidState");
        String action = request.getParameter("action");
        //test = falls hashmap leer durch falschen Konstruktor, sollte eingentlich wieder gelöscht werden können
        HashMap<Integer, Ticket>ticketIdList = (HashMap<Integer, Ticket>) context.getAttribute("TicketIDs");
        System.out.println("Unpaid handle");


         if(action.equals("pay")){
             String id_String = request.getParameter("ID");

             //HashMap<Integer, Ticket> ticketIdList = (HashMap<Integer, Ticket>) context.getAttribute("TicketIDs");
             Integer id = Integer.parseInt(id_String);
             context.setAttribute("tmp", id);

             // Berechnung des zu zahlenden Betrags
             LocalDateTime ende = LocalDateTime.parse(request.getParameter("zeitpunkt"), ISO_LOCAL_DATE_TIME);
             //Prüfe, ob ein gültiger Zeitpunkt eingegeben wurde:
             if (ende.isBefore((LocalDateTime)(context.getAttribute("zeit")))){
                 throw new IllegalStateException("Raum-Zeit-Kontinuum verletzt");
             }
                 //Ansonsten aktualisiere die Zeit im Parkhaus:
                 context.setAttribute("zeit",ende);
                 System.out.println("Pay, Aktuelle Zeit: "+context.getAttribute("zeit"));

                 int endeHour = ende.getHour();
                 int dauer=(endeHour-ticketIdList.get(id).getStartDate().getHour());
                 double zuZahlenderBetrag = dauer * Administration.getCurrentPrice() + 2;
                 context.setAttribute("preis", zuZahlenderBetrag);
                 Ticket ticket = ticketIdList.get(id);
                 ticket.setEndDate(ende);

             // Ausgabe der Ergebnisse
             if(!ticketIdList.get(id).getAlready()) {
                 response.setContentType("text/html");
                 PrintWriter out = response.getWriter();
                 out.println("<h1>Ticketnummer: " + id + "</h1>");
                 out.println("<h2>Parkdauer: " + dauer + " Stunden</h2>");
                 out.println("<h3>Zu zahlender Betrag: " + zuZahlenderBetrag + " Euro</h3>");
                 out.println("</body></html>");
                 ticketIdList.get(id).setAlready(true);
             }else{
                 response.setContentType("text/html");
                 zuZahlenderBetrag = 1.0;
                 context.setAttribute("preis", zuZahlenderBetrag);
                 PrintWriter out = response.getWriter();
                 out.println("<h1>Ticketnummer: " + id + "</h1>");
                 out.println("<h2> Zusatzgebühr fällig</h2>");
                 out.println("<h3>Zu zahlender Betrag: " + zuZahlenderBetrag + " Euro</h3>");
                 out.println("</body></html>");
             }
             // Setze den nächsten Zustand

             forwardToJSP(request, response, "/pay.jsp");

        } else if (action.equals("bar") ){
             //HashMap<Integer, Ticket> ticketIdList = (HashMap<Integer, Ticket>) context.getAttribute("TicketIDs");
             Integer id = Integer.parseInt((String.valueOf(context.getAttribute("tmp"))));

          /**   if(ticketIdList.get(id).getTicketPaid() && ticketIdList.get(id).getAlready() ){
                 forwardToJSP(request, response, "/bar.jsp");
                 return;

             }*/

             int einwurf;

             int zwei, ein;
             if(request.getParameter("counter2Euro") != null && request.getParameter("counter1Euro" ) != null) {
                 System.out.println("counter != null");
                 zwei = Integer.parseInt(request.getParameter("counter2Euro"));
                 ein = Integer.parseInt(request.getParameter("counter1Euro"));
             }else{
                 zwei = 0;
                 ein = 0;
             }

             einwurf = (zwei * 2) + ein;
             context.setAttribute("sum" ,einwurf + getSum());
             System.out.println("Einwurf "+einwurf);

             response.setContentType("text/html");
             PrintWriter out = response.getWriter();

             if(einwurf != 0){
                 System.out.println("Einwurf != 0");
                 System.out.println("getSum() "+getSum());
                 out.println("Eingeworfener Betrag: " + getSum() + " Euro");
                 if(getSum() < (double)context.getAttribute("preis")){
                     out.println("Bitte noch einwerfen: " + ((double)context.getAttribute("preis") - getSum()));
                 }else if(getSum() > (double) context.getAttribute("preis")){
                     out.println("Vielen Dank! Bitte entnehmen Sie Ihr Rückgeld in Höhe von: " + (getSum() - (double)context.getAttribute("preis")));

                     //neuer State -> Unterscheidung Nachzahlung Ja/Nein mit Hilfsvariable, da setAlready dafür zu früh gesetzt wird
                    /* if(ticketIdList.get(id).isFakeStatePayPenalty()) {
                         ticketIdList.get(id).setState(new SingleTicketPaid(context,ticketIdList.get(id)));
                     } else {
                     */
                       ticketIdList.get(id).setState(new SingleTicketInParkingLot(context,ticketIdList.get(id)));
                       /*ticketIdList.get(id).setFakeStatePayPenalty(true);
                     }
                     */

                     context.setAttribute("income", (double)context.getAttribute("income" )+ (double)context.getAttribute("preis"));
                     context.setAttribute("sum",0.0);
                     ticketIdList.get(id).setTicketPaid(true);
                     forwardToJSP(request, response, "/parkhaus.jsp");
                     return;
                 }else if(getSum() == (double) context.getAttribute("preis")){
                     System.out.println("Einwurf == getSum() "+getSum());
                     out.println("Vielen Dank! Das Parkhaus kann verlassen werden");

                     //neuer State -> Unterscheidung Nachzahlung Ja/Nein mit Hilfsvariable, da setAlready dafür zu früh gesetzt wird
                    /* if(ticketIdList.get(id).isFakeStatePayPenalty()) {
                         ticketIdList.get(id).setState(new SingleTicketPaid(context,ticketIdList.get(id)));
                      } else {
                         System.out.println("Zielstate erreicht ");
                     */
                         ticketIdList.get(id).setState(new SingleTicketInParkingLot(context,ticketIdList.get(id)));
                    /*     ticketIdList.get(id).setFakeStatePayPenalty(true);
                     } */
                     context.setAttribute("income", (double)context.getAttribute("income" )+ (double)context.getAttribute("preis"));
                     context.setAttribute("sum",0.0);
                     ticketIdList.get(id).setTicketPaid(true);
                     forwardToJSP(request, response, "/parkhaus.jsp");
                     return;
                 }
             }else{
                 out.println("<h2>Bitte werfen Sie das Geld ein</h2>");
             }
             out.println("</body></html>");

             if(!ticketIdList.get(id).getTicketPaid() || ticketIdList.get(id).getTicketPaid() && ticketIdList.get(id).getAlready()) {
                 forwardToJSP(request, response, "/bar.jsp");
             }

             forwardToJSP(request, response, "/parkhaus.jsp");

         } else if (action.equals("karte") || action.equals("in")){

             //HashMap<Integer, Ticket> ticketIdList = (HashMap<Integer, Ticket>) context.getAttribute("TicketIDs");
             Integer id= Integer.parseInt(String.valueOf(context.getAttribute("tmp")));
             response.setContentType("text/html");
             PrintWriter out = response.getWriter();

             if(request.getParameter("action").equals("in")){
                 out.println("Ticket bezahlt! Bitte entnehmen Sie Ihre Karte. Vielen Dank!");
                 ticketIdList.get(id).setTicketPaid(true);
                 context.setAttribute("income", (double)context.getAttribute("income") + (double)context.getAttribute("preis"));

                 //neuer State -> Unterscheidung Nachzahlung Ja/Nein mit Hilfsvariable, da setAlready dafür zu früh gesetzt wird
                 /*if(ticketIdList.get(id).isFakeStatePayPenalty()) {
                     ticketIdList.get(id).setState(new SingleTicketPaid(context,ticketIdList.get(id)));
                  } else { */
                     ticketIdList.get(id).setState(new SingleTicketInParkingLot(context,ticketIdList.get(id)));
                 /*    ticketIdList.get(id).setFakeStatePayPenalty(true);
                 } */
                 forwardToJSP(request,response,"/parkhaus.jsp");
             }else{
                 out.println("<h2>Bitte Karte einfügen</h2>");
                 forwardToJSP(request,response,"/karte.jsp");
             }
        }
    }
    @Override
    public void assignParkingLotToTicket(Integer parkingLotLevel) {
        throw new IllegalArgumentException("Ticket mit der ID "  + this.ticket.getTicketID() + " wurde schon eingeparkt");
    }

    @Override
    public void removeTicketFromParkingLot() {
        throw new IllegalArgumentException("Ticket mit der ID "  + this.ticket.getTicketID() + " wurde noch nicht bezahlt, ausparken geht also nicht");
    }

    @Override
    public void enter(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Ticket mit der ID "  + this.ticket.getTicketID() + "  befindet sich schon im Parkhaus");
    }

    @Override
    public void leave(HttpServletRequest request, HttpServletResponse response) {
        throw new IllegalArgumentException("Ticket mit der ID "  + this.ticket.getTicketID() + " vor dem Verlassen noch bezahlen und ausparken");
    }

    @Override
    public String toString() {
        return "Unpaid";
    }

    @Override
    public String getStateDescription() {
        /*
        if(ticket.isFakeStatePayPenalty()) {
            return "Nachzahlung erforderlich";
        } else { */
        return "geparkt, noch nicht bezahlt";
        //}
    }
}

