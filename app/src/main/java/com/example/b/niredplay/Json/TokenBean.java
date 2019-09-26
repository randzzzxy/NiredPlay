package com.example.b.niredplay.Json;

import java.util.List;

public class TokenBean {

    /**
     * code : 0
     * cid : 205361747
     * userip : 113.5.3.171
     * data : {"expiration":80400,"items":[{"subcode":0,"songmid":"004Wzo7K0D2qMQ","filename":"C400004Wzo7K0D2qMQ.m4a","vkey":"9688CBB94A0D67DD4DBB6D02DB373C34845D2379F45ACE0F13BD22D1EAD392D5B70C503CB3131351AE04FD4CD7716FC05C89BFC2F6B0F452"}]}
     */

    private int code;
    private int cid;
    private String userip;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * expiration : 80400
         * items : [{"subcode":0,"songmid":"004Wzo7K0D2qMQ","filename":"C400004Wzo7K0D2qMQ.m4a","vkey":"9688CBB94A0D67DD4DBB6D02DB373C34845D2379F45ACE0F13BD22D1EAD392D5B70C503CB3131351AE04FD4CD7716FC05C89BFC2F6B0F452"}]
         */

        private int expiration;
        private List<ItemsBean> items;

        public int getExpiration() {
            return expiration;
        }

        public void setExpiration(int expiration) {
            this.expiration = expiration;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            /**
             * subcode : 0
             * songmid : 004Wzo7K0D2qMQ
             * filename : C400004Wzo7K0D2qMQ.m4a
             * vkey : 9688CBB94A0D67DD4DBB6D02DB373C34845D2379F45ACE0F13BD22D1EAD392D5B70C503CB3131351AE04FD4CD7716FC05C89BFC2F6B0F452
             */

            private int subcode;
            private String songmid;
            private String filename;
            private String vkey;

            public int getSubcode() {
                return subcode;
            }

            public void setSubcode(int subcode) {
                this.subcode = subcode;
            }

            public String getSongmid() {
                return songmid;
            }

            public void setSongmid(String songmid) {
                this.songmid = songmid;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getVkey() {
                return vkey;
            }

            public void setVkey(String vkey) {
                this.vkey = vkey;
            }
        }
    }
}
