package org.puviyan.visitor.management.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.puviyan.visitor.management.model.Visitor;
import org.puviyan.visitor.management.model.VisitorRequest;
import org.puviyan.visitor.management.repository.VisitorRepository;
import org.puviyan.visitor.management.service.VisitorManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Service
public class VisitorManagerServiceImpl implements VisitorManagementService {

    @Autowired
    private VisitorRepository visitorRepository;

    public boolean saveVisitor(VisitorRequest request){
        try {
            Visitor visitor = new Visitor();
            visitor.setName(request.getName());
            visitor.setPhoneNumber(request.getPhoneNumber());
            visitor.setEmail(request.getEmail());
            visitor.setLoginTime(LocalDateTime.now().withNano(0));
            visitor.setIdentityProof(request.getIdentityProof());
            visitor.setIdentityNumber(request.getIdentityNumber());
            visitor.setPuviyanBuddy(request.getPuviyanBuddy());
            visitorRepository.save(visitor);
            sendEmail(request.getEmail(), "Welcome", "Hi");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveLogout(Long id){
        Visitor visitor = visitorRepository.getVisitorById(id);
        visitor.setLogoutTime(LocalDateTime.now().withNano(0));
        visitorRepository.save(visitor);
        return true;
    }

    @Override
    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    @Override
    public List<Visitor> getVisitorsByLoginAndLogoutDate(LocalDateTime loginDate, LocalDateTime logoutDate) {
        return visitorRepository.findByLoginTimeGreaterThanEqualAndLogoutTimeLessThanEqual(loginDate, logoutDate);
    }

    @Override
    public List<Visitor> getVisitorsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return visitorRepository.findAll(pageable).getContent();
    }

    private void sendEmail(String to, String subject, String body) {
        final String username = "maniavudai10@gmail.com";
        // Use your Gmail App Password here instead of your regular Gmail password
        final String appPassword = "dyaf ykjm mxxy mvpt"; // <-- Replace with your Gmail App Password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, appPassword);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
