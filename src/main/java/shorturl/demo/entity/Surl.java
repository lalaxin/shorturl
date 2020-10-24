package shorturl.demo.entity;

public class Surl {
    private String urlId;
    private String shortId;
    private String longUrl;
    private String createTime;
    private String viewPwd;

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getViewPwd() {
        return viewPwd;
    }

    public void setViewPwd(String viewPwd) {
        this.viewPwd = viewPwd;
    }

    @Override
    public String toString() {
        return "Surl{" +
                "urlId='" + urlId + '\'' +
                ", shortId='" + shortId + '\'' +
                ", longUrl='" + longUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", viewPwd='" + viewPwd + '\'' +
                '}';
    }
}
