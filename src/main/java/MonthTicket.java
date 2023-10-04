import javax.servlet.ServletContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MonthTicket extends TicketAbs {

    private final String id; //ID zur Identifizierung/Unterscheidung der einzelnen MTs (Muster: 'MT' + Zahl)
    private final LocalDate startdate; //Datum, ab dem das MT gilt
    private final LocalDate enddate; //Datum, ab dem das MT nicht mehr gilt
    private static int counter = 0; //Anzahl der MTs
    private boolean penalty; //True, wenn das MT ungültig, aber noch im PH ist
    private LocalDateTime penaltyTime; //Der Zeitpunkt, bis zu dem das PH verlassen werden muss
    private boolean isInPH = false; //Besitzer des Tickets im PH?
    private boolean currentlyInParkingLot = false;  //befindet sich das Ticket aktuell auf einem Parkplatz?
    public MonthTicket(LocalDate ld){
        startdate = ld;
        enddate = startdate.plusDays(30); //Das MT ist für 30 Tage gültig
        priceStamp = Administration.getCurrentMonthPrice();
        id = "MT" + ++counter;
    }
    public String getID() {return id;}
    public static void resetCounter(){counter = 0;}
    public LocalDate getstartdate() {return startdate;}
    public LocalDate getenddate() {return enddate;}
    public void isValid(LocalDate datum, ServletContext context) {
        if (datum.isAfter(getenddate()) && getIsInPH()) {
            //Falls die Strafe bereits bezahlt wurde:
            if (getPenalty()) {
                if (getPenaltyTime().isBefore((LocalDateTime) context.getAttribute("zeit"))) {
                    throw new IllegalStateException("Die 15 Minuten wurden überschritten. Bitte bezahlen Sie am Automaten die fällige Gebühr.");
                } else {
                    return; //Das PH kann verlassen werden
                }
            }
            //Berechne die Gebühr anhand der überzogenen Stunden (Pro Stunde 1€)
            long exceededHours = 24 * java.time.temporal.ChronoUnit.DAYS.between(getenddate(), datum) - 24 + ((LocalDateTime) context.getAttribute("zeit")).getHour();
            throw new IllegalStateException("Das Monatsticket ist nicht mehr gültig. Bitte bezahlen Sie am Automaten eine Gebühr von " + exceededHours + " €");
        } else if (datum.isAfter(getenddate())){
            throw new IllegalStateException("Das Monatsticket ist nicht mehr gültig.");
        } else if (datum.isBefore(getstartdate())) {
            throw new IllegalStateException("Das Monatsticket ist noch nicht gültig.");
        } else if (isCurrentlyInParkingLot()) {
            throw new IllegalStateException("Sie müssen ausparken, bevor Sie das Parkhaus verlassen können");
        }
    }

    public void setIsInPH() {isInPH = !isInPH;}
    public boolean getIsInPH() {return isInPH;}
    public int getFloor(){
        return -1;
    }
    public void setFloor(int floor){}

    public boolean isCurrentlyInParkingLot() {
        return currentlyInParkingLot;
    }
    public void setCurrentlyInParkingLot(boolean currentlyInParkingLot) {
        this.currentlyInParkingLot = currentlyInParkingLot;
    }
    public void setPenalty(){
        penalty = !penalty;
    }
    public boolean getPenalty(){
        return penalty;
    }
    public void setPenaltyTime(LocalDateTime pt){
        penaltyTime = pt;
    }
    public LocalDateTime getPenaltyTime(){
        return penaltyTime;
    }
}