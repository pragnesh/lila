package lila.gameSearch

import lila.game.PgnRepo
import lila.game.actorApi._
import lila.search.{ actorApi ⇒ S }

import akka.actor._

private[gameSearch] final class Indexer(lowLevel: ActorRef) extends Actor {

  def receive = {

    case InsertGame(game) ⇒ PgnRepo getOption game.id foreach {
      _ foreach { pgn ⇒
        lowLevel ! S.InsertOne(game.id, Game.from(game, pgn))
      }
    }
  }
}
