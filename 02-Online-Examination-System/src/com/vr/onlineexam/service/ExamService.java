// ExamService.java
// Simple service that provides exam questions. File-backed later if needed.
package com.vr.onlineexam.service;
import java.util.*;

public class ExamService {

    public static class Question {
        public final String text;
        public final String[] options;
        public final int correctIndex;
        public final String explanation;

        public Question(String text, String[] options, int correctIndex, String explanation) {
            this.text = text;
            this.options = options;
            this.correctIndex = correctIndex;
            this.explanation = explanation;
        }
    }

    /** Returns a default set of 10 Java questions (easy-medium). */
    public static List<Question> getDefaultQuestions() {
        List<Question> qs = new ArrayList<>();
        qs.add(new Question("Which of these is NOT a primitive type in Java?", new String[]{"int","boolean","String","double"}, 2, "String is a reference type."));
        qs.add(new Question("What keyword is used to inherit a class in Java?", new String[]{"implements","extends","inherits","uses"}, 1, "extends is used for class inheritance."));
        qs.add(new Question("Which collection preserves insertion order and allows null elements?", new String[]{"HashSet","ArrayList","Hashtable","PriorityQueue"}, 1, "ArrayList preserves insertion order."));
        qs.add(new Question("Which exception is thrown for an invalid array index?", new String[]{"NullPointerException","ArrayIndexOutOfBoundsException","IndexOutOfRange","IllegalArgumentException"}, 1, "ArrayIndexOutOfBoundsException."));
        qs.add(new Question("Which method is the entry point of a Java application?", new String[]{"start()","main(String[] args)","run()","init()"}, 1, "main(String[] args) is the entry."));
        qs.add(new Question("Which keyword prevents a method from being overridden?", new String[]{"static","final","native","private"}, 1, "final prevents overriding."));
        qs.add(new Question("Which interface is used for external sorting order?", new String[]{"Comparator","Serializable","Iterable","Comparable"}, 0, "Comparator supplies external comparison."));
        qs.add(new Question("What does JVM stand for?", new String[]{"Java Virtual Machine","Java Variable Manager","Java Version Manager","Joint Virtual Machine"}, 0, "Java Virtual Machine."));
        qs.add(new Question("Which keyword is used to handle exceptions in Java?", new String[]{"try-catch","handle","except","trap"}, 0, "try-catch blocks handle exceptions."));
        qs.add(new Question("Which access modifier allows visibility only within the same package?", new String[]{"public","private","protected","default (no modifier)"}, 3, "Package-private (no modifier)."));
        return qs;
    }
}
