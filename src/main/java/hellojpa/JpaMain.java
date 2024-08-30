package hellojpa;

import jakarta.persistence.*;
import super_class.Movie;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code
        EntityTransaction tx = em.getTransaction();

        List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();
        //jpql 객체를 다루는 쿼리라 * 안써지는거같음
        Member findMember1= em.find(Member.class,150L);
        Member findMember2= em.find(Member.class,150L);

        System.out.println("result="+(findMember1 == findMember2));


//        for (Member member : resultList) {
//            System.out.println(member.getName());
//        }

        tx.begin();
        Movie movie = new Movie();
        movie.setActor("A");
        movie.setName("바람과함께 사라지다");
        movie.setDirector("B");
        movie.setPrice(10000);

        em.persist(movie);
        tx.commit();


        em.close();
        emf.close();
    }
}
