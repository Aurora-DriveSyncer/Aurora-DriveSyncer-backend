package com.aurora.drivesyncer.entity;

public class Config {
    private String url;
    private String username;
    private String password;
    private String localPath;
    private String filePassword;

    public Config() {
    }

    public Config(String url, String username, String password, String localPath, String filePassword) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.localPath = localPath;
        this.filePassword = filePassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getFilePassword() {
        return filePassword;
    }

    public void setFilePassword(String filePassword) {
        this.filePassword = filePassword;
    }
}
