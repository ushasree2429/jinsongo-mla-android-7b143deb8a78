package com.paril.mlaclientapp.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userId",
        "lastname"
})
public class UserModel {
    @JsonProperty("userId")
    public String userId;
    @JsonProperty("publicKey")
    public String publicKey;

    @JsonProperty("privateKey")
    public String privateKey;
    @JsonIgnore
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("userId")
    public String getuserIde() {
        return userId;
    }

    @JsonProperty("userId")
    public void setuserId(String firstname) {
        this.userId = userId;
    }

    @JsonProperty("publicKey")
    public String getpublicKey() {
        return publicKey;
    }

    @JsonProperty("publicKey")
    public void setpublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @JsonProperty("privateKey")
    public String getPrivateKey() {
        return privateKey;
    }
    @JsonProperty("privateKey")
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

  public String toJson()
  {
      return "{\"userId\":\"9\",\n"+
              "\"publicKey\":\"65869\",\n"+
              "\"privateKey\":\"67589--896\"}";
  }
}
