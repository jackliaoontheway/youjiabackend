package com.polarj.common.scheduler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;

import com.polarj.common.CommonConstant;
import com.polarj.common.service.impl.EmailServiceImpl;
import com.polarj.model.EmailDraft;
import com.polarj.model.annotation.SchedulerInfo;
import com.polarj.model.enumeration.EmailStatus;
import com.polarj.model.service.EmailDraftService;

import lombok.Setter;

// 发送邮件的定时任务
@SchedulerInfo(name = "Send Email Task", maxStuckNum = 5)
public class SendEmailScheduler extends SchedulerBase
{
    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private EmailDraftService emailDraftService;

    // 启动一次邮件发送任务，总共发送邮件的数量
    private @Setter int bundleSize = 10;

    @Override
    protected void task()
    {
        List<EmailDraft> drafts = (List<EmailDraft>) emailDraftService.listNewDraftEmail(bundleSize);
        if (drafts == null || drafts.size() == 0)
        {
            logger.info("No pending draft email to send.");
            return;
        }
        logger.info("will send " + drafts.size() + " email(s) this time.");
        for (EmailDraft draft : drafts)
        {
            if (validEmail(draft))
            {
                try
                {
                    String[] bccs = draft.getBccAddress() == null ? null : draft.getBccAddress().split(",");
                    emailService.sendEmail(draft.getToAddress().split(","), bccs, draft.getSubject(),
                            draft.getContent());
                    draft.setStatus(EmailStatus.SUCCESS.name());
                    emailDraftService.update(draft.getId(), draft, CommonConstant.systemUserAccountId,
                            CommonConstant.defaultSystemLanguage);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                    draft.setStatus(EmailStatus.SEND_ERROR.name());
                    if(e.getMessage().length()>250)
                    {
                        draft.setErrorMessage(e.getMessage().substring(0, 250));
                    }
                    else
                    {
                        draft.setErrorMessage(e.getMessage());
                    }
                    emailDraftService.update(draft.getId(), draft, CommonConstant.systemUserAccountId,
                            CommonConstant.defaultSystemLanguage);
                }
            }
            else
            {
                draft.setStatus(EmailStatus.VALID_ERROR.name());
                draft.setErrorMessage("Related Email info has error.");
                emailDraftService.update(draft.getId(), draft, CommonConstant.systemUserAccountId,
                        CommonConstant.defaultSystemLanguage);
            }
        }
    }

    private boolean validEmail(EmailDraft draft)
    {
        if (!validEmailAddress(draft.getToAddress()))
        {
            logger.error(draft.getToAddress() + " to address in the draft is incorrect.");
            return false;
        }
        if (StringUtils.isNotBlank(draft.getBccAddress()))
        {
            if (!validEmailAddress(draft.getBccAddress()))
            {
                logger.error(draft.getToAddress() + " bcc address in the draft is incorrect.");
                return false;
            }
        }
        if (!validEmailAddress(draft.getFromAddress()))
        {
            logger.error(draft.getToAddress() + " from address in the draft is incorrect.");
            return false;
        }
        if (draft.getSubject() == null || draft.getSubject().length() == 0)
        {
            logger.error("Email should have subject.");
            return false;
        }
        if (draft.getContent() == null || draft.getContent().length() == 0)
        {
            logger.error("Email should have content.");
            return false;
        }
        return true;
    }

    private boolean validEmailAddress(String emailAdds)
    {
        if (emailAdds == null)
        {
            return false;
        }
        if (emailAdds.length() == 0)
        {
            return false;
        }
        String[] allEmailAdds = emailAdds.split(",");
        for (String emailAdd : allEmailAdds)
        {
            boolean valid = EmailValidator.getInstance().isValid(emailAdd.trim());
            if (!valid)
            {
                return false;
            }
        }
        return true;
    }
}
