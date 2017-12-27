package beans

import utils.JsonUtil

/**
  * @author xzy
  */
class AjaxResult(
  val code: String = "0",
  val message: String = "Success",
  val data: Any = AnyRef
) {

  override def toString: String = JsonUtil.anyToString(this)

}

object AjaxResult {

  def newSuccess(): AjaxResult = {
    new AjaxResult()
  }

  def newSuccess(code: String = "0", message: String = "Success", data: Any): AjaxResult = {
    new AjaxResult(code = code, message = message, data = data)
  }

  def newSuccess(data: Any): AjaxResult = {
    new AjaxResult(data = data)
  }

  def newError(): AjaxResult = new AjaxResult(code = "-1", message = "Error")

  def newIllegalArgument(): AjaxResult = new AjaxResult(code = "-2", message = "IllegalArgument")

  def newTokenError(): AjaxResult = new AjaxResult(code = "-3", message = "TokenError")

}