
### 영속성 컨텍스트?

영속성 컨텍스트 - 엔티티를 영구 저장하는 환경
JPA의 EntityManager 를 통해 객체가 영속성 컨텍스트에 접근한다

엔티티의 생명주기

비영속 -> 객체와 jpa가 아무런 연관관계가 없는 상태.
ex)
```java
Member member = new Member();
```
영속 -> Jpa 엔티티 매니저 내부의 영속성 컨텍스트에 객체가 접근한 상태
ex)
```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
Member member = new Member();

// 엔티티 매니저(em)의 영속성 컨텍스트에 객체가 접근함.
em.persist(member);

```

준영속 -> 객체가 영속성 컨텍스트에 저장되었다 분리된 상태.
ex)
```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
EntityManager em = emf.createEntityManager();
Member member = new Member();

// 엔티티 매니저(em)의 영속성 컨텍스트에 객체가 접근함.
em.persist(member);

//엔티티 매니저의 영속성 컨텍스트에서 객체가 분리됨.
em.detach(member);
```

삭제


### 영속성 컨텍스트의 이점

**1차 캐시에서 조회**


엔티티 매니저의 find 메서드로 데이터를 조회하면
1차로 캐시에 저장된 값을 가져오므로 db에 접근하지 않고 바로 값을 조회한다.

만약 캐시에 저장된 값이 없으면 db에서 찾아 조회하고  영속성 컨텍스트에 저장하여
같은 데이터를 조회할땐 영속성 컨텍스트에서 조회한다.
중복되는 쿼리를 제거하는 이점이있음.

**동일성 보장**
```java
//영속
//JPA에서 엔티티를 조회하면 무조건 영속성 컨텍스트에 저장한다.
Member findMember1 = em.find(Member.class,101L);
Member findMember2 = em.find(Member.class,101L);
System.out.println("result"+(findMember1==findMember2));
```

**트랜잭션을 지원하는 쓰기 지연**

애플리케이션 단계에서 트랜잭션지원

엔티티 매니저에는 영속성 컨텍스트 이외에
쓰기 지연 SQL 저장소 라는것이 있다.

이를통해 트랜잭션에 의한 commit 이전에 영속성 컨텍스트에 쌓아올린 데이터를
한번에 db에 접근시킨다.

```java

```

**변경감지(dirty checking)**

엔티티매니저의 영속성 컨텍스트 내부에는
최초로 저장된 엔티티의 스냅샷을 가지고있다.

트랜잭션단계에서 조회된 데이터의 스냅샷을 가지고
트랜잭션을 commit하게 되면 flush()가 호출되고 현재 엔티티의 상태와 스냅샷을 비교하여 변경감지를한다.
엔티티 현재 상태와

변경이 감지된 엔티티는 JPA에서 자동으로 쓰기지연 SQL 저장소에 변경이 감지된 엔티티의 update 쿼리를 생성한다.
따라서 자동으로 엔티티의변경을 감지해 업데이트하므로 엔티티 매니저의 persist()를 사용할 필요가 없다.
//flush()가 호출되면 데이터베이스에저장될때

### 플러시
영속성 컨텍스트의 변경 내용을 데이터베이스에 반영

영속성 컨텍스트를 플러시 하는 방법은 3가지가 있다.

em.flush 직접호출(바로 db에 반영)
트랜잭션 커밋- 플러시 자동 호출
jpql 쿼리 실행 - 자동호출

jpql 쿼리 실행시 flush가 자동 호출 되는 이유
```java
Member member1 = new Member(150L,"A");
Member member2 = new Member(160L,"B");

List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();
```
여기까지만 보았을 때 위의 코드에서 jpql 쿼리 실행시 아무것도 조회할수 없다
영속성 컨텍스트에 담긴 객체가 commit이나 트랜잭션 종료로 db에 반영되지 않았지만
jpql에서는 db에 직접 쿼리하여 조회하려 하기 때문이다.

이런 문제를 해결하기 위해 jpql실행시 자동으로 영속성 컨텍스트에 담긴 데이터를 자동 flush 호출을 통해 db에 반영한 다음
jpql 쿼리가 실행된다.

    flush()
    • 영속성 컨텍스트를 비우지 않음
    • 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
    • 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화

엔티티를 준영속 상태로 만드는 법

• em.detach(entity)
특정 엔티티만 준영속 상태로 전환
• em.clear()
영속성 컨텍스트를 완전히 초기화
• em.close()
영속성 컨텍스트를 종료

영속성 컨텍스트에서 객체를 분리하면 준영속 상태가 된다.

특정 엔티티를 영속성 컨텍스트로관리할 필요없을때 분리할수있다.
```java
em.detach(member1);
```