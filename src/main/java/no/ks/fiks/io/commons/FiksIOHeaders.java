package no.ks.fiks.io.commons;

import lombok.NonNull;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class FiksIOHeaders {
    public static final String AVSENDER_ID = "avsender-id";
    public static final String MELDING_ID = "melding-id";
    public static final String AVSENDER_NAVN = "avsender-navn";
    public static final String MELDING_TYPE = "type";
    public static final String DOKUMENTLAGER_ID = "dokumentlager-id";
    public static final String SVAR_PA_MELDING_ID = "svar-til";
    public static final String SVAR_PA_MELDING_TYPE = "svar-til-type";
    public static final String KONTO_QUEUE_NAME_PREFIX = "fiksio.konto.";
    public static final String EGENDEFINERT_HEADER_PREFIX = "egendefinert-header.";
    public static final String EGENDEFINERT_KLIENT_KORRELASJONSID = "klientKorrelasjonsId";
    public static final String EGENDEFINERT_KLIENT_MELDINGID = "klientMeldingId";

    public static String getKontoQueueName(@NonNull UUID kontoId){
        return KONTO_QUEUE_NAME_PREFIX + kontoId;
    }
    public static String getEgendefinertHeaderName(@NonNull String navn) {
        return EGENDEFINERT_HEADER_PREFIX + navn;
    }

    public static UUID extractKontoId(@NonNull String kontoQueueName){
        try {
            return UUID.fromString(kontoQueueName.replace(KONTO_QUEUE_NAME_PREFIX, ""));
        } catch (Exception e){
            throw new IllegalArgumentException(String.format("Feil under konvertering av queue-navn \"%s\" til konto-id, queue-navn må følge standarden %s<queue-navn>", kontoQueueName, KONTO_QUEUE_NAME_PREFIX), e);
        }
    }

    public static Map<String, String> extractEgendefinerteHeadere(Map<String, Object> headers) {
        return headers.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(EGENDEFINERT_HEADER_PREFIX))
                .collect(Collectors.toMap(e -> e.getKey().substring(EGENDEFINERT_HEADER_PREFIX.length()), v -> v.getValue().toString()));
    }
}
