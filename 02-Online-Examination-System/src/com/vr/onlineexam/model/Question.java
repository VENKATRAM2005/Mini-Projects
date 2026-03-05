import java.io.Serializable;
import java.util.List;
import java.util.Objects;
package com.vr.onlineexam.model;
/**
 * Simple immutable question model with explanation and marks.
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 3L;

    public enum Type { MULTIPLE_CHOICE, TRUE_FALSE }

    private final String id;
    private final String text;
    private final List<String> options;
    private final int correctIndex;
    private final Type type;
    private final int marks;
    private final String explanation;

    public Question(String id, String text, java.util.List<String> options, int correctIndex, Type type, int marks, String explanation) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
        this.type = type;
        this.marks = marks;
        this.explanation = explanation == null ? "" : explanation;
    }

    public String getId() { return id; }
    public String getText() { return text; }
    public java.util.List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public Type getType() { return type; }
    public int getMarks() { return marks; }
    public String getExplanation() { return explanation; }

    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question q = (Question) o;
        return Objects.equals(id, q.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

