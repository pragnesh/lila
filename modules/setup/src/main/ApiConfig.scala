package lila.setup

import chess.{ Variant, Mode, Clock }
import lila.common.EloRange
import lila.game.{ Game, Player, Source }
import lila.lobby.Color

private[setup] case object ApiConfig extends Config with GameGenerator {

  val color = Color.White
  val variant = Variant.Standard
  val mode = Mode.Casual
  val clock = false
  val time = 5
  val increment = 8

  def game = Game.make(
    game = makeGame,
    ai = None,
    whitePlayer = Player.white,
    blackPlayer = Player.black,
    creatorColor = creatorColor,
    mode = mode,
    variant = variant,
    source = Source.Api,
    pgnImport = None)
}
