package ua.org.gostroy.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by panser on 5/20/14.
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;

    private String email;
    private String login;
    private String password;
    private boolean enabled;
    private String role;

    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserAddress> userAddresses;
    private UserSex sex;

//    @Lob
//    private MultipartFile avatarImage;
    private byte[] avatarImage;
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private Date birthDay;
    @DateTimeFormat
    private Date createDate;
    @DateTimeFormat
    private Date deleteDate;

    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "author")
    private List<Article> articles;
    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "author")
    private List<Comment> comments;

    public User() {
        role = "ROLE_USER";
        enabled = true;
        createDate = new Date();
    }

    public User(String login) {
        login = login;
        role = "ROLE_USER";
        enabled = true;
        createDate = new Date();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(byte[] avatarImage) {
        this.avatarImage = avatarImage;
    }

/*
    public MultipartFile getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(MultipartFile avatarImage) {
        this.avatarImage = avatarImage;
    }
*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<UserAddress> getUserAddresses() {
        return userAddresses;
    }

    public void setUserAddresses(List<UserAddress> userAddresses) {
        this.userAddresses = userAddresses;
    }

    public UserSex getSex() {
        return sex;
    }

    public void setSex(UserSex sex) {
        this.sex = sex;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
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

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", version=" + version +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
//                ", articles=" + articles +
//                ", comments=" + comments +
                '}';
    }

/*
    public void addArticle(Article article){
        articles.add(article);
    }
*/
}
