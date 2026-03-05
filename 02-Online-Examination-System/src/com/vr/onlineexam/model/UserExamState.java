import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
package com.vr.onlineexam.model;
/**
 * Keeps user answers and time, and calculates marks using question marks.
 */
public class UserExamState implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String userName;
    private final Map<String,Integer> answers = new HashMap<>();
    private final Map<String,Integer> timeTaken = new HashMap<>();

    public UserExamState(User u) { this.userName = u.getUsername(); }

    public void setAnswer(String qid, int idx) { answers.put(qid, idx); }
    public int getAnswer(String qid) { return answers.getOrDefault(qid, -1); }

    public void setTimeTaken(String qid, int seconds) { timeTaken.put(qid, seconds); }
    public int getTimeTaken(String qid) { return timeTaken.getOrDefault(qid, 0); }

    public int calculateMarks(Question[] questions) {
        int score = 0;
        if (questions == null) return 0;
        for (Question q : questions) {
            int sel = getAnswer(q.getId());
            if (q.isCorrect(sel)) score += q.getMarks();
        }
        return score;
    }

    public void reset() { answers.clear(); timeTaken.clear(); }
    public String getUserName() { return userName; }
}

