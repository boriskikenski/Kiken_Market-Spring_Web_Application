package mk.com.kikenmarket.demo.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerController {

    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public String
    defaultErrorHandler(HttpServletRequest req, Exception e, Model model) throws Exception {
        model.addAttribute("exception", e);
        model.addAttribute("url", req.getRequestURL());
        model.addAttribute("bodyContent", "error");
        return "template";
    }
}
