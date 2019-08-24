package com.hwcargo.model.controller;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hwcargo.model.CartonData;
import com.hwcargo.model.ManifestData;
import com.hwcargo.model.service.CartonDataService;
import com.hwcargo.model.service.ManifestDataService;
import com.polarj.common.web.controller.ModelController;
import com.polarj.common.web.model.ClientRequest;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.model.UserAccount;
import com.polarj.model.enumeration.ModelOperation;

@RestController
@RequestMapping("/manifestdatas")
public class ManifestDataController extends ModelController<ManifestData, Integer, ManifestDataService>
{
    public ManifestDataController()
    {
        super(ManifestData.class);
    }

    @Autowired
    private CartonDataService cartonDataService;

    @PostMapping(value = "uploadManifest", consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>>
            uploadManifest(MultipartHttpServletRequest multipleFileRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.CREATE.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            String destFileName = saveUploadedFileLocally(multipleFileRequest, userAcc, null);

            Boolean result = service.upload(destFileName, userAcc, userAcc.getUserSetting().getWorkLang());

            File fDest = new File(destFileName);
            fDest.delete();

            return generateRESTFulResponse(result);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping("fetchByPlNumber")
    public @ResponseBody ResponseEntity<ServerResponse<ManifestData>>
            fetchEntitiesByCriteria(@RequestBody ClientRequest<ManifestData> clientRequest)
    {
        try
        {
            ManifestData requestData = clientRequest.getData();
            if (requestData == null || StringUtils.isEmpty(requestData.getPackingListNo()))
            {
                return generateErrorRESTFulResponse(new WebErrorStatus("Request data is null"));
            }

            // 这里的 requestData 里面的 packingListNO 有两种情况 一种是packinglistnumber 一种是 cartonname
            // 先查cartondata 通过 cartonname查到plnumber
            String plNo = requestData.getPackingListNo().trim();
            List<CartonData> cartonDataList = cartonDataService.findByCartonName(requestData.getPackingListNo().trim());
            if (cartonDataList != null && cartonDataList.size() > 0)
            {
                CartonData cartonData = cartonDataList.get(0);
                if(StringUtils.isNotEmpty(cartonData.getPlNo())) {
                    plNo = cartonData.getPlNo().trim();
                }
            }

            ManifestData manifestData = service.findByPlNumber(plNo);
            if (manifestData == null)
            {
                return generateErrorRESTFulResponse(new WebErrorStatus("No data : " + requestData.getPackingListNo()));
            }

            return generateRESTFulResponse(manifestData);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }
}
