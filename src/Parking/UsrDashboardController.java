package Parking;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import static java.lang.Float.parseFloat;

public class UsrDashboardController implements Initializable {
    public static int timeline2Time =1;
    public static String time_in;
    public static float discount=20;


    @FXML
    private Label welcome_lbl;
    @FXML
    private Button print_btn;
    @FXML
    private Label usrName_lbl;

    @FXML
    private Button logout_btn;
    @FXML
    private TextArea ticketArea;
    @FXML
    private Button unbook_btn;
    @FXML
    private Button pay_btn;
    @FXML
    private Button slotSelect_btn;
    //
    @FXML
    private Label day_1;

    @FXML
    private Label day_2;

    @FXML
    private Label day_3;

    @FXML
    private Label hour_1;

    @FXML
    private Label hour_2;

    @FXML
    private Label hour_3;

    @FXML
    private Label min_1;

    @FXML
    private Label min_2;

    @FXML
    private  Button payAmount_;
    @FXML
    private  Label totalAmount;
    @FXML
    private  Label totalTime;

    @FXML
    private TextField pick_slot_Field;
    @FXML
    private TextField location_search;
    @FXML
    private TextField regNo_Field;
    @FXML
    private TextField unbookField;

    @FXML
    private ComboBox<String> vCategory_Combo;

    @FXML
    private TableView <SlotPick_table> picked_slot_tbl;
    @FXML
    private TableColumn <SlotPick_table,Integer> c_id;
    @FXML
    private TableColumn <SlotPick_table, String> c_slot;
    @FXML
    private TableColumn <SlotPick_table, String> c_location;
    @FXML
    private TableColumn <SlotPick_table, String> c_reg_No;
    @FXML
    private TableColumn <SlotPick_table, String> c_ve_category;

    //free slots
    @FXML
    private TableView <SlotFree_table> free_slots;
    @FXML
    private TableColumn <SlotFree_table,Integer> c_fid;
    @FXML
    private TableColumn <SlotFree_table,String> c_fslot;
    @FXML
    private TableColumn <SlotFree_table, String> c_flocation;
//    private Object totalAmount_;





