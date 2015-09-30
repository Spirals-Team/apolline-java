package org.apolline.cmd.particule;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReadCrcCmdTest {
    protected OutputStream out;
    protected InputStream is;
    protected byte[] val_cmd={(byte)0x61,(byte)0x30};

    protected ReadCRCCmd cmd;

    @Before
    public void setUp(){
        out=mock(OutputStream.class);
        is=mock(InputStream.class);

        cmd=new ReadCRCCmd(out,is);
    }

    @Test
    public void testExecute() throws IOException {
        doNothing().when(out).write(val_cmd);

        cmd.execute();

        verify(out).write(val_cmd);
    }

    @Test
    public void test_findCmd(){
        assertArrayEquals(val_cmd, cmd.findCmd());
    }

    @Test
    public void test_isFinish_returnFalse(){
        cmd.nbResponse=1;

        assertFalse(cmd.isFinish());
    }

    @Test
    public void test_isFinish_returnFalse2(){
        cmd.nbResponse=3;

        assertFalse(cmd.isFinish());
    }

    @Test
    public void test_isFinish_returnTrue(){
        cmd.nbResponse=2;

        assertTrue(cmd.isFinish());
    }

    @Test
    public void testFindData(){
        cmd.nbResponse=0;
        cmd.readByte[1]=(byte)0xAA;

        cmd.findData();

        assertEquals(1, cmd.nbResponse);
        assertEquals((byte) 0xAA, cmd.readByte[1]);
    }

    @Test
    public void test_parseResponse(){
        cmd.data[0]=(byte)0x01;
        cmd.data[1]=(byte)0x00;
        cmd.resp=null;

        cmd.parseResponse();

        assertEquals("1", cmd.resp);
    }

    @Test
    public void test_parseResponse2(){
        cmd.data[0]=(byte)0x00;
        cmd.data[1]=(byte)0x01;
        cmd.resp=null;

        cmd.parseResponse();

        assertEquals("256",cmd.resp);
    }
}
