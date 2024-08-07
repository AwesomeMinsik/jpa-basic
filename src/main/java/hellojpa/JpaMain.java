package hellojpa;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        //code
        EntityTransaction tx = em.getTransaction();

        List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();
        //jpql 객체를 다루는 쿼리라 * 안써지는거같음

        for (Member member : resultList) {
            System.out.println(member.getName());
        }


        em.close();
        emf.close();
    }
}
