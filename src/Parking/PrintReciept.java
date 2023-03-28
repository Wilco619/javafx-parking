package Parking;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static Parking.models.AdminDbConfig.executeQuery;

public class PrintReciept {

    static String location;

    static String[] printReciet() throws IOException, SQLException {
        LocalDateTime name = LocalDateTime.now();
        String filename = "reciept-" + String.valueOf(name) + ".txt";
        File file = new File(filename);
        String[] arr2 = new String[0];
        int price = 0;
        if (file.createNewFile()) {
            FileWriter writer = new FileWriter(file);
            ArrayList<String> arr = new ArrayList<>();
            arr = (ArrayList<String>) executeQuery("SELECT * FROM Picked_slot;");
            for (int i = 0; i < arr.size(); i++) {
                String[] arr1 = arr.get(i).substring(1, arr.get(i).length() - 1).split("'");

                for (int j = 0; j < arr1.length; j++) {
                    arr2 = arr1[j].split(",");
                    LocalTime time1 = LocalTime.parse("18:47:30.549250046");
                    LocalTime time2 = LocalTime.now();
                    Duration duration = Duration.between(time1, time2);
                    long seconds = duration.getSeconds();
                    System.out.println(seconds);

                    price = (int) (seconds*0.167);
                    location = (String) executeQuery("select slot_location from Picked_slot where slot_Id='" + arr2[1].replaceAll("\\s", "") + "';");
                    System.out.println("--->" + arr2[1].replaceAll("\\s", ""));
                    writer.write("Group 7 Parking System \n" +
                            "----------------------------------- \n" +
                            "Ticket Number 0001 \n" +
                            "----------------------------------------\n" +
                            "Location:\t " + location + "\n" +
                            "Slot booked:\t " + arr2[1] + "\n " +
                            "Vehicle Reg: \t" + arr2[0] + "\n" +
                            "Vehicle Cat:\t " + arr2[3] + "\n" +
                            "Time in:\t " + arr2[2] + "\n" +
                            "Time out:\t " + LocalTime.now() + "\n" +
                            "Price:\t " + price + "\n" +
                            "----------------------------------------------------\n" +
                            "Thank you\n" +
                            "------------------------------------------------\n" +
                            "\n \n \n");
                }
            }
            writer.close();
        }
        System.out.println(location);
        return new String[]{location, arr2[1], arr2[0], arr2[3], arr2[2], String.valueOf(LocalTime.now()), String.valueOf(price)};

    }
}
