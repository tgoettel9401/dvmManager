package dsj.dvmManager.teamMatch;

import dsj.dvmManager.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {
    Optional<TeamMatch> findByTeamWhiteAndTeamBlack(Team teamWhite, Team teamBlack);
}
