package Parking;

import Parking.models.AdminDbConfig;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private Label adminName;
    @FXML
    private Label adminPhone;
    @FXML
    private Label adminId;

    @FXML
    private Button admin_logout;

    @FXML
    private Button settings_btn;
    @FXML
    private AreaChart<?, ?> areaChart;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private TableView<AdminTable> adminTable;

    @FXML
    private TableColumn<AdminTable, String> adminTableAmount;

    @FXML
    private TableColumn<AdminTable, String> adminTableSlot_id;

    @FXML
    private TableColumn<AdminTable, String> adminTableTime_in;

    @FXML
    private TableColumn<AdminTable, String> adminTableTime_out;

    @FXML
    private TableColumn<AdminTable, String> adminTablereg_no;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AdminDbConfig dbc = new AdminDbConfig();
        try {
            adminTable.setItems((ObservableList<AdminTable>) dbc.executeQuery("select * from vehicle"));
            setupTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        admin_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene(event, "Dashboard1.fxml","LogIn!",null);
            }
        });
        settings_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dbHandler.changeScene(event,"settings.fxml","Settings And Reports",null);
            }
        });
        //Charts
        XYChart.Series series = new XYChart.Series();
        XYChart.Series series1 = new XYChart.Series();
        series.setName("Amount");
//        series.getData().add(new XYChart.Data("1",200));
        ArrayList vals = new ArrayList();
        try {
            vals = (ArrayList) dbc.executeQuery("SELECT DATE(date_) as day, AVG(amount) as average FROM payments WHERE date_ >= DATE_SUB(NOW(), INTERVAL 7 DAY)GROUP BY DAY(date_);");

            for (int i = 0; i< vals.size(); i++) {
                System.out.println();
                series.getData().add(new XYChart.Data(String.valueOf(i + 1), vals.get(i)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //series1
        series1.setName("Amount");
        areaChart.getData().addAll(series1);
        barChart.getData().addAll(series);
        ArrayList vals2 = new ArrayList();
        try {
            vals2 = (ArrayList) dbc.executeQuery("SELECT MONTH(date_) as day, AVG(amount) as average FROM payments WHERE YEAR(date_)=2023 GROUP BY MONTH(date_);");
            String[] months ={"JAN","FEB","MAR","APR","MAY","JUNE","JULY","AUG","SEP","OCT","NOV","DEC"};
            for (int i = 0; i< vals2.size(); i++) {
                series1.getData().add(new XYChart.Data(months[i],vals2.get(i)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private void setupTable(){
        adminTableAmount.setCellValueFactory(new PropertyValueFactory<AdminTable,String>("amount"));
        adminTableSlot_id.setCellValueFactory(new PropertyValueFactory<AdminTable,String>("slot_id"));
        adminTableTime_in.setCellValueFactory(new PropertyValueFactory<AdminTable,String>("time_in"));
        adminTableTime_out.setCellValueFactory(new PropertyValueFactory<AdminTable,String>("time_out"));
        adminTablereg_no.setCellValueFactory(new PropertyValueFactory<AdminTable,String>("reg_no"));
    }
    public void setUserName(String usrName) {
        adminName.setText("Name : " + usrName);
    }
    public void setUserPhone(Integer phone) {
        adminPhone.setText("Phone : " + phone);
    }
    public void setUserId(String admin_id) {
        adminId.setText("ID : " + admin_id);
    }

}
