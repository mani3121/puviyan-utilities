package org.puviyan.visitor.management.service.impl;

import org.puviyan.visitor.management.model.Visitor;
import org.puviyan.visitor.management.model.VisitorRequest;
import org.puviyan.visitor.management.repository.VisitorRepository;
import org.puviyan.visitor.management.service.VisitorManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitorManagerServiceImpl implements VisitorManagementService {

    @Autowired
    private VisitorRepository visitorRepository;

    public boolean saveVisitor(VisitorRequest request){
        try {
            Visitor visitor = new Visitor();
            visitor.setName(request.getName());
            visitor.setPhoneNumber(request.getMobileNumber());
            visitor.setEmail(request.getEmail());
            visitor.setLoginTime(LocalDateTime.now().withNano(0));
            visitorRepository.save(visitor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveLogout(int id){
        Visitor visitor = visitorRepository.getVisitorById(id);
        visitor.setLogoutTime(LocalDateTime.now().withNano(0));
        visitorRepository.save(visitor);
        return true;
    }

    @Override
    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }
}
