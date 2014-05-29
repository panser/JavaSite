package ua.org.gostroy.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.net.URI;
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

    @NotEmpty(message="{validation.user.login.NotEmpty.message}")
    @Size(min=3, max=60, message="{validation.user.login.Size.message}")
    private String login;
    @NotEmpty(message="{validation.user.email.NotEmpty.message}")
    @Email(message = "{validation.user.email.Email.message}")
    private String email;
    @NotEmpty(message="{validation.user.password.NotEmpty.message}")
    @Size(min=3, max=20, message="{validation.user.password.Size.message}")
    private String password;
    private boolean enabled;
    private String regUrI;
    private String role;
    private UserSex sex;

    @ElementCollection(fetch=FetchType.LAZY)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserAddress> userAddresses;

    @Basic(fetch=FetchType.LAZY)
    @Lob
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
        this.role = "ROLE_USER";
        this.enabled = false;
        this.createDate = new Date();
    }

    public User(String login) {
        this.login = login;
        this.role = "ROLE_USER";
        this.enabled = false;
        this.createDate = new Date();
    }

    public User(String login, String role) {
        this.login = login;
        this.role = role;
        this.enabled = false;
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

    public String getRegUrI() {
        return regUrI;
    }

    public void setRegUrI(String regUrI) {
        this.regUrI = regUrI;
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
                ", regUrI='" + regUrI + '\'' +
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
