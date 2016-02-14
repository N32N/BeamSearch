package Instance;

/**
 * Created by n on 08/02/16.
 */
public class Job {
    public int number;
    public int product;
    public int type;
    public int quantity;
    public int dueDate;

    public Job(int number, int p, int t, int q, int d) {
        this.number=number;
        this.product = p;
        this.type = t;
        this.quantity = q;
        this.dueDate = d;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDueDate() {
        return dueDate;
    }

    public void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAsString(){
        String s = number+" "+product+" "+type+" "+quantity+" "+dueDate;
        return s;
    }
}
