package com.oddy.demossm.controller;

import com.oddy.demossm.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
public class HelloController {

  @Autowired
  private User userRequest;

  @Autowired
  private User userSession;

  @ResponseBody
  @RequestMapping("/hello")
  public String hello() {
    return "Hello World!";
  }

  //  1. ModelAndView 基本用法
  //  复杂写法
  //  @RequestMapping("/")
  //  public ModelAndView index() {
  //    ModelAndView modelAndView = new ModelAndView("index");
  //    modelAndView.getModel().put("username", "Oddy");
  //    modelAndView.getModel().put("password", "Oddy");
  //    return modelAndView;
  //  }
  //  简单写法，直接返回 view 名称字符串
  //  使用自动装配的 Model 实现数据传递
  @RequestMapping("/")
  public String index(Model model, HttpSession session) {
    User user = new User();
    user.setUsername("Oddy Simple");
    user.setPassword("Oddy Simple Password");
    model.addAttribute("username", user.getUsername());
    model.addAttribute("password", user.getPassword());
    session.setAttribute("user", user);
    return "index";
  }

  // 2. @RequestParam、@RequestHeader
  // params 属性限制请求参数
  // params="xxx" 表示必须携带 xxx 参数
  // params!="xxx" 表示不能携带 xxx 参数
  // params="xxx=yyy" 表示必须携带 xxx 参数，且值为 yyy
  // params="xxx!=yyy" 表示必须携带 xxx 参数，且值不能为 yyy
  // @RequestParam 注解可以获取请求参数，
  // 如果请求参数名和方法参数名一致，可以省略 @RequestParam 注解
  //  @RequestMapping(value = "/login")
  //  public String login(Model model, @RequestParam String username, @RequestParam String password) {
  //    log.info("username: " + username);
  //    log.info("password: " + password);
  //    model.addAttribute("username", username);
  //    model.addAttribute("password", password);
  //    return "index";
  //  }
  // 此外，还可以使用对象作为方法形参，接收请求参数，
  // 请求参数名和对象属性名一致，Spring 会为我们自动组装为对象
  @RequestMapping("/loginObj")
  public String loginObj(Model model, User user) {
    log.info(user.toString());
    model.addAttribute("username", user.getUsername());
    model.addAttribute("password", user.getPassword());
    return "index";
  }
  // @RequestHeader 注解可以获取请求头信息，用法和 @RequestParam 类似，不再赘述

  // 3. @CookieValue、@SessionAttribute
  @RequestMapping("/cookie-session")
  public String cookieSession(Model model, @CookieValue("JSESSIONID") String sessionId,
      @SessionAttribute("user") User user) {
    log.info("sessionId: " + sessionId);
    log.info("user: " + user.toString());
    model.addAttribute("sessionId", sessionId);
    model.addAttribute("username", user.getUsername());
    model.addAttribute("password", user.getPassword());
    return "cookie-session";
  }

  // 4. Redirect、Forward
  @RequestMapping("/redirect")
  public String redirect() {
    return "redirect:/hello";
  }

  @RequestMapping("/forward")
  public String forward() {
    return "forward:/hello";
  }

  // 5. Bean 的 Web 作用域
  @RequestMapping("/scope")
  // 表示返回的是字符串而不是页面的名称
  @ResponseBody
  public String scope() {
    return "userRequest: " + userRequest.toSuperString() + "<br />" + "userSession: "
        + userSession.toSuperString();
  }

  // Restful 是一种风格，如下所示
  // 即将路径的一部分作为参数使用
  // 并且为一个 url，按请求方法不同分为不同功能
  @PostMapping("/restful/{str}")
  @ResponseBody
  public String restfulPost(HttpSession session, @PathVariable("str") String sth) {
    session.setAttribute("restful", "restful: " + sth);
    return "restful post: " + sth;
  }

  @GetMapping("/restful")
  @ResponseBody
  public String restfulGet(HttpSession session) {
    Object restful = session.getAttribute("restful");
    if (restful == null) {
      return "restful get-null";
    }
    return "restful get: " + restful;
  }

  @DeleteMapping("/restful")
  @ResponseBody
  public String restfulDelete(HttpSession session) {
    session.removeAttribute("restful");
    return "restful delete";
  }

  // 6. 异常处理
  @RequestMapping("/error/{str}")
  public String error(@PathVariable String str) {
    throw new RuntimeException(str);
  }
}
