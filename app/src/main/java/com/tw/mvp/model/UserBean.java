package com.tw.mvp.model;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Edison on 2016/12/20.
 */
public class UserBean
{
    @JSONField(name = "profile-image")
    private String profileImage;
    private String avatar;
    private String nick;
    private String username;

    public String getProfileImage()
    {
        return profileImage;
    }

    public void setProfileImage(String profileImage)
    {
        this.profileImage = profileImage;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getNick()
    {
        return nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Override
    public String toString()
    {
        return "UserInfo{" +
                "profileImage='" + profileImage + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nick='" + nick + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
