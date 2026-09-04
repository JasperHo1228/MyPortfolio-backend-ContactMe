package Myportfoliobackend.MyPortfoliobackend;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetSocketAddress;
import java.net.Socket;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${FRONTEND_URL}")
public class EmailController {

    private final Resend resend;

    @Value("${RESEND_API_KEY}")
    private String resendApiKey;

    @Value("${MAIL_USERNAME}")
    private String defaultEmail;

    public EmailController() {
        this.resend = null;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request)
    {
        String sender = request.getSender();
        String name = request.getName();
        String message =
                "Hi Jasper,\n\n"
                        + request.getMessage()
                        + "\n\nIf you are interested, please feel welcome to reach out to me at my email "
                        + sender
                        + "\n\nKind regards,\n\n"
                        + name;
        String subject = name + " sent you a message from your portfolio";

        try
        {
            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(sender)
                    .to(defaultEmail)
                    .subject(subject)
                    .text(message)
                    .build();

            resend.emails().send(params);

            return ResponseEntity.ok("Email sent successfully.");
        }
        catch (Exception e)
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        }
    }

    @GetMapping("/test-smtp")
    public String testSmtp() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("smtp.gmail.com", 587), 5000);
            socket.close();
            return "SMTP connection successful";
        } catch (Exception e) {
            return "SMTP connection failed: " + e.getMessage();
        }
    }

    @PostMapping("/test-resend")
    public ResponseEntity<String> testResend() {

        String apiKey = System.getenv("RESEND_API_KEY");

        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(defaultEmail)
                .subject("Test from my portfolio")
                .html("<h1>Hello!</h1><p>Resend is working!</p>")
                .build();

        try
        {
            CreateEmailResponse response = resend.emails().send(params);

            return ResponseEntity.ok(
                    "Email sent successfully. ID: " + response.getId()
            );
        }
        catch (ResendException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Resend error: " + e.getMessage());
        }
    }

}

