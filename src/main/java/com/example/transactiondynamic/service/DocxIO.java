package com.example.transactiondynamic.service;

import lombok.extern.slf4j.Slf4j;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.docx4j.wml.*;

import javax.xml.bind.JAXBElement;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.transactiondynamic.service.DocxTableIO.*;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 14:30 2019/12/14
 */
@Slf4j
public class DocxIO {
    //表示四个\t
    private static final String space = "                ";
//    static String imagePath = "C:\\Users\\xueweidong\\Desktop\\湖南省长沙市星沙县美美时尚女装店\\签名.png";
    //模板文件
    static String filePath = "C:\\Users\\xueweidong\\Desktop\\湖南省长沙市星沙县美美时尚女装店\\爱心绿源葫芦岛农业科技有限公司_3.docx";
    static String context1 = "乙方：";
    static String context2 = "甲方：易生支付有限公司      乙方：";
    static String context3 = "    丙方：深圳盒子信息科技有限公司";

    static ArrayList<String> errList = new ArrayList();
    public static void main(String[] args) throws Exception {
        String dirPath = "G:\\合规\\pic";
        String newPath = "G:\\合规\\协议";
        String errPath = "G:\\合规\\err.txt";
        File errFile = new File(errPath);
        File dir = new File(dirPath);
        File[] list = dir.listFiles();
        assert list != null;
        for (File file : list) {
            if (file.isDirectory()) {
                for (File child : Objects.requireNonNull(file.listFiles())) {
                    //获取签名图片child
                    if (child.getName().contains("签名")&&(child.getName().substring(child.getName().lastIndexOf(".")).contains("jpg"))) {
                        replaceTask(filePath, child.getAbsolutePath(), newPath, child.getParentFile().getName());
                        break;
                    }
                }
            }
        }
        //错误信息输出到err.txt中
        BufferedWriter writer = new BufferedWriter(new FileWriter(errPath));
        for (String l:errList){
            writer.write(l + "\r\n");
        }
        writer.close();
    }

    private static void replaceTask(String filePath, String imagePath, String newFilePath, String text) throws Exception {
        File file = new File(filePath);
        //加载文件
        WordprocessingMLPackage mlPackage = loadDocxFile(file);
        ObjectFactory factory = Context.getWmlObjectFactory();
        //获取所有段落
        List<Object> paragraphs = getContext(getDocument(mlPackage));
        //拿到指定段落**************************************************
        P paragraph = getParagraph(paragraphs, "负责人签章");
        //要添加的内容
        R run = getDrawRun(factory, imagePath, mlPackage, text);
        if (run == null){
            return;
        }
        //删除原来段落的内容
        removeAll(paragraph);
        //设置段落格式
        setParagraphStyle(paragraph);
        paragraph.getContent().add(run);

        //拿到指定段落***************************************************
        P p1 = getParagraph(paragraphs, "乙方：爱心绿源葫芦岛农业科技有限公司");
        P p2 = getParagraph(paragraphs, "甲方：易生支付有限公司   乙方：爱心绿源葫芦岛农业科技有限公司       丙方：深圳盒子信息科技有限公司");
        removeAll(p1);
        removeAll(p2);
        R run1 = getRun(context1, text, "");setRPrBoldStyle(run1);
        R run2 = getRun(context2, text, context3);
        p1.getContent().add(run1);
        p2.getContent().add(run2);
        p1.getPPr().getRPr().setRFonts(rFonts);
        p2.getPPr().getRPr().setRFonts(rFonts);
        //拿到指定表格***************************************************
        Tbl tbl = getTbl(paragraphs, new Tbl());
        replaceTbl(tbl);
        //保存文件
        mlPackage.save(new File(newFilePath + "\\" + text + "特约商户受理银联卡业务协议.docx"));
//        setFontMapper(mlPackage);
//        Docx4J.toPDF(mlPackage, new FileOutputStream(new File(newFilePath + "\\特约商户受理银联卡业务协议.pdf")));
    }

    /**
     * @Description: 加粗
     */
    public static void setRPrBoldStyle(R r) {
        BooleanDefaultTrue b = new BooleanDefaultTrue();
        b.setVal(true);
        RPr rPr = new RPr();
        rPr.setB(b);
        r.setRPr(rPr);
    }

    //加载文件
    public static WordprocessingMLPackage loadDocxFile(File file) throws Docx4JException {
        return WordprocessingMLPackage.load(file);
    }

