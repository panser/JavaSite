package ua.org.gostroy.entity;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by panser on 5/23/14.
 */
@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;

    private String text;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    private String name;
    @Email
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

    /*
    public List<Comment> getChildren() {
        return children;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }
*/

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

    public static Comparator<Comment> CommentDepthComparator = new Comparator<Comment>(){
        @Override
        public int compare(Comment o1, Comment o2) {
            Long date1 = o1.getCreateDate().getTime();
            Long date2 = o2.getCreateDate().getTime();

            int tmp = 1;
            if(date1 < date2){
                tmp = -1;
                if(o2.getParent() != null) {
                    if (o2.getParent().getCreateDate().getTime() > o1.getCreateDate().getTime()) {
                        tmp = 1;
                    }
                }
            }
            return tmp;
        }
    };

}
