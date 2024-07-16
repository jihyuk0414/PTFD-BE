package org.example.service;

import jakarta.activation.DataSource;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.dto.exception.CustomMailException;
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
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                // 수신자
                mimeMessageHelper.setTo(paymentReq.getSeller());
                // 제목
                mimeMessageHelper.setSubject("게시하신 PT를 다른 고객님이 예약하셨습니다.");

                // 이미지 ->datasource로 변경.
                URL imageUrl = new URL(postRepository.findImagePostByPostId(paymentReq.getPost_id()));
                byte[] imageData = IOUtils.toByteArray(imageUrl);

                String htmlContentWithInlineImage = "<html><body>"
                        + "<img src='cid:image_reservation' style='width: 100px; height: auto;'/>"
                        + "<p>사이트를 이용해주셔서 감사합니다.</p>"
                        + "<p>대표 전화번호: 010-8852-6778</p>"
                        + "<p>대표 이메일: darakbang0414@naver.com</p>"
                        + "<p>행복한 PT 되시기를 기원하겠습니다.</p>"
                        + "</body></html>";

                // 이미지 ->datasource로 변경.
                mimeMessageHelper.setText(htmlContentWithInlineImage, true);

                DataSource dataSource = new ByteArrayDataSource(imageData, "image/jpeg");
                mimeMessageHelper.addInline("image_reservation", dataSource);
                // 이메일 발신자 설정
                mimeMessageHelper.setFrom(new InternetAddress(mailSenderId+"@naver.com"));
                // 이메일 보내기
                javaMailSender.send(mimeMessage);
            }
            return "메일 전송 완료되었습니다.";
        } catch (Exception e) {
            throw new CustomMailException();
        }
    }

    public String sendEmail(List<PaymentsReq> paymentsReqList, String consumer_email) {
        try {

            for (PaymentsReq paymentReq : paymentsReqList) {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                // 수신자
                mimeMessageHelper.setTo(consumer_email);
                // 제목
                mimeMessageHelper.setSubject("신청하신 PT가 예약되었습니다.");

                // 이미지 ->datasource로 변경.
                URL imageUrl = new URL(postRepository.findImagePostByPostId(paymentReq.getPost_id()));
                byte[] imageData = IOUtils.toByteArray(imageUrl);

                String htmlContentWithInlineImage = "<html><body>"
                        + "<img src='cid:image_reservation' style='width: 100px; height: auto;'/>"
                        + "<p>사이트를 이용해주셔서 감사합니다.</p>"
                        + "<p>대표 전화번호: 010-8852-6778</p>"
                        + "<p>대표 이메일: darakbang0414@naver.com</p>"
                        + "<p>행복한 PT 되시기를 기원하겠습니다.</p>"
                        + "</body></html>";

                mimeMessageHelper.setText(htmlContentWithInlineImage, true);

                DataSource dataSource = new ByteArrayDataSource(imageData, "image/jpeg");
                mimeMessageHelper.addInline("image_reservation", dataSource);
                // 이메일 발신자 설정
                mimeMessageHelper.setFrom(new InternetAddress(mailSenderId+"@naver.com"));
                // 이메일 보내기
                javaMailSender.send(mimeMessage);

                mimeMessageHelper.setTo(paymentReq.getSeller());
                mimeMessageHelper.setSubject("게시하신 PT를 다른 고객님이 예약하셨습니다.");
                // 본문
                javaMailSender.send(mimeMessage);
            }
            return "메일 전송 완료되었습니다.";
        } catch (Exception e) {
            throw new CustomMailException();
        }
    }
}