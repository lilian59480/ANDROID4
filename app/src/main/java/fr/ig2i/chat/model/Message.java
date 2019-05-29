package fr.ig2i.chat.model;

import androidx.annotation.Nullable;

public class Message {

    private Integer id;
    private Integer idUser;
    private Integer idConversation;
    private String message;

    public Message() {
    }

    @Nullable
    public Integer getId() {
        return this.id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @Nullable
    public Integer getIdUser() {
        return this.idUser;
    }

    public void setIdUser(@Nullable Integer idUser) {
        this.idUser = idUser;
    }

    @Nullable
    public Integer getIdConversation() {
        return this.idConversation;
    }

    public void setIdConversation(@Nullable Integer idConversation) {
        this.idConversation = idConversation;
    }

    @Nullable
    public String getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }
}
