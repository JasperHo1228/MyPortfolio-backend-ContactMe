package Myportfoliobackend.MyPortfoliobackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Service
@CrossOrigin(origins = "https://myportfolio-n862.onrender.com")
public class EmailController {
    @Autowired
    private final JavaMailSender javaMailSender;

    public EmailController(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        String sender = request.getSender(); // Replace with your email address
        String name = request.getName() + " send you a message from your portfolio";
        String message = "Hi Jasper,\n\n"
                          + request.getMessage() +
                          "\n\nIf you are interested, please feel welcome to reach out to me at my email " +
                            sender +
                          "\n\nKind regards,\n\n"
                          + request.getName();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("tsunyinho1996@gmail.com");
        mailMessage.setSubject(name);
        mailMessage.setText(message);

        try {
            javaMailSender.send(mailMessage);
            return ResponseEntity.ok("Email sent successfully.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        }
    }
}

