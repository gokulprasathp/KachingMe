package com.wifin.kachingme.pojo;

public class MessageGetSet {

    int _id;
    int key_from_me;
    String key_id;
    int status;
    int needs_push;
    String data;
    long timestamp;
    long received_timestamp;
    long send_timestamp;
    long receipt_server_timestamp;
    long receipt_device_timestamp;
    int media_size;
    String media_url;
    String media_mime_type;
    String media_wa_type;
    String media_name;
    String media_hash;
    int media_duration;
    int origin;
    Double latitude;
    Double longitude;
    byte[] thumb_image;
    byte[] row_data;
    int is_sec_chat;
    int is_owner;
    int mPosition;
    long self_des_time;
    String remote_resource;
    String key_remote_jid;

    public void setPostion(int position) {

        this.mPosition = position;

    }

    public int getPosition() {

        return mPosition;
    }

    public int getIs_owner() {
        return is_owner;
    }

    public void setIs_owner(int is_owner) {
        this.is_owner = is_owner;
    }

    public int getIs_sec_chat() {
        return is_sec_chat;
    }

    public void setIs_sec_chat(int is_sec_chat) {
        this.is_sec_chat = is_sec_chat;
    }

    public long getSelf_des_time() {
        return self_des_time;
    }

    public void setSelf_des_time(long self_des_time) {
        this.self_des_time = self_des_time;
    }

    public byte[] getRow_data() {
        return row_data;
    }

    public void setRow_data(byte[] row_data) {
        this.row_data = row_data;
    }

    public byte[] getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(byte[] thumb_image) {
        this.thumb_image = thumb_image;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getKey_remote_jid() {
        return key_remote_jid;
    }

    public void setKey_remote_jid(String key_remote_jid) {
        this.key_remote_jid = key_remote_jid;
    }

    public int getKey_from_me() {
        return key_from_me;
    }

    public void setKey_from_me(int key_from_me) {
        this.key_from_me = key_from_me;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNeeds_push() {
        return needs_push;
    }

    public void setNeeds_push(int needs_push) {
        this.needs_push = needs_push;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getReceived_timestamp() {
        return received_timestamp;
    }

    public void setReceived_timestamp(long received_timestamp) {
        this.received_timestamp = received_timestamp;
    }

    public long getSend_timestamp() {
        return send_timestamp;
    }

    public void setSend_timestamp(long send_timestamp) {
        this.send_timestamp = send_timestamp;
    }

    public long getReceipt_server_timestamp() {
        return receipt_server_timestamp;
    }

    public void setReceipt_server_timestamp(long receipt_server_timestamp) {
        this.receipt_server_timestamp = receipt_server_timestamp;
    }

    public long getReceipt_device_timestamp() {
        return receipt_device_timestamp;
    }

    public void setReceipt_device_timestamp(long receipt_device_timestamp) {
        this.receipt_device_timestamp = receipt_device_timestamp;
    }

    public String getRemote_resource() {
        return remote_resource;
    }

    public void setRemote_resource(String remote_resource) {
        this.remote_resource = remote_resource;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_mime_type() {
        return media_mime_type;
    }

    public void setMedia_mime_type(String media_mime_type) {
        this.media_mime_type = media_mime_type;
    }

    public String getMedia_wa_type() {
        return media_wa_type;
    }

    public void setMedia_wa_type(String media_wa_type) {
        this.media_wa_type = media_wa_type;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_hash() {
        return media_hash;
    }

    public void setMedia_hash(String media_hash) {
        this.media_hash = media_hash;
    }

    public int getMedia_duration() {
        return media_duration;
    }

    public void setMedia_duration(int media_duration) {
        this.media_duration = media_duration;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getMedia_size() {
        return media_size;
    }

    public void setMedia_size(int media_size) {
        this.media_size = media_size;
    }

}
