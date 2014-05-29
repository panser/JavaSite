package ua.org.gostroy.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by panser on 5/23/14.
 */
@Entity
@Table(name = "articles")
public class Article implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;

    private String title;
    private String description;
    private String text;
//    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
    private List<Comment> comments;
    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "article")
    private List<Visitor> visitors;

    private Boolean visible;
    private Boolean allowComments;
    @DateTimeFormat
    private Date createDate;
    @DateTimeFormat
    private Date deleteDate;

    public Article() {
        this.visible = true;
        this.createDate = new Date();
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
}
