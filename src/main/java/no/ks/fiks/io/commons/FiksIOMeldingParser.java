package no.ks.fiks.io.commons;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class FiksIOMeldingParser {

    public static MottattMeldingMetadata parse(@NonNull GetResponse getResponse) {
        return parse(getResponse.getEnvelope(), getResponse.getProps());
    }

    public static MottattMeldingMetadata parse(@NonNull Envelope envelope, @NonNull AMQP.BasicProperties properties) {
        return MottattMeldingMetadata.builder()
                .meldingId(requireUUIDFromHeader(properties.getHeaders(), FiksIOHeaders.MELDING_ID))
                .meldingType(requireStringFromHeader(properties.getHeaders(), FiksIOHeaders.MELDING_TYPE))
                .avsenderKontoId(requireUUIDFromHeader(properties.getHeaders(), FiksIOHeaders.AVSENDER_ID))
                .mottakerKontoId(FiksIOHeaders.extractKontoId(envelope.getRoutingKey()))
                .svarPaMelding(getUUIDFromHeader(properties.getHeaders(), FiksIOHeaders.SVAR_PA_MELDING_ID).orElse(null))
                .deliveryTag(envelope.getDeliveryTag())
                .ttl(Optional.ofNullable(properties.getExpiration()).map(Long::valueOf).orElse(null))
                .headere(FiksIOHeaders.extractEgendefinerteHeadere(properties.getHeaders()))
                .resendt(envelope.isRedeliver())
                .build();
    }

    private static Optional<UUID> getUUIDFromHeader(Map<String, Object> headers, String header) {
        return getStringFromHeader(headers, header).map(UUID::fromString);
    }

    private static UUID requireUUIDFromHeader(Map<String, Object> headers, String header) {
        return getUUIDFromHeader(headers, header).orElseThrow(getMissingHeaderException(headers, header));
    }

    private static Optional<String> getStringFromHeader(Map<String, Object> headers, String header) {
        return Optional.ofNullable(headers.get(header)).map(Object::toString);
    }

    private static String requireStringFromHeader(Map<String, Object> headers, String header) {
        return getStringFromHeader(headers, header).orElseThrow(getMissingHeaderException(headers, header));
    }

    private static Supplier<RuntimeException> getMissingHeaderException(Map<String, Object> headers, String rabbitMqHeader) {
        return () -> new RuntimeException(String.format("Melding %s har mangler header %s, eller header verdi er ikke satt", getUUIDFromHeader(headers, FiksIOHeaders.MELDING_ID).orElse(null), rabbitMqHeader));
    }

}