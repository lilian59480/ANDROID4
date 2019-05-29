package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

public class Result {
    @Nullable
    protected String status;

    public Result() {
        this(null);
    }

    public Result(@Nullable String status) {
        this.status = status;
    }

    @Nullable
    public String getStatus() {
        return this.status;
    }

    public void setStatus(@Nullable String status) {
        this.status = status;
    }
}
