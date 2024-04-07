package com.JPA_Hibernate.hibernate_JPA;

import com.JPA_Hibernate.hibernate_JPA.dao.AppDAO;
import com.JPA_Hibernate.hibernate_JPA.entity.Instructor;
import com.JPA_Hibernate.hibernate_JPA.entity.InstructorDetail;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HibernateJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HibernateJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AppDAO appDAO){
		return runner ->{
			createConstructor(appDAO);
		};
	}

	private void createConstructor(AppDAO appDAO) {
		Instructor tempInstructor = new Instructor("Krupa", "Patel", "krupa@p.com");

		InstructorDetail tempInstructorDetail = new InstructorDetail("http://www.kp.com/youtube","Reading");

		//associate the objects -
		tempInstructor.setInstructorDetail(tempInstructorDetail);

		//save the instructor
		// this will also save the details object because of Cascade.ALL
		System.out.println("Saving instructor: "+ tempInstructor);
		appDAO.save(tempInstructor);

		System.out.println("Done!!");
	}
}
