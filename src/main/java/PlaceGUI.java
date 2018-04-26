import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlaceGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField placeNameText;
    private JTextField elevationText;
    private JButton addButton;
    private JList<Place> placeList;
    private JButton deleteButton;

    private Controller controller;

    private DefaultListModel<Place> allPlacesListModel;

    PlaceGUI(Controller controller){
        this.controller = controller; // Store a reference to the controller object.
        // Need this to make requests to the database

        //Configure the list model
        allPlacesListModel = new DefaultListModel<Place>();
        placeList.setModel(allPlacesListModel);

        // add listeners
        addListeners();

        // Regular setup stuff for the window / JFrame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setVisible(true);
    }

    // Easy method to use to show error message dialog boxes
    private void errorDialog(String message){
        JOptionPane.showMessageDialog(PlaceGUI.this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void showMessage(String message){
        JOptionPane.showMessageDialog(PlaceGUI.this, message);
    }

    private void addListeners() {

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Read data
                // Send message to database via the Controller
                String place = placeNameText.getText();

                // Get the name -- handle errors
                if (place.isEmpty()){
                    // use the errorDialog method we created above
                    errorDialog("Enter a place name.");
                    return;
                }

                // get the elevation -- handle errors
                double elev;

                try {
                    elev = Double.parseDouble(elevationText.getText());
                } catch (NumberFormatException nfe){
                    errorDialog("Enter a number for the elevation.");
                    return;
                }

                // add the Place
                Place placeRecord = new Place(place, elev);
                String result = controller.addPlaceToDatabase(placeRecord);

                if (result.equals(PlaceDB.OK)) {
                    // Clear input JTextFields
                    placeNameText.setText("");
                    elevationText.setText("");

                    // And request all data from DB to update list
                    ArrayList<Place> allData = controller.getAllData();
                    setListData(allData);
                } else {
                    errorDialog(result);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // identify what is selected
                Place place = placeList.getSelectedValue();

                if (place == null){
                    showMessage("Please select a place to delete.");
                } else {
                    // tell controller (who then tells DB) to delete this place
                    controller.deletePlace(place);
                    // update the GUI
                    ArrayList<Place> places = controller.getAllData();
                    setListData(places);
                }
            }
        });
    }

    public void setListData(ArrayList<Place> data) {
        // Display data in allPlacesListModel

        // This app will create a new database
        allPlacesListModel.clear();

        if (data != null){
            for (Place place : data){
                allPlacesListModel.addElement(place);
            }
        }
    }
}
