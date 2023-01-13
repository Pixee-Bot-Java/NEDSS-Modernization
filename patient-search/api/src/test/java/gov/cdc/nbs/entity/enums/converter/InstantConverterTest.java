package gov.cdc.nbs.entity.enums.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class InstantConverterTest {
    private final InstantConverter converter = new InstantConverter();

    @ParameterizedTest
    @CsvSource({
            "1/1/2022,1,1,2022",
            "12/30/22,12,30,2022",
            "12/30/2022,12,30,2022"
    })
    void shortFormat(String dateString, int month, int day, int year) {
        var instant = converter.read(dateString);
        assertNotNull(instant);
        var ld = LocalDate.ofInstant((Instant) instant, ZoneId.of("UTC"));
        assertEquals(month, ld.getMonthValue());
        assertEquals(day, ld.getDayOfMonth());
        assertEquals(year, ld.getYear());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2022-11-18 22:27:13.302",
            "2022-11-18 22:27:13.83",
            "2022-11-18 22:27:13.8",
            "2022-11-18T22:27:13.317",
            "2022-11-18T22:27:13"
    })
    void longFormat(String dateString) {
        var instant = converter.read(dateString);
        assertNotNull(instant);
        var ld = LocalDateTime.ofInstant((Instant) instant, ZoneId.of("UTC"));
        assertEquals(11, ld.getMonthValue());
        assertEquals(18, ld.getDayOfMonth());
        assertEquals(2022, ld.getYear());
        assertEquals(22, ld.getHour());
        assertEquals(27, ld.getMinute());
        assertEquals(13, ld.getSecond());
    }

    @Test
    void testWrite() {
        var now = Instant.now();
        var output = converter.write(now);
        assertNotNull(output);
    }

    @Test
    void readOutput() {
        // write value as String
        var now = Instant.now();
        var output = converter.write(now);
        assertNotNull(output);
        // parse String back to Instant
        var instant = (Instant) converter.read(output);
        assertNotNull(instant);

        // Verify Instant matches original
        LocalDateTime original = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
        LocalDateTime parsed = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

        assertEquals(original.getDayOfYear(), parsed.getDayOfYear());
        assertEquals(original.getHour(), parsed.getHour());
        assertEquals(original.getHour(), parsed.getHour());
        assertEquals(original.getMinute(), parsed.getMinute());
        assertEquals(original.getSecond(), parsed.getSecond());
    }
}
