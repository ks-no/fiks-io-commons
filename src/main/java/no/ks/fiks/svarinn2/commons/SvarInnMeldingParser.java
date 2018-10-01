package no.ks.fiks.svarinn2.commons;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import io.vavr.control.Option;
import lombok.NonNull;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class SvarInnMeldingParser {

    public static MottattMeldingMetadata parse(@NonNull GetResponse getResponse) {
        return parse(getResponse.getEnvelope(), getResponse.getProps());
    }

    public static MottattMeldingMetadata parse(@NonNull Envelope envelope, @NonNull AMQP.BasicProperties properties) {
        return MottattMeldingMetadata.builder()
                .meldingId(requireUUIDFromHeader(properties.getHeaders(), SvarInn2Headers.MELDING_ID))
                .meldingType(requireStringFromHeader(properties.getHeaders(), SvarInn2Headers.MELDING_TYPE))
                .avsenderKontoId(requireUUIDFromHeader(properties.getHeaders(), SvarInn2Headers.AVSENDER_ID))
                .mottakerKontoId(UUID.fromString(envelope.getRoutingKey()))
                .svarPaMelding(getUUIDFromHeader(properties.getHeaders(), SvarInn2Headers.SVAR_PA_MELDING_ID).getOrElse(() -> null))
                .deliveryTag(envelope.getDeliveryTag())
                .ttl(Long.valueOf(properties.getExpiration()))
                .build();
    }

    private static Option<UUID> getUUIDFromHeader(Map<String, Object> headers, String header) {
        return getStringFromHeader(headers, header).map(UUID::fromString);
    }

    private static UUID requireUUIDFromHeader(Map<String, Object> headers, String header) {
        return getUUIDFromHeader(headers, header).getOrElseThrow(getMissingHeaderException(headers, header));
    }

    private static Option<String> getStringFromHeader(Map<String, Object> headers, String header) {
        return Option.of(headers.get(header)).map(Object::toString);
    }

    private static String requireStringFromHeader(Map<String, Object> headers, String header) {
        return getStringFromHeader(headers, header).getOrElseThrow(getMissingHeaderException(headers, header));
    }

    private static Supplier<RuntimeException> getMissingHeaderException(Map<String, Object> headers, String rabbitMqHeader) {
        return () -> new RuntimeException(String.format("Melding %s har mangler header %s, eller header verdi er ikke satt", getUUIDFromHeader(headers, SvarInn2Headers.MELDING_ID).getOrElse(() -> null), rabbitMqHeader));
    }

}