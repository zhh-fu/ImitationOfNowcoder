package com.zhh_fu.mynowcoder.Util;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSendUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSendUtil.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public boolean sendWithHTMLTemplate(String to,String subject,
                                        String templateName,Map<String, Object> model){
        try{
            String nick = MimeUtility.encodeText("试试看");
            InternetAddress from = new InternetAddress(nick + "<137588897@qq.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            //通过helper来处理
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            //模板转化
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            String result = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result,true);
            mailSender.send(mimeMessage);
            return true;
        }
        catch (Exception ex){
            logger.error("发送邮件失败" + ex.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("137588897@qq.com");
        mailSender.setPassword("ovhjodmniurebhgg");
        //mailSender.setHost("smtp.exmail.qq.com");
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        //javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
