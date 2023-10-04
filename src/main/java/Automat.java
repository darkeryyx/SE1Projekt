/*
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Automat implements AutomatIF {

    private double eingenommenerBetrag = 0.0; //ein extra, wv automat insgesamt eingenommen hat
    private static double eingenommenerBetragMT; //Gesamtmenge an Geld, das durch den Verkauf von Monatstickets eingenommen wurde
    Scanner sc = new Scanner(System.in); //für user input

    //Konstruktor
    public Automat() {}

    public double getEingenommenerBetrag() {
        return eingenommenerBetrag;
    }
    public double getEingenommenerBetragMT() {
        return eingenommenerBetragMT;
    }

    public String bezahlen(Ticket ticket){
        String antwort; //antwort des users
        int zwei=0, ein=0,cent =0;
        //parkdauer holen
        LocalDateTime ende= LocalDateTime.now();
        //stunden berechnen
        int endeHour = ende.getHour();
        int beginnHour= ticket.getStartDate().getHour();

        double endpreis = ((endeHour-beginnHour)* Administration.getCurrentPrice()) + 2;

        System.out.println("Parkdauer: " + (endeHour-beginnHour)+" Stunden, zu bezahlen: " + endpreis );
        System.out.println("Bar oder mit Karte?");
        antwort = sc.nextLine();

        if(antwort.equals("Bar")){

            do{
                System.out.println("Bitte werfen Sie 'Zwei Euro', 'Ein Euro', oder '50 Cent' ein");
                antwort= sc.nextLine();
                if (antwort.equals("Zwei Euro")) {
                    zwei++;
                }else if(antwort.equals("Ein Euro")){
                    ein++;
                }else if(antwort.equals("50 Cent")){
                    cent++;
                }else{
                    System.out.println("ungueltiger Einwurf!");
                }
            }while(!antwort.equals(""));

            bar(ticket, endpreis,zwei,ein,cent);

        }else if(antwort.equals("Karte")){
            karte(ticket,endpreis);
        }

        if(!ticket.getTicketPaid()) {
            return "Ticket nicht bezahlt!";
        } else{
            return "Ticket bezahlt. Vielen Dank";
        }
    }
    private void karte(Ticket ticket, double preis){

        eingenommenerBetrag +=preis;
        ticket.setTicketPaid(true);
    }
    private void bar(Ticket ticket,double preis, int zweierMuenzen, int einerMuenzen, int cent) {

        double einwurf = (zweierMuenzen *2) + einerMuenzen + (cent * 0.50);

        if(einwurf <= 0) {
            System.out.println("ungueltiger Betrag");
            bezahlen(ticket);
            return;
        }

        if( einwurf < preis) {

            System.out.println("Einwurf: " +einwurf );
            System.out.println("Geldbetrag nicht ausreichend");
            rueckgeld(einwurf);

        }else {
            System.out.println();
            System.out.println("Einwurf: " +einwurf );
            ticket.setTicketPaid(true);
            rueckgeld(einwurf-preis);

            eingenommenerBetrag += preis;
        }
    }

    private void rueckgeld(double rueckgeld) {

        int counterzwei =0;
        int countereins = 0;
        int counterfuenfzig =0;

        while(rueckgeld >= 2) {

            counterzwei++;
            rueckgeld -= 2;
        }
        while (rueckgeld >= 1) {

            countereins++;
            rueckgeld -= 1;
        }
        while(rueckgeld >= 0.50) {

            counterfuenfzig++;
            rueckgeld -= 0.50;
        }

        System.out.println("Rueckgeld:");
        System.out.println(counterzwei + " x 2 Euro");
        System.out.println(countereins + " x 1  Euro");
        System.out.println(counterfuenfzig + " x 50 Cent");

    }
    public static MonthTicket monatsticketZiehen(LocalDate start){
        MonthTicket mt = new MonthTicket(start);
        eingenommenerBetragMT += Administration.getCurrentMonthPrice();
        return mt;
    }
}
 */
