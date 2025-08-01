# fiks-io-commons
Modeller for beskrivelse av melding-metadata i FIKS IO

Standard branch er `main`

## Egendefinerte headere
Hvis klientene legger til egen header så vil disse få prefixen `EGENDEFINERT_HEADER_PREFIX = "egendefinert-header."`

Fiks IO klientene for Java og .NET har støtte for at klientene kan bruke 2 predefinerte egendefinerte headere ved behov:

- klientMeldingId
- klientKorrelasjonsid

Begge disse vil få prefix i selve Fiks IO meldingene, men blir pakket inn og ut igjen med prefix av vårt bibliotek, slik at de klientene ser bare headeren de puttet inn.

### klientMeldingId
Denne kan brukes av klienten som en unik id de kan sette selv. Den brukes for å vise tydelig at man sender en melding på nytt.
Da gjenbruker man `klientMeldingId` i meldingen man sender på nytt. Mottaker kan da se at dette er en duplikat.

### klientKorrelasjonsId
Denne kan brukes av klienten som en unik id de kan sette selv og som følger hele dialogen. Når klientene for Java og .NET ser denne headeren vil de kopiere den inn igjen i svar-meldingen.
DLQ meldinger vil også kopiere denne inn i feilmeldinger man får tilbake fra Fiks IO. Dette gjør at avsender av første melding kan sette en korrelasjonsid selv ved behov.

