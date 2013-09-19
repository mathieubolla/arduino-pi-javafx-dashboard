package samplejavafx.model.statuscheck;

public class StatusCheck {
    public final String name;
    public final Status status;
    
    public StatusCheck(String name, Status status) {
        this.name = name;
        this.status = status;
    }
    
    public enum Status {
        Ok("#00cc00"), Unknown("#ffa500"), Fail("#cc0000");
        
        private String colorCode;
        
        Status(String colorCode) {
            this.colorCode = colorCode;
        }
        
        public String getColorCode() {
            return colorCode;
        }
    }
}
