package hello.core.order;

public class Order {

    Long memberId;
    String itemName;
    int itemPrice;
    int discountPrice;


    public Order(Long memberId, String itemName, int itemPrice, int discountPrice) {
        this.memberId = memberId;
        this.itemPrice = itemPrice;
        this.itemName = itemName;
        this.discountPrice = discountPrice;
    }


    int calculatePrice() {
        return itemPrice - discountPrice;
    }


    public Long getMemberId() {
        return memberId;
    }


    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }


    public int getItemPrice() {
        return itemPrice;
    }


    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }


    public String getItemName() {
        return itemName;
    }


    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public int getDiscountPrice() {
        return discountPrice;
    }


    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }


    @Override
    public String toString() {
        return "Order{" + "memberId=" + memberId + ", itemPrice=" + itemPrice + ", itemName='" + itemName + '\''
                + ", discountPrice=" + discountPrice + '}';
    }
}


