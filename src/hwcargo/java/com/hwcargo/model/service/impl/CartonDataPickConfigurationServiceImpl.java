package com.hwcargo.model.service.impl;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.hwcargo.model.CartonDataPickConfiguration;
import com.hwcargo.model.service.CartonDataPickConfigurationService;
import com.polarj.common.CommonConstant;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class CartonDataPickConfigurationServiceImpl extends EntityServiceImpl<CartonDataPickConfiguration, Integer>
        implements CartonDataPickConfigurationService
{

    @Override
    public CartonDataPickConfiguration findConfigurationByExcel(Sheet sheet)
    {

        List<CartonDataPickConfiguration> allConfigs = this.list(CommonConstant.defaultSystemLanguage);

        if (sheet == null || allConfigs == null)
        {
            return null;
        }

        for (CartonDataPickConfiguration configuration : allConfigs)
        {
            if (!checkPlNo(sheet, configuration))
            {
                continue;
            }
            if (!checkCartonNo(sheet, configuration))
            {
                continue;
            }
            if (!checkGw(sheet, configuration))
            {
                continue;
            }
            if (!checkSize(sheet, configuration))
            {
                continue;
            }
            return configuration;
        }

        return null;
    }

    private boolean checkSize(Sheet sheet, CartonDataPickConfiguration configuration)
    {
        Integer dataRowIndex = configuration.getDataRowIndex();
        if (dataRowIndex == null || configuration.getSizeIndicator() == null)
        {
            return false;
        }

        Cell cell = getCell(sheet, dataRowIndex - 1, configuration.getSizeCellIndex());
        if (cell == null)
        {
            return false;
        }
        String size = cell.getStringCellValue();
        if (size == null)
        {
            return false;
        }
        size = size.toUpperCase();
        return size.startsWith(configuration.getSizeIndicator().toUpperCase());
    }

    private boolean checkGw(Sheet sheet, CartonDataPickConfiguration configuration)
    {
        Integer dataRowIndex = configuration.getDataRowIndex();
        if (dataRowIndex == null || configuration.getGwIndicator() == null)
        {
            return false;
        }

        Cell cell = getCell(sheet, dataRowIndex - 1, configuration.getGwCellIndex());
        if (cell == null)
        {
            return false;
        }
        String gw = cell.getStringCellValue();
        if (gw == null)
        {
            return false;
        }
        gw = gw.toUpperCase();
        return gw.startsWith(configuration.getGwIndicator().toUpperCase());
    }

    private boolean checkCartonNo(Sheet sheet, CartonDataPickConfiguration configuration)
    {
        Integer dataRowIndex = configuration.getDataRowIndex();
        if (dataRowIndex == null || configuration.getCartonNoIndicator() == null)
        {
            return false;
        }

        Cell cell = getCell(sheet, dataRowIndex - 1, configuration.getCartonNoCellIndex());
        if (cell == null)
        {
            return false;
        }
        String cartonNo = cell.getStringCellValue();
        if (cartonNo == null)
        {
            return false;
        }
        cartonNo = cartonNo.toUpperCase();
        return cartonNo.startsWith(configuration.getCartonNoIndicator().toUpperCase());
    }

    private boolean checkPlNo(Sheet sheet, CartonDataPickConfiguration configuration)
    {
        Cell cell = getCell(sheet, configuration.getPlNoRowIndex(), configuration.getPlNoCellIndex());
        if (cell == null)
        {
            return false;
        }
        String plNo = null;
        if (cell.getCellType().equals(CellType.STRING))
        {
            plNo = cell.getStringCellValue();
        }
        if (plNo == null || configuration.getPlNoIndicator() == null)
        {
            return false;
        }
        plNo = plNo.toUpperCase();
        return plNo.startsWith(configuration.getPlNoIndicator().toUpperCase());
    }

    // private Cell getCell(Sheet sheet, Integer rowIndex, Integer cellIndex)
    // {
    // if (rowIndex != null && cellIndex != null && sheet.getRow(rowIndex) != null
    // && sheet.getRow(rowIndex).getCell(cellIndex) != null)
    // {
    // return sheet.getRow(rowIndex).getCell(cellIndex);
    // }
    // return null;
    // }

    private Cell getCell(Sheet sheet, Integer rowIndex, Integer cellIndex)
    {
        if (rowIndex == null)
        {
            return null;
        }
        if (cellIndex == null)
        {
            return null;
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null)
        {
            return null;
        }
        Cell cell = row.getCell(cellIndex);
        if (cell == null)
        {
            return null;
        }
        return cell;
    }
}
