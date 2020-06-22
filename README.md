## Getting Started

pom.xml
```xml
<dependency>
  <groupId>com.github.lazaronixon</groupId>
  <artifactId>active-persistence</artifactId>
  <version>0.0.4</version>
</dependency>
```

models/Student.java
```java
@Entity
public class Student extends Base<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Student() {
    }

    // Get/Set omitted by brevity
}
```

services/StudentService.java
```java
@ApplicationScoped
public class StudentsService extends Base<Student> {

    @PersistenceContext
    private EntityManager em;

    public StudentsService() {
        super(Student.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
```

## CRUD: Reading and Writing Data

### Create
```java
Student student = new Student();
student.name = "Nixon";
student.address = "Avenue 45, number 4";

usersService.create(student);
```

### Read
```java
List<Student> students = studentsService.all().fetch();
Student student = studentsService.first();
Student student = studentsService.findBy("student.name = 'Nixon'");
List<Student> students = studentsService.where("student.name = 'Nixon' AND student.occupation = 'Code Artist'").order("student.createdAt DESC").fetch();
```

### Update
```java
User user = usersService.findBy("student.name = 'David'");
user.name = 'Dave';
usersService.save(user);
// OR
usersService.update(user);
```

### Delete
```java
User user = usersService.findBy("student.name = 'David'")
usersService.destroy(user);
```

## Retrieving Objects from the Database

### Retrieving a Single Object
```java
Student student = studentsService.find(10);
Student student = studentsService.take();

Student student = studentsService.first()
List<Student> students = studentsService.first(3);

Student student = studentsService.last()
List<Student> students = studentsService.last(3);

Student student = studentsService.findBy("student.name = 'Lifo'");
```

### Conditions
```java
List<Client> clients = studentsService.where("student.ordersCount = ?1", 10).fetch();
List<Client> clients = studentsService.where("student.ordersCount = ?1 AND student.locked = ?2", 10, false).fetch();
```

### Ordering
```java
List<Client> clients = studentsService.order("student.createdAt").fetch();
List<Client> clients = studentsService.order("student.createdAt DESC").fetch();
List<Client> clients = studentsService.order("student.createdAt ASC").fetch();
```

### Selecting Specific Fields
```java
List<Client> clients = studentsService.select("student.viewableBy", "student.locked").fetch();
List<Client> clients = studentsService.select("student.name").distinct().fetch();
```

### Limit and Offset
```java
List<Client> clients = studentsService.limit(5).fetch();
List<Client> clients = studentsService.limit(5).offset(30).fetch();
```

### Group
```java
List<Client> clients = studentsService.select("date(student.createdAt), sum(price)").group("date(student.createdAt)").fetch();
```

### Having
```java
List<Client> clients = studentsService.select("date(student.createdAt), sum(student.price)").group("date(student.createdAt)").having("sum(student.price) > 100").fetch();
```

### Reselect
```java
List<Client> clients = studentsService.select("student.title", "student.body").reselect("student.createdAt").fetch();
```

### Reorder
```java
List<Client> clients = studentsService.order("student.title", "student.body").reorder("student.createdAt").fetch();
```

### Rewhere
```java
List<Client> clients = studentsService.where("student.trashed = true").rewhere("student.trashed = false").fetch();
```

### Null Relation
```java
List<Client> clients = studentsService.none();
```

### Locking Records for Update
```java
Client client = studentsService.lock().first();
```

### Joining Tables
```java
List<Author> authors = studentsService.joins("INNER JOIN posts").fetch();
```

### Eager Loading Associations
```java
List<Client> clients = studentsService.includes("student.address").limit(10).fetch();
List<Client> clients = studentsService.eagerLoads("student.address").limit(10).fetch();
```

### Existence of Objects
```java
boolean exists = studentsService.exists("student.name = 'Lifo'");
boolean exists = studentsService.where("student.name = 'Lifo'").exists();
```

### Calculations
```java
long   count   = studentsService.count();
long   count1  = studentsService.count("student.id");
long   sum     = (long) studentsService.sum("student.id");
double average = (double) studentsService.average("student.id");
int    minimum = (int) studentsService.minimum("student.id");
int    maximum = (int) studentsService.maximum("student.id");
```

## Requirements
* JakartaEE 8

## More info
* https://guides.rubyonrails.org/active_record_querying.html
