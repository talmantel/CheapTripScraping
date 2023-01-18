package org.example.visual.additional_classes;

public class DBCredentials {
    private String user;
    private String password;
    private String URL;
    private String schema;

    public DBCredentials (String user, String password, String URL, String schema) {
        this.user = user;
        this.password = password;
        this.URL = URL;
        this.schema = schema;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "DBCredentials{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", URL='" + URL + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }
}
