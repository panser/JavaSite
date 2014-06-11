package ua.org.gostroy.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by panser on 5/23/14.
 */
@Entity
@Table(name = "comments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment implements Serializable {
    private transient final Logger log = LoggerFactory.getLogger(getClass());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;

    @NotEmpty(message="{validation.comment.text.NotEmpty.message}")
    @Size(min=1, max=1000, message="{validation.comment.text.Size.message}")
    private String text;
    private Float sortId;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @NotEmpty(message="{validation.comment.name.NotEmpty.message}")
    @Size(min=3, max=50, message="{validation.comment.name.Size.message}")
    private String name;
    @Email(message = "{validation.comment.email.Email.message}")
    private String email;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;
    private Boolean visible;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Comment parent;
    private Integer depth;
//    @ElementCollection(fetch=FetchType.LAZY)
//    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "id")
//    private List<Comment> children;
    @DateTimeFormat
    private Date createDate;
    @DateTimeFormat
    private Date deleteDate;

    public Comment() {
        this.visible = true;
        this.createDate = new Date();
        this.depth = 0;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

/*
    public Float getSortId() {
//        log.trace("getSortId() before, id:" + getId() + ", sortId:" + getSortId());
        if(sortId != null) return sortId;
        sortId = (float)id;
        Comment tmpObject = this;
        for(int i = 1; i <= depth; i++){
            tmpObject = tmpObject.getParent();
            sortId += tmpObject.getId()/(10*i);
            log.trace("getSortId() in for, id:" + getId() + ", sortId:" + getSortId());
        }
        setSortId(sortId);
        log.trace("getSortId() after, id:" + getId() + ", sortId:" + getSortId());
        return sortId;
    }
*/

    public Float getSortId() {
        return sortId;
    }

    public void setSortId(Float sortId) {
        this.sortId = sortId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
//                ", article=" + article.getId() +
                ", createDate=" + createDate +
                '}';
    }


    public static Comparator<Comment> сommentDepthComparator = new Comparator<Comment>(){
        private transient final Logger log = LoggerFactory.getLogger(getClass());
        @Override
        public int compare(Comment o1, Comment o2) {
            if(hashCodeByDepthId(o1) > hashCodeByDepthId(o2)){
                return 1;
            }
            return -1;
        }

        public float hashCodeByDepthId(Comment obj) {
            if(obj.getSortId() != null){
//            if(obj.getSortId() != 0 && obj.getSortId() != null){
                return obj.getSortId();
            }

            long id = obj.getId();
            int depth = obj.getDepth();
            int idLength = String.valueOf(id).length();
//            log.trace("hashCodeByDepthId() idLength: " + idLength);
            float sortId = (float)id;
//            log.trace("hashCodeByDepthId() id: " + id + "start. depthId:" + sortId);
            for(int i = 1; i <= depth; ++i){
                obj = obj.getParent();
                sortId = sortId/(int)(Math.pow(10,idLength)) + obj.getId();
//                log.trace("hashCodeByDepthId() id: " + id + "in FOR depthId:" + sortId);
            }
//            log.trace("hashCodeByDepthId() id: " + id + "finish. depthId:" + sortId);
            return sortId;
        }
    };

}
