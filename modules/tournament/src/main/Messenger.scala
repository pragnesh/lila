package lila.tournament

import lila.db.api.$find
import tube.tournamentTube
import lila.user.{ User, UserRepo, Room ⇒ UserRoom }

private[tournament] final class Messenger(val netDomain: String) extends UserRoom {

  import Room._

  def init(tour: Created): Fu[List[Message]] = for {
    userOption ← UserRepo named tour.data.createdBy
    username = userOption.fold(tour.data.createdBy)(_.username)
    message ← systemMessage(tour, "%s creates the tournament" format username)
  } yield List(message)

  def userMessage(tournamentId: String, userId: String, text: String): Fu[Message] = for {
    userOption ← UserRepo named userId
    tourOption ← $find byId tournamentId
    message ← (for {
      user ← userOption filter (_.canChat) toValid "This user cannot chat"
      _ ← tourOption toValid "No such tournament"
      msg ← createMessage(user, text)
      (u, t) = msg
    } yield Message(u.some, t)).future
    _ ← RoomRepo.addMessage(tournamentId, message) 
  } yield message

  def systemMessage(tour: Tournament, text: String): Fu[Message] =
    Message(none, text) |> { message ⇒
      RoomRepo.addMessage(tour.id, message) inject message
    }

  def getMessages(tournamentId: String): Fu[List[Room.Message]] = 
    RoomRepo room tournamentId map (_.decodedMessages)
}
