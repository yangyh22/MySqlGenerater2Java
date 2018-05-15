# MySqlGenerater2Java
mysql字段转换成Java实体类,以下为需要做的事情，排名不分先后
* 动态上传模板文件(一次性)
* 配合一次性模板，选择相应的字段生成实体类 
* 动态上传模板文件(持久化)
* 模板文件与用户关联
* 用户模板文件数量与大小限制
* 实体类中，自定义父类，剔除父类中存在的值
* daoBase
* serviceBase
* 页面优化：取消选中，折叠、展开
* docker化(与dockerHub)关联
* 生成的代码路径，从页面端返回(压缩包?)(done,现在使用打压缩包然后一起下载的方式)
* 选择生成代码类型（entity、dao）
* ssh,ftp 传输文件（内网）（no need）
* 传输到windows（内网）（no need）