package dk.atp.api

import domain._
import dk.atp.api.tournament._

class GenericATPMatchesLoader(tournamentApi: TournamentAtpApi, numOfThreads: Int = 16) extends ATPMatchesLoader {

  /**Loads tennis matches from http://www.atpworldtour.com/ web site.*/
  def loadMatches(year: Int): List[MatchComposite] = {

    collection.parallel.ForkJoinTasks.defaultForkJoinPool.setParallelism(numOfThreads)

    val tournaments = tournamentApi.parseTournaments(year)
    val filteredTournaments = tournaments.filter(t => !t.tournamentUrl.isEmpty() && !t.tournamentUrl.endsWith("mds.pdf"))

    val matchesComposite = filteredTournaments.par.flatMap { tournament =>

      val tennisMatches = tournamentApi.parseTournament(tournament.tournamentUrl)

      tennisMatches.par.map { tennisMatch =>
        val matchFacts = tournamentApi.parseMatchFacts(tennisMatch.matchFactsUrl)

        MatchComposite(tournament, tennisMatch, matchFacts)
      }
    }

    matchesComposite.toList
  }

}