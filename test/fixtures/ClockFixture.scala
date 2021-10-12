package fixtures

import java.sql.Timestamp
import java.time.{Clock, Instant, LocalDate, LocalTime, ZoneId}

trait ClockFixture {

  def getClock(localDate: LocalDate): Clock =
    Clock.fixed(
      Instant.from(localDate.atTime(LocalTime.MIDNIGHT).atZone(zoneId)),
      zoneId
    )

  def toTimestamp(localDate: LocalDate): Timestamp =
    Timestamp.valueOf(localDate.atStartOfDay())

  val zoneId = ZoneId.of("America/Argentina/Buenos_Aires")

  implicit val defaultClock: Clock = Clock.systemDefaultZone()

}
