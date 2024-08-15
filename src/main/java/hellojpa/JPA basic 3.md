###  JPA 엔티티 매핑

jpa는 persistenc 설정을 통해 애플리케이션 실행시
엔티티 매핑 방법을 정할 수있다.

```xml
 <property name="hibernate.hbm2ddl.auto" value="update" />

```
위의 persistence xml 설정 코드에서 value 값에 따라 
애플리케이션이 실행될 때 데이터베이스 스키마 자동생성에 
대한 속성을 정할 수 있다.
    
    create - 기존테이블 삭제 후 다시 생성 (DROP + CREATE)
    create-drop - create와 같으나 종료시점에 테이블 DROP
    update - 변경분만 반영(운영DB에는 사용하면 안됨)
    validate - 엔티티와 테이블이 정상 매핑되었는지만 확인
    none - 사용하지 않음

테스트 또는 로컬 서버에서는 create, create-drop,update 를 사용해도 되지만
스테이징, 운영서버는 validate,none 사용을 원칙으로 해야한다.
운영중인 서버에서 테이블에 대한 ddl 사용은 엄격하게 금지되거나,
신중하게 사용한다. 테이블 변경에 따른 서버나 어플리케이션 장애가 생길 수 있기 때문이다.


JPA 는 DDL 생성 기능으로 DB 밴더에 상관없이
엔티티에 제약조건을 추가할 수있다.
```java
//유니크 제약조건 추가
@Table(UniqueConstraint(name="NAME_AGE_UNIQUE",columnNames={"NAME","AGE"}))

//제약조건추가
@Column(name = "name", nullable = false, length = 10)
private String username;
```

JPA 엔티티 매핑 어노테이션

    @Column 컬럼 매핑
    @Temporal 날짜 타입 매핑
    @Enumerated enum 타입 매핑
    @Lob BLOB, CLOB 매핑
    @Transient 특정 필드를 컬럼에 매핑하지 않음(매핑 무시) jpa랑 관련없는 필드 사용시

기본 키 생성 역할을 DB에 위임하는 IDENTITY 전략을 사용할땐
데이터 베이스에 insert sql 을 실행 한 후에 ID 값을 알 수있다.

따라서 IDENTITY 전략을 사용 할 경우에 JPA 엔티티매니저의 .persist 가 호출되는 시점에 
즉시 db에 insert 쿼리를 보내 DB에서 식별자를 조회한다.

```java
//매핑 방법
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```

데이터베이스 시퀀스는 유일한 값을 순서대로 생성하는 특별한
데이터베이스 오브젝트

```java
//매핑방법
@Entity
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
        initialValue = 1, allocationSize = 1)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR")
    private Long id;
    }
```


**권장하는 식별자 전략**

    • 기본 키 제약 조건: null 아님, 유일, 변하면 안된다.
    • 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대체키)를 사용하자.
    • 예를 들어 주민등록번호도 기본 키로 적절하기 않다. 
    (정보 보호법과 관련해 규정 수정시 해당 기본 키를 참조키로 사용하는 모든 테이블에 영향을 미침)
    • 권장: Long형 + 대체키 + 키 생성전략 사용


### JPA 에서 JAVA ENUM 클래스 사용시 주의점

JPA 에서 ENUM 클래스를 매핑할때 사용하는 방식은 다음과같다
```java
@Enumerated
```
enum 클래스를 매핑할땐 옵션을 설정할 수있는데
ordinal,string 두가지로 분류된다.

여기서 주의해야 할 것은 ordinal 로 설정시
enum 클래스의 필드가 JPA 의 엔티티 매니저의 영속성 컨텍스트를 통해 
DB에 접근할 땐 정수로 저장되는데, ordinal 옵션을 사용하는 enum 클래스는,
필드가 추가되는 경우, 맨 앞에있는 필드가 0부터 차례대로 db에 저장되기 때문에,
**기존에 사용중이던 enum 클래스의 컬럼 값이 의도치 않게 바뀔 수 있다.**

string 옵션의 경우 해당 클래스 필드의 문자값이 그대로 저장되기 때문에 필드 추가시 문제가 되지않는다.
현대에는 데이터베이스 용량과 플랫폼 성능또한 월등 하므로
ordinal 옵션보다 db의 공간을 아주 미세하게 차지할 뿐 성능차이는 거의 없다고 본다.
따라서 ordinal 옵션은 요구사항이 없다면 안정성을 위해 대체로 사용하지않는다.