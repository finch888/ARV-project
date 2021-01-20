import Data.DataModel;
import Data.Event;
import Data.EventDeserializer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Controller implements Initializable {

    // Lottery
    @FXML
    private ComboBox<String> lotteryTypeCB;
    @FXML
    private ComboBox<String> visualOptCB;
    @FXML
    private TextField confirmText;

    // Sport event
    @FXML
    private TextField playerOneName;
    @FXML
    private TextField playerTwoName;
    @FXML
    private TextField eventName;
    @FXML
    private TextField date;
    @FXML
    private TextField time;
    @FXML
    private TextArea questionText;
    @FXML
    private ComboBox<String> assocCBsport;
    @FXML
    private CheckBox IDcb;
    @FXML
    private Button saveTargetManBtn;

    // Sport event with pictures
    @FXML
    private TextArea textAreaForPics;
    @FXML
    private Button showImageOne;
    @FXML
    private Button showImageTwo;
    @FXML
    private CheckBox IDcheckBox;

    // Choose target pane
    @FXML
    TableView<Event> eventsTableView;
    @FXML
    TableColumn<Event, String> dateCol;
    @FXML
    TableColumn<Event, String> timeCol;
    @FXML
    TableColumn<Event, String> competitionCol;
    @FXML
    TableColumn<Event, String> oddsHCol;
    @FXML
    TableColumn<Event, String> oddsACol;
    @FXML
    ProgressBar progressBar;
    @FXML
    Button downloadEventsBtn;
    @FXML
    Button saveTargetBtn;
    @FXML
    TextField saveTargetLabel;
    @FXML
    Label timeLabel;


    // Lottery section
    ObservableList<String> lottoTypes = FXCollections.observableArrayList("Ötöslottó", "Hatoslottó", "Skandináv");
    ObservableList<String> visualisationOptions = FXCollections.observableArrayList("Shapes and colors", "Colors only");

    @FXML
    private void generateLotteryAction() {
        confirmText.clear();

        String lotteryChoice = lotteryTypeCB.getValue();
        String visualisationChoice = visualOptCB.getValue();

        List<String> colorsList;
        List<String> shapeList;

        try {
           colorsList = DataModel.getInstance().readFile(DataModel.getInstance().getLotteryConfPath() + File.separator + "colors.txt");
           shapeList = DataModel.getInstance().readFile(DataModel.getInstance().getLotteryConfPath() + File.separator + "shapes.txt");
        } catch (IOException e) {
            confirmText.setText("Unable to read file containing target substitutes.");
            return;
        }

        List<String> combosList = new ArrayList<>();

        if (visualisationChoice.compareTo("Shapes and colors") == 0) {
            for (String color : colorsList) {
                for (String shape : shapeList) {
                    combosList.add(color + " " + shape);
                }
            }
        } else {
            for (int i = 0; i < colorsList.size()-1; i++) {
                for (int j = i+1; j < colorsList.size(); j++) {
                    combosList.add(colorsList.get(i) + " + " + colorsList.get(j));
                }
            }
        }

        Map<String, Integer> assocMap = new LinkedHashMap<>();
        int maxNum = 0;
        int currentNum = 1;

        switch (lotteryChoice) {
            case "Ötöslottó" :
                maxNum = 90;
                break;
            case "Hatoslottó" :
                maxNum = 45;
                break;
            case "Skandináv" :
                maxNum = 35;
                break;
        }

        while (currentNum < maxNum+1) {
            int randIndex = ThreadLocalRandom.current().nextInt(0, combosList.size());
            assocMap.put(combosList.get(randIndex), currentNum++);
            combosList.remove(randIndex);
        }

        try {
            DataModel.getInstance().saveLotteryMap(assocMap);
            confirmText.setText("File saved successfully.");
        } catch (IOException e) {
            confirmText.setText("Error saving file.");
        }
    }

    // Sport Section Colors
    ObservableList<String> assocListSport = FXCollections.observableArrayList("Single color", "Color pair", "Shapes and colors", "People", "Music");

    private int checkFields() {
        List<String> fieldList = new ArrayList<>();
        fieldList.add(eventName.getText());
        fieldList.add(playerOneName.getText());
        fieldList.add(playerTwoName.getText());
        fieldList.add(date.getText());
        fieldList.add(time.getText());

        int count = 0;

        for (String string : fieldList) {
            if(string.length()!=0) {
                count++;
            }
        }
        return count;
    }

    @FXML
    private void generateButtonAction() {
        int fieldsCheck = checkFields();
        List<String> eventDetails = new ArrayList<>();

        String question;
        String playerOne;
        String playerTwo;

        if (fieldsCheck == 0) {
            try {
                eventDetails = DataModel.getInstance().getEventDetails();
            } catch (IOException e) {
                questionText.setText("All fields are empty and no saved target found.");
                return;
            }
            playerOne = eventDetails.get(0);
            playerTwo = eventDetails.get(1);
            question = Event.generateQuestion(playerOne, playerTwo, eventDetails.get(2), eventDetails.get(3), eventDetails.get(4));

        } else if (fieldsCheck == 5) {
            String eventString = eventName.getText();
            playerOne = playerOneName.getText();
            playerTwo = playerTwoName.getText();
            String dateString = date.getText();
            String timeString = time.getText();
            question = Event.generateQuestion(playerOne, playerTwo, dateString, timeString, eventString);
        } else {
            questionText.setText("One or more fields are empty.");
            return;
        }

        String assocSelection = assocCBsport.getValue();

        Queue<String> pairs = new LinkedList<>();

        try {
            switch (assocSelection) {
                case "Single color": {
                    List<String> colorsList = DataModel.getInstance().readFile(DataModel.getInstance().getSportColorsConfPath() + File.separator + "colors.txt");
                    while (pairs.size() < 2) {
                        int index = ThreadLocalRandom.current().nextInt(0, colorsList.size());
                        pairs.add(colorsList.get(index));
                        colorsList.remove(index);
                    }
                }
                case "Color pair": {
                    List<String> colorsList = DataModel.getInstance().readFile(DataModel.getInstance().getSportColorsConfPath() + File.separator + "colors.txt");
                    while (pairs.size() < 2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        int indexOne = ThreadLocalRandom.current().nextInt(0, colorsList.size());
                        stringBuilder.append(colorsList.get(indexOne)).append(" + ");
                        colorsList.remove(indexOne);
                        int indexTwo = ThreadLocalRandom.current().nextInt(0, colorsList.size());
                        stringBuilder.append(colorsList.get(indexTwo));
                        colorsList.remove(indexTwo);
                        pairs.add(stringBuilder.toString());
                    }
                }
                break;
                case "Shapes and colors": {
                    List<String> colorsList = DataModel.getInstance().readFile(DataModel.getInstance().getSportColorsConfPath() + File.separator + "colors.txt");
                    List<String> shapeList = DataModel.getInstance().readFile(DataModel.getInstance().getSportColorsConfPath() + File.separator + "shapes.txt");
                    while (pairs.size() < 2) {
                        StringBuilder stringBuilder = new StringBuilder();
                        int indexOne = ThreadLocalRandom.current().nextInt(0, colorsList.size());
                        stringBuilder.append(colorsList.get(indexOne)).append(" + ");
                        colorsList.remove(indexOne);
                        int indexTwo = ThreadLocalRandom.current().nextInt(0, shapeList.size());
                        stringBuilder.append(shapeList.get(indexTwo));
                        shapeList.remove(indexTwo);
                        pairs.add(stringBuilder.toString());
                    }
                }
                break;
                case "Music": {
                    List<String> music = DataModel.getInstance().readFile(DataModel.getInstance().getSportColorsConfPath() + File.separator + "music.txt");
                    for (int i = 0; i < 2; i++) {
                        int randIndex = ThreadLocalRandom.current().nextInt(0, music.size());
                        pairs.add(music.get(randIndex));
                        music.remove(randIndex);
                    }
                }
                break;
                case "People": {
                    List<String> people = DataModel.getInstance().readFile(DataModel.getInstance().getSportColorsConfPath() + File.separator + "people.txt");
                    for (int i = 0; i < 2; i++) {
                        int randIndex = ThreadLocalRandom.current().nextInt(0, people.size());
                        pairs.add(people.get(randIndex));
                        people.remove(randIndex);
                    }
                }
                break;
            }
        } catch (IOException e) {
            questionText.setText("Unable to read file containing target substitutes.");
            return;
        }

        try {
            DataModel.getInstance().saveSportColorsAssoc(question, playerOne, playerTwo, pairs);
            questionText.setText(IDcb.isSelected() ? Event.questionWithID(eventDetails.get(3)) : question);
        } catch (IOException e) {
            questionText.setText("Unable to save associations.");
        }
    }

    private boolean checkFieldLength() {
        int competition = eventName.getText().length();
        int playerOne = playerOneName.getText().length();
        int playerTwo = playerTwoName.getText().length();
        int dateInt = date.getText().length();
        int timeInt = time.getText().length();

        return competition > 0 && playerOne > 0 && playerTwo > 0 && dateInt > 0 && timeInt > 0;
    }

    @FXML
    private void saveTargetManual() {
      Event event = new Event(playerOneName.getText(), playerTwoName.getText(), date.getText(), time.getText(), eventName.getText());

      try {
          DataModel.getInstance().updateEventDetails(event);
          questionText.setText("Event saved successfully.");
      } catch (IOException e) {
          questionText.setText("Unable to save event.");
      }
    }

    // Sport section with pictures
    @FXML
    private void generateButtonPicsOnAction() {
        List<String> eventDetails;
        try {
            eventDetails = DataModel.getInstance().getEventDetails();
        } catch (IOException e) {
            textAreaForPics.setText("No saved target found.");
            return;
        }

        Set<String> recentImagesSet = new HashSet<>();
        try {
            DataModel.getInstance().deleteOldImages();
            recentImagesSet = DataModel.getInstance().getRecentImages();
        } catch (SQLException e) {
            textAreaForPics.setText("Unable to update database.");
        }

        int originalSetSize = recentImagesSet.size();

        String question = Event.generateQuestion(eventDetails.get(0), eventDetails.get(1), eventDetails.get(2), eventDetails.get(3), eventDetails.get(4));

        textAreaForPics.setText(IDcheckBox.isSelected() ? Event.questionWithID(eventDetails.get(3)) : question);

        File folder = new File(DataModel.getInstance().getSportPicsConfPath());
        String[] fileNames = folder.list();
        if (fileNames == null) {
            textAreaForPics.setText("No pictures found.");
            return;
        }

        int indexOne = -1;
        int indexTwo = -1;

        while (recentImagesSet.size() != originalSetSize+2) {
            int random = ThreadLocalRandom.current().nextInt(0, fileNames.length);
            recentImagesSet.add(fileNames[random]);
            if (recentImagesSet.size() > originalSetSize) {
                if (indexOne == -1) {
                    indexOne = random;
                } else {
                    indexTwo = random;
                }
            }
        }

        try {
            DataModel.getInstance().saveCurrentImages(fileNames[indexOne], fileNames[indexTwo]);
        } catch (SQLException sqlException) {
            textAreaForPics.setText("Unable to update database.");
        }

        String picOneFile = DataModel.getInstance().getSportPicsConfPath() + File.separator + fileNames[indexOne];
        String picTwoFile = DataModel.getInstance().getSportPicsConfPath() + File.separator + fileNames[indexTwo];

        boolean flip = ThreadLocalRandom.current().nextBoolean();

        String playerOne = flip ? eventDetails.get(1) : eventDetails.get(0);
        String playerTwo = flip ? eventDetails.get(0) : eventDetails.get(1);

        try {
            DataModel.getInstance().saveQuestionAndAssoc(question, playerOne, playerTwo, picOneFile, picTwoFile);
        } catch (IOException e) {
            textAreaForPics.setText("Unable to save associations.");
        }

        showImageOne.setDisable(false);
        showImageTwo.setDisable(false);

        showImageOne.setOnAction(event -> {
            try {
                ModalWindow.display(picOneFile, playerOne);
            } catch (FileNotFoundException fileNotFoundException) {
                textAreaForPics.setText("No pictures found.");
            }
        });

        showImageTwo.setOnAction(event -> {
            try {
                ModalWindow.display(picTwoFile, playerTwo);
            } catch (FileNotFoundException fileNotFoundException) {
                textAreaForPics.setText("No pictures found.");
            }
        });
    }

    @FXML
    private void getLastSession() {
        List<String> savedEventList;

        try {
            savedEventList = DataModel.getInstance().readLastSession();
        } catch (IOException e) {
            textAreaForPics.setText("Unable to retrieve last session.");
            return;
        }

        textAreaForPics.setText(savedEventList.get(0));

        showImageOne.setDisable(false);
        showImageTwo.setDisable(false);

        String nameOne = savedEventList.get(1);
        String nameTwo = savedEventList.get(3);
        String imageOne = savedEventList.get(2);
        String imageTwo = savedEventList.get(4);

        showImageOne.setOnAction(event -> {
            try {
                ModalWindow.display(imageOne, nameOne);
            } catch (FileNotFoundException fileNotFoundException) {
                textAreaForPics.setText("Unable to load image.");
            }
        });

        showImageTwo.setOnAction(event -> {
            try {
                ModalWindow.display(imageTwo, nameTwo);
            } catch (FileNotFoundException fileNotFoundException) {
                textAreaForPics.setText("Unable to load image.");
            }
        });
    }

    // Choose event section
    @FXML
    private void getEvents() {
        eventsTableView.itemsProperty().bind(EventDeserializer.getInstance().valueProperty());

        progressBar.progressProperty().bind(EventDeserializer.getInstance().progressProperty());
        progressBar.setVisible(true);
        EventDeserializer.getInstance().setOnFailed(event -> {progressBar.setVisible(false);
            saveTargetLabel.setText("Events download failed.");});
        EventDeserializer.getInstance().setOnSucceeded(event -> {progressBar.setVisible(false); downloadEventsBtn.setDisable(true);});

        new Thread(EventDeserializer.getInstance()).start();
    }

    @FXML
    private void saveTarget() {
        Event target = eventsTableView.getSelectionModel().getSelectedItem();

        try {
            DataModel.getInstance().updateEventDetails(target);
            saveTargetLabel.setText("Event saved successfully.");
        } catch (IOException e) {
            saveTargetLabel.setText("Error saving event.");
            return;
        }

        IDcheckBox.setDisable(false);
        IDcb.setDisable(false);
    }

    private void initClock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss  yyyy-MM-dd");
            timeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> eventName.requestFocus());

        lotteryTypeCB.setValue("Ötöslottó");
        lotteryTypeCB.setItems(lottoTypes);

        visualOptCB.setValue("Shapes and colors");
        visualOptCB.setItems(visualisationOptions);

        assocCBsport.setValue("Single color");
        assocCBsport.setItems(assocListSport);

        eventName.lengthProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                if (checkFieldLength()) {
                    saveTargetManBtn.setDisable(false);
                }
            } else {
                saveTargetManBtn.setDisable(true);
            }
        }));

        playerOneName.lengthProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                if (checkFieldLength()) {
                    saveTargetManBtn.setDisable(false);
                }
            } else {
                saveTargetManBtn.setDisable(true);
            }
        }));

        playerTwoName.lengthProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                if (checkFieldLength()) {
                    saveTargetManBtn.setDisable(false);
                }
            } else {
                saveTargetManBtn.setDisable(true);
            }
        }));

        date.lengthProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                if (checkFieldLength()) {
                    saveTargetManBtn.setDisable(false);
                }
            } else {
                saveTargetManBtn.setDisable(true);
            }
        }));

        time.lengthProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                if (checkFieldLength()) {
                    saveTargetManBtn.setDisable(false);
                }
            } else {
                saveTargetManBtn.setDisable(true);
            }
        }));

        showImageOne.setDisable(true);
        showImageTwo.setDisable(true);

        saveTargetBtn.disableProperty().bind(eventsTableView.getSelectionModel().selectedItemProperty().isNull());

        initClock();
    }
}
