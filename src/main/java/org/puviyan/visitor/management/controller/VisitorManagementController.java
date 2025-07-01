package org.puviyan.visitor.management.controller;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.puviyan.visitor.management.model.SMSRequest;
import org.puviyan.visitor.management.model.Visitor;
import org.puviyan.visitor.management.model.VisitorRequest;
import org.puviyan.visitor.management.service.VisitorManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    @GetMapping("/exportVisitors")
    public ResponseEntity<byte[]> exportVisitorsToExcel(
            @RequestParam String loginDate,
            @RequestParam String logoutDate) {

        // Expecting format "01-Jul-25"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime loginDateTime;
        LocalDateTime logoutDateTime;
        try {
            loginDateTime = LocalDateTime.parse(loginDate, formatter);
        } catch (DateTimeParseException e) {
            // If only date is provided, set time to start of day
            loginDateTime = java.time.LocalDate.parse(loginDate, formatter).atStartOfDay();
        }
        try {
            logoutDateTime = LocalDateTime.parse(logoutDate, formatter);
        } catch (DateTimeParseException e) {
            // If only date is provided, set time to end of day
            logoutDateTime = java.time.LocalDate.parse(logoutDate, formatter).atTime(23, 59, 59);
        }

        List<Visitor> visitors = visitorManagementService.getVisitorsByLoginAndLogoutDate(loginDateTime, logoutDateTime);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             Workbook wb = new Workbook(out, "Visitors", "1.0")) {
            Worksheet ws = wb.newWorksheet("Visitors");
            // Header
            ws.value(0, 0, "ID");
            ws.value(0, 1, "Name");
            ws.value(0, 2, "Phone Number");
            ws.value(0, 3, "Email");
            ws.value(0, 4, "Login Time");
            ws.value(0, 5, "Logout Time");

            int row = 1;
            for (Visitor v : visitors) {
                ws.value(row, 0, v.getId());
                ws.value(row, 1, v.getName());
                ws.value(row, 2, v.getPhoneNumber());
                ws.value(row, 3, v.getEmail());
                ws.value(row, 4, v.getLoginTime() != null ? v.getLoginTime().toString() : "");
                ws.value(row, 5, v.getLogoutTime() != null ? v.getLogoutTime().toString() : "");
                row++;
            }
            wb.finish();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=visitors.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
