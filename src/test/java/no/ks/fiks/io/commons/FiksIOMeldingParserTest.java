package no.ks.fiks.io.commons;

import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FiksIOMeldingParserTest {

    @DisplayName("Leser MottattMeldingMetadata fra envelope og properties")
    @Test
    void parse() {

        final UUID mottakerKonto = UUID.randomUUID();
        final UUID meldingId = UUID.randomUUID();
        final UUID avsenderKonto = UUID.randomUUID();
        final UUID svarPaMeldingId = UUID.randomUUID();
        final long experiation = 10_000L;
        final String meldingType = "meldingType";
        final String egenHeaderNavn = "egenHeader";

        final Envelope envelope = new Envelope(Long.MAX_VALUE, true, "exchange", FiksIOHeaders.KONTO_QUEUE_NAME_PREFIX + mottakerKonto.toString());
        final AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().appId("appId")
                .headers(ImmutableMap.<String, Object>builder()
                        .put(FiksIOHeaders.MELDING_ID, meldingId.toString())
                        .put(FiksIOHeaders.MELDING_TYPE, meldingType)
                        .put(FiksIOHeaders.AVSENDER_ID, avsenderKonto.toString())
                        .put(FiksIOHeaders.SVAR_PA_MELDING_ID, svarPaMeldingId.toString())
                        .put(FiksIOHeaders.EGENDEFINERT_HEADER_PREFIX + egenHeaderNavn, "EgenVerdi")
                        .build())
                .expiration(Long.toString(experiation))
                .build();


        final MottattMeldingMetadata mottattMeldingMetadata = FiksIOMeldingParser.parse(new GetResponse(envelope, properties, new byte[]{}, 1));

        assertNotNull(mottattMeldingMetadata);
        assertEquals(mottakerKonto, mottattMeldingMetadata.getMottakerKontoId());
        assertEquals(avsenderKonto, mottattMeldingMetadata.getAvsenderKontoId());
        assertEquals(meldingId, mottattMeldingMetadata.getMeldingId());
        assertEquals(svarPaMeldingId, mottattMeldingMetadata.getSvarPaMelding());
        assertEquals(meldingType, mottattMeldingMetadata.getMeldingType());
        assertEquals(envelope.getDeliveryTag(), mottattMeldingMetadata.getDeliveryTag().longValue());
        assertEquals(experiation, mottattMeldingMetadata.getTtl().longValue());
        assertEquals(envelope.isRedeliver(), mottattMeldingMetadata.isResendt());
        assertNotNull(mottattMeldingMetadata.getHeadere().get(egenHeaderNavn));

    }

    @DisplayName("Mangler påkrevd header")
    @Test
    void manglerHeader() {
        assertThrows(RuntimeException.class,
                () -> FiksIOMeldingParser.parse(
                        new Envelope(Long.MAX_VALUE, false, "exhange", FiksIOHeaders.KONTO_QUEUE_NAME_PREFIX + UUID.randomUUID().toString()),
                        new AMQP.BasicProperties.Builder().headers(Collections.emptyMap()).build()));

    }
}