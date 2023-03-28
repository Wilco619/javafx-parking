package Parking;
import java.io.IOException;
import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    userTablesController utc = new userTablesController();



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
            slotSelect_btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        tryDelete(pick_slot_Field.getText());
//                        tryUnbook();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    pick_slot_Field.setText("");regNo_Field.setText("");
                }
            });
            try {
                showPickTable();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        Timeline timeline2;
        timeline2 = new Timeline(new KeyFrame(Duration.seconds(2),event->{
                showFree_table();
        }));
        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.play();

        logout_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene(event, "Dashboard1.fxml", "Log In!",null);
            }
        });


        unbook_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    tryUnbook(unbookField.getText());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                ticketArea.setText("");
                unbookField.setText("");
            }
        });
        pay_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    dbHandler.selectSlot(event, pick_slot_Field.getText(),regNo_Field.getText(),vCategory_Combo.getValue());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    ticket_Area();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    private void tryUnbook(String slot_id) throws SQLException {
        ArrayList<String> arr = (ArrayList<String>) utc.executeQuery("select * from Picked_slot where slot_Id='"+slot_id+"';");
        insertToTable(arr);
        utc.executeQuery("delete from Picked_slot where slot_Id='"+slot_id+"';");
    }
    Timeline t2 = new Timeline();

    private void insertToTable(ArrayList<String> arr) throws SQLException {
        utc.executeQuery("insert into added_slot(slot_Id,slot_Location)values('"+arr.get(0)+"','"+arr.get(1)+"')");

    }

    public void tryDelete(String slot_id) throws SQLException {
        String time = String.valueOf(LocalTime.now());
        String q2 = (String) utc.executeQuery("select slot_Location from added_slot where slot_Id='"+slot_id+"'");
        utc.executeQuery("insert into Picked_slot(slot_id,slot_Location,reg_No,v_category,time_in) values('"+slot_id+"','"+q2+"','"+regNo_Field.getText()+"','"+vCategory_Combo.getValue()+"','"+time+"');");
        utc.executeQuery("delete from added_slot where id='"+slot_id+"'");
    }
    public void setUserInformation(String usrName){
        usrName_lbl.setText("Welcome " + usrName + "!");
    }

    //pick table
    public void showPickTable() throws SQLException {
        ObservableList<SlotPick_table> list = (ObservableList<SlotPick_table>) getTableList(false);
        c_id.setCellValueFactory(new PropertyValueFactory<SlotPick_table, Integer>("id"));
        c_slot.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("slot_id"));
        c_location.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("slot_Location"));
        c_reg_No.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("reg_No"));
        c_ve_category.setCellValueFactory(new PropertyValueFactory<SlotPick_table, String>("vehicle_category"));
        picked_slot_tbl.setItems(list);
    }


    //picked slot
    public static   Object getTableList(boolean dcd) throws SQLException {
        ArrayList<Object> prime = new ArrayList<>();
        ObservableList<SlotPick_table> tableList = FXCollections.observableArrayList();
        Connection conn = dbHandler.getConnection();

        String query = "select * from Picked_slot;";
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
                LocalTime time_now = LocalTime.now();
                time_in = rs.getString("time_in");
                ArrayList<Float> arr = new ArrayList<Float>();
                float x=0;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                ZoneId zone = ZoneId.of("America/Montreal");
                ZonedDateTime dateTime1 = ZonedDateTime.of(2020, 5, 8, 16, 38, 37, 0, zone);
                ZonedDateTime dateTime2 = ZonedDateTime.of(2020, 4, 8, 20, 18, 10, 0, zone);
//                          float currt = (float) (parseFloat(String.valueOf(arr.get(0))) * 3600 + parseFloat(String.valueOf(arr.get(1))) * 60 + parseFloat(String.valueOf(arr.get(2)))*0.01666666666);
                float billedTime = 909/3600;
                billed+=billedTime;
                Payments.add((float) (90*0.0167));

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
        }finally {
            conn.close();
        }
        if (dcd ==true){
            return vals;
        }else{
            return tableList;
        }
    }boolean f= true;
    public void updatePayment() throws SQLException {
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
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

        f=!f;
    }



    //free table
    public void showFree_table(){
        LocalTime time1 = LocalTime.parse("18:47:30.549250046");
        LocalTime time2 = LocalTime.now();
        java.time.Duration duration = java.time.Duration.between(time1, time2);
        long seconds = duration.getSeconds();
        System.out.println(seconds);
        int price = (int) (seconds*0.167);
        totalAmount.setText(String.valueOf(price));
        totalTime.setText(String.valueOf(duration.getSeconds()/3600)+" Hours");
        System.out.println("------------------------------------------------------------------------");
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
    public void ticket_Area() throws SQLException, IOException {
        String slot = pick_slot_Field.getText();
        String rg_no = regNo_Field.getText();
        String v_category = vCategory_Combo.getValue();
        String location = PrintReciept.printReciet()[0];
        String slot_ = PrintReciept.printReciet()[1];
        String rg_no_ = PrintReciept.printReciet()[2];
        String time_out = PrintReciept.printReciet()[3];
        String time_in = PrintReciept.printReciet()[4];
        String v_category_ = PrintReciept.printReciet()[3];
        String price = PrintReciept.printReciet()[6];
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "           GROUP 7 PARKING SYSTEM              \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "                 TICKET : 001                  \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "LOCATION : " +location+ " \n");
        ticketArea.setText(ticketArea.getText() + "\t" + "SLOT BOOKED : " + "\t" +slot_+ "\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "VEHICLE REG_NO : " + "\t" +rg_no_+ "\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "VEHICLE CATEGORY : " + "\t" +v_category_+ "\n");
        ticketArea.setText(ticketArea.getText() + "\t" + "TIME IN : "+time_in+ " \n");
        ticketArea.setText(ticketArea.getText() + "\t" + "TIME OUT : " + LocalTime.now()+" \n");
        ticketArea.setText(ticketArea.getText() + "\t" + "PRICE : " +price + " \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
        ticketArea.setText(ticketArea.getText() + "                   THANK YOU                   \n");
        ticketArea.setText(ticketArea.getText() + "- - - - - - - - - - - - - - - - - - - - - - - - - -\n");
    }

//    public void display_min1(String minOne){
//        min_1.setText(minOne);
//    }
}

