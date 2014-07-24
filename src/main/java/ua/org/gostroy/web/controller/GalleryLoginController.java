package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.exception.EntityNotFound;
import ua.org.gostroy.exception.HaveNotAccess;
import ua.org.gostroy.exception.NeedAuthorize;
import ua.org.gostroy.model.Album;
import ua.org.gostroy.model.Image;
import ua.org.gostroy.model.User;
import ua.org.gostroy.service.AlbumService;
import ua.org.gostroy.service.GalleryLoginControllerService;
import ua.org.gostroy.service.ImageService;
import ua.org.gostroy.service.UserService;
import ua.org.gostroy.web.editor.AlbumEditor;
import ua.org.gostroy.web.util.AjaxUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by panser on 6/2/2014.
 */
@Controller
@RequestMapping("/gallery/{login}")
public class GalleryLoginController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String root = System.getProperty("user.dir");

    @Autowired(required = true)
    private ImageService imageService;
    @Autowired(required = true)
    private AlbumService albumService;
    @Autowired(required = true)
    private UserService userService;
    @Autowired
    private GalleryLoginControllerService galleryLoginControllerService;

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder, @PathVariable String login) {
        binder.registerCustomEditor(Album.class, new AlbumEditor(this.albumService,login));
    }



    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listAlbumsGET(Model model, @PathVariable String login){
        model.addAttribute("login", login);
        model.addAttribute("albums", albumService.findByUserLogin(login));
        if(!model.containsAttribute("albumNew")) {
            model.addAttribute("albumNew", new Album());
        }
        return "/gallery/albumList";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.POST)
    public String listAlbumsPOST(@Valid Album albumFromForm, BindingResult result,
                                 @PathVariable String login, RedirectAttributes redirectAttributes) {
        validateAlbumOnUnique(login, albumFromForm,result);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.albumNew", result);
            redirectAttributes.addFlashAttribute("albumNew", albumFromForm);
        } else {
            User user = userService.findByLogin(login);
            albumFromForm.setUser(user);
            albumService.create(albumFromForm);
        }
        return "redirect:/gallery/" + login + "/";
    }
    @RequestMapping(value = {"/{albumName}"}, method = RequestMethod.GET, params = "delete")
    public String listAlbumsDELETE(@PathVariable String login, @PathVariable String albumName){
        List<Image> images = imageService.findByUserLoginAndAlbumName(login, albumName);
        Album album = albumService.findByUserLoginAndName(login,albumName);
        for(Image image : images){
            imageService.delete(image);
        }
        albumService.delete(album);
        return "redirect:/gallery/" + login + "/";
    }



    @RequestMapping(value = {"/{albumName}/"}, method = RequestMethod.GET)
    public String listImages(Model model, @PathVariable String login, @PathVariable String albumName){
        Album album = albumService.findByUserLoginAndName(login,albumName);
        model.addAttribute("login", login);
        model.addAttribute("album", album);
        model.addAttribute("images", imageService.findByUserLoginAndAlbumName(login, albumName));
        if(!model.containsAttribute("imageNew")){
            model.addAttribute("imageNew", new Image());
        }
        return "/gallery/imageList";
    }

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        Boolean ajaxRequest = AjaxUtils.isAjaxRequest(request) == true ? true : null;
        Boolean ajaxUpload = AjaxUtils.isAjaxUploadRequest(request) == true ? true : null;

        model.addAttribute("ajaxRequest", ajaxRequest);
        model.addAttribute("ajaxUpload", ajaxUpload);
    }
    //    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = {"/{albumName}/"}, method = RequestMethod.POST)
    public String uploadImagesPOST(RedirectAttributes redirectAttributes, @PathVariable String login, @PathVariable String albumName,
                                   MultipartRequest multipartRequest, Locale locale,
                                   @ModelAttribute("ajaxUpload") Boolean ajaxUpload, @ModelAttribute("ajaxRequest") Boolean ajaxRequest,
                                   Model model)
            throws IOException, NoSuchAlgorithmException{
        log.trace("uploadImagesPOST(), start ...");
        Model modelUpload = galleryLoginControllerService.uploadImages(multipartRequest, login, albumName, locale);
        ajaxUpload = (ajaxUpload == null) ? false : ajaxUpload;
        if(ajaxUpload){
            model.addAllAttributes(modelUpload.asMap());
            return "/gallery/imageList";
        }
        else {
            redirectAttributes.addFlashAttribute("errors", modelUpload.asMap().get("errors"));
            return "redirect:/gallery/" + login + "/" + albumName + "/";
        }
    }



    @RequestMapping(value = {"/{albumName}/{imageName}"}, method = RequestMethod.GET)
    public String editImageGET(Model model, HttpServletRequest request,
                            @PathVariable String login, @PathVariable String albumName, @PathVariable String imageName){
        Album album = albumService.findByUserLoginAndName(login,albumName);
        if(!model.containsAttribute("image")) {
            Image image = imageService.findByUserLoginAndAlbumNameAndName(login, albumName, imageName);
            model.addAttribute("image", image);
        }
        model.addAttribute("login", login);
        model.addAttribute("album", album);

        List<Album> albums = albumService.findByUserLogin(login);
        Map<String, String> albumList = new LinkedHashMap<String, String>();
        for(Album album1 : albums){
            albumList.put(album1.getName(), album1.getName());
        }
        model.addAttribute("albumList", albumList);

        String requestURL = request.getRequestURL().toString();
        log.trace("viewImage(), requestURL: " + requestURL);
        model.addAttribute("requestURL", requestURL);
        return "/gallery/imageEdit";
    }
    @RequestMapping(value = {"/{albumName}/{imageName}"}, method = RequestMethod.PUT)
    public String editImagePUT(RedirectAttributes redirectAttributes, @PathVariable String login, @PathVariable String albumName, @PathVariable String imageName,
                               @Valid @ModelAttribute("image") Image imageFromForm, BindingResult result){
//        TODO: fix RUSSIAN imageFromForm.getName in URI field
//        validateImageOnUnique(login,albumName,imageFromForm,result);
        if(result.hasErrors()){
            log.trace("editImagePUT(), result: " + result);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.image", result);
            redirectAttributes.addFlashAttribute("image", imageFromForm);
        }
        else {
            Image image = imageService.findByUserLoginAndAlbumNameAndName(login, albumName, imageName);
            log.trace("editImagePUT(), image after find: " + image);

            image.setName(imageFromForm.getName());
            image.setDescription(imageFromForm.getDescription());
            image.setAlbum(imageFromForm.getAlbum());
            if(imageFromForm.getCheckDefForAlbum()){
                log.trace("editImagePUT(), image.getDefAlbum()1: " + image.getDefAlbum());
                if(image.getDefAlbum() != null) {
                    image.getDefAlbum().setDefImage(null);
                    image.setDefAlbum(image.getAlbum());
                    image.getDefAlbum().setDefImage(image);
                }
                else{
                    image.setDefAlbum(image.getAlbum());
                    image.getDefAlbum().setDefImage(image);
                }
            }
            else{
                if(image.getDefAlbum() != null){
                    image.getDefAlbum().setDefImage(null);
                }
            }

            log.trace("editImagePUT(), image before update: " + image);
            imageService.update(image);
//            log.trace("editImagePUT(), image.getDefAlbum()2: " + image.getDefAlbum());
            log.trace("editImagePUT(), finish");
        }

        log.trace("editImagePUT(), redirectURL: " + "/gallery/" + login + "/" + imageFromForm.getAlbum().getName() + "/" + imageFromForm.getName());
        return "redirect:/gallery/" + login + "/" + imageFromForm.getAlbum().getName() + "/" + imageFromForm.getName();
    }

    @RequestMapping(value = {"/{albumName}/{imageName}"}, method = RequestMethod.GET, params = "delete")
    public String editImageDELETE(@PathVariable String login, @PathVariable String albumName, @PathVariable String imageName){
        Image image = imageService.findByUserLoginAndAlbumNameAndName(login,albumName,imageName);
        imageService.delete(image);
        return "redirect:/gallery/" + login + "/" + albumName + "/";
    }






    @RequestMapping(value = {"/{albumName}/{imageName}/full"}, method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage viewImage(@PathVariable String login, @PathVariable String imageName, @PathVariable String albumName, Principal user)
            throws HaveNotAccess,NeedAuthorize,EntityNotFound, IOException {
        Album album = albumService.findByUserLoginAndName(login, albumName);
        BufferedImage bufferedImage = null;
        if(album == null){
            ClassPathResource resourceImage = new ClassPathResource("templates/image/notFound.gif");
            bufferedImage = ImageIO.read(resourceImage.getFile());
            return bufferedImage;
//            throw new EntityNotFound("Album not found");
        }
        if(!album.getPublicAccess()){
            String authLogin = (user == null) ? "guest" : user.getName();
            log.trace("viewImage(), authLogin: " + authLogin);
            if(!authLogin.equals(login)){
                    ClassPathResource resourceImage = new ClassPathResource("templates/image/accessDenied.jpg");
                    bufferedImage = ImageIO.read(resourceImage.getFile());
                    return bufferedImage;
//                    throw new HaveNotAccess("You have not access to view Image in private Album");
            }
        }

        Image image = imageService.findByUserLoginAndAlbumNameAndName(login, albumName, imageName);
        if (image == null) {
            ClassPathResource resourceImage = new ClassPathResource("templates/image/notFound.gif");
            bufferedImage = ImageIO.read(resourceImage.getFile());
            return bufferedImage;
//            throw new EntityNotFound("Image not found");
        } else {
            File filePath = new File(root + image.getPath() + image.getDigest());
            log.trace("viewImage(), filePath:" + filePath.toString());
            bufferedImage = ImageIO.read(filePath);
        }
        Graphics g = bufferedImage.getGraphics();
        g.drawString("gostroy.org.ua", 20, 20);
        return bufferedImage;
    }


    //    vallidation on unique
    public void validateAlbumOnUnique(String userLogin, Album album, Errors errors){
        Object checkObject = albumService.findByUserLoginAndName(userLogin,album.getName());
        if(checkObject != null){
            errors.rejectValue("name", "validation.album.name.Duplicate", "already exists");
        }
    }
    public void validateImageOnUnique(String userLogin, String albumName, Image image, Errors errors){
        Object checkObject = imageService.findByUserLoginAndAlbumNameAndName(userLogin,albumName,image.getName());
        if(checkObject != null){
            errors.rejectValue("name", "validation.image.name.Duplicate", "already exists");
        }
    }

}
