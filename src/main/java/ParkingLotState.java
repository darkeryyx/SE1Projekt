public abstract class ParkingLotState {
    protected ParkingLot parkingLot;
    protected ParkingLotState(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public abstract ParkingLotState leave(TicketIF ticket);
    public abstract ParkingLotState occupy(TicketIF ticket);

    //ist der Parkplatz belegt?
    public abstract boolean isOccupied();

}
