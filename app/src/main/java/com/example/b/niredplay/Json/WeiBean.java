package com.example.b.niredplay.Json;

import java.util.List;

public class WeiBean {
    /**
     * Code : OK
     * Body : [{"id":536570450,"title":"魔术与歌曲：告白气球","author":"周杰伦","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=536570450","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=536570450","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=536570450"},{"id":298317,"title":"屋顶","author":"温岚","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=298317","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=298317","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=298317"},{"id":210049,"title":"布拉格广场","author":"蔡依林","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=210049","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=210049","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=210049"},{"id":210062,"title":"骑士精神","author":"蔡依林","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=210062","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=210062","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=210062"},{"id":255020,"title":"刀马旦","author":"李玟","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=255020","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=255020","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=255020"},{"id":298101,"title":"祝我生日快乐 (Live)","author":"温岚","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=298101","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=298101","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=298101"},{"id":185742,"title":"霍元甲(Live)","author":"周杰伦","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=185742","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=185742","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=185742"},{"id":186001,"title":"七里香","author":"周杰伦","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=186001","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=186001","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=186001"},{"id":418603077,"title":"告白气球","author":"周杰伦","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=418603077","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=418603077","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=418603077"},{"id":186016,"title":"晴天","author":"周杰伦","url":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=186016","pic":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=186016","lrc":"https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=186016"}]
     */

    private String Code;
    private List<BodyBean> Body;

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public List<BodyBean> getBody() {
        return Body;
    }

    public void setBody(List<BodyBean> Body) {
        this.Body = Body;
    }

    public static class BodyBean {
        /**
         * id : 536570450
         * title : 魔术与歌曲：告白气球
         * author : 周杰伦
         * url : https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=url&id=536570450
         * pic : https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=pic&id=536570450
         * lrc : https://api.mlwei.com/music/api/wy/?key=523077333&cache=1&type=lrc&id=536570450
         */

        private int id;
        private String title;
        private String author;
        private String url;
        private String pic;
        private String lrc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getLrc() {
            return lrc;
        }

        public void setLrc(String lrc) {
            this.lrc = lrc;
        }
    }
}
