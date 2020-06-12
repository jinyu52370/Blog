package com.jinyu.blog.vo;

/**
 * @author <a href="jinyu52370@163.com">JJJ</a>
 * @date 2020/6/3 20:49
 */
public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommend;

    public BlogQuery() {
    }

    public BlogQuery(Long typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}
