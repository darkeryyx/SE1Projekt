public class ParkingLotOccupied extends ParkingLotState{
    protected ParkingLotOccupied(ParkingLot parkingLot) {
        super(parkingLot);
    }

    @Override
    public ParkingLotState leave(TicketIF ticketIF) {
        if(ticketIF instanceof Ticket) {
            Ticket ticket = (Ticket) ticketIF;
            //entfernt Ticket von Parkplatz, setzt den State in TicketStatePatter weiter
            ticket.getState().removeTicketFromParkingLot();
        } else {
            MonthTicket ticket = (MonthTicket) ticketIF;
            ticket.setCurrentlyInParkingLot(false);
        }
        //entfernt die TicketID vom Parkplatz
        parkingLot.setTicketId(null);
        return new ParkingLotEmpty(parkingLot);
    }

    @Override
    public ParkingLotState occupy(TicketIF ticket) {
        throw new IllegalStateException("Parkplatz mit ID " + parkingLot.getId() + " ist belegt");
    }
    @Override
    public boolean isOccupied() {
        return true;
    }
}
