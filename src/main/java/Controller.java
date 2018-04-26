import java.util.ArrayList;

public class Controller {

    // Create PlaceGUI and PlaceDB objects -- be able to send messages between them
    private PlaceGUI gui;
    private PlaceDB db;

    public static void main(String[] args) {
        new Controller().startApp();
    }

    private void startApp() {
        db = new PlaceDB();

        ArrayList<Place> allData = db.fetchAllRecords();

        gui = new PlaceGUI(this);// send a reference to itself when creating PlaceGUI
        gui.setListData(allData); // set GUI data with data from the DB
    }

    ArrayList<Place> getAllData() {
        return db.fetchAllRecords();
    }

    public String addPlaceToDatabase(Place placeRecord) {
        return db.addRecord(placeRecord);
    }

    public void deletePlace(Place place){
        db.delete(place);
    }
}
