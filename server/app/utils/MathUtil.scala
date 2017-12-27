package utils

import java.lang.Math.toRadians

object MathUtil {

  /**
    *
    * @param lat1 纬度
    * @param lng1 经度
    * @param lat2
    * @param lng2
    * @return
    */
  def distanceSimplifyChina(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double = {
    val dx = lng1 - lng2  // 经度差值
    val dy = lat1 - lat2  // 纬度差值
    val b = (lat1 + lat2) / 2.0  // 平均纬度
    val Lx = toRadians(dx) * 6367000.0 * Math.cos(toRadians(b))  // 东西距离
    val Ly = 6367000.0 * toRadians(dy)  // 南北距离
    Double.box(Math.sqrt(Lx * Lx + Ly * Ly))  // 用平面的矩形对角距离公式计算总距离
  }

}
