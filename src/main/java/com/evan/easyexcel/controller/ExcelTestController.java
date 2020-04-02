package com.evan.easyexcel.controller;

import com.evan.easyexcel.model.ExportModel;
import com.evan.easyexcel.model.ImportModel;
import com.evan.easyexcel.utils.excel.ExcelUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExcelTestController
 * @Description
 * @Author Evan Wang
 * @Version 1.0.0
 * @Date 2020/4/1 21:25
 */
@RestController
@RequestMapping(value = "/easyExcel")
public class ExcelTestController {

    @PostMapping(value = "/import")
    public List<ImportModel> read(MultipartFile excel) {
        return ExcelUtil.readExcel(excel, ImportModel.class, 0);
    }

    @GetMapping(value = "/export")
    public void writeExcel(HttpServletResponse response) {
        List<ExportModel> list = getList();
        String fileName = "Excel导出测试";
        String sheetName = "sheet1";
        ExcelUtil.writeDynamicHeadExcel(response, list, fileName, sheetName, ExportModel.class, head());
    }

    private List<ExportModel> getList() {
        List<ExportModel> modelList = new ArrayList<>();

        ExportModel firstModel = new ExportModel();
        firstModel.setName("李明");
        firstModel.setSex("男");
        firstModel.setAge(20);
        modelList.add(firstModel);

        ExportModel secondModel = new ExportModel();
        secondModel.setName("珍妮");
        secondModel.setSex("女");
        secondModel.setAge(19);
        modelList.add(secondModel);

        return modelList;
    }

    private List<List<String>> head() {
        List<List<String>> headList = new ArrayList<>();
        List<String> nameHead = new ArrayList<>();
        nameHead.add("姓名");
        List<String> genderHead = new ArrayList<>();
        genderHead.add("性别");
        List<String> ageHead = new ArrayList<>();
        ageHead.add("年龄");
        headList.add(nameHead);
        headList.add(genderHead);
        headList.add(ageHead);

        return headList;
    }
}
