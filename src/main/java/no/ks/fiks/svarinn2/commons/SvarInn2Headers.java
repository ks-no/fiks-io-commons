package no.ks.fiks.svarinn2.commons;

import lombok.NonNull;

import java.util.UUID;

public final class SvarInn2Headers {
    public static final String AVSENDER_ID = "avsender-id";
    public static final String MELDING_ID = "melding-id";
    public static final String AVSENDER_NAVN = "avsender-navn";
    public static final String MELDING_TYPE = "type";
    public static final String DOKUMENTLAGER_ID = "dokumentlager-id";
    public static final String SVAR_PA_MELDING_ID = "svar-til";

    public static final String KONTO_QUEUE_NAME_PREFIX = "fiksio.konto.";

    public static String getKontoQueueName(@NonNull UUID kontoId){
        return KONTO_QUEUE_NAME_PREFIX + kontoId;
    }

    public static UUID extractKontoId(@NonNull String kontoQueueName){
        try {
            return UUID.fromString(kontoQueueName.replace(KONTO_QUEUE_NAME_PREFIX, ""));
        } catch (Exception e){
            throw new IllegalArgumentException(String.format("Feil under konvertering av queue-navn \"%s\" til konto-id, queue-navn må følge standarden %s<queue-navn>", kontoQueueName, KONTO_QUEUE_NAME_PREFIX), e);
        }
    }
}
