package com.example.transactiondynamic.service;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.transactiondynamic.service.DocxIO.*;
import static com.example.transactiondynamic.service.DocxTableIO.rFonts;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 15:14 2020/1/7
 */
public class DocxTextIO {

    static String filePath = "C:\\Users\\xueweidong\\Desktop\\湖南省长沙市星沙县美美时尚女装店\\对公商户名单.xlsx";
    static String targetFilePath = "C:\\Users\\xueweidong\\Desktop\\湖南省长沙市星沙县美美时尚女装店\\目标";
    static String sourceFilePath = "C:\\Users\\xueweidong\\Desktop\\湖南省长沙市星沙县美美时尚女装店\\特约商户受理银联卡业务协议target.docx";
    static String context1 = "乙方：";
    static String context2 = "甲方：易生支付有限公司      乙方：";
    static String context3 = "    丙方：深圳盒子信息科技有限公司";
    static ArrayList<String> text = new ArrayList<>(50);
    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        XSSFSheet sheet =  workbook.getSheetAt(0);
        for (int i = 1; i < 51; i++){
            XSSFRow row = sheet.getRow(i);
            XSSFCell cell = row.getCell(2);
            text.add(cell.getStringCellValue());
        }
        for (String textValue:text){
            replaceTask(sourceFilePath, targetFilePath, textValue);
        }

    }

    private static void replaceTask(String filePath, String newFilePath, String text) throws Exception {
        File file = new File(filePath);
        //加载文件
        WordprocessingMLPackage mlPackage = loadDocxFile(file);
        ObjectFactory factory = Context.getWmlObjectFactory();
        //获取所有段落
        List<Object> paragraphs = getContext(getDocument(mlPackage));

        //拿到指定段落***************************************************
        P p1 = getParagraph(paragraphs, "乙方：湖南省长沙市星沙县美美时尚女装店");
        P p2 = getParagraph(paragraphs, "甲方：易生支付有限公司      乙方：湖南省长沙市星沙县美美时尚女装店    丙方：深圳盒子信息科技有限公司");
        removeAll(p1);
        removeAll(p2);
        R run1 = getRun(context1, text, "");setRPrBoldStyle(run1);
        R run2 = getRun(context2, text, context3);
        p1.getContent().add(run1);
        p2.getContent().add(run2);
        p1.getPPr().getRPr().setRFonts(rFonts);
        p2.getPPr().getRPr().setRFonts(rFonts);
        //保存文件
        mlPackage.save(new File(newFilePath + "\\" + text + "特约商户受理银联卡业务协议.docx"));
//        setFontMapper(mlPackage);
//        Docx4J.toPDF(mlPackage, new FileOutputStream(new File(newFilePath + "\\特约商户受理银联卡业务协议.pdf")));
    }

    private static R getRun(String before, String value, String after) {
        Text text = new Text();
        String result = before + value +"     "+ after;
        text.setValue(result);
        text.setSpace("preserve");
        R run = new R();
        run.getContent().add(text);
        return run;
    }
}
