package org.puviyan.visitor.management.service;

import org.puviyan.visitor.management.model.Visitor;
import org.puviyan.visitor.management.model.VisitorRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitorManagementService {

   boolean saveVisitor(VisitorRequest request);
   boolean saveLogout(int id);
   List<Visitor> getAllVisitors();

   List<Visitor> getVisitorsByLoginAndLogoutDate(LocalDateTime loginDate, LocalDateTime logoutDate);
}
