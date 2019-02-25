package pl.vanhok.myapplication;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;



class WebPage  {
    private Document html;

    String getWebAddress() {
        return WebAddress;
    }

    private String WebAddress;
    private boolean isConnected;

    Document getHtml() {
        return html;
    }

    boolean isConnected() {
        return isConnected;
    }


    WebPage(String WebAddress){
        try {
            this.WebAddress = WebAddress;
            html = Jsoup.connect(WebAddress)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .timeout(5000)
                    .get();
            isConnected = true;

        } catch (IOException io) {
            isConnected = false;
            io.printStackTrace();
        }
    }

}
