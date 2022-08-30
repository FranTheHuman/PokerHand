package application.models.errors

case class PersistenceError(error: String) extends Throwable
