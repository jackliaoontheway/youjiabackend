package com.hwcargo.service;

import org.springframework.web.multipart.MultipartFile;

public interface MergePackingListService
{

    void mergePackingList(MultipartFile[] files, String path, String fileName, String three8number);

}
