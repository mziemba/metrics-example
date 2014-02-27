import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{dd/MMM HH:mm:ss.SSS} %-5level [%thread] %logger{0}:%line -> %msg%n"
    }
}
root(DEBUG, ["CONSOLE"])