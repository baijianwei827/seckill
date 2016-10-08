Java电商系统---高并发秒杀项目 分页版
使用mybaits的分页插件PageHelper
maven中使用：
<dependency>
      <groupId>com.github.pagehelper</groupId>
      <artifactId>pagehelper</artifactId>
      <version>4.1.6</version>
</dependency>
在mybaits-config.xml中，配置<plugins><plugins>属性：
<plugins>
        <!-- com.github.pagehelper为PageHelper类所在包名 -->
        <plugin interceptor="com.github.pagehelper.PageHelper">
            <!-- 4.0.0以后版本可以不设置该参数 -->
            <property name="dialect" value="mysql"/>
            <!-- 该参数默认为false -->
            <!-- 设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用 -->
            <!-- 和startPage中的pageNum效果一样-->
            <property name="offsetAsPageNum" value="true"/>
            <!-- 该参数默认为false -->
            <!-- 设置为true时，使用RowBounds分页会进行count查询 -->
            <property name="rowBoundsWithCount" value="true"/>
            <!-- 设置为true时，如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果 -->
            <!-- （相当于没有执行分页查询，但是返回结果仍然是Page类型）-->
            <property name="pageSizeZero" value="true"/>
            <!-- 3.3.0版本可用 - 分页参数合理化，默认false禁用 -->
            <!-- 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页 -->
            <!-- 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据 -->
            <property name="reasonable" value="false"/>
            <!-- 3.5.0版本可用 - 为了支持startPage(Object params)方法 -->
            <!-- 增加了一个`params`参数来配置参数映射，用于从Map或ServletRequest中取值 -->
            <!-- 可以配置pageNum,pageSize,count,pageSizeZero,reasonable,orderBy,不配置映射的用默认值 -->
            <!-- 不理解该含义的前提下，不要随便复制该配置 -->
            <property name="params" value="pageNum=pageHelperStart;pageSize=pageHelperRows;"/>
            <!-- 支持通过Mapper接口参数来传递分页参数 -->
            <property name="supportMethodsArguments" value="false"/>
            <!-- always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page -->
            <property name="returnPageInfo" value="none"/>
        </plugin>
    </plugins>
action中的代码如下：
/**
     * 查询
     * @param model
     * @return
     */
    @RequestMapping(value="/list/{page}",method = RequestMethod.GET)
    //使用@PathVariable注解page的页数
    public String list(@PathVariable("page") Integer page,Model model){
        int rowsPerPage=4;
        int start=(page-1)*rowsPerPage;
        List<Seckill> list=secKillService.getSecKillList(start,rowsPerPage);
        //将查询出的list放入到PageInfo中
        PageInfo pageInfo =new PageInfo(list);
        int totalPages= (int) pageInfo.getPages()+1;
        model.addAttribute("page",page);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("list",list);
        return "/list";
    }
jsp中的代码：
<div id="pages">
                <c:choose>
                    <c:when test="${page>1}">
                        <a href="/seckill/list/${page-1}">上一页</a>
                    </c:when>
                    <c:otherwise><a href="#">上一页</a></c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${page<totalPages}">
                        <a href="/seckill/list/${page+1}">下一页</a>
                    </c:when>
                    <c:otherwise><a href="#">下一页</a></c:otherwise>
                </c:choose>
            </div>
