package com.tw.mvp.model;
import com.alibaba.fastjson.annotation.JSONField;
import com.tw.mvp.base.tools.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edison on 2016/12/20.
 */
public class TweetsBean
{
    private String content;
    private SenderBean sender;
    private String error;
    @JSONField(name = "unknown error")
    private String unknownError;
    private List<ImagesBean> images = new ArrayList<ImagesBean>();
    private List<CommentsBean> comments = new ArrayList<CommentsBean>();

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public String getUnknownError()
    {
        return unknownError;
    }

    public void setUnknownError(String unknownError)
    {
        this.unknownError = unknownError;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public SenderBean getSender()
    {
        return sender;
    }

    public void setSender(SenderBean sender)
    {
        this.sender = sender;
    }

    public List<ImagesBean> getImages()
    {
        return images;
    }

    public void setImages(List<ImagesBean> images)
    {
        this.images = images;
    }

    public List<CommentsBean> getComments()
    {
        return comments;
    }

    public void setComments(List<CommentsBean> comments)
    {
        this.comments = comments;
    }

    public boolean isValid()
    {
        return StringUtils.isEmpty(error)&&StringUtils.isEmpty(unknownError);
    }
}
