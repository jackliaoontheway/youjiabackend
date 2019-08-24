package com.hwcargo.model.service;

import com.hwcargo.model.ManifestData;
import com.polarj.model.UserAccount;
import com.polarj.model.service.EntityService;

public interface ManifestDataService extends EntityService<ManifestData, Integer>
{

    Boolean upload(String destFileName, UserAccount userAcc, String workLang);

    ManifestData findByPlNumber(String plNumber);
}
