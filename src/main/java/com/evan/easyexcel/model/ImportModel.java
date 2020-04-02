package com.evan.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @ClassName ImportModel
 * @Description 导入模型
 * @Author Evan Wang
 * @Version 1.0.0
 * @Date 2020/4/1 20:54
 */
@Data
public class ImportModel {

    @ExcelProperty(index = 0)
    private String date;

    @ExcelProperty(index = 1)
    private String author;

    @ExcelProperty(index = 2)
    private String book;

}
