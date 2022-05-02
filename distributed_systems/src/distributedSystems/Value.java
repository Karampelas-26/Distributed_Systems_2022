package distributedSystems;

import java.io.Serializable;

public class Value implements Serializable {

    private MultimediaFile multimediaFile;

    public Value(MultimediaFile multimediaFile) {
        this.multimediaFile = multimediaFile;
    }

    public Value() {
    }

}