package application.runners

/** Trait in charge of run the game with selected effect
  * @tparam F
  *   effect
  */
trait GameRunner[F[_]] {
  def run: F[Unit]
}
