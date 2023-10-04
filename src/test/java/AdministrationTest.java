import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdministrationTest {

    Administration v;

    @BeforeEach
    void init(){
        v = Administration.getInstance();
    }
    @AfterEach
    void reset(){
        v = Administration.resetInstance();
    }

    @Test
    void onlyOneObjectOfAdministration(){
        v = Administration.getInstance();
        assertNotNull(v);
        //Ändere einen Wert
        v.setPrice(2.5f);
        //Die Änderung sollte beibehalten werden
        v = Administration.getInstance();
        assertEquals(2.5f, v.getCurrentPrice());
    }

    @Test
    @DisplayName("Ändert den aktuellen Stundenpreis und prüft, ob die Änderung korrekt ist.")
    void setPrice() {
        v.setPrice(0.5f);
        assertEquals(v.getCurrentPrice(), 0.5f);
    }
    @Test
    @DisplayName("Prüft, ob nur gültige Werte für das Tagesticket übergeben werden können.")
    void setPrice_throwIAE() {
        assertThrows(IllegalArgumentException.class, () -> v.setPrice(-10f));
        assertNotEquals(v.getCurrentPrice(), -10f);
    }

    @Test
    @DisplayName("Ändert den aktuellen Monatspreis und prüft, ob die Änderung korrekt ist.")
    void setMonthPrice() {
        v.setMonthPrice(20f);
        assertEquals(v.getCurrentMonthPrice(), 20f);
    }
    @Test
    @DisplayName("Prüft, ob nur gültige Werte für das Monatsticket übergeben werden können.")
    void setMonthPrice_throwIAE() {
        assertThrows(IllegalArgumentException.class, () -> v.setMonthPrice(-10f));
        assertNotEquals(v.getCurrentMonthPrice(), -10f);
    }
}