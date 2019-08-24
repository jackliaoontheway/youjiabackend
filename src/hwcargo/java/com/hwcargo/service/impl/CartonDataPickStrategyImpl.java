package com.hwcargo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hwcargo.model.CartonData;
import com.hwcargo.model.CartonDataPickConfiguration;
import com.hwcargo.model.service.CartonDataService;
import com.polarj.common.CommonConstant;

@Service
public class CartonDataPickStrategyImpl implements CartonDataPickStrategy
{
    @Autowired
    private CartonDataService cartonDataService;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public final CartonDataPickResponse generateCartonData(Sheet sheet, CartonDataPickConfiguration configuration)
    {
        if (sheet == null)
        {
            return null;
        }
        CartonDataPickResponse response = new CartonDataPickResponse();
        List<CartonData> cartonDataList = new ArrayList<>();

        StringBuffer errorMsg = new StringBuffer();
        String plNo = getPlNo(sheet, configuration);
        if (StringUtils.isEmpty(plNo))
        {
            errorMsg.append("Can't find P/L NO ");
            response.setErrorMsg(errorMsg.toString());
            return response;
        }

        Iterator<Row> rowIterator = sheet.iterator();
        int cartonNameCellIndex = -1;
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            Integer rowIndex = row.getRowNum();

            if ((rowIndex + 1) == configuration.getDataRowIndex())
            {
                cartonNameCellIndex = getCartonNameCellIndex(row);
            }

            if (rowIndex < configuration.getDataRowIndex())
            {
                continue;
            }
            CartonData cartonData = new CartonData();
            cartonData.setPlNo(plNo.trim());
            try
            {
                Cell firstCell = row.getCell(0);
                if (isEndRow(firstCell, configuration))
                {
                    break;
                }

                Cell cartonNoCell = row.getCell(configuration.getCartonNoCellIndex());
                if (CellType.BLANK.equals(cartonNoCell.getCellType()))
                {
                    continue;
                }
                if (cartonNoCell.getCellType().equals(CellType.STRING))
                {
                    if (StringUtils.isEmpty(cartonNoCell.getStringCellValue()))
                    {
                        continue;
                    }
                    cartonData.setCartonNo(Integer.parseInt(cartonNoCell.getStringCellValue()));
                }
                else if (cartonNoCell.getCellType().equals(CellType.NUMERIC))
                {
                    if (cartonNoCell.getNumericCellValue() == 0)
                    {
                        continue;
                    }
                    cartonData.setCartonNo(new Double(cartonNoCell.getNumericCellValue()).intValue());
                }

                Cell gwCell = row.getCell(configuration.getGwCellIndex());
                if (gwCell.getCellType().equals(CellType.STRING))
                {
                    cartonData.setGrossWeight(Double.parseDouble(gwCell.getStringCellValue()));
                }
                else if (gwCell.getCellType().equals(CellType.NUMERIC))
                {
                    cartonData.setGrossWeight(gwCell.getNumericCellValue());
                }

                Cell sizeCell = row.getCell(configuration.getSizeCellIndex());
                if (sizeCell.getCellType().equals(CellType.STRING))
                {
                    cartonData.setSize(convertSizeUnit(sizeCell.getStringCellValue()));
                }
                else if (sizeCell.getCellType().equals(CellType.NUMERIC))
                {
                    cartonData.setSize(new BigDecimal(sizeCell.getNumericCellValue()).toPlainString());
                }

                Cell volumnCell = row.getCell(configuration.getSizeCellIndex() + 1);
                if (volumnCell.getCellType().equals(CellType.STRING))
                {
                    cartonData.setVolumn(Double.parseDouble(volumnCell.getStringCellValue()));
                }
                else if (volumnCell.getCellType().equals(CellType.NUMERIC))
                {
                    cartonData.setVolumn(volumnCell.getNumericCellValue());
                }

                if (cartonNameCellIndex != -1)
                {
                    Cell cartonNameCell = row.getCell(cartonNameCellIndex);
                    if (cartonNameCell.getCellType().equals(CellType.STRING))
                    {
                        cartonData.setCartonName(cartonNameCell.getStringCellValue());
                    }
                    else if (cartonNameCell.getCellType().equals(CellType.NUMERIC))
                    {
                        cartonData.setCartonName(new BigDecimal(cartonNameCell.getNumericCellValue()).toPlainString());
                    }
                }

                cartonDataList.add(cartonData);

                // validate 总箱数
                if (!rowIterator.hasNext() && cartonData.getCartonNo() != cartonDataList.size())
                {
                    errorMsg.append("Total of carton not equal last carton number");
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                logger.error(e.getMessage());
                errorMsg.append(row.getRowNum() + "has error." + e.getMessage());
            }
        }
        if (errorMsg.length() > 0)
        {
            response.setErrorMsg(errorMsg.toString());
        }
        else
        {
            for (CartonData cartonData : cartonDataList)
            {
                if(StringUtils.isEmpty(cartonData.getCartonName())) {
                    continue;
                }
                List<CartonData> dbCartonDataList = cartonDataService.findByCartonName(cartonData.getCartonName());
                if (dbCartonDataList != null && dbCartonDataList.size() > 0)
                {
                    cartonDataService.delete(dbCartonDataList, CommonConstant.systemUserAccountId);
                }
            }

            cartonDataService.create(cartonDataList, CommonConstant.systemUserAccountId,
                    CommonConstant.defaultSystemLanguage);

            response.setCartonDataList(cartonDataList);
        }
        return response;
    }

