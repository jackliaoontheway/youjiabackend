package com.hwcargo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hwcargo.service.MergePackingListService;
import com.polarj.common.web.controller.BaseController;
import com.polarj.common.web.model.ServerResponse;

@RestController
@RequestMapping("/mergePackingList")
public class MergePackingListController extends BaseController
{
    private String dateTimePatternString = "yyyyMMddHHmmss";

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat(dateTimePatternString);

    @Autowired
    private MergePackingListService mergePackingListService;

    @RequestMapping(value = ("upload"), headers = "content-type=multipart/form-data", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<ServerResponse<String>> uploadPackingList(
            @RequestParam("files") MultipartFile[] files, @RequestParam("three8number") String three8number)
    {
        if (files == null || files.length == 0)
        {
            return null;
        }

        try
        {
            String fileName = three8number +"_"+ dateTimeFormat.format(new Date()) + "_result.xls";
            String path = getPermanentFilePath(null, null);

            mergePackingListService.mergePackingList(files, path, fileName, three8number);
            return generateRESTFulResponse(fileName);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }

    }

    @GetMapping("download")
    public void download(@RequestParam(required = true) String fileName, HttpServletResponse resp)
    {
        String path = getPermanentFilePath(null, null);
        sendFileToPage(path, fileName, resp);
    }

}
