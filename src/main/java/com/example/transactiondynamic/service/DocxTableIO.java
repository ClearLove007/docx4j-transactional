package com.example.transactiondynamic.service;

import org.docx4j.TextUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.*;

import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static com.example.transactiondynamic.service.DocxIO.*;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 16:14 2019/12/16
 */
public class DocxTableIO {

    static ArrayList<String> oldTextList = new ArrayList<>();
    static RFonts rFonts = new RFonts();
    static RPr rPr = new RPr();
    static {
        oldTextList.add("绥中镇盛花园2期");
        oldTextList.add("赵猛1");
        oldTextList.add("赵猛2");
        oldTextList.add("211421198912126416");
        oldTextList.add("139988338471");
        oldTextList.add("139988338472");
        oldTextList.add("孙立佳");
        oldTextList.add("中国建设银行股份有限公司沈阳汇金支行");
        oldTextList.add("6227000736010631069");
        oldTextList.add("139988338473");

        rFonts.setAscii("宋体");
        rFonts.setEastAsia("宋体");
        rFonts.setHAnsi("宋体");

        rPr.setRFonts(rFonts);
    }

    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\xueweidong\\Desktop\\湖南省长沙市星沙县美美时尚女装店\\爱心绿源葫芦岛农业科技有限公司_3.docx");
        File newFile = new File("C:\\Users\\xueweidong\\Desktop\\湖南省长沙市星沙县美美时尚女装店\\爱心绿源葫芦岛农业科技有限公司_target.docx");
        //加载文件
        WordprocessingMLPackage mlPackage = loadDocxFile(file);
        //获取所有段落
        List<Object> paragraphs = getContext(getDocument(mlPackage));
        Tbl tbl = new Tbl();
        for (Object p : paragraphs) {
            if (p instanceof JAXBElement && ((JAXBElement) p).getValue() instanceof Tbl) {
                tbl = (Tbl) ((JAXBElement) p).getValue();
                break;
            }
        }
        for (String result : getTblContentList(tbl)) {
            System.out.println(result);
        }


        mlPackage.save(newFile);
    }

    public static void replaceTbl(Tbl tbl)throws Exception{
        if (null == tbl){
            return;
        }
        //获取所有的行
        List<Tr> trList = getTblAllTr(tbl);
        String old = "";
        for (Tr tr : trList) {
            StringBuilder sb = new StringBuilder();
            //获取该行所有的列
            List<Tc> tcList = getTrAllCell(tr);

            next:
            for (Tc tc : tcList) {
                for (String str:oldTextList){
                    old = str;
                    if (getElementContent(tc).contains(old)){
                        setTcContent(tc, rPr, "要替换的内容1");
                        continue next;
                    }
                }
            }
        }
    }

    /**
     * @Description: 获取表格内容
     */
    public static List<String> getTblContentList(Tbl tbl) throws Exception {
        List<String> resultList = new ArrayList<>();
        List<Tr> trList = getTblAllTr(tbl);
        for (Tr tr : trList) {
            StringBuilder sb = new StringBuilder();
            List<Tc> tcList = getTrAllCell(tr);
            for (Tc tc : tcList) {
                sb.append(tcList.indexOf(tc)).append(getElementContent(tc)).append("\n");
            }
            resultList.add(sb.toString());
        }
        return resultList;
    }

    /**
     * @Description: 得到表格所有的行
     */
    public static List<Tr> getTblAllTr(Tbl tbl) {
        List<Object> objList = getAllElementFromObject(tbl, Tr.class);
        List<Tr> trList = new ArrayList<>();
        if (objList == null) {
            return trList;
        }
        for (Object obj : objList) {
            if (obj instanceof Tr) {
                Tr tr = (Tr) obj;
                trList.add(tr);
            }
        }
        return trList;
    }

    /**
     * @Description: 获取所有的单元格
     */
    public static List<Tc> getTrAllCell(Tr tr) {
        List<Object> objList = getAllElementFromObject(tr, Tc.class);
        List<Tc> tcList = new ArrayList<>();
        if (objList == null) {
            return tcList;
        }
        for (Object tcObj : objList) {
            if (tcObj instanceof Tc) {
                Tc objTc = (Tc) tcObj;
                tcList.add(objTc);
            }
        }
        return tcList;
    }

    /**
     * @Description:得到指定类型的元素
     */
    public static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
        List<Object> result = new ArrayList<>();
        if (obj instanceof JAXBElement)
            obj = ((JAXBElement<?>) obj).getValue();
        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }
        return result;
    }

    public static String getElementContent(Object obj) throws Exception {
        StringWriter stringWriter = new StringWriter();
        TextUtils.extractText(obj, stringWriter);
        return stringWriter.toString();
    }

    /**
     * @Description:设置单元格内容,content为null则清除单元格内容
     */
    public static void setTcContent(Tc tc, RPr rpr, String content) {
        List<Object> pList = tc.getContent();
        P p = new P();
        pList.removeAll(pList);
        tc.getContent().add(p);
//        if (pList != null && pList.size() > 0) {
//            if (pList.get(0) instanceof P) {
//                p = (P) pList.get(0);
//            }
//        } else {
//            p = new P();
//            tc.getContent().add(p);
//        }
        R run = null;
        List<Object> rList = p.getContent();
        if (rList != null && rList.size() > 0) {
            for (int i = 0, len = rList.size(); i < len; i++) {
                // 清除内容(所有的r
                p.getContent().remove(0);
            }
        }
        run = new R();
        p.getContent().add(run);
        if (content != null) {
            String[] contentArr = content.split("\n");
            Text text = new Text();
            text.setSpace("preserve");
            text.setValue(contentArr[0]);
            run.setRPr(rpr);
            run.getContent().add(text);

            for (int i = 1, len = contentArr.length; i < len; i++) {
                Br br = new Br();
                run.getContent().add(br);// 换行
                text = new Text();
                text.setSpace("preserve");
                text.setValue(contentArr[i]);
                run.setRPr(rpr);
                run.getContent().add(text);
            }
        }
    }

    /**
     * @Description:设置单元格内容,content为null则清除单元格内容
     */
    public static void removeTcContent(Tc tc) {
        List<Object> pList = tc.getContent();
        P p = new P();
        if (pList != null && pList.size() > 0) {
            if (pList.get(0) instanceof P) {
                p = (P) pList.get(0);

            }
        } else {
            return;
        }
        List<Object> rList = p.getContent();
        if (rList != null && rList.size() > 0) {
            for (int i = 0, len = rList.size(); i < len; i++) {
                // 清除内容(所有的r
                p.getContent().remove(0);
            }
        }
    }
}
