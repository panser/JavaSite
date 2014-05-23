package ua.org.gostroy.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by panser on 5/23/14.
 */
@ControllerAdvice
@Controller
public class AppBindingInitializer {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
//        binder.registerCustomEditor(int.class,new IntEditor());
//        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(Integer.class,new CustomNumberEditor(Integer.class, null, true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class,null, true));
        binder.registerCustomEditor(Double.class,  new CustomNumberEditor(Double.class, null, true));
        binder.registerCustomEditor(Byte.class, new CustomNumberEditor(Byte.class, null, true));
        binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, null, true));
        binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

        binder.setBindEmptyMultipartFiles(false);
//        binder.setIgnoreInvalidFields(true);
//        binder.setIgnoreUnknownFields(true);
//        binder.setDisallowedFields("avatarImage");
    }
}
