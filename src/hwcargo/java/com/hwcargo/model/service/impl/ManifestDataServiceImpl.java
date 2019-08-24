package com.hwcargo.model.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.hwcargo.model.ManifestData;
import com.hwcargo.model.repository.ManifestDataRepos;
import com.hwcargo.model.service.ManifestDataService;
import com.polarj.model.UserAccount;
import com.polarj.model.service.impl.EntityServiceImpl;

@Service
public class ManifestDataServiceImpl extends EntityServiceImpl<ManifestData, Integer> implements ManifestDataService
{
    @Override
    public Boolean upload(String destFileName, UserAccount userAcc, String workLang)
    {

        List<ManifestData> entities = readFile(destFileName);

        if (entities != null && entities.size() > 0)
        {
            entities = this.create(entities, userAcc.getId(), workLang);
        }

        return (entities != null && entities.size() > 0);
    }

    private List<ManifestData> readFile(String fileName)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //format.setTimeZone(TimeZone.getTimeZone("CTT"));

        List<ManifestData> list = new ArrayList<>();
        try
        {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(fileName));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            String mawb = "";
            String flightNo = "";
            String deadLine = "";
            String destination = "";

            String etd = "";
            String eta = "";
            Integer qtyTotal = null;
            Double weightTotal = null;

            if (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                Cell mawbCell = row.getCell(1);
                if (mawbCell.getCellType().equals(CellType.NUMERIC))
                {
                    mawb = new BigDecimal(mawbCell.getNumericCellValue()).toPlainString();
                }
                else if (mawbCell.getCellType().equals(CellType.STRING))
                {
                    mawb = mawbCell.getStringCellValue();
                }

                Cell flightNoCell = row.getCell(3);
                if (flightNoCell.getCellType().equals(CellType.NUMERIC))
                {
                    flightNo = new BigDecimal(flightNoCell.getNumericCellValue()).toPlainString();
                }
                else if (flightNoCell.getCellType().equals(CellType.STRING))
                {
                    flightNo = flightNoCell.getStringCellValue();
                }
                
                Cell destinationCell = row.getCell(5);
                if (destinationCell.getCellType().equals(CellType.NUMERIC))
                {
                    destination = new BigDecimal(destinationCell.getNumericCellValue()).toPlainString();
                }
                else if (destinationCell.getCellType().equals(CellType.STRING))
                {
                    destination = destinationCell.getStringCellValue();
                }

                Cell deadLineCell = row.getCell(7);
                if (deadLineCell != null)
                {
                    if (deadLineCell.getCellType().equals(CellType.NUMERIC))
                    {
                        deadLine = format.format(deadLineCell.getDateCellValue());
                    }
                    else if (deadLineCell.getCellType().equals(CellType.STRING))
                    {
                        deadLine = deadLineCell.getStringCellValue();
                    }
                }
            }

            if (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                Cell etdCell = row.getCell(1);
                if (etdCell != null)
                {
                    if (etdCell.getCellType().equals(CellType.NUMERIC))
                    {
                        etd = format.format(etdCell.getDateCellValue());
                    }
                    else if (etdCell.getCellType().equals(CellType.STRING))
                    {
                        etd = etdCell.getStringCellValue();
                    }
                }

                Cell etaCell = row.getCell(3);
                if (etaCell != null)
                {
                    if (etaCell.getCellType().equals(CellType.NUMERIC))
                    {
                        eta = format.format(etaCell.getDateCellValue());
                    }
                    else if (etaCell.getCellType().equals(CellType.STRING))
                    {
                        eta = etaCell.getStringCellValue();
                    }
                }

                Cell qtyTotalCell = row.getCell(5);
                if (qtyTotalCell != null)
                {
                    if (qtyTotalCell.getCellType().equals(CellType.NUMERIC))
                    {
                        qtyTotal = new Double(qtyTotalCell.getNumericCellValue()).intValue();
                    }
                    else if (qtyTotalCell.getCellType().equals(CellType.STRING))
                    {
                        String qtyTotalStr = qtyTotalCell.getStringCellValue();
                        if (StringUtils.isNotEmpty(qtyTotalStr))
                        {
                            try
                            {
                                qtyTotal = Integer.parseInt(qtyTotalStr);
                            }
                            catch (Exception e)
                            {
                                logger.error(e.getMessage());
                            }
                        }
                    }
                }

                Cell weightTotalCell = row.getCell(7);
                if (weightTotalCell != null)
                {
                    if (weightTotalCell.getCellType().equals(CellType.NUMERIC))
                    {
                        weightTotal = weightTotalCell.getNumericCellValue();
                    }
                    else if (weightTotalCell.getCellType().equals(CellType.STRING))
                    {
                        String weightTotalStr = weightTotalCell.getStringCellValue();
                        if (StringUtils.isNotEmpty(weightTotalStr))
                        {
                            weightTotal = Double.parseDouble(weightTotalStr);
                        }
                    }
                }
            }

            rowIterator.next();
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                ManifestData data = new ManifestData();
                data.setMawb(mawb);
                data.setFlightNo(flightNo);
                data.setDeadLine(deadLine);
                data.setDestination(destination);

