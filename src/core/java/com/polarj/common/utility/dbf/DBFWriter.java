package com.polarj.common.utility.dbf;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class DBFWriter
{
    private BufferedOutputStream stream;

    private int recCount;

    private JDBField[] fields;

    private String fileName;

    private String dbfEncoding;

    public DBFWriter(String s, JDBField[] ajdbfield) throws JDBFException
    {
        this.stream = null;
        this.recCount = 0;
        this.fields = null;
        this.fileName = null;
        this.dbfEncoding = null;
        this.fileName = s;
        try
        {
            init(new FileOutputStream(s), ajdbfield);
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            throw new JDBFException(filenotfoundexception);
        }
    }

    public DBFWriter(OutputStream outputstream, JDBField[] ajdbfield) throws JDBFException
    {
        this.stream = null;
        this.recCount = 0;
        this.fields = null;
        this.fileName = null;
        this.dbfEncoding = null;
        init(outputstream, ajdbfield);
    }

    public DBFWriter(String s, JDBField[] ajdbfield, String s1) throws JDBFException
    {
        this.stream = null;
        this.recCount = 0;
        this.fields = null;
        this.fileName = null;
        this.dbfEncoding = null;
        this.fileName = s;
        try
        {
            this.dbfEncoding = s1;
            init(new FileOutputStream(s), ajdbfield);
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            throw new JDBFException(filenotfoundexception);
        }
    }

    private void init(OutputStream outputstream, JDBField[] ajdbfield) throws JDBFException
    {
        this.fields = ajdbfield;
        try
        {
            this.stream = new BufferedOutputStream(outputstream);
            writeHeader();
            for (int i = 0; i < ajdbfield.length; ++i)
            {
                writeFieldHeader(ajdbfield[i]);
            }

            this.stream.write(13);
            this.stream.flush();
        }
        catch (Exception exception)
        {
            throw new JDBFException(exception);
        }
    }

    private void writeHeader() throws IOException
    {
        byte[] abyte0 = new byte[16];
        abyte0[0] = 3;
        Calendar calendar = Calendar.getInstance();
        abyte0[1] = (byte) (calendar.get(1) - 1900);
        abyte0[2] = (byte) calendar.get(2);
        abyte0[3] = (byte) calendar.get(5);
        abyte0[4] = 0;
        abyte0[5] = 0;
        abyte0[6] = 0;
        abyte0[7] = 0;
        int i = (this.fields.length + 1) * 32 + 1;
        abyte0[8] = (byte) (i % 256);
        abyte0[9] = (byte) (i / 256);
        int j = 1;
        for (int k = 0; k < this.fields.length; ++k)
        {
            j += this.fields[k].getLength();
        }

        abyte0[10] = (byte) (j % 256);
        abyte0[11] = (byte) (j / 256);
        abyte0[12] = 0;
        abyte0[13] = 0;
        abyte0[14] = 0;
        abyte0[15] = 0;
        this.stream.write(abyte0, 0, abyte0.length);
        for (int l = 0; l < 16; ++l)
        {
            abyte0[l] = 0;
        }

        this.stream.write(abyte0, 0, abyte0.length);
    }

    private void writeFieldHeader(JDBField jdbfield) throws IOException
    {
        byte[] abyte0 = new byte[16];
        String s = jdbfield.getName();
        int i = s.length();
        if (i > 10)
        {
            i = 10;
        }
        for (int j = 0; j < i; ++j)
        {
            abyte0[j] = (byte) s.charAt(j);
        }

        for (int k = i; k <= 10; ++k)
        {
            abyte0[k] = 0;
        }

        abyte0[11] = (byte) jdbfield.getType();
        abyte0[12] = 0;
        abyte0[13] = 0;
        abyte0[14] = 0;
        abyte0[15] = 0;
        this.stream.write(abyte0, 0, abyte0.length);
        for (int l = 0; l < 16; ++l)
        {
            abyte0[l] = 0;
        }

        abyte0[0] = (byte) jdbfield.getLength();
        abyte0[1] = (byte) jdbfield.getDecimalCount();
        this.stream.write(abyte0, 0, abyte0.length);
    }

    public void addRecord(Object[] aobj) throws JDBFException
    {
        if (aobj.length != this.fields.length)
        {
            throw new JDBFException("Error adding record: Wrong number of values. Expected " + this.fields.length
                    + ", got " + aobj.length + ".");
        }

        int i = 0;
        for (int j = 0; j < this.fields.length; ++j)
        {
            i += this.fields[j].getLength();
        }

        byte[] abyte0 = new byte[i];
        int k = 0;
        for (int l = 0; l < this.fields.length; ++l)
        {
            String s = this.fields[l].format(aobj[l]);
            byte[] abyte1;
            try
            {
                if (this.dbfEncoding != null)
                {
                    abyte1 = s.getBytes(this.dbfEncoding);
                }
                else
                    abyte1 = s.getBytes();
            }
            catch (UnsupportedEncodingException unsupportedencodingexception)
            {
                throw new JDBFException(unsupportedencodingexception);
            }
            for (int i1 = 0; i1 < this.fields[l].getLength(); ++i1)
            {
                abyte0[(k + i1)] = abyte1[i1];
            }

            k += this.fields[l].getLength();
        }
        try
        {
            this.stream.write(32);
            this.stream.write(abyte0, 0, abyte0.length);
            this.stream.flush();
        }
        catch (IOException ioexception)
        {
            throw new JDBFException(ioexception);
        }
        this.recCount += 1;
    }

    public void close() throws JDBFException
    {
        try
        {
            this.stream.write(26);
            this.stream.close();
            RandomAccessFile randomaccessfile = new RandomAccessFile(this.fileName, "rw");
            randomaccessfile.seek(4L);
            byte[] abyte0 = new byte[4];
            abyte0[0] = (byte) (this.recCount % 256);
            abyte0[1] = (byte) (this.recCount / 256 % 256);
            abyte0[2] = (byte) (this.recCount / 65536 % 256);
            abyte0[3] = (byte) (this.recCount / 16777216 % 256);
            randomaccessfile.write(abyte0, 0, abyte0.length);
            randomaccessfile.close();
        }
        catch (IOException ioexception)
        {
            throw new JDBFException(ioexception);
        }
    }
}