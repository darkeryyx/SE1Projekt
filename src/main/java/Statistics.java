import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Statistics {

    private static Statistics instance = new Statistics();
    private  HashMap<Integer, Ticket> ticketList;

    private Statistics() {
        ticketList = new HashMap<>();
    }

    public void setTicketList(Integer key, Ticket value){
        ticketList.put(key,value);
    }

    public HashMap<Integer,Ticket> getTicketList(){
        return ticketList;
    }
    public static Statistics getInstance(){
        return instance;
    }


    /*
    * Soll dafür sorgen, die Einnahmen passend zu den Tagen oder Monaten
    * auflisten zu können
    * */
    public static List<String> linkIncomeWithDate(HashMap<Integer, Ticket> tickets, String format){
        Map<String, Double> linkedIncome = new HashMap<>(); // Neue Hashmap Key = Datum, value = Gesamteinnahmen pro Zeitraum
        DateTimeFormatter dateFormatter = null; // Formatierung der Zeitdarstellung. Bestimmt ob Monats oder Tages einnahmen berechnet werden
        if(format.equals("Tag")){
            dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if(format.equals("Monat")){
            dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        }


        for(Ticket ticket : tickets.values()){ // geht durch alle vorhanden Tickets
            if(ticket.getState().toString().equals("Archived")){
                // Datum und gezahlten Preis zwischen Speichern
                String date = ticket.getEndDate().format(dateFormatter);
                double price = ticket.getPriceStamp();

                // Wenn das Datum der Tickets gleich ist → gezahlten Preise addieren
                if(linkedIncome.containsKey(date)){
                    double totalPrice = linkedIncome.get(date) + price;
                    linkedIncome.put(date,totalPrice);
                } else {
                    linkedIncome.put(date,price);
                }
            }

        }

        List<String> resultList = linkedIncome.entrySet().stream().
                map(entry -> "Datum: " + entry.getKey() + ", Gesamteinnahmen: " + entry.getValue())
                .collect(Collectors.toList());

        return resultList;
    }

    /*
     * Zählt die Autos, die im Parkhaus geparkt haben und aktuell Parken
     * und ordnet sie dem passenden Datum zu.
     * Die ausnutzung der Etagen kann mit bestimmt werden.
     */
    public static List<String> linkCustomersAndFloor(HashMap<Integer,Ticket> tickets, String format){
        Map<String, Integer[]> customersAndFloors = new HashMap<>(); // Key = Datum, values = Anzahl der Autos und Anzahl der pro Etage geparkten Wagen
        DateTimeFormatter dateFormatter;
        if(format.equals("Tag")){
            dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } else if(format.equals("Monat")){
            dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        } else {
            dateFormatter = null;
        }

        Integer[] values = {0,0,0}; // Codierung: Autos, Etage 1, Etage 2
        int[] floor;

        for(Ticket ticket : tickets.values()){

            if(ticket.getState().toString().equals("Archived")){

            String date = ticket.getEndDate().format(dateFormatter);

            // Prüft, ob das Auto in Etage 1 oder 2 geparkt hat (Momentan gibts noch keine Etagen)

            if(ticket.getFloor() != 1){
                floor = new int[]{1, 1, 0}; //erste Etage
            } else{
                floor = new int[]{1, 0, 1}; // zweite Etage
            }
            //Hier werden die Autos gezählt
            for (int i = 0; i < 3; i++) {
                values[i] = values[i] + floor[i];
            }
            customersAndFloors.put(date,values);
            }
        }

        List<String> resultList = customersAndFloors.entrySet().stream().
                map(entry -> {String date = entry.getKey();
                    Integer[] counts = entry.getValue();
                    return date + ": Autos, Etage A, Etage B: " + Arrays.toString(counts);}).
                collect(Collectors.toList());

        return resultList;
    }

}
