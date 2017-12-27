package beans

object Bean

case class UserBean(var id: String, var phone: String)

case class TrainNumberBean(
  var lineName: String,
  var trainNumber: String,
  var trainName: String,
  var startTime: Long,
  var distance: Double,
  var travelTime: Long,
  var endTime: Long,
  var price: Double
)
