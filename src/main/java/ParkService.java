import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ParkService  {
    private static ParkService instance;
    private final int parkingLotsPerLevel = 5;
    //Parkplätze pro Etage

    private final int levels = 2;
    //kann nur eine einstellige Zahl sein

    private final ArrayList<ParkingLot> parkingLotList;
    //Liste die alle Parkplätze verwaltet

    private ParkService() {
        parkingLotList = new ArrayList<>();
        char c = 'A'; //Auf jeder Ebene erhalten die Parkpätze einen Buchstaben, jeweils angefangen bei A
        for(int i = 0; i < parkingLotsPerLevel*levels; i++) {
            Integer ebene = i/parkingLotsPerLevel; // es gibt 5 Plätze pro Ebene, Ebene 0 ist das Erdgeschoss
            Character id = (char) (c+ i% parkingLotsPerLevel); //A + 1 = B... neue Ebene fängt wegen Modulo wieder bei A + 0 an
            ParkingLot p = (new ParkingLot(ebene,id));
            parkingLotList.add(p);
        }
    }

    public static ParkService getInstance() {
        if (instance == null) {
            instance = new ParkService();
        }
        return instance;
    }
    public static ParkService resetInstance() {
        instance = new ParkService();
        return instance;
    }

    //Methoden für einparken und ausparken
    public void occupy(String parkingLotId_string,String ticketId_string, String mticket_Id_String, HashMap<Integer,Ticket> TagesTicketList, HashMap<String, MonthTicket> MonatsTicketList) {
        //aus dem der ID den Index in der Liste bestimmen
        TicketIF ticket = getTicketFromId(mticket_Id_String, ticketId_string, TagesTicketList, MonatsTicketList);
        Integer parkingLotIndex = calculateIndexFromString(parkingLotId_string);
        ParkingLot parkingLot = parkingLotList.get(parkingLotIndex);
        parkingLot.occupy(ticket);
    }

    public void leave(String parkingLotId, HashMap<Integer,Ticket> TagesTicketList, HashMap<String, MonthTicket> MonatsTicketList) {
        int index = calculateIndexFromString(parkingLotId);
        ParkingLot parkingLot = parkingLotList.get(index);
        TicketIF ticket = getTicketFromId(parkingLot.getTicketId(), parkingLot.getTicketId(), TagesTicketList, MonatsTicketList);
        parkingLotList.get(index).leave(ticket);
    }

    private TicketIF getTicketFromId(String monthlyTicketId_String, String ticketId_string, HashMap<Integer,Ticket> TagesTicketList, HashMap<String, MonthTicket> MonatsTicketList) {
        //Ticket aus einer der beiden Listen holen und auf Existenz prüfen
        //Tagesticket != null -> in TagesTicketList schauen, sonst in MonatsTicketList
        TicketIF ticket = MonatsTicketList.get(monthlyTicketId_String) != null ?
                MonatsTicketList.get(monthlyTicketId_String) :
                TagesTicketList.get(ticketId_string.equals("") ? null : Integer.parseInt(ticketId_string));
                //Fall abfangen, dass ticketId leer ist

        //TicketID für die Ausgabe setzen
        String ticket_id = ticketId_string.equals("") ? monthlyTicketId_String : ticketId_string;
        if(ticket_id.equals("")) {
            throw new IllegalArgumentException("keine TicketID angegeben");
        }
        if(ticket == null) {
            throw new IllegalArgumentException("Ticket mit der ID " + ticket_id + " existiert nicht");
        }
        return ticket;
    }

    //berechnet ListenIndex aus String, Rechnung wie in Konstruktor
    private Integer calculateIndexFromString(String id_string) {
        int level = Integer.parseInt(id_string.substring(0,1));
        char levelId = id_string.charAt(1);
        return parkingLotsPerLevel * level + ((levelId - 'A'));
    }

    //Methode soll nur zum Testen genutzt werden
    public ParkingLot getParkingLotById(String id) {
        char parkingLotId = id.charAt(1);
        if('A' > parkingLotId || parkingLotId >= ('A' + parkingLotsPerLevel)) {
            throw new IllegalArgumentException("Der Parkplatz mit der ID existiert nicht");
        }
        int level = id.charAt(0) - '0';
        if(0 > level || level >= levels) {
            throw new IllegalArgumentException("Der Parkplatz mit der ID existiert nicht");
        }
        int index = calculateIndexFromString(id);
        return parkingLotList.get(index);
    }

//Methoden, die die Parkingplatzliste nach verschiedenen Kriterien filtern, wenn no usage werden die nur von den JSP gebraucht

    //gibt die Etagen in einer Liste wieder
    public List<Integer> getLevelsAsList() {
        int j = 0;
        ArrayList<Integer> levelsAsList = new ArrayList<>();
        for(int i = 0; i < parkingLotList.size(); i += parkingLotsPerLevel) {
            levelsAsList.add(j++);
        }
        System.out.println(levelsAsList);
        return levelsAsList;
    }

    /*returned alle leeren Parkplätze für eine Etage
    * bisher nicht gebraucht
    * */

    /*returned alle Parkplätze für eine Etage bisher nicht gebraucht */
    public List<ParkingLot> getParkingLotsByLevel(String string_level) {
        return  parkingLotList.stream()
                .filter(ParkingLot -> ParkingLot.getLevel() == Integer.parseInt(string_level))
                .collect(Collectors.toList());
    }

    /*returned alle belegten Parkplätze in JSP gebraucht*/
    public List<ParkingLot> occupiedParkingLots() {
        return  parkingLotList.stream()
                .filter(ParkingLot ->  ParkingLot.isOccupied())
                .collect(Collectors.toList());
    }

    //returned alle Parkplätze
    public List<ParkingLot> getParkingLotList() {
        return parkingLotList;
    }


    /*returned den ersten freien Parkplatz, angefangen bei 0A
    * oder null, wenn es keinen freien Parkplatz gibt*/
    public ParkingLot GetFirstEmptyParkingLot()  {
        return parkingLotList.stream()
                .filter(ParkingLot -> (!ParkingLot.isOccupied()))
                .findAny().orElse(null);
    }

}
