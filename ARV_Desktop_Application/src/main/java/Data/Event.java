package Data;

import javafx.beans.property.SimpleStringProperty;

import java.util.Date;
import java.util.TimeZone;

public class Event {
    private SimpleStringProperty nameOne;
    private SimpleStringProperty nameTwo;
    private SimpleStringProperty date;
    private SimpleStringProperty time;
    private SimpleStringProperty competition;
    private SimpleStringProperty oddsH;
    private SimpleStringProperty oddsA;
    private SimpleStringProperty eventID;

    public Event(String nameOne, String nameTwo, String date, String time, String competition, String oddsH, String oddsA, String eventID) {
        this.nameOne = new SimpleStringProperty(nameOne);
        this.nameTwo = new SimpleStringProperty(nameTwo);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.competition = new SimpleStringProperty(competition);
        this.oddsH = new SimpleStringProperty(oddsH);
        this.oddsA = new SimpleStringProperty(oddsA);
        this.eventID = new SimpleStringProperty(eventID);
    }

    public Event(String nameOne, String nameTwo, String date, String time, String competition) {
        this.nameOne = new SimpleStringProperty(nameOne);
        this.nameTwo = new SimpleStringProperty(nameTwo);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.competition = new SimpleStringProperty(competition);
    }

    public String getNameOne() {
        return nameOne.get();
    }

    public SimpleStringProperty nameOneProperty() {
        return nameOne;
    }

    public void setNameOne(String nameOne) {
        this.nameOne.set(nameOne);
    }

    public String getNameTwo() {
        return nameTwo.get();
    }

    public SimpleStringProperty nameTwoProperty() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo.set(nameTwo);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getCompetition() {
        return competition.get();
    }

    public SimpleStringProperty competitionProperty() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition.set(competition);
    }

    public String getOddsH() {
        return oddsH.get();
    }

    public SimpleStringProperty oddsHProperty() {
        return oddsH;
    }

    public void setOddsH(String oddsH) {
        this.oddsH.set(oddsH);
    }

    public String getOddsA() {
        return oddsA.get();
    }

    public SimpleStringProperty oddsAProperty() {
        return oddsA;
    }

    public void setOddsA(String oddsA) {
        this.oddsA.set(oddsA);
    }

    public String getEventID() {
        return eventID.get();
    }

    public SimpleStringProperty eventIDProperty() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID.set(eventID);
    }

    public static String generateQuestion(String nameOne, String nameTwo, String date, String time, String competition) {

        char lastDigit = date.charAt(date.length()-1);
        String lastTwo = date.substring(date.length()-2);
        String toAdd;
        if (lastDigit == '1' || lastDigit == '4' || lastDigit == '5' || lastDigit == '7' || lastDigit == '9' || lastTwo.equals("10")) {
            toAdd = "-én";
        } else {
            toAdd = "-án";
        }

        boolean isDaylight = TimeZone.getDefault().inDaylightTime(new Date());
        String timezone = isDaylight ? "CEST " : "CET ";

        return "Ki fogja megnyerni a " + competition + ", " +
                nameOne + " és " +  nameTwo + " közötti asztalitenisz mérkőzést," +
                " amely " + date + toAdd + " " + timezone + time + " órakor kezdődik?";
    }

    public static String questionWithID(String eventID) {
        String pronoun;
        char firstDigit = eventID.charAt(0);
        if (firstDigit == '1' || firstDigit == '5') {
            pronoun = "az";
        } else {
            pronoun = "a";
        }
        return "Ki fogja megnyerni " + pronoun + " " + eventID + " azonosítószámú asztalitenisz mérkőzést?";
    }

}
