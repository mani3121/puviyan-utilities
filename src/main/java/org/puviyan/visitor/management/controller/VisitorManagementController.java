package org.puviyan.visitor.management.controller;

import org.puviyan.visitor.management.model.SMSRequest;
import org.puviyan.visitor.management.model.Visitor;
import org.puviyan.visitor.management.model.VisitorRequest;
import org.puviyan.visitor.management.service.VisitorManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visitor")
@CrossOrigin(origins = "*")
public class VisitorManagementController {

    @Autowired
    private VisitorManagementService visitorManagementService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody VisitorRequest request) {
        boolean saved = visitorManagementService.saveVisitor(request);
        if (saved) {
            return ResponseEntity.ok("Visitor saved successfully.");
        } else {
            return ResponseEntity.status(500).body("Failed to save visitor.");
        }
    }

    @PostMapping("/logout")
    public boolean logout(@RequestParam int id) {
       return visitorManagementService.saveLogout(id);
    }

    @GetMapping("/getVisitors")
    public List<Visitor> getVisitors() {
       return visitorManagementService.getAllVisitors();
    }
}
