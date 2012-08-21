package net.unicon.cas.passwordmanager.flow;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>Bean for holding the security challenge. Consists of the user's NetID
 * and a list of SecurityQuestion objects.</p>
 */
public class SecurityChallenge implements Serializable {

    private static final long serialVersionUID = 1L;

    // Instance Members.
    private final String netId;
    private final List<SecurityQuestion> questions;
    
    public SecurityChallenge(String netId, List<SecurityQuestion> questions) {
        
        // Assertions.
        if (netId == null) {
            String msg = "Argument 'netId' cannot be null";
            throw new IllegalArgumentException(msg);
        }
        if (questions == null) {
            String msg = "Argument 'questions' cannot be null";
            throw new IllegalArgumentException(msg);
        }
        if (questions.size() == 0) {
            String msg = "Argument 'questions' must contain at least one element";
            throw new IllegalArgumentException(msg);
        }
        
        this.netId = netId;
        this.questions = Collections.unmodifiableList(questions);

    }
    
    public String getNetId() {
        return netId;
    }
    
    public List<SecurityQuestion> getQuestions() {
        return questions;
    }
    
}
