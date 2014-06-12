package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.org.gostroy.model.Album;
import ua.org.gostroy.model.Image;
import ua.org.gostroy.model.User;
import ua.org.gostroy.exception.EntityNotFound;
import ua.org.gostroy.exception.HaveNotAccess;
import ua.org.gostroy.exception.NeedAuthorize;
import ua.org.gostroy.service.AlbumService;
import ua.org.gostroy.service.ImageService;
import ua.org.gostroy.service.UserService;
import ua.org.gostroy.web.editor.AlbumEditor;
import ua.org.gostroy.web.form.UploadStatus;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by panser on 6/2/2014.
 */
@Controller
@RequestMapping("/gallery")
public class GalleryController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String root = System.getProperty("user.dir");

    @Autowired(required = true)
    private ImageService imageService;
    @Autowired(required = true)
    private AlbumService albumService;
    @Autowired(required = true)
    private UserService userService;

    @Autowired(required = true)
    private MessageSource messageSource;

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder, @PathVariable String login) {
        binder.registerCustomEditor(Album.class, new AlbumEditor(this.albumService,login));
    }


    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listImagesGET(Model model){
        model.addAttribute("images", imageService.findAll());
        return "/gallery/imageAll";
    }

    @RequestMapping(value = {"/{login}/"}, method = RequestMethod.GET)
    public String listAlbumsGET(Model model, @PathVariable String login){
        model.addAttribute("login", login);
        model.addAttribute("albums", albumService.findByUserLogin(login));
        model.addAttribute("albumNew", new Album());
        return "/gallery/albumList";
    }

    @RequestMapping(value = {"/{login}/"}, method = RequestMethod.POST)
    public String listAlbumsPOST(@Valid @ModelAttribute("albumNew") Album albumFromForm, BindingResult result,
                                 @PathVariable String login) {
        String viewName;
        if (result.hasErrors()) {
            viewName = "/gallery/albumList";
        } else {
            User user = userService.findByLogin(login);
            albumFromForm.setUser(user);
            albumService.create(albumFromForm);
            viewName = "redirect:/gallery/" + login + "/";
        }
        return viewName;
    }
    @RequestMapping(value = {"/{login}/{albumName}"}, method = RequestMethod.GET, params = "delete")
    public String listAlbumsDELETE(@PathVariable String login, @PathVariable String albumName){
        List<Image> images = imageService.findByUserLoginAndAlbumName(login, albumName);
        Album album = albumService.findByUserLoginAndName(login,albumName);
        for(Image image : images){
            imageService.delete(image);
        }
        albumService.delete(album);
        return "redirect:/gallery/" + login + "/";
    }



    @RequestMapping(value = {"/{login}/{albumName}/"}, method = RequestMethod.GET)
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
/*
    @RequestMapping(value = {"/{albumName}/upload"}, method = RequestMethod.GET)
    public String uploadImagesGET(Model model, @PathVariable String login){
        model.addAttribute("image", new Image());
        return "/gallery/imageUpload";
    }
*/
//    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = {"/{login}/{albumName}/"}, method = RequestMethod.POST)
    public String uploadImagesPOST(RedirectAttributes redirectAttributes, @PathVariable String login, @PathVariable String albumName,
                                   MultipartRequest multipartRequest)
            throws IOException, NoSuchAlgorithmException{
        log.trace("uploadImagesPOST(), start ...");
        Map<String, String> errorsMap = new HashMap<String, String>();
        List<MultipartFile> files = multipartRequest.getFiles("files");
        if(files.get(0).isEmpty()){
            errorsMap.put("", messageSource.getMessage("error.upload.notselected", null, LocaleContextHolder.getLocale()));
        }
        else {
            User user = userService.findByLogin(login);
            Album album = albumService.findByUserLoginAndName(login, albumName);
            if (album == null) {
                album = new Album();
                album.setName(albumName);
                album.setUser(user);
            }
            for (MultipartFile multipartFile : files) {
                    Image image = new Image();
                    image.setUser(user);
                    image.setAlbum(album);
                    image.setMultipartFile(multipartFile);

                    UploadStatus uploadStatus = imageService.create(image);
                    String fileName = image.getMultipartFile().getOriginalFilename();
                    if (uploadStatus.equals(UploadStatus.EXISTS)) {
                        errorsMap.put(fileName, messageSource.getMessage("error.upload.exists", null, LocaleContextHolder.getLocale()));
                    } else if (uploadStatus.equals(UploadStatus.INVALID)) {
                        errorsMap.put(fileName, messageSource.getMessage("error.upload.invalid", null, LocaleContextHolder.getLocale()));
                    } else if (uploadStatus.equals(UploadStatus.FAILED)) {
                        errorsMap.put(fileName, messageSource.getMessage("error.upload.failed", null, LocaleContextHolder.getLocale()));
                    }
                }
            }
        redirectAttributes.addFlashAttribute("errorsMap", errorsMap);
        return "redirect:/gallery/" + login + "/" + albumName + "/";
    }



    @RequestMapping(value = {"/{login}/{albumName}/{imageName}"}, method = RequestMethod.GET)
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
    @RequestMapping(value = {"/{login}/{albumName}/{imageName}"}, method = RequestMethod.PUT)
    public String editImagePUT(RedirectAttributes redirectAttributes, @PathVariable String login, @PathVariable String albumName, @PathVariable String imageName,
                               @Valid @ModelAttribute("image") Image imageFromForm, BindingResult result){
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
            log.trace("editImagePUT(), image.getDefAlbum()2: " + image.getDefAlbum());
            log.trace("editImagePUT(), finish");
        }

        return "redirect:/gallery/" + login + "/" + imageFromForm.getAlbum().getName() + "/" + imageFromForm.getName();
    }
    @RequestMapping(value = {"/{login}/{albumName}/{imageName}"}, method = RequestMethod.GET, params = "delete")
    public String editImageDELETE(@PathVariable String login, @PathVariable String albumName, @PathVariable String imageName){
        Image image = imageService.findByUserLoginAndAlbumNameAndName(login,albumName,imageName);
        imageService.delete(image);
        return "redirect:/gallery/" + login + "/" + albumName + "/";
    }






    @RequestMapping(value = {"/{login}/{albumName}/{imageName}/full"}, method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage viewImage(@PathVariable String login, @PathVariable String imageName, @PathVariable String albumName)
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
            String authLogin = SecurityContextHolder.getContext().getAuthentication().getName();
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

}
