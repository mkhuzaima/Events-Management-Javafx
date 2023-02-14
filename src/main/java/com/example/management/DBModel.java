package com.example.management;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;

        import java.sql.SQLException;

public class DBModel {
    private static DBConnection dbConnection = new DBConnection();

//    //Add User
//    public Boolean addUser(String fname,String lname,String phone,String address,String username,String password) throws SQLException {
//        String query = "INSERT INTO user(fname,lname,phone,address,username,password) VALUES(?,?,?,?,?,?)";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.setString(1,fname);
//            dbConnection.preStatement.setString(2,lname);
//            dbConnection.preStatement.setString(3,phone);
//            dbConnection.preStatement.setString(4,address);
//            dbConnection.preStatement.setString(5,username);
//            dbConnection.preStatement.setString(6,password);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            dbConnection.preStatement.close();
//            return false;
//        }
//    }
//    //Add Covid Data
//    public Boolean addCovidData(String country,String totalCases,String totalDeath,String activeCases,String totalVaccination) throws SQLException{
//        String query = "INSERT INTO covid_data(country,total_cases,total_death,active_cases,total_vaccination) VALUES(?,?,?,?,?)";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.setString(1,country);
//            dbConnection.preStatement.setString(2,totalCases);
//            dbConnection.preStatement.setString(3,totalDeath);
//            dbConnection.preStatement.setString(4,activeCases);
//            dbConnection.preStatement.setString(5,totalVaccination);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            dbConnection.preStatement.close();
//            return false;
//        }
//    }
//    //Add Favourite
//    public Boolean addFavourite(String country) throws SQLException{
//        String query = "INSERT INTO favourite(country) VALUES(?)";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.setString(1,country);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            dbConnection.preStatement.close();
//        }
//    }
//    //Add Moderate
//    public Boolean addModerate(String fname,String lname,String phone,String address,String username,String password) throws SQLException{
//        String query = "INSERT INTO moderate(fname,lname,phone,address,username,password) VALUES(?,?,?,?,?,?)";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.setString(1,fname);
//            dbConnection.preStatement.setString(2,lname);
//            dbConnection.preStatement.setString(3,phone);
//            dbConnection.preStatement.setString(4,address);
//            dbConnection.preStatement.setString(5,username);
//            dbConnection.preStatement.setString(6,password);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            dbConnection.preStatement.close();
//            return false;
//        }
//    }
//    //View User
//    public ObservableList<User> getUser() throws SQLException {
//        ObservableList<User> datalist = FXCollections.observableArrayList();
//        String query = "SELECT * FROM user";
//        try {
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.result = dbConnection.preStatement.executeQuery();
//            while (dbConnection.result.next()){
//                User user = new User();
//                user.setId(dbConnection.result.getInt("id"));
//                user.setFname(dbConnection.result.getString("fname"));
//                user.setLname(dbConnection.result.getString("lname"));
//                user.setPhone(dbConnection.result.getString("phone"));
//                user.setAddress(dbConnection.result.getString("address"));
//                user.setUsername(dbConnection.result.getString("username"));
//                user.setPassword(dbConnection.result.getString("password"));
//                datalist.add(user);
//            }
//            return datalist;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //View Moderate
//    public ObservableList<Moderate> getModerate() throws SQLException {
//        ObservableList<Moderate> datalist = FXCollections.observableArrayList();
//        String query = "SELECT * FROM moderate";
//        try {
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.result = dbConnection.preStatement.executeQuery();
//            while (dbConnection.result.next()){
//                Moderate moderate = new Moderate();
//                moderate.setId(dbConnection.result.getInt("id"));
//                moderate.setFname(dbConnection.result.getString("fname"));
//                moderate.setLname(dbConnection.result.getString("lname"));
//                moderate.setPhone(dbConnection.result.getString("phone"));
//                moderate.setAddress(dbConnection.result.getString("address"));
//                moderate.setUsername(dbConnection.result.getString("username"));
//                moderate.setPassword(dbConnection.result.getString("password"));
//                datalist.add(moderate);
//            }
//            return datalist;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //View COVID data
//    public ObservableList<CovidData> getCovidData() throws SQLException{
//        ObservableList<CovidData> datalist = FXCollections.observableArrayList();
//        String query = "SELECT * FROM covid_data";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.result = dbConnection.preStatement.executeQuery();
//            while (dbConnection.result.next()){
//                CovidData covidData = new CovidData();
//                covidData.setId(dbConnection.result.getInt(1));
//                covidData.setCountry(dbConnection.result.getString(2));
//                covidData.setTotalCases(dbConnection.result.getString(3));
//                covidData.setTotalDeath(dbConnection.result.getString(4));
//                covidData.setActiveCases(dbConnection.result.getString(5));
//                covidData.setTotalVaccination(dbConnection.result.getString(6));
//                datalist.add(covidData);
//            }
//            return datalist;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //View Favourite
//    public ObservableList<Favourite> getFavourite() throws SQLException{
//        dbConnection.preStatement = null;
//        dbConnection.result = null;
//        ObservableList<Favourite> datalist = FXCollections.observableArrayList();
//        String query = "SELECT * FROM favourite";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.result  =dbConnection.preStatement.executeQuery();
//            while (dbConnection.result.next()){
//                Favourite favourite = new Favourite();
//                favourite.setId(dbConnection.result.getInt("id"));
//                favourite.setCountry(dbConnection.result.getString("country"));
//                datalist.add(favourite);
//            }
//            return datalist;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //Search User
//    public User searchUser(String id) throws SQLException{
//        User user = new User();
//        String query = "SELECT * FROM user WHERE id="+id;
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.result = dbConnection.preStatement.executeQuery();
//            while (dbConnection.result.next()){
//                user.setId(dbConnection.result.getInt("id"));
//                user.setFname(dbConnection.result.getString("fname"));
//                user.setLname(dbConnection.result.getString("lname"));
//                user.setPhone(dbConnection.result.getString("phone"));
//                user.setAddress(dbConnection.result.getString("address"));
//                user.setUsername(dbConnection.result.getString("username"));
//                user.setPassword(dbConnection.result.getString("password"));
//            }
//            return user;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //Search Covid Data
//    public CovidData searchData(String id) throws SQLException{
//        CovidData covidData = new CovidData();
//        String query = "SELECT * FROM covid_data WHERE id="+id;
//        try {
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.result = dbConnection.preStatement.executeQuery();
//            while(dbConnection.result.next()){
//                covidData.setId(dbConnection.result.getInt("id"));
//                covidData.setCountry(dbConnection.result.getString("country"));
//                covidData.setTotalCases(dbConnection.result.getString("total_cases"));
//                covidData.setTotalDeath(dbConnection.result.getString("total_death"));
//                covidData.setActiveCases(dbConnection.result.getString("active_cases"));
//                covidData.setTotalVaccination(dbConnection.result.getString("total_vaccination"));
//            }
//            return covidData;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //Delete Data
//    public boolean deleteData(String id) throws SQLException{
//        String query = "DELETE FROM covid_data WHERE id="+id;
//        try {
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            dbConnection.preStatement.close();
//        }
//    }
//    //Update Data
//    public Boolean updateData(String Id,String country,String totalCases,String totalDeath,String activeCases,String totalVaccination) throws SQLException{
//        dbConnection.preStatement = null;
//        String query = "UPDATE covid_data SET country='"+country+"',total_cases='"+totalCases+"',total_death='"+totalDeath+"',active_cases='"+activeCases+"',total_vaccination='"+totalVaccination+"' WHERE id='"+Id+"'";
//        try {
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }
//    //Delete User
//    public boolean deleteUser(String id) throws SQLException{
//        String query = "DELETE FROM user WHERE id="+id;
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            dbConnection.preStatement.close();
//        }
//    }
//    //Search Moderate
//    public Moderate searchModerate(String id) throws SQLException{
//        Moderate user = new Moderate();
//        String query = "SELECT * FROM moderate WHERE id="+id;
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.result = dbConnection.preStatement.executeQuery();
//            while (dbConnection.result.next()){
//                user.setId(dbConnection.result.getInt("id"));
//                user.setFname(dbConnection.result.getString("fname"));
//                user.setLname(dbConnection.result.getString("lname"));
//                user.setPhone(dbConnection.result.getString("phone"));
//                user.setAddress(dbConnection.result.getString("address"));
//                user.setUsername(dbConnection.result.getString("username"));
//                user.setPassword(dbConnection.result.getString("password"));
//            }
//            return user;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //Delete Moderate
//    public boolean deleteModerate(String id) throws SQLException{
//        String query = "DELETE FROM moderate WHERE id="+id;
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            dbConnection.preStatement.close();
//        }
//    }
//    //Delete Favourite
//    public Boolean deleteFavourite(String country) throws SQLException{
//        String query = "DELETE FROM favourite WHERE country ='"+country+"'";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.executeUpdate();
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            dbConnection.preStatement.close();
//        }
//    }
//    //Moderate Login
//    public Boolean ModerateLogin(String username,String password) throws SQLException{
//        dbConnection.preStatement = null;
//        dbConnection.result = null;
//        String query = "SELECT * FROM moderate WHERE username = ? AND password = ?";
//        try{
//            dbConnection.preStatement = dbConnection.connection.prepareStatement(query);
//            dbConnection.preStatement.setString(1,username);
//            dbConnection.preStatement.setString(2,password);
//            dbConnection.result = dbConnection.preStatement.executeQuery();
//            return dbConnection.result.next();
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            dbConnection.preStatement.close();
//            dbConnection.result.close();
//        }
//    }
//
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
}
