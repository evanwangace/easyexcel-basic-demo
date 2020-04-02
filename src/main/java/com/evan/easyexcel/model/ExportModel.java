package com.evan.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * @ClassName ExportModel
 * @Description 导出模型
 * @Author Evan Wang
 * @Version 1.0.0
 * @Date 2020/4/1 20:55
 */
@ContentRowHeight(20)
@HeadRowHeight(25)
@ColumnWidth(25)
@Data
public class ExportModel  {

    @ExcelProperty(value = "姓名" ,index = 0)
    private String name;

    @ExcelProperty(value = "性别" ,index = 1)
    private String sex;

    @ExcelProperty(value = "年龄" ,index = 2)
    private Integer age;

}
