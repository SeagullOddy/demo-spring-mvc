package com.oddy.demossm.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// 相当于 web.xml 的 Java 版，且使用 DispatcherServlet 作为前端控制器
public class MainInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  /**
   * 定义基本的配置类，一般用于业务层
   *
   * @return
   */
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[]{WebConfig.class};
  }

  /**
   * 配置 DispatcherServlet 的配置类，一般用于 Controller 层等
   *
   * @return
   */
  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[0];
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/"};
  }

}
