package ua.org.gostroy.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by panser on 6/3/2014.
 */
@Entity
@Table(name = "albums")
public class Album extends BaseEntity {
    @Valid
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotEmpty(message="{validation.album.name.NotEmpty.message}")
    private String name;
    private String description;
    private Boolean publicAccess = true;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "defImage_id")
    private Image defImage;
    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "album")
//    private List<Image> images;
    private Set<Image> images;

    @DateTimeFormat
    private Date createDate = new Date();
    @DateTimeFormat
    private Date deleteDate;

    public Album() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setImagesInternal(Set<Image> images) {
        this.images = images;
    }

    public Set<Image> getImagesInternal() {
        if(this.images == null){
            this.images = new HashSet<Image>();
        }
        return this.images;
    }

    public List<Image> getImages() {
        List<Image> sortedImages = new ArrayList<Image>(getImagesInternal());
//        PropertyComparator.sort(sortedImages, new MutableSortDefinition("name", true, true));
        PropertyComparator.sort(sortedImages, new MutableSortDefinition("createDate", true, true));
        return Collections.unmodifiableList(sortedImages);
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

    public Boolean getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(Boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public Image getDefImage() {
        return defImage;
    }

    public void setDefImage(Image defImage) {
        this.defImage = defImage;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
