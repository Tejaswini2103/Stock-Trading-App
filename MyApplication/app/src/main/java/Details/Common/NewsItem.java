package Details.Common;
public class NewsItem {
    private String publisher;
    private String publishedAt;
    private String title;
    private String url;
    private String urlToImage;
    private String summary;

    NewsItem(){}
    public NewsItem(String publisher, String publishedAt, String title, String url, String urlToImage, String summary) {
        this.publisher = publisher;
        this.publishedAt = publishedAt;
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;
        this.summary = summary;
    }
    public String getUrlToImage() { return urlToImage; }
    public String getPublisher() { return publisher; }
    public String getPublishedAt() { return publishedAt; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getSummary() { return summary; }
}