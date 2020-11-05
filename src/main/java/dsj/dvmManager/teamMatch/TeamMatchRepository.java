package dsj.dvmManager.teamMatch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMatchRepository extends JpaRepository<TeamMatch, Long> {

}
