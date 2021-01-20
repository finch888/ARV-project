package Data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class EventDeserializer extends Task<ObservableList<Event>> {

    private EventDeserializer() {}

    private static final EventDeserializer eventDeserializer = new EventDeserializer();

    public static EventDeserializer getInstance() {
        return eventDeserializer;
    }

    @Override
    protected ObservableList<Event> call() {
        String jsonString = APIservice.getInstance().getEvents();

        List<Event> eventList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readValue(jsonString, JsonNode.class);

            JsonNode eventListRaw = jsonNode.get("events");

            for (int i = 0; i < eventListRaw.size(); i++) {

                String id = eventListRaw.get(i).get("id").toString();
                String[] fullDate = (eventListRaw.get(i).get("date").toString()).split("T|\\+");
                String date = fullDate[0].replace("\"", "");
                String time = fullDate[1];
                String competiton = eventListRaw.get(i).get("competition").get("name").toString().replace("\"", "");

                JsonNode outcomesList = eventListRaw.get(i).get("market").get("outcomes");
                if (outcomesList == null) {
                    continue;
                }
                String nameOne = outcomesList.get(0).get("name").toString().replace("\"", "");
                String oddsH = outcomesList.get(0).get("fixedOdds").toString();
                if (oddsH.length()==1) {
                    oddsH += ".00";
                } else if (oddsH.length()==3) {
                    oddsH += "0";
                }
                String nameTwo = outcomesList.get(1).get("name").toString().replace("\"", "");
                String oddsA = outcomesList.get(1).get("fixedOdds").toString();
                if (oddsA.length()==1) {
                    oddsA += ".00";
                } else if (oddsA.length()==3) {
                    oddsA += "0";
                }
                Event event = new Event(nameOne, nameTwo, date, time, competiton, oddsH, oddsA, id);

                eventList.add(event);

                updateProgress(i, eventListRaw.size());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList(eventList);
    }
}
