# easyexcel-basic-demo

#### ExcelUtil对easyexcel2.X进行封装，实现一个方法完成简单的excel导入和导出。

# 一. 背景

主流office文档操作组件性能比较

| 组件  |  功能简介 |  使用场景 |  测试环境 | 内存消耗  | 读取时间  |  写入时间 | 文件大小  |
| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ | ------------ | ------------ |
| poi             |1. 对Microsoft Office格式档案读和写的功能<br> 2. HSSF提供读写Excel XLS<br> 3. HPSF提供读写OLE2 Property Sets<br> 4. POIFS提供读写OLE2 Filesystem   |1. 操作Excel XLS<br> 2. HSSFWorkbook只能解析2003之前版本xls格式<br> 3.  使用HSSF时sheet最大行数65536，最大列数256| Win64 4核8g jdk1.8 5万行2列excel xls |  R:206.88MB W:138.34MB | 1049ms  | 2005ms  | 4.15MB  |
| poi-ooxml       |1. poi升级扩展版本<br> 2. XSSF提供读写XLSX<br> 3. XSLF提供读写PPTX<br> 4. XWPF提供读写DOCX<br> 5. CommonSS读写XLS、XLSX  |1. 操作pptx、docx、xlsx等<br> 2. XSSF基于内存写入方式，一个sheet最大行数1048576，最大列数16384<br> 3. SXSSF是在XSSF基础上基于内存+磁盘写入方式,用于大数据量的导出 | Win64 4核8g jdk1.8 5万行2列excel xlsx/5万行word docx  | XSSF-R:185.04MB XSSF-W:405.58MB SXSSF-R:140.34MB SXSSF-W:41.83MB XWPF-R:23.14MB XWPF-W:158.21MB  |XSSF:2502ms SXSSF:1354ms XWPF:634ms   | XSSF:4644ms SXSSF:1417ms XWPF:21555ms  |XSSF:1.36MB SXSSF:1.33MB XWPF:999KB   |
| poi-scratchpad  |1. HWPF提供读写Word DOC<br> 2. HSLF提供读写PPT<br> 3. HDGF提供读Visio VSD<br> 4. HPBF提供读Publisher PUB<br> 5. HSMF提供读Outlook MSG<br>   |1. 操作PPT、DOC、VSD、PUB、MSG等格式<br> 2. HWPFDocument写doc文件必须要先有doc文件<br> 3. 不建议使用HWPF等低版本office   |Win64 4核8g jdk1.8 5万行word doc   |R:81.80MB W:90.39MB   |221ms   |538ms   |3.74MB   |
| easyexcel       |阿里开源,重写了poi对Excel2007版的解析,不会出现OOM,2003版依赖POI的sax模式  | xls、xlsx操作  |Win64 4核8g jdk1.8 5万行2列xlsx   |R:80.10MB W:60.56MB   |1053ms   | 1149ms  |  1.33MB |

  

# 二. 依赖
目前easyexcel的版本是最新的正式版为 2.1.6
```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>2.1.6</version>
</dependency>
```

  

# 三. 需要的类

## 1. ExcelUtil
工具类，可以直接调用该工具类的方法完成 Excel 的读写

## 2. ExcelListener
监听类，可以根据需要，自定义处理获取到的数据

## 3. BeanConvert
Object与实体类转换工具类

## 4. lombok(非必须)
一款提高开发效率的插件，可以不用再去写getter,setter方法等。

  

# 四. 读取 Excel
读取 Excel 时只需要调用 ```ExcelUtil.readExcel()``` 方法
```
@PostMapping(value = "/import")
public List<ImportModel> read(MultipartFile excel){
    return ExcelUtil.readExcel(excel, ImportModel.class,0);
}
```

其中 excel 是 MultipartFile 类型的文件对象，而 ImportModel 是该 Excel 所映射的实体对象，如：
```
@Data
public class ImportModel {

    @ExcelProperty(index = 0)
    private String date;

    @ExcelProperty(index = 1)
    private String author;

    @ExcelProperty(index = 2)
    private String book;

}
```
作为映射实体类，通过 @ExcelProperty 注解与 index 变量可以标注成员变量所映射的列，同时不可缺少 setter 方法

  

# 五. 导出 Excel
### 1. 简单导出Excel
只需要调用 ```ExcelUtil.writeExcel()``` 方法：
```
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
```
fileName，sheetName 分别是导出文件的文件名和 sheet 名，ExportModel 为导出数据的映射实体对象，list 为导出数据。

对于映射实体类，可以根据可以通过 @ExcelProperty 注解自定义表头，通过@ContentRowHeight注解设置内容行高，@HeadRowHeight设置表头行高，@ColumnWidth设置列宽等，如：
```
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
```
value 为列名;index 为列的序号,可为null


### 2. 设置动态表头
调用 ```ExcelUtil.writeDynamicHeadExcel()```  方法：
```
public void writeDynamicHeadExcel(HttpServletResponse response) throws IOException {
    List<ExportModel> list = new ArrayList<>();

    ExportModel model1 = new ExportModel();
    model1.setName("孙悟空");
    model1.setSex("男");
    model1.setAge(500);
    list.add(model1);

    ExportModel model2 = new ExportModel();
    model2.setName("哪吒");
    model2.setSex("女");
    model2.setAge(499);
    list.add(model2);
    
    
    List<List<String>> headList = new ArrayList<List<String>>();
    List<String> head0 = new ArrayList<String>();
    head0.add("姓甚名啥");
    List<String> head1 = new ArrayList<String>();
    head1.add("是男是女");
    List<String> head2 = new ArrayList<String>();
    head2.add("贵庚几何");
    headList.add(head0);
    headList.add(head1);
    headList.add(head2);
    
    ExcelUtil.writeDynamicHeadExcel(response, list, fileName, sheetName, ExportModel.class,headList);
}
```
write时不传入class,table时传入并设置needHead为false

### 3. 导出表格带样式
调用 ```ExcelUtil.writeStyleExcel()```  方法：
```
public static  <T> void writeStyleExcel(HttpServletResponse response,List<T> list, String fileName, String sheetName, Class<T> clazz) {
    //表头策略
    WriteCellStyle headWriteCellStyle = new WriteCellStyle();
    //背景浅灰
    headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    WriteFont headWriteFont = new WriteFont();
    headWriteFont.setFontHeightInPoints((short)20);
    headWriteCellStyle.setWriteFont(headWriteFont);

    //内容策略
    WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
    //这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 否则无法显示背景颜色；头默认了FillPatternType
    contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
    //背景浅绿
    contentWriteCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
    WriteFont contentWriteFont = new WriteFont();
    //字体大小
    contentWriteFont.setFontHeightInPoints((short)15);
    contentWriteCellStyle.setWriteFont(contentWriteFont);

    HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

    OutputStream outputStream = getOutputStream(response, fileName);
    EasyExcel.write(outputStream, clazz).registerWriteHandler(horizontalCellStyleStrategy).sheet(sheetName).doWrite(list);

}
```
可根据实际需要自行定制

