package com.hwcargo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hwcargo.model.CartonData;
import com.hwcargo.model.CartonDataPickConfiguration;
import com.hwcargo.model.ManifestData;
import com.hwcargo.model.PackingListErrorData;
import com.hwcargo.model.service.CartonDataPickConfigurationService;
import com.hwcargo.service.MergePackingListService;
import com.polarj.common.utility.xls.ExcelReportFileReaderWriter;

@Service
public class MergePackingListServiceImpl implements MergePackingListService
{
    @Autowired
    private CartonDataPickStrategy strategy;

    @Autowired
    private CartonDataPickConfigurationService cartonDataPickConfigurationService;

    private final static String MANIFEST_TEMPLATE_PATH =
            System.getProperty("user.dir") + "/src/main/initdata/hwcargo_manifest_template.xls";

    @Override
    public void mergePackingList(MultipartFile[] files, String path, String fileName, String three8number)
    {
        try
        {
            List<CartonData> allData = new ArrayList<>();
            Map<String, String> errorMap = new HashMap<>();
            for (MultipartFile file : files)
            {
                Workbook workbook = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);

                CartonDataPickConfiguration configuration =
                        cartonDataPickConfigurationService.findConfigurationByExcel(sheet);

                if (configuration == null)
                {
                    errorMap.put(file.getOriginalFilename(), "Can't match column");
                    continue;
                }

                CartonDataPickResponse response = strategy.generateCartonData(sheet, configuration);
                if (response == null)
                {
                    errorMap.put(file.getOriginalFilename(), "Response is null");
                    continue;
                }

                if (StringUtils.isNotEmpty(response.getErrorMsg()))
                {
                    errorMap.put(file.getOriginalFilename(), response.getErrorMsg());
                }
                else
                {
                    List<CartonData> cartonDataList = response.getCartonDataList();
                    if (cartonDataList != null && cartonDataList.size() > 0)
                    {
                        allData.addAll(cartonDataList);
                    }
                }
            }

            if (errorMap != null && errorMap.size() > 0)
            {

                List<PackingListErrorData> errorDataList = new ArrayList<>();
                for (Entry<String, String> entry : errorMap.entrySet())
                {
                    PackingListErrorData errorData = new PackingListErrorData();
                    errorData.setFileName(entry.getKey());
                    errorData.setErrorMsg(entry.getValue());
                    errorDataList.add(errorData);
                }
                ExcelReportFileReaderWriter<PackingListErrorData> writer =
                        new ExcelReportFileReaderWriter<>(PackingListErrorData.getFieldNameAndHeaderMapping());
                writer.writeReportFile(errorDataList, path + fileName);
            }
            else
            {
                ManifestData manifestData = generateManifestData(allData, three8number);

                saveDataToExcel(manifestData, path + fileName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void saveDataToExcel(ManifestData manifestData, String fileName)
    {
        try
        {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(MANIFEST_TEMPLATE_PATH));
            Sheet sheet = workbook.getSheetAt(0);
            
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            cellStyle.setFont(font);
            
            Row row = sheet.createRow(3);

            Cell serial = row.createCell(0);
            serial.setCellStyle(cellStyle);
            serial.setCellValue(1);

            Cell hawb = row.createCell(1);
            hawb.setCellStyle(cellStyle);
            hawb.setCellValue("");

            Cell three8Number = row.createCell(2);
            three8Number.setCellStyle(cellStyle);
            three8Number.setCellValue(manifestData.getThree8Number());
            
            Cell electrInnered = row.createCell(3);
            electrInnered.setCellStyle(cellStyle);
            electrInnered.setCellValue("");
            
            Cell productName = row.createCell(4);
            productName.setCellStyle(cellStyle);
            productName.setCellValue("");

            Cell qty = row.createCell(5);
            qty.setCellStyle(cellStyle);
            qty.setCellValue(manifestData.getQty());

            Cell weight = row.createCell(6);
            weight.setCellStyle(cellStyle);
            weight.setCellValue(manifestData.getWeight());

            Cell dimensions = row.createCell(7);
            cellStyle.setWrapText(true);
            dimensions.setCellStyle(cellStyle);
            dimensions.setCellValue(new HSSFRichTextString(manifestData.getDimensions()));

            Cell volumn = row.createCell(8);
            cellStyle.setWrapText(false);
            volumn.setCellStyle(cellStyle);
            volumn.setCellValue(manifestData.getVolumn());

            Cell packingListNo = row.createCell(9);
            cellStyle.setWrapText(true);
            packingListNo.setCellStyle(cellStyle);
            packingListNo.setCellValue(new HSSFRichTextString(manifestData.getPackingListNo()));
            
            FileOutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
            workbook.close();
        }
        catch (EncryptedDocumentException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private ManifestData generateManifestData(List<CartonData> allData, String three8Number)
    {
        Map<String, Integer> map = new HashMap<>();
        Set<String> plNOSet = new HashSet<>();
        double weightTotal = 0;
        double volumnTotal = 0;
        for (CartonData data : allData)
        {
            weightTotal += data.getGrossWeight();
            volumnTotal += data.getVolumn();

            String size = data.getSize();
            Integer count = map.get(size);
            count = (count == null ? 0 : count);
            count += 1;
            map.put(size, count);
            plNOSet.add(data.getPlNo());
        }

        ManifestData manifestData = new ManifestData();
        manifestData.setSerialNum(1);
        manifestData.setQty(allData.size());
        
        BigDecimal weightTotalDecimal = new BigDecimal(weightTotal).setScale(0,BigDecimal.ROUND_HALF_UP);
        manifestData.setWeight(weightTotalDecimal.doubleValue());  
        
        BigDecimal volumnTotalDecimal = new BigDecimal(volumnTotal).setScale(2,BigDecimal.ROUND_HALF_UP);
        manifestData.setVolumn(volumnTotalDecimal.doubleValue());  

        StringBuffer plNoStr = new StringBuffer();

        Iterator<String> iterator = plNOSet.iterator();
        while (iterator.hasNext())
        {
            plNoStr.append(iterator.next());
            if (iterator.hasNext())
            {
                plNoStr.append("\r\n");
            }
        }
        manifestData.setPackingListNo(plNoStr.toString());

        StringBuffer sizeStr = new StringBuffer();
        Iterator<Map.Entry<String, Integer>> iterator2 = map.entrySet().iterator();
        while (iterator2.hasNext())
        {
            Map.Entry<String, Integer> entry = iterator2.next();
            sizeStr.append(entry.getKey());
            sizeStr.append("/");
            sizeStr.append(entry.getValue());
            if (iterator2.hasNext())
            {
                sizeStr.append("\r\n");
            }
        }
        manifestData.setPackingListNo(plNoStr.toString());
        manifestData.setDimensions(sizeStr.toString());
        manifestData.setThree8Number(three8Number);

        return manifestData;
    }
}
