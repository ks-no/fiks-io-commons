package no.ks.fiks.io.commons;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FiksIOHeadersTest {
    @Test
    void getKontoQueueName() {
        UUID kontoId = UUID.randomUUID();
        assertEquals("fiksio.konto." + kontoId, FiksIOHeaders.getKontoQueueName(kontoId));
    }

    @Test
    void extractKontoId() {
        UUID kontoId = UUID.randomUUID();
        assertEquals(kontoId, FiksIOHeaders.extractKontoId("fiksio.konto." + kontoId));
    }

}