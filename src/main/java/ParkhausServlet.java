import StartConf.StartConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
//TEST PUSH
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;


@WebServlet(name = "parkhausServlet", value = "/parkhaus-servlet")
public class ParkhausServlet extends HttpServlet {
    protected StartConfig startConf;
    protected double income = 0;
    protected final String password = "1234";
    private final LocalDateTime startzeit = LocalDateTime.parse("2023-05-04T09:00:00"); //Feste Startzeit

    protected ServletContext context;
    public void init() {
        context = getServletContext();
        System.out.println("*** init");
        context.setAttribute("TicketIDs", new HashMap<Integer,Ticket>()); //HashMap für die Tagestickets
        context.setAttribute("MTIDs", new HashMap<String, MonthTicket>()); //HashMap für die Monatstickets
        context.setAttribute("MTIDs_to_IDs", new HashMap<String, Ticket>()); //HashMap für die Zugehörigkeit von MTs zu Tagestickets
        Ticket.resetCounter();
        MonthTicket.resetCounter();
        startConf = StartConfig.resetInstance();
        context.setAttribute("administration", Administration.resetInstance());
        context.setAttribute("income",income);
        context.setAttribute("zeit",startzeit);
        context.setAttribute("Parkplätze", ParkService.resetInstance());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Test");
        String action = request.getParameter("aktion");
        if (action.equals("getTime")){
            doGet_CurrentTime(request, response);
        } else if (action.equals("income")) {
            doGet_Income(request, response);
        } else if (action.equals("std")) {
            doGet_StdPreis(request, response);
        }  else if(action.equals("parkMenu")) {
            doGet_initializeParkMenu(request,response);
        } else if(action.equals("firstEmptyParkingLot")) {
            doGet_firstEmptyParkingLot(request,response);
        } else if(action.equals("ticketOverview")) {
            doGet_ticketOverview(request,response);
        }
    }
    public void doGet_ticketOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        includeJSP(request,response,"/ticketOverview.jsp");
    }
    public void doGet_initializeParkMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        includeJSP(request,response,"/parkMenu.jsp");
    }
    public void doGet_firstEmptyParkingLot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ParkService ps = (ParkService) getServletContext().getAttribute("Parkplätze");
        ParkingLot parkplatz = ps.GetFirstEmptyParkingLot();
        if (parkplatz == null) {
            out.println("Derzeit sind alle Parkplätze belegt");
        } else {
            out.println("Der erste freie Parkplatz ist der Parkplatz " + parkplatz.getLevelId() + " auf der Etage " + parkplatz.getLevel() + ".");
        }
    }
    public void doGet_StdPreis(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stundenpreis = "Stundenpreis: " + Administration.getCurrentPrice();
        request.setAttribute("stundenpreis", stundenpreis);
        forwardToJSP(request,response,"/parkhaus.jsp");
    }
    //-----------------------------//
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "enter":
                updateTime(LocalDateTime.parse(request.getParameter("zeitpunkt"), ISO_LOCAL_DATE_TIME));
                doPost_Enter(request, response);
                break;
            case "leave":
                updateTime(LocalDateTime.parse(request.getParameter("zeitpunkt"), ISO_LOCAL_DATE_TIME));
                //"Normales Ticket oder Monatsticket?"
                if (request.getParameter("ticketID").length() > 0 && request.getParameter("ticketID").substring(0,1).equals("M")){
                    doPost_LeaveMT(request, response);
                } else {
                    doPost_Leave(request, response);
                }
                break;
            case "newstd":
                doPost_StdPreisaendern(request, response);
                break;
            case "pay":
                updateTime(LocalDateTime.parse(request.getParameter("zeitpunkt"), ISO_LOCAL_DATE_TIME));
                //"Normales Ticket oder Monatsticket?"
                if (request.getParameter("ID").length() > 0 && request.getParameter("ID").substring(0,1).equals("M")){
                    doPost_PayExtraMTStep1(request, response);
                } else {
                    HashMap<Integer, Ticket> singleTicketList = (HashMap<Integer,Ticket>)getServletContext().getAttribute("TicketIDs");
                    String id_String = request.getParameter("ID");
                    Integer id = Integer.parseInt(id_String);
                    Ticket ticket = singleTicketList.get(id);
                    ticket.getState().handle(request, response);
                }
                break;
            case "bar":
            case "karte":
            case "in": {
                TTState state = ((HashMap<Integer, Ticket>) getServletContext().getAttribute("TicketIDs")).get((Integer) getServletContext().getAttribute("tmp")).getState();
                state.handle(request, response);
                break;
            }
            case "payExtraMT":
                doPost_PayExtraMTStep2(request, response);
            case "mtziehen":
                doPost_Monatsticketziehen(request, response);
                break;
            case "entermt":
                updateTime(LocalDateTime.parse(request.getParameter("zeitpunkt"), ISO_LOCAL_DATE_TIME));
                doPost_EnterMT(request, response);
                break;
            case "newmt":
                doPost_MTPreisaendern(request, response);
                break;
            case "admin":
                doPost_PruefePasswort(request, response);
                break;
            case "reset":
                doPost_Reset(request, response);
                break;
            case "wahlParkplatz":
                doPost_pickParkingLot(request, response);
                break;
            case "pickLevel":
                doPost_pickLevel(request, response);
                break;
            case "leaveParkingLot":
                doPost_leaveParkingLot(request, response);
                break;
        }
    }
    //Methode, um einen Parkplatz auszuwählen
    public void doPost_pickParkingLot (HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        String parkingLotId_string = request.getParameter("p"); //ParkplatzID
        String ticketId_string = request.getParameter("ticketId");
        String monthlyTicketId_string = request.getParameter("mticketId");
        System.out.println(ticketId_string);
        ParkService p = (ParkService) getServletContext().getAttribute("Parkplätze");
        HashMap<Integer,Ticket>  TagesTicketList = (HashMap<Integer,Ticket>)getServletContext().getAttribute("TicketIDs");
        HashMap<String, MonthTicket> MonatsTicketList = (HashMap<String, MonthTicket>)getServletContext().getAttribute("MTIDs");
        p.occupy(parkingLotId_string, ticketId_string, monthlyTicketId_string, TagesTicketList,MonatsTicketList);
        includeJSP(request,response,"/ParkingLotOverview.jsp");
    }
    //Methode,um beim Reinfahren eine Etage auszuwählen
    public void doPost_pickLevel(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        String String_level = request.getParameter("level");//Etage im Parkhaus
        request.setAttribute("level",String_level);

        /*
        List<Ticket> singletTicketsToPark = v.getSingleTicketsToPark((Map<Integer, Ticket>) context.getAttribute("TicketIDs"));
        context.setAttribute("singleTicketsToPark", singletTicketsToPark);
        System.out.println(singletTicketsToPark);
        List<Monatsticket> monthlyTicketsToPark = v.getMonatsTicketsToPark((Map<String,Monatsticket>) context.getAttribute("MTIDs"));
        System.out.println(monthlyTicketsToPark);
        context.setAttribute("monthlyTicketsToPark", monthlyTicketsToPark);
        */
        includeJSP(request,response,"/occupyParkingLot.jsp");
    }
    //Verlassen eines Parkplatzes
    public void doPost_leaveParkingLot(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        String String_id = request.getParameter("id");//Parkplatz ID
        ParkService p = (ParkService)getServletContext().getAttribute("Parkplätze");
        HashMap<Integer,Ticket>  TagesTicketList = (HashMap<Integer,Ticket>)getServletContext().getAttribute("TicketIDs");
        HashMap<String, MonthTicket> MonatsTicketList = (HashMap<String, MonthTicket>)getServletContext().getAttribute("MTIDs");
        p.leave(String_id, TagesTicketList, MonatsTicketList);
        includeJSP(request,response,"/ParkingLotOverview.jsp");
    }
    //-------------------------------------//
    public void doGet_CurrentTime(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDateTime currentTime = (LocalDateTime)getServletContext().getAttribute("zeit");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = currentTime.format(formatter);
        request.setAttribute("currentTime",formatDateTime);
        includeJSP(request,response,"/time.jsp");
    }
    public void updateTime(LocalDateTime zeitpunkt){
        //Ist der angegebene Zeitpunkt gültig?
        LocalDateTime d = (LocalDateTime) context.getAttribute("zeit");
        if (zeitpunkt.isBefore(d)){
            throw new IllegalStateException("Raum-Zeit-Kontinuum verletzt");
        }
        //Aktualisiere die Zeit im Parkhaus
        context.setAttribute("zeit", zeitpunkt);
    }
    public void doPost_Reset(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //context.setAttribute("zeit",startzeit);
        init();
        includeJSP(request,response,"/time.jsp");
    }
    public void doPost_PruefePasswort(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        PrintWriter out = response.getWriter();
        String enteredPassword = request.getParameter("password");
        if (enteredPassword.equals(password)) {
            includeJSP(request,response,"/betreiber.jsp");
        } else {
            out.println("falsches Passwort");
        }
    }
    public void doPost_Enter(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        //gibt es noch freie Parkplätze
        if(StartConfig.getInstance().getFrei() <= 0) {
            throw new IllegalArgumentException("Es gibt keine freien Parkplätze mehr");
        }
        Ticket ticket = new Ticket();
        //Ticket wird erstellt und State wird gesetzt
        //in dem State "Unregistered" kommt das Ticket in die HashMap, die Zeiten werden
        //überprüft und die Zeitstempel werden gesetzt -> ist das schon Teil des State-Patterns?
        ticket.setState(new SingleTicketCreated(context, ticket));
        ticket.getState().enter(request, response);
    }
    public void doPost_EnterMT(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        HashMap<String, MonthTicket> mtIDList = (HashMap<String, MonthTicket>) context.getAttribute("MTIDs");
        HashMap<String, Ticket> idList = (HashMap<String, Ticket>)context.getAttribute("MTIDs_to_IDs");
        String id = request.getParameter("MTID");
        LocalDate d = ((LocalDateTime) context.getAttribute("zeit")).toLocalDate();
        //Überprüfen, ob MT gültig ist:
        if (!mtIDList.containsKey(id)){
            throw new IllegalArgumentException("Dieses Ticket existiert nicht.");
        } else if (!idList.get(id).getState().toString().equals("Archived")) {
            throw new IllegalStateException("Bitte verlassen Sie das Parkhaus mit Ihrem Tagesticket mit der ID " + idList.get(id).getTicketID());
        }
        mtIDList.get(id).isValid(d, context); //Die restlichen Prüfungen werden vom MT durchgeführt
        mtIDList.get(id).setIsInPH(); //Variable isInPH wird im entsprechenden MT auf true gesetzt,
        startConf.dec(); //Reduziere die Anzahl freier Parkplätze
        includeJSP(request,response,"/parkhaus.jsp");
    }
    public void doPost_Leave(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        HashMap<Integer, Ticket> singleTicketList = (HashMap<Integer,Ticket>)getServletContext().getAttribute("TicketIDs");
        String id_String = request.getParameter("ticketID");
        Integer id = Integer.parseInt(id_String);
        Ticket ticket = singleTicketList.get(id);
        if(ticket == null) {
            throw new IllegalArgumentException("Das Ticket existiert nicht");
        }
        ticket.getState().leave(request, response);
        System.out.println("Zeit: "+context.getAttribute("zeit"));
    }
    public void doPost_LeaveMT(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        HashMap<String, MonthTicket> mtIDList = (HashMap<String, MonthTicket>)context.getAttribute("MTIDs");
        LocalDate d = ((LocalDateTime) context.getAttribute("zeit")).toLocalDate();
        String id = request.getParameter("ticketID");
        if (!mtIDList.containsKey(id)) {
            throw new IllegalArgumentException("Dieses Ticket existiert nicht.");
        } else if (!mtIDList.get(id).getIsInPH()) {
            throw new IllegalStateException("Sie müssen sich im Parkhaus befinden, um es zu verlassen. Falls Sie ein normales Ticket besitzen, zeigen Sie bitte dieses vor, um das Parkhaus zu verlassen.");
        }
        mtIDList.get(id).isValid(d, context); //Die restlichen Prüfungen werden vom MT durchgeführt
        mtIDList.get(id).setIsInPH(); //Variable isInPH wird im entsprechenden MT auf false gesetzt
        startConf.inc(); //Erhöhe die Anzahl freier Parkplätze
        includeJSP(request,response,"/parkhaus.jsp");
    }
    public void doPost_PayExtraMTStep1(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        System.out.println("Pay MT");
        HashMap<String, MonthTicket> mtIDList = (HashMap<String, MonthTicket>)context.getAttribute("MTIDs");
        LocalDate d = ((LocalDateTime) context.getAttribute("zeit")).toLocalDate();
        String id = request.getParameter("ID");
        //"Existiert das angegebene Monatsticket?"
        if (!mtIDList.containsKey(id)){
            throw new IllegalArgumentException("Dieses Ticket existiert nicht.");
        }
        //Berechne die Gebühr anhand der überzogenen Stunden (Pro Stunde 1€)
        long exceededHours = 24 * java.time.temporal.ChronoUnit.DAYS.between(mtIDList.get(id).getenddate(), d) - 24 + ((LocalDateTime) context.getAttribute("zeit")).getHour();
        if (exceededHours <= 0){
            throw new IllegalStateException("Das Ticket ist noch nicht verfallen.");
        }
        request.setAttribute("amount", exceededHours);
        request.setAttribute("ID", id);
        forwardToJSP(request,response,"/payExtraMT.jsp");
    }
    public void doPost_PayExtraMTStep2(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        System.out.println("Pay MT Step 2");
        //System.out.println("amount "+request.getParameter("amount"));
        //Addiere den Betrag auf das Gesamteinkommen
        income = (double) context.getAttribute("income");
        income += Integer.parseInt(request.getParameter("amount"));
        HashMap<String, MonthTicket> mtIDList = (HashMap<String, MonthTicket>) context.getAttribute("MTIDs");
        String id = request.getParameter("ID");
        //mtIDList.get(id).setPenalty();
        if (!mtIDList.get(id).getPenalty()) {
            mtIDList.get(id).setPenalty(); //Nachzahlung erforderlich
        }
        mtIDList.get(id).setPenaltyTime(((LocalDateTime) context.getAttribute("zeit")).plusMinutes(15)); //15 Minuten, um das Parkhaus zu verlassen
        System.out.println("Strafzeit"+mtIDList.get(id).getPenaltyTime());
        context.setAttribute("income", income);
        includeJSP(request,response,"/parkhaus.jsp");
    }
    public void doPost_StdPreisaendern(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        Administration ad = (Administration) getServletContext().getAttribute("administration");
        float newprice = Float.parseFloat(request.getParameter("newstd"));
        ad.setPrice(newprice);
        includeJSP(request,response,"/betreiber.jsp");
    }
    public void doPost_MTPreisaendern(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        Administration ad = (Administration) getServletContext().getAttribute("administration");
        float newprice = Float.parseFloat(request.getParameter("newmt"));
        ad.setMonthPrice(newprice);
        includeJSP(request,response,"/betreiber.jsp");
    }
    public void doPost_Monatsticketziehen(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        MonthTicket mt = new MonthTicket(LocalDate.parse(request.getParameter("datum")));
        HashMap<String, MonthTicket> mtIDList = (HashMap<String, MonthTicket>) context.getAttribute("MTIDs");
        mtIDList.put(mt.getID(), mt);
        context.setAttribute("MTIDs",mtIDList);
        //Einkommen aktualisieren
        income += Administration.getCurrentMonthPrice();
        context.setAttribute("income",income);
        HashMap<String, Ticket> idlist = (HashMap<String, Ticket>) context.getAttribute("MTIDs_to_IDs");
        HashMap<Integer, Ticket> TicketIDList = (HashMap<Integer, Ticket>) context.getAttribute("TicketIDs");
        Integer ticketID = Integer.parseInt(request.getParameter("ID"));
        idlist.put(mt.getID(),TicketIDList.get(ticketID));
        context.setAttribute("MTIDs",mtIDList);
        context.setAttribute("MTIDs_to_IDs",idlist);
        request.setAttribute("message", "Das Monatsticket mit der ID "+mt.getID()+" wurde erzeugt.");
        request.setAttribute("ID", ticketID);
        includeJSP(request,response,"/enterSingleTicket.jsp");
    }
    public void doGet_Income(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        Statistics stats = Statistics.getInstance();
        HashMap<Integer, Ticket> ticketIdList = stats.getTicketList(); // FIXME Die Tickets werden aus Hashmap genommen brauche aber alle oder eine wo nur Archived drin sind

        // ServletConxt Attribute setzten
        request.setAttribute("incomeList", Statistics.linkIncomeWithDate(ticketIdList,"Tag"));
        request.setAttribute("customerAndFloorList", Statistics.linkCustomersAndFloor(ticketIdList, "Tag"));

        //weiterleiten an die Stats.jsp
        System.out.println(context.getAttribute("income"));
        RequestDispatcher rd = context.getRequestDispatcher("/Stats.jsp");
        rd.forward(request,response);
    }
    public ServletContext getServletContext(){
        if (this.context == null){
            context = getServletConfig().getServletContext();
        }
        return this.context;
    }
    protected void includeJSP(HttpServletRequest request, HttpServletResponse response, String jspPath) throws ServletException, IOException {
        RequestDispatcher rd = context.getRequestDispatcher(jspPath);
        rd.include(request, response);
    }
    protected void forwardToJSP(HttpServletRequest request, HttpServletResponse response, String jspPath) throws ServletException, IOException {
        RequestDispatcher rd = context.getRequestDispatcher(jspPath);
        rd.forward(request, response);
    }
}