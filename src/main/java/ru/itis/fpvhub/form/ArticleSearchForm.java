package ru.itis.fpvhub.form;

public class ArticleSearchForm {
    private String q;
    private String category;
    private String tag;
    private boolean videoOnly;
    private String sort = "newest";

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isVideoOnly() {
        return videoOnly;
    }

    public void setVideoOnly(boolean videoOnly) {
        this.videoOnly = videoOnly;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean hasCategory(String slug) {
        return slug != null && slug.equals(category);
    }

    public boolean hasTag(String slug) {
        return slug != null && slug.equals(tag);
    }

    public boolean hasSort(String value) {
        return value != null && value.equals(sort);
    }
}
