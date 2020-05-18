package com.github.horitaku1124.kotlin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTests {
  var formatter = DateTimeFormatter.ofPattern("MM-dd'T'HH:mm:ss.SSS")
  @Test
  fun test1() {
    var date = LocalDateTime.of(
      2020, 4, 1,
      12, 34,56, 0)
    assertEquals("04-01T12:34:56.000", date.format(formatter))
  }
  @Test
  fun test2() {
    var date = LocalDateTime.of(
      2020, 4, 1,
      12, 34,56, 123456789)
    assertEquals("04-01T12:34:56.123", date.format(formatter))
  }
}