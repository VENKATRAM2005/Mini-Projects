/**
 * Passenger model class
 * Author: Venkatram
 */
package com.venkatram.oibsip.task1;
public class Passenger {

    private final String name;
    private final int age;
    private final String from;
    private final String to;
    private final String train;

    public Passenger(String name, int age, String from, String to, String train) {
        this.name = name;
        this.age = age;
        this.from = from;
        this.to = to;
        this.train = train;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getTrain() { return train; }

    @Override
    public String toString() {
        return "Passenger: " + name +
               "\nAge: " + age +
               "\nFrom: " + from +
               "\nTo: " + to +
               "\nTrain: " + train;
    }
}
