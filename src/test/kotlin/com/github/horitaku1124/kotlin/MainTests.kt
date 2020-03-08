package com.github.horitaku1124.kotlin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class MainTests {
  @Test
  fun test1() {
    val arg = "host1:8080"
    val (host, port) = arg.let { str ->
      if (str.contains(':')) {
        str.split(":").let {list ->
          Pair(list[0], list[1].toInt())
        }
      } else {
        Pair("localhost", str.toInt())
      }
    }
    assertEquals("host1", host)
    assertEquals(8080, port)
  }

  @Test
  fun test2() {
    val arg = "1234"
    val (host, port) = arg.let { str ->
      if (str.contains(':')) {
        str.split(":").let {list ->
          Pair(list[0], list[1].toInt())
        }
      } else {
        Pair("localhost", str.toInt())
      }
    }
    assertEquals("localhost", host)
    assertEquals(1234, port)
  }
}