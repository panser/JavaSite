package ua.org.gostroy.model;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by panser on 6/2/2014.
 */
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;

    @Valid
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String name;
    private String description;
    private long size;
    private String path;
    private String file;
    private transient MultipartFile multipartFile;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    public Album album;
    @OneToOne(mappedBy = "defImage")
    private Album defAlbum;

    @DateTimeFormat
    private Date createDate = new Date();
    @DateTimeFormat
    private Date deleteDate;

    public Image() {
    }

    public Image(User user, String imagePath) {
        this.user = user;
        this.path = imagePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Album getDefAlbum() {
        return defAlbum;
    }

    public void setDefAlbum(Album defAlbum) {
        this.defAlbum = defAlbum;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", user.login=" + user +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", file='" + file + '\'' +
                ", album=" + album +
                '}';
    }
}
