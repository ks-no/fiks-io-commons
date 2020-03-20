package no.ks.fiks.io.commons;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class MottattMeldingMetadata {
    @NonNull private UUID meldingId;
    @NonNull private String meldingType;
    @NonNull private UUID avsenderKontoId;
    @NonNull private UUID mottakerKontoId;
    @NonNull private Long ttl;
    @NonNull private Long deliveryTag;
    private UUID svarPaMelding;
    private Map<String, String> headere;
    @Builder.Default
    private boolean resendt = false;
}