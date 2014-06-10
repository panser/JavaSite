package ua.org.gostroy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
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
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by panser on 6/2/2014.
 */
@Controller
@RequestMapping("/gallery/{login}")
public class GalleryController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private String root = System.getProperty("user.dir");

    @Autowired(required = true)
    private ImageService imageService;
    @Autowired(required = true)
    private AlbumService albumService;
    @Autowired(required = true)
    private UserService userService;


    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder, @PathVariable String login) {
        binder.registerCustomEditor(Album.class, new AlbumEditor(this.albumService,login));
    }


    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String listAlbumsGET(Model model, @PathVariable String login){
        model.addAttribute("login", login);
        model.addAttribute("albums", albumService.findByUserLogin(login));
        model.addAttribute("albumNew", new Album());
        return "/gallery/albumList";
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.POST)
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
    @RequestMapping(value = {"/{albumName}/delete"}, method = RequestMethod.GET)
    public String listAlbumsDELETE(@PathVariable String login, @PathVariable String albumName){
        Album album = albumService.findByUserLoginAndName(login,albumName);
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
/*
    @RequestMapping(value = {"/{albumName}/upload"}, method = RequestMethod.GET)
    public String uploadImagesGET(Model model, @PathVariable String login){
        model.addAttribute("image", new Image());
        return "/gallery/imageUpload";
    }
*/
    @RequestMapping(value = {"/{albumName}/"}, method = RequestMethod.POST)
    public String uploadImagesPOST(@Valid @ModelAttribute("imageNew") Image imageFromForm, BindingResult result, RedirectAttributes redirectAttributes,
                                   @PathVariable String login, @PathVariable String albumName){
        if(imageFromForm.getMultipartFile() == null){
            result.rejectValue("multipartFile", "error.upload.notselected");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.imageNew", result);
            redirectAttributes.addFlashAttribute("imageNew", imageFromForm);
        }
        else if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.imageNew", result);
            redirectAttributes.addFlashAttribute("imageNew", imageFromForm);
        }
        else {
            Image image = new Image();
            User user = userService.findByLogin(login);
            Album album = albumService.findByUserLoginAndName(login, albumName);
            if(album == null){
                album = new Album();
                album.setName(albumName);
                album.setUser(user);
                album.setDefImage(image);
            }

            image.setUser(user);
            image.setAlbum(album);

            String originalFilename = imageFromForm.getMultipartFile().getOriginalFilename();
            originalFilename = originalFilename.replace('.','_');
            image.setName(originalFilename);
            image.setMultipartFile(imageFromForm.getMultipartFile());

            UploadStatus uploadStatus = imageService.create(image);
            if (uploadStatus.equals(UploadStatus.EXISTS)){
                result.rejectValue("multipartFile", "error.upload.exists");
            } else if (uploadStatus.equals(UploadStatus.INVALID)){
                result.rejectValue("multipartFile", "error.upload.invalid");
            } else if (uploadStatus.equals(UploadStatus.FAILED)) {
                result.rejectValue("multipartFile", "error.upload.failed");
            }
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.imageNew", result);
            redirectAttributes.addFlashAttribute("imageNew", imageFromForm);
        }
        return "redirect:/gallery/" + login + "/" + albumName + "/";
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
//            if(imageFromForm.getDefAlbum() != null) {
//                image.setDefAlbum(imageFromForm.getDefAlbum());
//            }

            log.trace("editImagePUT(), image before update: " + image);
            imageService.update(image);
            log.trace("editImagePUT(), finish");
        }

        return "redirect:/gallery/" + login + "/" + imageFromForm.getAlbum().getName() + "/" + imageFromForm.getName();
    }
    @RequestMapping(value = {"/{albumName}/{imageName}/delete"}, method = RequestMethod.GET)
    public String editImageDELETE(@PathVariable String login, @PathVariable String albumName, @PathVariable String imageName){
        Image image = imageService.findByUserLoginAndAlbumNameAndName(login,albumName,imageName);
        imageService.delete(image);
        return "redirect:/gallery/" + login + "/" + albumName + "/";
    }






    @RequestMapping(value = {"/{albumName}/{imageName}/full"}, method = RequestMethod.GET)
    @ResponseBody
    public BufferedImage viewImage(@PathVariable String login, @PathVariable String imageName, @PathVariable String albumName)
            throws HaveNotAccess,NeedAuthorize,EntityNotFound, IOException {
        Album album = albumService.findByUserLoginAndName(login, albumName);
        if(album == null){
            throw new EntityNotFound("Album not found");
        }
        if(!album.getPublicAccess()){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = null;
            if (principal instanceof User) {
                user = (User) principal;
                if(user.getLogin() != login){
                    throw new HaveNotAccess("You have not access to view Image in private Album");
                }
            } else {
                throw new NeedAuthorize("You need to authorize to view private content");
            }
        }
//        log.trace("viewImage(), imageName:" + imageName);
        Image image = imageService.findByUserLoginAndAlbumNameAndName(login, albumName, imageName);
        if(image == null){
            throw new EntityNotFound("Image not found");
        }
//        Resource resource = new ClassPathResource(root + image.getPath() + image.getFile());
//        log.trace("viewImage(), resource:" + resource.getFilename());
//        BufferedImage bufferedImage = ImageIO.read(resource.getURL());
        File filePath = new File(root + image.getPath() + image.getFile());
        log.trace("viewImage(), filePath:" + filePath.toString());
        BufferedImage bufferedImage = ImageIO.read(filePath);
        Graphics g = bufferedImage.getGraphics();
        g.drawString("gostroy.org.ua",20,20);
        return bufferedImage;
    }

}
