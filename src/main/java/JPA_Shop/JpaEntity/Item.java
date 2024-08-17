package JPA_Shop.JpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table
public class Item {
    @Id
    @Column(name ="ITEM_ID")
    private Long Id;

    @Column(name="NAME")
    private String name;

    @Column(name="PRICE")
    private int price;

    @Column(name="STOCKQUANTITY")
    private int stockQuantity;


}