    public void Print(ActionEvent actionEvent) throws SQLException, IOException {
        new PrintReciept().printReciet();
    }
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle){
        vCategory_Combo.getItems().add("Small");
        vCategory_Combo.getItems().add("Standard");
        vCategory_Combo.getItems().add("Large");

        showFree_table();
        Timeline timeline;
        timeline = new Timeline(new KeyFrame(Duration.seconds(2),event->{
            System.out.println(34567);
            updatePayment();
            slotSelect_btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dbHandler.selectSlot(event, pick_slot_Field.getText(),regNo_Field.getText(),vCategory_Combo.getValue());

                    pick_slot_Field.setText("");regNo_Field.setText("");

                }
            });
            showPickTable();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        logout_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene(event, "Dashboard1.fxml", "Log In!",null);
                System.out.println(vCategory_Combo.getValue());

            }
        });


        unbook_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.deleteSlot(event, unbookField.getText());
                ticketArea.setText("");
                unbookField.setText("");
            }
        });


        //pay btn
        pay_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.selectSlot(event, pick_slot_Field.getText(),regNo_Field.getText(),vCategory_Combo.getValue());
                ticket_Area();
            }
        });

    }

    public void setUserInformation(String usrName){
        usrName_lbl.setText("Welcome " + usrName + "!");
    }

    //pick table
    public void showPickTable(){
        ObservableList<SlotPick_table> list = (ObservableList<SlotPick_table>) getTableList(false);
        c_id.setCellValueFactory(new PropertyValueFactory<SlotPick_table, Integer>("id"));
        c_slot.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("slot_id"));
        c_location.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("slot_Location"));
        c_reg_No.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("reg_No"));
        c_ve_category.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("vehicle_category"));
        picked_slot_tbl.setItems(list);
    }


    //picked slot
    public static   Object getTableList(boolean dcd) {
        ArrayList<Object> prime = new ArrayList<>();
        ObservableList<SlotPick_table> tableList = FXCollections.observableArrayList();
        Connection conn = dbHandler.getConnection();
        String query = " SELECT added_slot.id, added_slot.slot_id, added_slot.slot_Location, vehicle.reg_No, vehicle.v_category, vehicle.time_in FROM added_slot INNER JOIN vehicle ON added_slot.slot_id = vehicle.slot_id";
        Statement st;
        ResultSet rs;
        float payment = 0;
        ArrayList<Float> vals=new ArrayList<Float>();
        float billed =0;


        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            SlotPick_table slotPickTable;
            ArrayList<Float> Payments = new ArrayList<>();
            UsrDashboardController uc = new UsrDashboardController();

            while (rs.next()) {
                LocalTime myObj = LocalTime.now();
                String[] currenttime = String.valueOf(myObj).split(":", 3);
                time_in = rs.getString("time_in");
                ArrayList<Float> arr = new ArrayList<Float>();
                String[] dbtime = time_in.split(":", 3);
                for (int i = 0; i < dbtime.length; i++) {
                    float y = parseFloat(currenttime[i]) - parseFloat(dbtime[i]);
                    arr.add(y);
                }
//                System.out.println(arr.get(0)+"==============,"+ arr.get(1));
                float currt = (float) (parseFloat(String.valueOf(arr.get(0))) * 3600 + parseFloat(String.valueOf(arr.get(1))) * 60 + parseFloat(String.valueOf(arr.get(2)))*0.01666666666);
                float billedTime = currt/3600;
                billed+=billedTime;
                Payments.add((float) (currt*0.0167));

                slotPickTable = new SlotPick_table(time_in, rs.getString("slot_id"), rs.getString("slot_Location"), rs.getString("reg_No"), rs.getString("v_category"));
                tableList.add(slotPickTable);

            }
//            vals = new float[]{payment, billed};

            payment = 0;
            for(int i = 0; i < Payments.size(); i++) {
                payment += Payments.get(i);
            }
            vals.add(payment);
            vals.add(billed);
            prime.add(tableList);
            prime.add(payment);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if (dcd ==true){
            System.out.println(billed+"-------");
            return vals;
        }else{
            return tableList;
        }
    }boolean f= true;
    public void updatePayment(){
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        System.out.println(getTableList(true).toString());
        float num = Float.parseFloat(getTableList(true).toString().substring(1,5));
        int startIndex=12;
        if(f==true){
            startIndex =12;
        }else {
            startIndex=11;
        }
        String time =getTableList(true).toString().substring(startIndex,16);
        payAmount_.setText(String.valueOf(df.format(num-discount)));
        totalAmount.setText(String.valueOf(df.format(num)));
        totalTime.setText(time);

        System.out.println(!f);
        f=!f;
    }



    //free table
    public void showFree_table(){
//        test();
        ObservableList<SlotFree_table> list = getFreeTableList();
        c_fslot.setCellValueFactory(new PropertyValueFactory<SlotFree_table, String>("slot_id"));
        c_flocation.setCellValueFactory(new PropertyValueFactory<SlotFree_table, String>("slot_Location"));
        free_slots.setItems(list);

        //initial filteredList
        FilteredList<SlotFree_table> filteredData = new FilteredList<>(list, b -> true);

        location_search.textProperty().addListener((observable, oldValue, newValue) -> {


            filteredData.setPredicate(slotFree_table -> {
                timeline2Time=23;



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
    private ObservableList<SlotFree_table> getFreeTableList() {
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
        }
        return freetableList;

    }






    //ticket
    public void ticket_Area(){
        String slot = pick_slot_Field.getText();
        String rg_no = regNo_Field.getText();
        String v_category = vCategory_Combo.getValue();
//        Integer price = price.getText();
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "           GROUP 7 PARKING SYSTEM              \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "                 TICKET : 001                  \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "LOCATION : " + " \n");
        ticketArea.setText(ticketArea.getText() + "\t" + "STREET : " + " \n");
        ticketArea.setText(ticketArea.getText() + "\t" + "SLOT BOOKED : " + "\t" +slot+ "\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "VEHICLE REG_NO : " + "\t" +rg_no+ "\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "VEHICLE CATEGORY : " + "\t" +v_category+ "\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "TIME IN : " + " \n");
        ticketArea.setText(ticketArea.getText() + "\t" + "TIME OUT : " + " \n");
        ticketArea.setText(ticketArea.getText() + "\t" + "PRICE : " + " \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "                   THANK YOU                   \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
    }

//    public void display_min1(String minOne){
//        min_1.setText(minOne);
//    }
}

