package Parking;

public class AdminTable {
    private String reg_no;
    private String slot_id;
    private String time_in;
    private String time_out;
    private String amount;


    public AdminTable(String reg_no, String slot_id, String time_in,String time_out, String amount) {
        this.reg_no = reg_no;
        this.slot_id = slot_id;
        this.time_in = time_in;
        this.time_out = time_out;
        this.amount = amount;
    }


    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }



    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }
}
