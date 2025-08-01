package no.ks.fiks.io.commons;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void getEgendefinertHeaderName() {
        String name = UUID.randomUUID().toString();
        assertEquals(FiksIOHeaders.EGENDEFINERT_HEADER_PREFIX + name, FiksIOHeaders.getEgendefinertHeaderName(name));
    }

    @Test
    void getEgendefinertHeaderKlientKorrelasjonsid() {
        String name = FiksIOHeaders.EGENDEFINERT_KLIENT_KORRELASJONSID;
        assertEquals(FiksIOHeaders.EGENDEFINERT_HEADER_PREFIX + name, FiksIOHeaders.getEgendefinertHeaderName(name));
    }

    @Test
    void extractEgendefinerteHeadere() {
        Map<String, Object> headere = Map.of(
                FiksIOHeaders.getEgendefinertHeaderName("min_header"), "min_verdi",
                FiksIOHeaders.AVSENDER_ID, UUID.randomUUID().toString()
        );
        Map<String, String> egendefinerteHeadere = FiksIOHeaders.extractEgendefinerteHeadere(headere);
        assertEquals(1, egendefinerteHeadere.size());
        assertEquals("min_verdi", egendefinerteHeadere.get("min_header"));
    }

}