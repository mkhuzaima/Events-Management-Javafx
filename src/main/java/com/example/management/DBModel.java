package com.example.management;

import jakarta.mail.MessagingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBModel {
    private static final DBConnection dbConnection = new DBConnection();
    private static final GmailSender gmailSender = new GmailSender();

    //Get All Events
    public ObservableList<MyEvent> getAllEvents() throws SQLException {
        return getEvents("");
    }


    public ObservableList<MyEvent> getPendingEvents() {
        return getEvents("where isFormal is null");
    }

    private ObservableList<MyEvent> getEvents(String condition) {
        ObservableList<MyEvent> myEvents = FXCollections.observableArrayList();
        String query = "SELECT * FROM table_event "+condition;
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.result = dbConnection.preStatement.executeQuery();
            while (dbConnection.result.next()) {
                myEvents.add(new MyEvent(dbConnection.result.getString("Name"),
                        dbConnection.result.getDate("Date").toLocalDate(),
                        dbConnection.result.getTime("Time").toLocalTime().getHour(),
                        dbConnection.result.getTime("Time").toLocalTime().getMinute(),
                        dbConnection.result.getString("Venue"),
                        dbConnection.result.getString("Organizer"),
                        dbConnection.result.getInt("Id")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return myEvents;
    }


    public void approveEvent(int currentId, String type) throws SQLException {

        boolean isFormal = type.equals("Formal");

        String query = "UPDATE table_event SET isFormal = ? WHERE Id = ?";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setBoolean(1,isFormal);
            dbConnection.preStatement.setInt(2,currentId);
            dbConnection.preStatement.executeUpdate();

            String organizerEmail = getOrganizerEmailFromId(currentId);
            ArrayList<String> emails = getAllResidentsEmails();
            emails.add(organizerEmail);

            String message = "Your event has been approved as formal. Please check the system for more details.";
            if (!isFormal) {
                message = "Your event has been approved as recreational. Please check the system for more details.";
            }
            gmailSender.sendEmail(emails, "Event: approved.", message);

        }catch (SQLException e){
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error while sending email");
        } finally {
                dbConnection.preStatement.close();
        }
    }

    //Update Event
    public Boolean updateEvent(String name, LocalDate date, int hours, int minutes, String venue, String organizer, int id) throws SQLException {
        String query = "UPDATE table_event SET Name = ?, Date = ?, Time = ?, Venue = ?, Organizer = ? WHERE Id = ?";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setString(1,name);
            dbConnection.preStatement.setDate(2,java.sql.Date.valueOf(date));
            dbConnection.preStatement.setTime(3,java.sql.Time.valueOf(java.time.LocalTime.of(hours,minutes)));
            dbConnection.preStatement.setString(4,venue);
            dbConnection.preStatement.setString(5,organizer);
            dbConnection.preStatement.setInt(6,id);
            dbConnection.preStatement.executeUpdate();

            ArrayList<String> emails = getAllResidentsEmails();
            String organizerEmail = getOrganizerEmailFromId(id);
            emails.add(organizerEmail);

            gmailSender.sendEmail(emails, "Event: updated.", "An event has been updated in the system. Please check the system for more details.");

            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dbConnection.preStatement.close();
            return false;
        }
    }


    //Delete Event
    public Boolean deleteEvent(int id, boolean isRejected) throws SQLException {
        String organizerEmail = getOrganizerEmailFromId(id);

        String query = "DELETE FROM table_event WHERE Id = ?";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setInt(1,id);
            dbConnection.preStatement.executeUpdate();


//            if event was not approved, send email to only organizer
            if (isRejected) {
                ArrayList<String> recipients = new ArrayList<>();
                recipients.add(organizerEmail);
                gmailSender.sendEmail(recipients, "Event: rejected.", "Your event has been rejected. Please check the system for more details.");
            }
            else {
                ArrayList<String> emails = getAllResidentsEmails();
                emails.add(organizerEmail);
                gmailSender.sendEmail( emails, "Event: deleted.", "An event has been deleted from the system. Please check the system for more details.");
            }

            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            dbConnection.preStatement.close();
            return false;
        }
    }

    //Add Event
    public Boolean addEvent(String name, LocalDate date, int hours, int minutes, String venue, String organizer ) throws SQLException {

        System.out.println("add event called");
        String query = "INSERT INTO table_event (Name, Date, Time, Venue, Organizer) VALUES (?,?,?,?,?)";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setString(1,name);
            dbConnection.preStatement.setDate(2,java.sql.Date.valueOf(date));
            dbConnection.preStatement.setTime(3,java.sql.Time.valueOf(java.time.LocalTime.of(hours,minutes)));
            dbConnection.preStatement.setString(4,venue);
            dbConnection.preStatement.setString(5,organizer);
            dbConnection.preStatement.executeUpdate();

            ArrayList<String> emails = getAllResidentsEmails();
            emails.add(organizer);

            gmailSender.sendEmail(emails, "Event: "+name + " added.", "A new event has been added to the system. Please check the system for more details.");

            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
                dbConnection.preStatement.close();
            return false;
        }

    }
    
    private String getOrganizerEmailFromId(int id) {
        String query = "SELECT organizer from table_event where id = ?";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setInt(1,id);
            dbConnection.result = dbConnection.preStatement.executeQuery();
            if (dbConnection.result.next()) {
                return dbConnection.result.getString("organizer");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    private ArrayList<String> getAllResidentsEmails() {
        ArrayList<String> emails = new ArrayList<>();
        String query = "SELECT email FROM table_resident";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.result = dbConnection.preStatement.executeQuery();
            while (dbConnection.result.next()) {
                emails.add(dbConnection.result.getString("email"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return emails;
    }

    //User Login
    public Boolean UserLogin(String username,String password) throws SQLException{
        dbConnection.preStatement = null;
        dbConnection.result = null;

        String query = "SELECT id FROM user WHERE email = ? AND password = ?";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setString(1,username);
            dbConnection.preStatement.setString(2,password);
            dbConnection.result = dbConnection.preStatement.executeQuery();
            return dbConnection.result.next();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            dbConnection.preStatement.close();
            dbConnection.result.close();
        }
    }

    public void updateResident(String name, String houseNumber, String cluster, String email, int currentId) {
        String query = "UPDATE table_resident SET name = ?, house_number = ?, cluster = ?, email = ? WHERE id = ?";

        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setString(1, name);
            dbConnection.preStatement.setString(2, houseNumber);
            dbConnection.preStatement.setString(3, cluster);
            dbConnection.preStatement.setString(4, email);
            dbConnection.preStatement.setInt(5, currentId);
            dbConnection.preStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addResident(String name, String houseNumber, String cluster, String email) {
        String query = "INSERT INTO table_resident (name, house_number, cluster, email) VALUES (?, ?, ?, ?)";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setString(1, name);
            dbConnection.preStatement.setString(2, houseNumber);
            dbConnection.preStatement.setString(3, cluster);
            dbConnection.preStatement.setString(4, email);
            dbConnection.preStatement.executeUpdate();
        }catch (Exception e){

        }
    }

    public ObservableList<Resident> getAllResidents() {
        // get all residents from the database
        ObservableList<Resident> residents = FXCollections.observableArrayList();
        String query = "SELECT * FROM event_management.table_resident";
        try {
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.result = dbConnection.preStatement.executeQuery();
            while (dbConnection.result.next()) {
                Resident resident = new Resident();
                resident.setId(dbConnection.result.getInt("id"));
                resident.setName(dbConnection.result.getString("name"));
                resident.setHouseNumber(dbConnection.result.getString("house_number"));
                resident.setCluster(dbConnection.result.getString("cluster"));
                resident.setEmail(dbConnection.result.getString("email"));
                residents.add(resident);
            }
            return residents;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteResident(int id) {
        String query = "DELETE FROM table_resident WHERE id = ?";
        try{
            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
            dbConnection.preStatement.setInt(1, id);
            dbConnection.preStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendAnnouncement(String announcement) throws MessagingException {
        ArrayList<String> emails = getAllResidentsEmails();
        gmailSender.sendEmail(emails, "Announcement", announcement);

    }
}