    private int getCartonNameCellIndex(Row row)
    {
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext())
        {
            Cell cell = cellIterator.next();
            if (cell.getCellType().equals(CellType.STRING))
            {
                String value = cell.getStringCellValue();
                if (StringUtils.isEmpty(value))
                {
                    return -1;
                }
                if ("BOX NAME".equals(value.trim().toUpperCase()))
                {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1;
    }

    private String getPlNo(Sheet sheet, CartonDataPickConfiguration configuration)
    {
        int plNorowNumber = configuration.getPlNoRowIndex();
        int plNocellNumber = configuration.getPlNoCellIndex();

        Row plNoRow = sheet.getRow(plNorowNumber);
        if (plNoRow == null)
        {
            return null;
        }
        Cell plNoRowCell = plNoRow.getCell(plNocellNumber);
        if (plNoRowCell == null)
        {
            return null;
        }

        String plNo = plNoRowCell.getStringCellValue();
        if (StringUtils.isEmpty(plNo))
        {
            return null;
        }

        int comonIndex = plNo.indexOf(".");
        if (comonIndex == -1)
        {
            comonIndex = plNo.indexOf(":");
            if (comonIndex == -1)
            {
                comonIndex = plNo.indexOf("：");
            }
        }
        if (comonIndex == -1)
        {
            return plNo;
        }

        plNo = plNo.substring(comonIndex + 1, plNo.length());
        if (StringUtils.isEmpty(plNo))
        {
            plNoRowCell = plNoRow.getCell(plNocellNumber + 1);
            if (plNoRowCell.getCellType().equals(CellType.STRING))
            {
                plNo = plNoRowCell.getStringCellValue();
            }
        }
        plNo = plNo.replace("：", "");
        plNo = plNo.replace(":", "");
        return plNo;
    }

    protected boolean isEndRow(Cell firstCell, CartonDataPickConfiguration configuration)
    {
        String cellStr = firstCell.getStringCellValue();
        if (firstCell.getCellType().equals(CellType.STRING))
        {
            cellStr = firstCell.getStringCellValue();
            if (StringUtils.isEmpty(cellStr))
            {
                return false;
            }
        }
        else if (firstCell.getCellType().equals(CellType.NUMERIC))
        {
            cellStr = firstCell.getNumericCellValue() + "";
        }
        cellStr = cellStr.toUpperCase();
        return cellStr.startsWith(configuration.getEndRowIndicator());
    }

    protected String convertSizeUnit(String stringCellValue)
    {
        String[] dimensions = stringCellValue.split("\\*");
        StringBuffer convertedStr = new StringBuffer();
        for (String dimension : dimensions)
        {
            dimension = dimension.trim();
            int di = Integer.parseInt(dimension);
            BigDecimal decimal = new BigDecimal(di);
            decimal = decimal.divide(new BigDecimal(10)).setScale(0, BigDecimal.ROUND_HALF_UP);
            convertedStr.append(decimal.intValue());
            convertedStr.append("*");
        }
        return convertedStr.substring(0, convertedStr.length() - 1);
    }
}
