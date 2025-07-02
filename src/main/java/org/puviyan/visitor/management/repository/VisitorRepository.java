package org.puviyan.visitor.management.repository;

import org.puviyan.visitor.management.model.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Visitor getVisitorById(Long id);

    List<Visitor> findByLoginTimeGreaterThanEqualAndLogoutTimeLessThanEqual(LocalDateTime loginTime, LocalDateTime logoutTime);

}
