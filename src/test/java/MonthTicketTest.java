import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MonthTicketTest {

    MonthTicket mt;
    int month;
    LocalDate start;
    LocalDate gueltig;
    LocalDate ungueltig;
    LocalDate now;

    @BeforeEach
    void init(){
        now = LocalDate.parse("2023-05-10");
        gueltig = LocalDate.parse("2023-05-10");
        ungueltig = LocalDate.parse("2023-07-10");
        start = LocalDate.parse("2023-05-05");
        mt = new MonthTicket(start);
        LocalDate today = LocalDate.now();
        month = today.getMonthValue();
    }
    @Test
    void testPrice(){
        assertEquals(100f,mt.getPriceStamp());
    }
    @Test
    void testGueltig(){
        assertTrue(now.isAfter(mt.getstartdate()) || now.isEqual(mt.getstartdate()));
        assertTrue(mt.getstartdate().isBefore(ungueltig));
    }
}
