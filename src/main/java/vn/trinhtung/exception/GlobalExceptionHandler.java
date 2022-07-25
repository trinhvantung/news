package vn.trinhtung.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView processResourceNotFoundException(ResourceNotFoundException exception) {
        ModelAndView mav = new ModelAndView("error/resource-not-found");
        mav.addObject("message", exception.getMessage());

        return mav;
    }
}
