package model;

public class SearchMinMaxItem {
    private String itemCode;
    private int sumOfQty;

    public SearchMinMaxItem() {
    }

    public SearchMinMaxItem(String itemCode) {
        this.itemCode = itemCode;
    }

    public SearchMinMaxItem(String itemCode, int sumOfQty) {
        this.itemCode = itemCode;
        this.sumOfQty = sumOfQty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getSumOfQty() {
        return sumOfQty;
    }

    public void setSumOfQty(int sumOfQty) {
        this.sumOfQty = sumOfQty;
    }
}
