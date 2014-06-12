package ua.org.gostroy.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by panser on 5/23/14.
 */
@Entity
@Table(name = "articles")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Article{
    @NotEmpty(message="{validation.article.title.NotEmpty.message}")
    @Size(min=5, max=60, message="{validation.article.title.Size.message}")
    @XmlElement
    private String title;
    @NotEmpty(message="{validation.article.description.NotEmpty.message}")
    @Size(min=5, max=500, message="{validation.article.description.Size.message}")
    @XmlElement
    private String description;
    @XmlElement
    private String text;
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @XmlElement
    private User author;
    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
    private List<Comment> comments;
    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
    private List<Visitor> visitors;

    private Boolean visible;
    private Boolean allowComments;
    @DateTimeFormat
    @XmlElement
    private Date createDate;
    @DateTimeFormat
    private Date deleteDate;

    public Article() {
        this.visible = true;
        this.createDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(Boolean allowComments) {
        this.allowComments = allowComments;
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

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", version=" + version +
                ", title='" + title + '\'' +
//                ", author=" + author.getLogin() +
                '}';
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Version
    protected Long version;

    protected transient final Logger log = LoggerFactory.getLogger(getClass());

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isNew() {
        return (this.id == null);
    }

}
