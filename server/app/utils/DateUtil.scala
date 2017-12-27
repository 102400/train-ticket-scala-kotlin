package com.vatcore.util

import java.util.Date
import java.time._


/**
  * @author xzy
  * @version 20171020, 20170917
  * @since 1.8
  */
object DateUtil {
  /**
    * @param date java.util.Date
    * @return java.time.LocalDateTime
    */
  def toLocalDateTime(date: Date): LocalDateTime = LocalDateTime.ofInstant(date.toInstant, ZoneId.systemDefault)

  /**
    * @param date java.util.Date
    * @return java.time.LocalDate
    */
  def toLocalDate(date: Date): LocalDate = LocalDateTime.ofInstant(date.toInstant, ZoneId.systemDefault).toLocalDate

  /**
    * @param date java.util.Date
    * @return java.time.LocalTime
    */
  def toLocalTime(date: Date): LocalTime = LocalDateTime.ofInstant(date.toInstant, ZoneId.systemDefault).toLocalTime

  /**
    * @param localDateTime java.time.LocalDateTime
    * @return java.util.Date
    */
  def toDate(localDateTime: LocalDateTime): Date = Date.from(localDateTime.atZone(ZoneId.systemDefault).toInstant)

  /**
    * @param localDate java.time.LocalDate
    * @return java.util.Date
    */
  def toDate(localDate: LocalDate): Date = Date.from(localDate.atStartOfDay.atZone(ZoneId.systemDefault).toInstant)

  /**
    * @param localDate java.time.LocalDate
    * @return Unix timestamp
    */
  def getLong(localDate: LocalDate): Long = getLong(localDate.atStartOfDay)

  /**
    * @param localDateTime java.time.LocalDateTime
    * @return Unix timestamp
    */
  def getLong(localDateTime: LocalDateTime): Long = localDateTime.atZone(ZoneId.systemDefault).toInstant.toEpochMilli

}
