package fr.ig2i.chat.model.result;

import androidx.annotation.Nullable;

public class IdResult extends Result {

    private int id;

    public IdResult() {
        this(null, 0);
    }

    public IdResult(@Nullable String status, int id) {
        super(status);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
