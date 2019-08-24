package com.polarj.common.web.service;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polarj.common.CommonConstant;
import com.polarj.model.EmailDraft;
import com.polarj.model.UserAccount;
import com.polarj.model.enumeration.EmailStatus;
import com.polarj.model.service.EmailDraftService;

import lombok.Setter;

// 处理用户注册，忘记密码，等自管理服务
@Service
public class UserSelfServiceService
{
    private @Setter String confirmationEmailTemplateFileName = "confirm.txt";

    private @Setter String resetPasswordTemplateFileName = "resetpassword.txt";

    private Logger logger = LoggerFactory.getLogger(UserSelfServiceService.class);

    @Autowired
    private EmailDraftService emailService;

    public void sendRegisterConfirmationEmail(String baseURL, UserAccount ua)
    {
        sendNotificationEmail(baseURL, ua, confirmationEmailTemplateFileName, "register", "Register Confirmation");
    }

    public void sendResetPasswordEmail(String baseURL, UserAccount ua)
    {
        sendNotificationEmail(baseURL, ua, resetPasswordTemplateFileName, "reset-password", "Reset Password");
    }

    private void sendNotificationEmail(String baseURL, UserAccount ua, String templateFileName, String urlPath,
            String subject)
    {
        if (ua == null || ua.getContact() == null)
        {
            return;
        }
        String name = ua.getContact().getName();
        if (StringUtils.isEmpty(name))
        {
            name = ua.getLoginName();
        }
        String url = String.format(baseURL + urlPath + "?loginName=%s&nonLoginToken=%s", ua.getLoginName(),
                ua.getNonLoginToken());
        String emailContent = null;

        try
        {
            emailContent = readTemplate(templateFileName);
        }
        catch (Exception e)
        {
            emailContent = null;
            logger.error(e.getMessage(), e);
        }
        if (emailContent == null)
        {
            emailContent = "{name}: please click {actionurl}";
        }
        emailContent = emailContent.replaceAll("\\{name\\}", name).replaceAll("\\{actionurl\\}", url);
        createEmailDraft(ua.getLoginName(), subject, emailContent);
    }

    private void createEmailDraft(String toAdd, String subject, String content)
    {
        EmailDraft draftEmail = new EmailDraft();
        draftEmail.setToAddress(toAdd);
        draftEmail.setBccAddress("polarj.pm@gmail.com");
        draftEmail.setContent(content);
        draftEmail.setFromAddress("polarj.pm@gmail.com");
        draftEmail.setSubject(subject);
        draftEmail.setStatus(EmailStatus.NEW.name());
        EmailDraft res = emailService.create(draftEmail, CommonConstant.systemUserAccountId,
                CommonConstant.defaultSystemLanguage);
        if (res == null)
        {
            logger.error("Create EmailDraft error.");
        }
    }

    private String readTemplate(String fileName) throws Exception
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fr = classLoader.getResourceAsStream(fileName);
        byte[] b = new byte[4096];
        fr.read(b);
        fr.close();
        String fileContent = new String(b);
        return fileContent.trim();
    }

}
