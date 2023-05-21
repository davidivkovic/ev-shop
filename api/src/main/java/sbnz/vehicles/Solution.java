package sbnz.vehicles;

public class Solution {

    public Part part;
    public String description;
    public double price;

    public Solution() {}

    public Solution(Part part, String description) {
        this.part = part;
        this.description = description;
        this.price = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }


}
