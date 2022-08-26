package application.runners

import cats.effect.ExitCode

/** Trait in charge of run the game with selected effect
  *
  * @tparam F
  *   effect
  */
trait GameRunner[F[_]] {
  def run: F[ExitCode]
}
