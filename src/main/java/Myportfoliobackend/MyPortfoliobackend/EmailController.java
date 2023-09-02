package Myportfoliobackend.MyPortfoliobackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Service
@CrossOrigin(origins = "https://myportfolio-n862.onrender.com")
public class EmailController {
    @Autowired
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String defaultEmail;

    public EmailController(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        return executor;
    }

    @PostMapping("/send-email")
    @Async("taskExecutor")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
        String sender = request.getSender();
        String name = request.getName();
        String message = "Hi Jasper,\n\n"
                          + request.getMessage() +
                          "\n\nIf you are interested, please feel welcome to reach out to me at my email " +
                            sender +
                          "\n\nKind regards,\n\n"
                          + name;
        String subject = name + " send you a message from your portfolio";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(defaultEmail);
        mailMessage.setSubject(subject);
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

