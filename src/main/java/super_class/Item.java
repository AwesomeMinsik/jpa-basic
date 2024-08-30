package super_class;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
// 상속 전략 설정: 부모와 자식 엔티티를 각각의 테이블로 분리하여 저장하며, 부모 테이블의 기본 키를 자식 테이블의 외래 키로 사용
@DiscriminatorColumn //부모 테이블에 DTYPE 컬럼을 자동으로 생성하여 상속된 자식 엔티티의 타입 정보를 저장함
public abstract class Item {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
