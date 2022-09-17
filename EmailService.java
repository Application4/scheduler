package com.javatechie.service;

import com.javatechie.dao.CourseDao;
import com.javatechie.dto.Employee;
import com.javatechie.dto.MailRequest;
import com.javatechie.model.CourseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CourseDao courseDao;

    public String sendSimpleMail(MailRequest mailRequest) {
        // Creating a simple mail message
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // Setting up necessary details
        mailMessage.setFrom(sender);
        mailMessage.setTo(mailRequest.getTo().split(","));
        mailMessage.setText(mailRequest.getMessageBody());
        mailMessage.setSubject(mailRequest.getSubject());

        javaMailSender.send(mailMessage);
        return "Email forwarded successfully to : " + mailRequest.getTo();
    }

    public String sendSimpleMailWithAttachment(MailRequest mailRequest) throws MessagingException {
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(mailRequest.getTo());
        mimeMessageHelper.setText(mailRequest.getMessageBody());
        mimeMessageHelper.setSubject(
                mailRequest.getSubject());

        // Adding the attachment
        FileSystemResource file
                = new FileSystemResource(
                new File(mailRequest.getAttachment()));

        mimeMessageHelper.addAttachment(
                Objects.requireNonNull(file.getFilename()), file);

        // Sending the mail
        javaMailSender.send(mimeMessage);
        return "Mail sent Successfully with attachment " + file.getFilename();

    }


    public String sendEmailWithDynamicData() throws IOException, MessagingException {
        List<CourseEntity> entityList = StreamSupport.stream(courseDao.findAll().spliterator(), false).collect(Collectors.toList());
        byte[] report = reportService.generateReport(entityList);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage, true);
        mimeMessageHelper.setSubject("List of available course details_"+new Date().getTime());
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(new String[]{"java.gyan.mantra@gmail.com", "basanta.kumar.hota.4@gmail.com"});

        mimeMessageHelper.setText("Hi Dear,\n\nPlease find the attachment below \n\nRegards,\njavatechie");

        ByteArrayResource content = new ByteArrayResource(report);
        mimeMessageHelper.addAttachment("courses.xlsx", content);

        javaMailSender.send(mimeMessageHelper.getMimeMessage());
        return "email forwarded successfully with dynamic attachment";
    }

    public Employee addNewEmployeeToSystem(Employee employee) {
        employee.setEmpId(new Random().nextInt(6824638));
        return employee;
    }
}
