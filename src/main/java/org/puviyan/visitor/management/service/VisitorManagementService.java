package org.puviyan.visitor.management.service;

import org.puviyan.visitor.management.model.Visitor;
import org.puviyan.visitor.management.model.VisitorRequest;

import java.util.List;

public interface VisitorManagementService {

   boolean saveVisitor(VisitorRequest request);
   void saveLogout(int id);
   List<Visitor> getAllVisitors();
}
