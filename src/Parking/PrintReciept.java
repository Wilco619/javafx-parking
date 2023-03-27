package Parking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static Parking.models.AdminDbConfig.executeQuery;

public class PrintReciept {


    static void printReciet() throws IOException, SQLException {
        LocalDateTime name = LocalDateTime.now();
        String filename="reciept-"+String.valueOf(name)+".txt";
        File file = new File(filename);
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            ArrayList<String> arr = new ArrayList<>();
            arr= (ArrayList<String>) executeQuery("SELECT * FROM vehicle");
            for(int i=0;i<arr.size();i++){
                String[] arr1=arr.get(i).substring(1,arr.get(i).length()-1).split("'");
//                    System.out.println(arr1[0]);
                for(int j = 0; j< arr1.length; j++){
                    String[] arr2= arr1[j].split(",");
                    System.out.println(arr1[j]);
                    int price =2323;
                    String location = (String) executeQuery("select slot_location from added_slot where slot_Id='"+arr2[1].replaceAll("\\s","")+"';");


                    writer.write("Group 7 Parking System \n" +
                            "----------------------------------- \n" +
                            "Ticket Number 0001 \n" +
                            "----------------------------------------\n" +
                            "Location:\t "+location+"\n" +
                            "Slot booked:\t "+arr2[1]+"\n " +
                            "Vehicle Reg: \t"+ arr2[0]+"\n" +
                            "Vehicle Cat:\t "+arr2[3]+"\n" +
                            "Time in:\t "+ arr2[2]+"\n" +
                            "Time out:\t "+ LocalTime.now()+"\n" +
                            "Price:\t "+price+"\n" +
                            "----------------------------------------------------\n" +
                            "Thank you\n" +
                            "------------------------------------------------\n" +
                            "\n \n \n");



                }


            }
            writer.close();
        }

    }
}
