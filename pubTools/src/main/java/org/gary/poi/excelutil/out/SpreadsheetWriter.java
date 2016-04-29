package org.gary.poi.excelutil.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;
 public class SpreadsheetWriter {
        private final Writer _out;
        private int _rownum;

        public SpreadsheetWriter(Writer out){
            _out = out;
        }

        public void beginSheet() throws IOException {
            _out.write("<?xml version=\"1.0\" encoding=\""+"UTF-8"+"\"?>" +
                    "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">" );
            _out.write("<sheetData>\n");
        }

        public void endSheet() throws IOException {
            _out.write("</sheetData>");
            _out.write("</worksheet>");
        }

        /**
         * Insert a new row
         *
         * @param rownum 0-based row number
         */
        public void insertRow(int rownum) throws IOException {
            _out.write("<row r=\""+(rownum+1)+"\">\n");
            this._rownum = rownum;
        }

        /**
         * Insert row end marker
         */
        public void endRow() throws IOException {
            _out.write("</row>\n");
        }

        public void createCell(int columnIndex, String value, int styleIndex) throws IOException {
            String ref = new CellReference(_rownum, columnIndex).formatAsString();
            _out.write("<c r=\""+ref+"\" t=\"inlineStr\"");
            if(styleIndex != -1) _out.write(" s=\""+styleIndex+"\"");
            _out.write(">");
            _out.write("<is><t><![CDATA["+ org.apache.commons.lang3.StringEscapeUtils.escapeXml10( value ) +"]]></t></is>");
            _out.write("</c>");
        }

        public void createCell(int columnIndex, String value) throws IOException {
            createCell(columnIndex, value, -1);
        }

        public void createCell(int columnIndex, double value, int styleIndex) throws IOException {
            String ref = new CellReference(_rownum, columnIndex).formatAsString();
            _out.write("<c r=\""+ref+"\" t=\"n\"");
            if(styleIndex != -1) _out.write(" s=\""+styleIndex+"\"");
            _out.write(">");
            _out.write("<v>"+value+"</v>");
            _out.write("</c>");
        }

        public void createCell(int columnIndex, double value) throws IOException {
            createCell(columnIndex, value, -1);
        }

        public void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException {
            createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
        }
        
        public void substitute(File zipfile, File tmpfile, String entry, OutputStream out) throws IOException {
            ZipFile zip = new ZipFile(zipfile);
            ZipOutputStream zos = new ZipOutputStream(out);
            @SuppressWarnings("unchecked")
            Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
            while (en.hasMoreElements()) {
                ZipEntry ze = en.nextElement();
                if(!ze.getName().equals(entry)){
                    zos.putNextEntry(new ZipEntry(ze.getName()));
                    InputStream is = zip.getInputStream(ze);
                    copyStream(is, zos);
                    is.close();
                }
            }
            zos.putNextEntry(new ZipEntry(entry));
            InputStream is = new FileInputStream(tmpfile);
            copyStream(is, zos);
            is.close();
            zos.close();
            zip.close() ;
        }
        
        private static void copyStream(InputStream in, OutputStream out) throws IOException {
            byte[] chunk = new byte[1024];
            int count;
            while ((count = in.read(chunk)) >=0 ) {
              out.write(chunk,0,count);
            }
        }
    }