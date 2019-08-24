package com.polarj.common.utility.dbf;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DBFReader
{
    private DataInputStream stream;

    private JDBField[] fields;

    private byte[] nextRecord;

    private int nFieldCount;

    public DBFReader(String s) throws JDBFException
    {
        this.stream = null;
        this.fields = null;
        this.nextRecord = null;
        this.nFieldCount = 0;
        try
        {
            init(new FileInputStream(s));
        }
        catch (FileNotFoundException filenotfoundexception)
        {
            throw new JDBFException(filenotfoundexception);
        }
    }

    public DBFReader(InputStream inputstream) throws JDBFException
    {
        this.stream = null;
        this.fields = null;
        this.nextRecord = null;
        init(inputstream);
    }

    private void init(InputStream inputstream) throws JDBFException
    {
        try
        {
            this.stream = new DataInputStream(inputstream);
            int i = readHeader();
            this.fields = new JDBField[i];
            int j = 1;
            for (int k = 0; k < i; ++k)
            {
                this.fields[k] = readFieldHeader();
                if (this.fields[k] != null)
                {
                    this.nFieldCount += 1;
                    j += this.fields[k].getLength();
                }

            }

            this.nextRecord = new byte[j];
            try
            {
                this.stream.readFully(this.nextRecord);
            }
            catch (EOFException eofexception)
            {
                this.nextRecord = null;
                this.stream.close();
            }

            int pos = 0;
            for (int p = 0; p < j; ++p)
            {
                if ((this.nextRecord[p] == 32) || (this.nextRecord[p] == 42))
                {
                    pos = p;
                    break;
                }
            }
            if (pos > 0)
            {
                byte[] others = new byte[pos];
                this.stream.readFully(others);

                for (int p = 0; p < j - pos; ++p)
                {
                    this.nextRecord[p] = this.nextRecord[(p + pos)];
                }
                for (int p = 0; p < pos; ++p)
                {
                    this.nextRecord[(j - p - 1)] = others[(pos - p - 1)];
                }
            }
        }
        catch (IOException ioexception)
        {
            throw new JDBFException(ioexception);
        }
    }

    private int readHeader() throws IOException, JDBFException
    {
        byte[] abyte0 = new byte[16];
        try
        {
            this.stream.readFully(abyte0);
        }
        catch (EOFException eofexception)
        {
            throw new JDBFException("Unexpected end of file reached.");
        }
        int i = abyte0[8];
        if (i < 0)
            i += 256;
        i += 256 * abyte0[9];
        i = --i / 32;
        --i;
        try
        {
            this.stream.readFully(abyte0);
        }
        catch (EOFException eofexception1)
        {
            throw new JDBFException("Unexpected end of file reached.");
        }
        return i;
    }

    private JDBField readFieldHeader() throws IOException, JDBFException
    {
        byte[] abyte0 = new byte[16];
        try
        {
            this.stream.readFully(abyte0);
        }
        catch (EOFException eofexception)
        {
            throw new JDBFException("Unexpected end of file reached.");
        }

        if ((abyte0[0] == 13) || (abyte0[0] == 0))
        {
            this.stream.readFully(abyte0);
            return null;
        }

        StringBuffer stringbuffer = new StringBuffer(10);
        int i = 0;
        for (i = 0; i < 10; ++i)
        {
            if (abyte0[i] == 0)
            {
                break;
            }
        }
        stringbuffer.append(new String(abyte0, 0, i));

        char c = (char) abyte0[11];
        try
        {
            this.stream.readFully(abyte0);
        }
        catch (EOFException eofexception1)
        {
            throw new JDBFException("Unexpected end of file reached.");
        }

        int j = abyte0[0];
        int k = abyte0[1];
        if (j < 0)
            j += 256;
        if (k < 0)
            k += 256;
        return new JDBField(stringbuffer.toString(), c, j, k);
    }

    public int getFieldCount()
    {
        return this.nFieldCount;
    }

    public JDBField getField(int i)
    {
        return this.fields[i];
    }

    public boolean hasNextRecord()
    {
        return (this.nextRecord != null);
    }

    public Object[] nextRecord() throws JDBFException
    {
        if (!(hasNextRecord()))
        {
            throw new JDBFException("No more records available.");
        }
        Object[] aobj = new Object[this.nFieldCount];
        int i = 1;
        for (int j = 0; j < aobj.length; ++j)
        {
            int k = this.fields[j].getLength();
            StringBuffer stringbuffer = new StringBuffer(k);
            stringbuffer.append(new String(this.nextRecord, i, k));
            aobj[j] = this.fields[j].parse(stringbuffer.toString());
            i += this.fields[j].getLength();
        }
        try
        {
            this.stream.readFully(this.nextRecord);
        }
        catch (EOFException eofexception)
        {
            this.nextRecord = null;
        }
        catch (IOException ioexception)
        {
            throw new JDBFException(ioexception);
        }
        return aobj;
    }

    public Object[] nextRecord(Charset charset) throws JDBFException
    {
        if (!(hasNextRecord()))
        {
            throw new JDBFException("No more records available.");
        }
        Object[] aobj = new Object[this.nFieldCount];
        int i = 1;
        for (int j = 0; j < aobj.length; ++j)
        {
            int k = this.fields[j].getLength();
            StringBuffer stringbuffer = new StringBuffer(k);
            stringbuffer.append(new String(this.nextRecord, i, k, charset));
            aobj[j] = this.fields[j].parse(stringbuffer.toString());
            i += this.fields[j].getLength();
        }
        try
        {
            this.stream.readFully(this.nextRecord);
        }
        catch (EOFException eofexception)
        {
            this.nextRecord = null;
        }
        catch (IOException ioexception)
        {
            throw new JDBFException(ioexception);
        }
        return aobj;
    }

    public void close() throws JDBFException
    {
        this.nextRecord = null;
        try
        {
            this.stream.close();
        }
        catch (IOException ioexception)
        {
            throw new JDBFException(ioexception);
        }
    }
}