                data.setEta(eta);
                data.setEtd(etd);
                data.setWeightTotal(weightTotal);
                data.setQtyTotal(qtyTotal);

                Cell serialCell = row.getCell(0);
                if (serialCell != null)
                {
                    if (serialCell.getCellType().equals(CellType.NUMERIC))
                    {
                        Double serial = serialCell.getNumericCellValue();
                        data.setSerialNum(serial.intValue());
                    }
                    else if (serialCell.getCellType().equals(CellType.STRING))
                    {
                        String serial = serialCell.getStringCellValue();
                        if (StringUtils.isEmpty(serial))
                        {
                            break;
                        }
                        if (!StringUtils.isNumeric(serial))
                        {
                            break;
                        }
                        if (StringUtils.isNotEmpty(serial))
                        {
                            double seriald = Double.parseDouble(serial);
                            data.setSerialNum(new Double(seriald).intValue());
                        }
                    }
                }
                if (data.getSerialNum() == 0)
                {
                    break;
                }

                Cell hawbCell = row.getCell(1);
                if (hawbCell != null)
                {
                    if (hawbCell.getCellType().equals(CellType.NUMERIC))
                    {
                        String hawb = new BigDecimal(hawbCell.getNumericCellValue()).toPlainString();
                        data.setHawb(hawb);
                    }
                    else if (hawbCell.getCellType().equals(CellType.STRING))
                    {
                        String hawb = hawbCell.getStringCellValue();
                        data.setHawb(hawb);
                    }
                }

                Cell three8NumberCell = row.getCell(2);
                if (three8NumberCell != null)
                {
                    if (three8NumberCell.getCellType().equals(CellType.NUMERIC))
                    {
                        String three8Number = new BigDecimal(three8NumberCell.getNumericCellValue()).toPlainString();
                        data.setThree8Number(three8Number);
                    }
                    else if (three8NumberCell.getCellType().equals(CellType.STRING))
                    {
                        String three8Number = three8NumberCell.getStringCellValue();
                        data.setThree8Number(three8Number);
                    }
                }

                Cell electrInneredCell = row.getCell(3);
                if (electrInneredCell != null)
                {
                    String electrInnered = electrInneredCell.getStringCellValue();
                    data.setElectrInnered(electrInnered);
                }

                Cell productNameCell = row.getCell(4);
                if (productNameCell != null)
                {
                    String productName = productNameCell.getStringCellValue();
                    data.setProductName(productName);
                }

                Cell qtyCell = row.getCell(5);
                if (qtyCell != null)
                {
                    if (qtyCell.getCellType().equals(CellType.NUMERIC))
                    {
                        Double qty = qtyCell.getNumericCellValue();
                        data.setQty(qty.intValue());
                    }
                    else if (qtyCell.getCellType().equals(CellType.STRING))
                    {
                        String qty = qtyCell.getStringCellValue();
                        if (StringUtils.isNotEmpty(qty))
                        {
                            data.setQty(Integer.parseInt(qty));
                        }
                    }
                }

                Cell weightCell = row.getCell(6);
                if (weightCell != null)
                {
                    if (weightCell.getCellType().equals(CellType.NUMERIC))
                    {
                        data.setWeight(weightCell.getNumericCellValue());
                    }
                    else if (weightCell.getCellType().equals(CellType.STRING))
                    {
                        String weight = weightCell.getStringCellValue();
                        if (StringUtils.isNotEmpty(weight))
                        {
                            data.setWeight(Double.parseDouble(weight));
                        }
                    }
                }

                Cell sizeCell = row.getCell(7);
                if (sizeCell != null)
                {
                    String size = sizeCell.getStringCellValue();
                    data.setDimensions(size);
                }

                Cell volumnCell = row.getCell(8);
                if (volumnCell != null)
                {

                    if (volumnCell.getCellType().equals(CellType.NUMERIC))
                    {
                        data.setVolumn(volumnCell.getNumericCellValue());
                    }
                    else if (volumnCell.getCellType().equals(CellType.STRING))
                    {
                        String volumn = volumnCell.getStringCellValue();
                        if (StringUtils.isNotEmpty(volumn))
                        {
                            data.setVolumn(Double.parseDouble(volumn));
                        }
                    }
                }

                Cell plNoCell = row.getCell(9);
                if (plNoCell != null)
                {

                    if (plNoCell.getCellType().equals(CellType.NUMERIC))
                    {
                        String plNo = new BigDecimal(plNoCell.getNumericCellValue()).toPlainString();
                        data.setPackingListNo(plNo);
                    }
                    else if (plNoCell.getCellType().equals(CellType.STRING))
                    {
                        String plNo = plNoCell.getStringCellValue();
                        data.setPackingListNo(plNo);
                    }
                }

                list.add(data);
            }

        }
        catch (EncryptedDocumentException | IOException e)
        {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return list;
    }

    @Override
    public ManifestData findByPlNumber(String plNumber)
    {
        ManifestDataRepos repos = (ManifestDataRepos) this.getRepos();
        return repos.findFirstByPackingListNoLikeOrderByIdDesc("%" + plNumber + "%");
    }
}
