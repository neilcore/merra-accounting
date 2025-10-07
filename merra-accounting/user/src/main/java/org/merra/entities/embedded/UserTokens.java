package org.merra.entities.embedded;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserTokens implements Serializable {
    private String verificationToken;
    private String resetToken;

    public UserTokens(String verificationToken, String resetToken) {
        this.verificationToken = verificationToken;
        this.resetToken = resetToken;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

}
