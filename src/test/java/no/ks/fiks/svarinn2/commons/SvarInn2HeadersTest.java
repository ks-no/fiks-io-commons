package no.ks.fiks.svarinn2.commons;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SvarInn2HeadersTest {
    @Test
    void getKontoQueueName() {
        UUID kontoId = UUID.randomUUID();
        assertEquals("fiksio.konto." + kontoId, SvarInn2Headers.getKontoQueueName(kontoId));
    }

    @Test
    void extractKontoId() {
        UUID kontoId = UUID.randomUUID();
        assertEquals(kontoId, SvarInn2Headers.extractKontoId("fiksio.konto." + kontoId));
    }

}