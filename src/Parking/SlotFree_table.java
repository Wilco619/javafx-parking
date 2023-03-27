package Parking;

public class SlotFree_table {

    public Integer id;
    public String slot_id;
    public String slot_Location;

    //free slot table
    public SlotFree_table(Integer id, String slot_id, String slot_Location){
        this.id = id;
        this.slot_id = slot_id;
        this.slot_Location = slot_Location;
    }

    //free slot
    public Integer getId(){
        return id;
    }
    public String getSlot_id(){
        return slot_id;
    }
    public String getSlot_Location(){
        return slot_Location;
    }

}
