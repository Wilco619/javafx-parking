package Parking;

public class SlotPick_table {

    public String time_in;
    public String slot_id;
    public String slot_Location;
    public String getSlot_Location;
    public String reg_No;
    public String v_category;

    //picked slot table
    public SlotPick_table(String time_in, String slot_id, String slot_Location, String reg_No, String v_category){
        this.time_in = time_in;
        this.slot_id = slot_id;
        this.slot_Location = slot_Location;
        this.reg_No = reg_No;
        this.v_category = v_category;
    }

    //pick slot
    public String getId(){
        return time_in;
    }
    public String getSlot_id(){
        return slot_id;
    }
    public String getSlot_Location(){
        return slot_Location;
    }
    public String getReg_No(){
        return reg_No;
    }
    public String getVehicle_category(){
        return v_category;
    }

}
