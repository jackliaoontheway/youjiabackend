package com.polarj.common.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polarj.common.CommonConstant;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.model.I18nResource;
import com.polarj.model.enumeration.SupportedLanguage;
import com.polarj.model.service.I18nResourceService;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController
{
    @Autowired
    private I18nResourceService resourceService;

    @GetMapping("")
    public @ResponseBody ResponseEntity<String> home()
    {
        return new ResponseEntity<String>("Welcome to this application.", HttpStatus.OK);
    }

    @GetMapping("languages")
    public @ResponseBody ResponseEntity<ServerResponse<String>> listSupportedLanguange()
    {
        try
        {
            List<String> languageIds = new ArrayList<String>();
            List<I18nResource> resources = resourceService.fetchI18nResourceFor(SupportedLanguage.class.getName());
            for (I18nResource resource : resources)
            {
                if (resource.getLanguageId().equals(CommonConstant.defaultSystemLanguage))
                {
                    for (SupportedLanguage sl : SupportedLanguage.values())
                    {
                        if (resource.getI18nKey()
                                .equals(SupportedLanguage.class.getName() + "." + sl.getI18nResourceKey()))
                        {
                            String s = sl.getI18nResourceKey() + "," + resource.getI18nValue();
                            languageIds.add(s);
                        }
                    }
                }
            }
            return generateRESTFulResponse(languageIds);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }
}
