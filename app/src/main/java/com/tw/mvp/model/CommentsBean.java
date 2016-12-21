package com.tw.mvp.model;
/**
 * Created by Leagmain on 2014/9/28 0028.
 */
public class CommentsBean
{
    private String content;
    private SenderBean sender;

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
}
