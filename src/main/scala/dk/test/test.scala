package dk.test

import dk.atp.api.tournament.GenericTournamentAtpApi
import dk.atp.api._

object MatchesLoadSimple extends App {
  //Increase 3000ms connection timeout, if loading tennis matches fails.
  //There are dozens of requests sent to atp website, so
  //even with a higher timeout the loadMatches method may sometimes fail.
  //With a high speed internet connection, loading all tennis matches
  //for 2011 takes between 3-6 seconds.
  var tournamentApi: GenericTournamentAtpApi = new GenericTournamentAtpApi(360000)
  val genericATPMatchesLoader = new GenericATPMatchesLoader(tournamentApi)
  val matches =  genericATPMatchesLoader.loadMatches(2013)
  CSVATPMatchesLoader.toCSVFile(matches, "C:\\Devs\\matches_2013.csv")
}
