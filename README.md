# Read Me First

# SpringBoot-Hibernate-JPA
Spring boot project which depicts Hibernate and JPA.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.1/maven-plugin/reference/html/#build-image)

# Project Creation and software details 
* **Technologies used -** JAVA, Springboot, MySQL, Maven
* Create project using Spring Initializr - https://start.spring.io/


### Setting up MySQL Database and Spring Boot Application

* MYSQL includes two components - **MySQL Database Server and MySQL Workbench**
* MySQL Database Server is the main engine of database. It stores data for database
* MySQL Workbench is a GUI for interacting with the database
* Add following dependencies in the project --
  * MySQL Drivers - **_mysql-connector-j_**
  * Spring Data JPA - **_spring-boot-starter-data-jpa_**
* Add sql url, user and password in the application.properties file --
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hb-advance-mappings
spring.datasource.username=springstudent
spring.datasource.password=springstudent
```


# JPA/ Hibernate Advanced Mappings
* In the database, there are multiple tables and relationships between tables.
* There are following advanced mapping techniques --
    * One-to-One
    * One-to-Many/ Many-to-One
    * Many-to-Many

### Entity Lifecycle
* Following are the operations --
1. **Detach** - If entity is detached, it is not associated with Hibernate session.
2. **Merge** - If instance is detached from the session, then merge will reattach to session.
3. **Persist** - Transitions new instances to managed state. Next flush/ commit will save in db.
4. **Remove** - Transitions managed entity to be removed. Next flush/ commit will delete from db.
5. **Refresh** - Reload/ sync object with data from db. Prevents stale data.

### Cascade and its types --
* Cascade means to apply same operations to related entities.
* @OneToOne Cascade Types--
    1. **PERSIST -** If entity is persisted/ saved, related entity will also be persisted.
    2. **REMOVE -** If entity is removed/ deleted, related entity will also be deleted.
    3. **REFRESH -** If entity is refreshed, related entity will also be refreshed.
    4. **DETACH -** If entity is detached (not associated with session), then related entity will also be detached.
    5. **MERGE -** If entity is merged, related entity will also be merged.
    6. **ALL -** All the above cascade types.



* **Syntax - @OneToOne(cascade=CascadeType.ALL).**
* By default no operations are cascaded. 

### Hibernate - Implementing One-to-One Mapping

1. Create class named InstructorDetail and annotate it as **@Entity and @Table**.
```java
@Entity
@Table(name = "instructor_detail")
public class InstructorDetail {
    // define fields and constructors
    // annotate the fields with db column names
}
```
2. Define various fields and annotate it with **@Id** for primary key and **@Column** for specifing database table column name for each fields.
3. Define getter, setters, constructors and toString() method.

**NOTE-** Refer InstructorDetails.java class.

4. Define Instructor class and its fields and constructors.
5. Define One-to-One mapping to the InstructorDetail table as follows -
```java
@Table(name = "instructor")
public class Instructor{
  // define fields and constructors
  // annotate the fields with db column names

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "instructor_detail_id")
  private InstructorDetail instructorDetail;
}
   
    
```
6. Saved the details using persist() method in DAO.
```java
@Repository
public class AppDAOImpl implements AppDAO{
   //define field for Entity manager;
    private EntityManager entityManager;

    // injects entity manager using constructor injection
    @Autowired
    public AppDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Instructor instructor) {
        entityManager.persist(instructor);
    }
}

```

### One-to-Many or Many-to-One Mapping
* It depicts that one row in a table is mapped or associated with multiple rows in other table.
* It is implemented using **@ManyToOne**, **@OneToMany** and **@JoinColumn**

```java

@Entity
@Table(name= "course")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;
  @Column(name = "title")
  private String title;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "instructor_id")
  private Instructor instructor;

  // define constructors, getters, setters and toString method
}
```

* Add following properties in the Instructor table -- 
```java
@Entity
@Table(name= "instructor")
public class Instructor {
  //...
  @OneToMany(mappedBy = "instructor", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  private List<Course> courses;
  
}
```
### Many-to-Many Mapping
* Many-to-Many mapping is made between two entities when one entity has many instance of the other entity and vice versa.
* In this type of mapping it is necessary to keep a track of the relations between the entities. Hence, a **join table** is required.
* **Join Table-** it is a table that provides a mapping between two tables. It has foreign keys for each table to define the mapping relationship.
* Add a **@ManyToMany** annotation, **@JoinTable** and inverseJoinColumns to implement this mapping.
* inverse refers to the "other side" of the relationship.


Defining Student class and adding @ManyToMany to Course class

```java
@Entity
@Table(name="student")
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;
    
  // define constructors, getters, setters and toString method 
}
```

```java
@Entity
@Table(name= "course")
public class Course {
  //... define fields

  @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinTable(name = "course_student",
          joinColumns = @JoinColumn(name="course_id"),
          inverseJoinColumns = @JoinColumn(name="student_id"))
  private List<Student> students;
}
```

