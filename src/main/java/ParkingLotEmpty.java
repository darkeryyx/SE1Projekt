public class ParkingLotEmpty extends ParkingLotState{
    protected ParkingLotEmpty(ParkingLot parkingLot) {
        super(parkingLot);
    }
    @Override
    public ParkingLotState leave(TicketIF ticket) {
        throw new IllegalArgumentException("Der Parkplatz mit der ID " +  parkingLot.getId() + " ist leer");
    }
    public ParkingLotState occupy(TicketIF ticketIF) {
        if (ticketIF instanceof Ticket) {
            return occupySingleTicket((Ticket) ticketIF);
        } else { /*
            Fallunterscheidung, da die Tickets nicht wirklich miteinander kompatibel sind
            und das Tagesticket/Ticket ein State-Pattern hat und das Monatsticket ohne State Patter
            implementiert ist */
            return occupyMonthlyTicket((MonthTicket) ticketIF);
        }
    }

    private ParkingLotState occupySingleTicket(Ticket ticket) {
        //schreibt Etage auf das Ticket und verschiebt den State von nicht geparkt weiter
        ticket.getState().assignParkingLotToTicket(this.parkingLot.getLevel());
        //schreibt TicketID auf den Parkplatz (für die Dauer des Parkens)
        this.parkingLot.setTicketId(String.valueOf(ticket.getTicketID()));
        return new ParkingLotOccupied(parkingLot);
    }

    private ParkingLotState occupyMonthlyTicket(MonthTicket ticket) {
        if(!ticket.getIsInPH()) {
            throw new IllegalArgumentException("Das Ticket mit der ID " + ticket.getID() + " befindet sich nicht im Parkhaus");
        }
        if(ticket.isCurrentlyInParkingLot()) {
            throw new IllegalArgumentException("Das Ticket mit der ID " + ticket.getID() + " befindet schon auf einem Parkplatz");
        }
        ticket.setCurrentlyInParkingLot(true);
        this.parkingLot.setTicketId(ticket.getID());

        return new ParkingLotOccupied(parkingLot);
    }
    @Override
    public boolean isOccupied() {
        return false;
    }
}

