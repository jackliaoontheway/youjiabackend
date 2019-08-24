package com.polarj.common.utility.report;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

public abstract class ReportFileReaderWriter<T>
{
    private String datePatternString = "yyyy-MM-dd";

    private String dateTimePatternString = "yyyy-MM-dd HH:mm:ss";

    private @Getter @Setter String extraDatePatternString;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat(datePatternString);

    protected SimpleDateFormat dateTimeFormat = new SimpleDateFormat(dateTimePatternString);

    protected String defaultChartSet = System.getProperties().getProperty("file.encoding");

    protected String inputFileChartSet = "";

    protected final Logger logger;

    protected FieldNameAndHeaderMapping fieldNameAndHeaderMapping;

    public ReportFileReaderWriter(FieldNameAndHeaderMapping fieldNameAndHeaderMapping)
    {
        this(null, null, fieldNameAndHeaderMapping);
    }

    public ReportFileReaderWriter(String datePattern, String datetimePattern,
            FieldNameAndHeaderMapping fieldNameAndHeaderMapping)
    {
        logger = LoggerFactory.getLogger(this.getClass());
        this.fieldNameAndHeaderMapping = fieldNameAndHeaderMapping;
        if (datePattern != null && datePattern.length() > 0)
        {
            try
            {
                this.dateFormat = new SimpleDateFormat(datePattern);
            }
            catch (Exception e)
            {
                logger.error("ReportFileReaderWriter datePattern=" + datePattern + e.getMessage(), e);
                this.dateFormat = new SimpleDateFormat(datePatternString);
            }
        }
        if (datetimePattern != null && datetimePattern.length() > 0)
        {
            try
            {
                this.dateTimeFormat = new SimpleDateFormat(datetimePattern);
            }
            catch (Exception e)
            {
                logger.error("ReportFileReaderWriter datetimePattern=" + datetimePattern + e.getMessage(), e);
                this.dateTimeFormat = new SimpleDateFormat(dateTimePatternString);
            }
        }
    }

    private boolean found = false;

    private String encoding = null;

    final protected String guessFileEncoding(InputStream inputStream) throws FileNotFoundException, IOException
    {
        nsDetector det = new nsDetector();
        det.Init(new nsICharsetDetectionObserver()
        {
            public void Notify(String charset)
            {
                found = true;
                encoding = charset;
            }
        });
        BufferedInputStream imp = new BufferedInputStream(inputStream);
        byte[] buf = new byte[1024];
        int len;
        boolean done = false;
        boolean isAscii = true;

        while ((len = imp.read(buf, 0, buf.length)) != -1)
        {
            // Check if the stream is only ascii.
            if (isAscii)
                isAscii = det.isAscii(buf, len);

            // DoIt if non-ascii and not done yet.
            if (!isAscii && !done)
                done = det.DoIt(buf, len, false);
        }
        det.DataEnd();

        if (isAscii)
        {
            encoding = "ASCII";
            found = true;
        }

        if (!found)
        {
            String prob[] = det.getProbableCharsets();
            if (prob.length > 0)
            {
                encoding = prob[0];
            }
            else
            {
                return null;
            }
        }
        return encoding;
    }

    final protected String guessFileEncoding(String fileName) throws FileNotFoundException, IOException
    {
        FileInputStream fis = new FileInputStream(fileName);
        String res = guessFileEncoding(fis);
        fis.close();
        return res;
    }

    abstract public List<T> readReportFile(Class<T> rowClass, String fileName) throws Exception;

    abstract public void writeReportFile(List<T> content, String fileName) throws Exception;
}
