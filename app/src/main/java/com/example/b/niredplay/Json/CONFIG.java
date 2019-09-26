package com.example.b.niredplay.Json;

public class CONFIG {
    public class API{
       public final static String URL = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?aggr=1&cr=1&flag_qc=0&p=1&n=1&w=";

    }
    public static String getToken(String songmId)
    {
        return "https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?format=json205361747&platform=yqq&cid=205361747&songmid="+songmId+"&filename=C400"+songmId+".m4a&guid=126548448";
    }
    public static String getSongURL(String songmid,String key)
    {
        return "http://ws.stream.qqmusic.qq.com/"+songmid+"?fromtag=0&guid=126548448&vkey="+key;
    }
}
