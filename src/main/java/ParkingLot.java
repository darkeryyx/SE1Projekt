public class ParkingLot {
    private String id;
    //Etage + Kennung auf der Etage 0A -> Erdgeschoss Parkplatz A
    private Character levelId;
    //Parkplatz bekommt Buchstabe auf Etage, auf jeder Etage wird bei A angefangen
    private Integer level; //Etage: 0 = Erdgeschoss
    private String ticketId; //TicketID, die den Parkplatz belegt, wird bei Verlassen null gesetzt
    private ParkingLotState state;

    public ParkingLot(Integer level, Character levelId) {
        this.id = level + levelId.toString();
        this.levelId = levelId;
        this.level = level ;
        state = new ParkingLotEmpty(this);
    }

    public void leave(TicketIF ticket) {
        state = state.leave(ticket);
    }

    public void occupy(TicketIF ticket) {
        state = state.occupy(ticket);
    }

    public String getId() {
        return id;
    }

    public Character getLevelId() {
        return levelId;
    }

    public Integer getLevel() {
        return level;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public ParkingLotState getState() {
        return state;
    }

    //ist der Parkplatz belegt, Abfrage könnte man auch über instanceof state machen
    public boolean isOccupied() {
        return state.isOccupied();
    }
}
