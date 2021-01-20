package Data;

import java.io.*;
import java.sql.*;
import java.util.*;


public class DataModel {

    private Connection connection;

    private final String path = System.getProperty("user.home") + File.separator + "ARV";
    private final File arv = new File(path);

    private final String lotteryPath = path + File.separator + "Lottery";
    private final String lotteryConfPath = lotteryPath + File.separator + "Config";
    private final String sportPath = path + File.separator + "Sport";
    private final String sportColorsPath = path + File.separator + "Sport" + File.separator + "Colors";
    private final String sportColorsConfPath = sportColorsPath + File.separator + "Config";
    private final String sportPicsPath = path + File.separator + "Sport" + File.separator + "Pictures";
    private final String sportPicsConfPath = sportPicsPath + File.separator + "Config" + File.separator + "Pics";

    private DataModel() {}

    private static final DataModel instance = new DataModel();

    public static DataModel getInstance () {
        return instance;
    }



    public void open() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + path + File.separator + "ARV.db");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS 'recent images' (file TEXT, date TEXT)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
   }

    public String getLotteryConfPath() {
        return lotteryConfPath;
    }

    public String getSportColorsConfPath() {
        return sportColorsConfPath;
    }

    public String getSportPicsConfPath() {
        return sportPicsConfPath;
    }

    public void createFolders() throws IOException {

        if (! arv.exists()) {
            if (arv.mkdir()){
                File lottery = new File(lotteryConfPath);
                if(lottery.mkdirs()) {
                    File colors = new File(lottery.getPath() + File.separator + "colors.txt");
                    colors.createNewFile();
                    File shapes = new File(lottery.getPath() + File.separator + "shapes.txt");
                    shapes.createNewFile();
                }

                File sportColors = new File(sportColorsConfPath);
                if (sportColors.mkdirs()) {
                    File colors = new File(sportColors.getPath() + File.separator + "colors.txt");
                    colors.createNewFile();
                    File shapes = new File(sportColors.getPath() + File.separator + "shapes.txt");
                    shapes.createNewFile();
                    File music = new File(sportColors.getPath() + File.separator + "music.txt");
                    music.createNewFile();
                    File people = new File(sportColors.getPath() + File.separator + "people.txt");
                    people.createNewFile();
                }

                File sportPics = new File(sportPicsConfPath);
                sportPics.mkdirs();
            }
        }
   }

   public void saveLotteryMap(Map<String, Integer> assocMap) throws IOException {

       try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter( lotteryPath + File.separator + "associations.txt"))) {
           for (Map.Entry<String, Integer> entry : assocMap.entrySet()) {
               bufferedWriter.write(entry.getKey() + ": " + entry.getValue());
               bufferedWriter.newLine();
           }
       }
   }

   public void saveSportColorsAssoc(String question, String playerOne, String playerTwo, Queue<String> pairs) throws IOException {
       try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sportColorsPath + File.separator + "associations.txt"))) {
           bufferedWriter.write(question + System.lineSeparator());
           bufferedWriter.write(playerOne + " - " + pairs.poll() + System.lineSeparator());
           bufferedWriter.write(playerTwo + " - " + pairs.poll());
       }
   }

   public void saveQuestionAndAssoc(String question, String playerOne, String playerTwo, String picOneFile, String picTwoFile) throws IOException {
       try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sportPicsPath + File.separator + "saved session.txt"))) {
           bufferedWriter.write(question);
           bufferedWriter.newLine();
           bufferedWriter.write(playerOne.trim() + ";" + picOneFile);
           bufferedWriter.newLine();
           bufferedWriter.write(playerTwo.trim() + ";" + picTwoFile);
       }
   }

   public void saveCurrentImages(String imageOne, String imageTwo) throws SQLException {
       Statement statement = connection.createStatement();
       statement.execute("INSERT INTO 'recent images' VALUES ('" + imageOne + "', DATE('now'))");
       statement.execute("INSERT INTO 'recent images' VALUES ('" + imageTwo + "', DATE('now'))");
       statement.close();
   }

    public void deleteOldImages() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM 'recent images' WHERE date <= DATE('now', '-6 day')");
        statement.close();
    }

    public Set<String> getRecentImages() throws SQLException {
        Set<String> set = new HashSet<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM 'recent images'");

        while (resultSet.next()) {
            set.add(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();

        return set;
    }

    public List<String> readFile (String string) throws IOException{
        List<String> toReturn = new ArrayList<>();
        String line;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(string))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineArray = line.split(";");
                toReturn.addAll(Arrays.asList(lineArray));
            }
        }
        return toReturn;
    }

    public List<String> getEventDetails() throws IOException {
        String line;
        List<String> eventDetails = new ArrayList<>();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(sportPath + File.separator + "event details.txt"))) {
            while ((line = bufferedReader.readLine()) != null) {
                eventDetails.add(line);
            }
        }

        return eventDetails;
    }

    public void updateEventDetails(Event event) throws IOException{
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sportPath + File.separator + "event details.txt"))) {
            bufferedWriter.write(event.getNameOne());
            bufferedWriter.newLine();
            bufferedWriter.write(event.getNameTwo());
            bufferedWriter.newLine();
            bufferedWriter.write(event.getDate());
            bufferedWriter.newLine();
            bufferedWriter.write(event.getTime());
            bufferedWriter.newLine();
            bufferedWriter.write(event.getCompetition());
            bufferedWriter.newLine();
            bufferedWriter.write(event.getEventID());
        }
    }

    public List<String> readLastSession() throws IOException{
        List<String> savedEventList = new ArrayList<>();

        String line;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sportPicsPath + File.separator + "saved session.txt"))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] temp = line.split(";");
                for (String string : temp) {
                    savedEventList.add(string.trim());
                }
            }
        }

        return savedEventList;
    }

}
