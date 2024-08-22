package org.example.service;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.dto.exception.CustomMailException;
import org.example.dto.mail.PostForMail;
import org.example.dto.purchase.PaymentsReq;
import org.example.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final PostRepository postRepository;
    private final JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private String mailSenderId ;

    public String sendEmailToSeller(List<PaymentsReq> paymentsReqList) {
        try {

            for (PaymentsReq paymentReq : paymentsReqList) {
                log.info("쿼리 시작");
                PostForMail p=postRepository.findImageAndNamePostByPostId(paymentReq.getPost_id());
                log.info(p.getImage_post());
                log.info(p.getPost_name());
                URL imageUrl = new URL(p.getImage_post());
                String postname = p.getPost_name();
                byte[] imageData = IOUtils.toByteArray(imageUrl);
                DataSource dataSource = new ByteArrayDataSource(imageData, "image/jpeg");
                String selleremail = paymentReq.getSeller();

                sendOneEmailToSeller(selleremail,postname,dataSource);
            }
            return "메일 전송 완료되었습니다.";
        } catch (Exception e) {
            throw new CustomMailException();
        }
    }

    public String sendEmail(List<PaymentsReq> paymentsReqList, String consumer_email) {
        try {

            for (PaymentsReq paymentReq : paymentsReqList) {
                PostForMail p=postRepository.findImageAndNamePostByPostId(paymentReq.getPost_id());
                URL imageUrl = new URL(p.getImage_post());
                String postname = p.getPost_name();
                byte[] imageData = IOUtils.toByteArray(imageUrl);
                DataSource dataSource = new ByteArrayDataSource(imageData, "image/jpeg");

                sendOneEmailToconsumer(consumer_email, postname, dataSource);

                sendOneEmailToSeller(paymentReq.getSeller(), postname, dataSource);
            }
            return "메일 전송 완료되었습니다.";
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CustomMailException();
        }
    }

    //이메일 하나에게 보내는 부분(소비자)
    public void sendOneEmailToconsumer(String consumerEmail, String postName, DataSource imageDataSource) throws MessagingException
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setTo(consumerEmail);
        mimeMessageHelper.setSubject("신청하신 PT가 예약되었습니다.");

        String htmlcontent = "<html><body>"
                + "<img src='cid:image_reservation' style='width: 100px; height: auto;'/>"
                + "<h1>" + postName + "를(을) 예약하셨습니다.</h1>"
                + "<p>사이트를 이용해주셔서 감사합니다.</p>"
                + "<p>대표 전화번호: 010-8852-6778</p>"
                + "<p>대표 이메일: 5-stars16@naver.com</p>"
                + "<p>행복한 PT 되시기를 기원하겠습니다.</p>"
                + "</body></html>";

        mimeMessageHelper.setText(htmlcontent, true);
        mimeMessageHelper.addInline("image_reservation", imageDataSource);
        mimeMessageHelper.setFrom(new InternetAddress(mailSenderId + "@naver.com"));

        javaMailSender.send(mimeMessage);
    }

    //판매자 한명에게만 보내기(판매자)
    public void sendOneEmailToSeller(String sellerEmail, String postName, DataSource imageDataSource) throws MessagingException
    {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        mimeMessageHelper.setTo(sellerEmail);
        mimeMessageHelper.setSubject("게시하신 PT를 다른 고객님이 예약하셨습니다.");

        String htmlcontent = "<html><body>"
                + "<img src='cid:image_reservation' style='width: 100px; height: auto;'/>"
                + "<h1>" + postName + "을(를) 다른 분이 예약하셨습니다.</h1>"
                + "<p>사이트를 이용해주셔서 감사합니다.</p>"
                + "<p>대표 전화번호: 010-8852-6778</p>"
                + "<p>대표 이메일: 5-stars16@naver.com</p>"
                + "<p>행복한 PT 되시기를 기원하겠습니다.</p>"
                + "</body></html>";

        mimeMessageHelper.setText(htmlcontent, true);
        mimeMessageHelper.addInline("image_reservation", imageDataSource);
        mimeMessageHelper.setFrom(new InternetAddress(mailSenderId + "@naver.com"));

        javaMailSender.send(mimeMessage);
    }
}