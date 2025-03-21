package no.ks.fiks.io.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MottattMeldingMetadataTest {

    @DisplayName("Sjekker at klassen er kompatibel med Jackson JSON biblioteket")
    @Test
    void jsonSerialization() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final MottattMeldingMetadata mottattMeldingMetadata = MottattMeldingMetadata.builder()
                .avsenderKontoId(UUID.randomUUID())
                .deliveryTag(Long.MAX_VALUE)
                .meldingId(UUID.randomUUID())
                .meldingType("type")
                .mottakerKontoId(UUID.randomUUID())
                .klientMeldingId(UUID.randomUUID())
                .klientKorrelasjonsid(UUID.randomUUID().toString())
                .mottakerKontoId(UUID.randomUUID())
                .resendt(true)
                .ttl(20000L)
                .svarPaMelding(UUID.randomUUID())
                .headere(Collections.emptyMap())
                .build();
        final String jsonString = objectMapper.writeValueAsString(mottattMeldingMetadata);
        assertNotNull(jsonString);

        final MottattMeldingMetadata deserializedObject = objectMapper.readValue(jsonString, MottattMeldingMetadata.class);
        assertEquals(mottattMeldingMetadata, deserializedObject);
    }
}