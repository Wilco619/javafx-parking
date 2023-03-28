package Parking;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private TextField add_slot;
    @FXML
    private TextField add_location;
    @FXML
    private TextField rm_slot;
    @FXML
    private TextField rl_slot;
    @FXML
    private Button rl_slot_btn;
    @FXML
    private Button rm_slot_btn;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField usrNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField pwdField;
    @FXML
    private TextField pwdRepeatField;
    @FXML
    private Button add_slot_btn;
    @FXML
    private Button register_btn;
    @FXML
    private Button exit_btn;
    @FXML
    private Button cn_slot_btn;
    //
    @FXML
    private TextField day1;

    @FXML
    private TextField day2;

    @FXML
    private TextField day3;

    @FXML
    private TextField hour1;

    @FXML
    private TextField hour2;

    @FXML
    private TextField hour3;

    @FXML
    private TextField min1;

    @FXML
    private TextField min2;

    @FXML
    private TextField min3;
    @FXML
    private TextField location_search;
    @FXML
    private Button rate_submit;

    @FXML
    private TableView<SlotFree_table> free_slots;
    @FXML
    private TableColumn<SlotFree_table,Integer> c_fid;
    @FXML
    private TableColumn <SlotFree_table,String> c_fslot;
    @FXML
    private TableColumn <SlotFree_table, String> c_flocation;
    //

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            showFree_table();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //timeline
        Timeline timeline;
        timeline = new Timeline(new KeyFrame(Duration.seconds(2),event->{}));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        add_slot_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    dbHandler.addSlot(event, add_slot.getText(),add_location.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                add_slot.setText("");add_location.setText("");
            }
        });

        exit_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene(event,"Admin.fxml","Admin!",null);
            }
        });

        register_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    dbHandler.adminSignup(event,idField.getText(), nameField.getText() , usrNameField.getText(), emailField.getText(), Integer.valueOf(phoneField.getText()), pwdField.getText(), pwdRepeatField.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                idField.setText("");nameField.setText("");usrNameField.setText("");emailField.setText("");phoneField.setText("");pwdField.setText("");pwdRepeatField.setText("");
            }
        });

        cn_slot_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                add_slot.setText("");add_location.setText("");
            }
        });
        cn_slot_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rm_slot.setText("");
            }
        });

        rm_slot_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.deleteFreeSlot(event, rm_slot.getText());
                rm_slot.setText("");
            }
        });
        rl_slot_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    dbHandler.deleteSlot(event, rl_slot.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                rl_slot.setText("");
            }
        });

    }
//    String minOne = min1.getText();
//    UsrDashboardController.display_min1(minOne);

    //free table
    public void showFree_table() throws SQLException {
//        test();
        ObservableList<SlotFree_table> list = getFreeTableList();
        c_fslot.setCellValueFactory(new PropertyValueFactory<SlotFree_table, String>("slot_id"));
        c_flocation.setCellValueFactory(new PropertyValueFactory<SlotFree_table, String>("slot_Location"));
        free_slots.setItems(list);

        //initial filteredList
        FilteredList<SlotFree_table> filteredData = new FilteredList<>(list, b -> true);

        location_search.textProperty().addListener((observable, oldValue, newValue) -> {


            filteredData.setPredicate(slotFree_table -> {



                //if no search value then display all records or whatever records
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){

                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (slotFree_table.getSlot_id().toString().indexOf(searchKeyword) > -1){
                    return true; //means we found a match in parking slot id
                } else if (slotFree_table.getSlot_Location().toLowerCase().indexOf(searchKeyword) > -1) {
                    return true;
                }else
                    return false; // no match found

            });
        });

        SortedList<SlotFree_table> sortedData = new SortedList<>(filteredData);

        //bind sorted result with table view
        sortedData.comparatorProperty().bind(free_slots.comparatorProperty());

        //apply filtered and sorted data to the table view
        free_slots.setItems(sortedData);
    }


    //free slot
    private ObservableList<SlotFree_table> getFreeTableList() throws SQLException {
        ObservableList<SlotFree_table> freetableList = FXCollections.observableArrayList();
        Connection conn = dbHandler.getConnection();
        String query = "SELECT * FROM added_slot";
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            SlotFree_table slotFree_table;
            while(rs.next()){
                slotFree_table = new SlotFree_table(rs.getInt("id"),rs.getString("slot_id"),rs.getString("slot_Location"));
                freetableList.add(slotFree_table);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }finally {
            conn.close();
        }
        return freetableList;

    }
}
