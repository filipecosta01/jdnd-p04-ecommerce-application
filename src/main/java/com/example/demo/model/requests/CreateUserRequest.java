package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
    private String password;

    @JsonProperty
    private String mirrorPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMirrorPassword() {
        return mirrorPassword;
    }

    public void setMirrorPassword(final String mirrorPassword) {
        this.mirrorPassword = mirrorPassword;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