    //拿到指定段落
    public static P getParagraph(List<Object> paragraphs, String context) {
        P paragraph = new P();
        for (Object object : paragraphs) {
            if (object.toString().contains(context)) {
                paragraph = (P) object;
                break;
            }
        }
        return paragraph;
    }

    //拿到表格
    public static Tbl getTbl(List<Object> paragraphs, Tbl tbl){
        for (Object p : paragraphs) {
            if (p instanceof JAXBElement && ((JAXBElement) p).getValue() instanceof Tbl) {
                tbl = (Tbl) ((JAXBElement) p).getValue();
                return tbl;
            }
        }
        return null;
    }

    //拿到指定段落
    public static List<P> getParagraph(String context, List<Object> paragraphs) {
        List<P> result = new ArrayList<>();
        for (Object object : paragraphs) {
            if (object.toString().contains(context)) {
                result.add((P) object);
            }
        }
        return result;
    }

    //获取段落
    public static List<Object> getContext(Document document) {
        return document.getContent();
    }

    //获取正文
    public static Document getDocument(WordprocessingMLPackage mlPackage) throws Docx4JException {
        //获取主段落
        MainDocumentPart documentPart = mlPackage.getMainDocumentPart();
        //获取正文
        return documentPart.getContents();
    }

    //创建要添加的内容
    private static R getRun(String before, String value, String after) {
        Text text = new Text();
        String result = before + value + space + after;
        text.setValue(result);
        text.setSpace("preserve");
        R run = new R();
        run.getContent().add(text);
        return run;
    }

    //创建要添加的内容
    private static R getDrawRun(ObjectFactory factory, String imgPath, WordprocessingMLPackage mlPackage, String filePath) throws Exception {
        Text text = factory.createText();
        text.setValue("负责人签章:");

        // R对象是匿名的复杂类型
        R run = getR(factory);
        // drawing理解为画布？
        Drawing drawing = getDraw(imgPath, mlPackage, factory, filePath);
        if (drawing == null){
            return null;
        }
        //将文字和图片按照顺序放入R中
        run.getContent().add(setSpace(text));
        run.getContent().add(text);
        run.getContent().add(drawing);
        run.getContent().add(setSpace());
        run.getContent().add(setSpace(text));
        return run;
    }

    //创建图片
    public static Drawing getDraw(String imgPath, WordprocessingMLPackage mlPackage, ObjectFactory factory, String filePath) throws Exception {
        InputStream is = new FileInputStream(imgPath);
        byte[] bytes = IOUtils.toByteArray(is);
        // 创建一个行内图片
        try{
            BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(mlPackage, bytes);
            Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 1500);
            // drawing理解为画布
            Drawing drawing = factory.createDrawing();
            drawing.getAnchorOrInline().add(inline);
            return drawing;
        }catch (Exception e){
            log.error("创建行内图片异常:{}", e);
            errList.add(filePath);
            return null;
        }
    }

    //设置文字间距为四个\t
    public static Text setSpace(Text text) {
        Text result = new Text();
        result.setValue(text.getValue() + space);
        result.setSpace("preserve");
        return result;
    }

    //获取空格
    public static Text setSpace() {
        Text result = new Text();
        result.setValue(space);
        result.setSpace("preserve");
        return result;
    }

    //创建R对象
    public static R getR(ObjectFactory factory) {
        return factory.createR();
    }

    //删除段落全部内容
    public static void removeAll(P p) {
        p.getContent().removeAll(p.getContent());
    }

    //获取段落的部分格式(不完整)
    public static void getParagraphPPr(P p) {
        PPr pPr = p.getPPr();
        log.info("行间距：{}", pPr.getSpacing().getLine());
        log.info("字体大小：{}", pPr.getRPr().getSz().getVal());
    }

    public static void setParagraphStyle(P p) {
        PPrBase.Spacing spacing = new PPrBase.Spacing();
        spacing.setLineRule(STLineSpacingRule.AUTO);
        p.getPPr().setSpacing(spacing);
    }

    private static void setFontMapper(WordprocessingMLPackage mlPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        fontMapper.put("PMingLiU", PhysicalFonts.get("PMingLiU"));
//        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
//        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
//        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
//        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
//        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
//        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
//        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
//        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
//        fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
//        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
//        fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
//        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
//        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
//        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));

        mlPackage.setFontMapper(fontMapper);
    }

}
