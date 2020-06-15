## Getting Started

pom.xml
```xml
<dependency>
  <groupId>com.github.lazaronixon</groupId>
  <artifactId>active-persistence</artifactId>
  <version>0.0.1</version>
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

services/ApplicationService.java
```java
public abstract class ApplicationService<T> extends Base<T> {

    public ApplicationService(Class<T> entityClass) {
        super(entityClass);
    }

}
```

services/StudentService.java
```java
@ApplicationScoped
public class StudentsService extends ApplicationService<Student> {

    @PersistenceContext
    private EntityManager em;

    public StudentsService() {
        super(Student.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
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
Student student = studentsService.find_by("this.name = 'Nixon'");
List<Student> students = studentsService.where("this.name = 'Nixon' AND this.occupation = 'Code Artist'").order("this.createdAt DESC").fetch();
```

### Update
```java
User user = usersService.find_by("this.name = 'David'");
user.name = 'Dave';
usersService.save(user);
// OR
usersService.update(user);
```

### Delete
```java
User user = usersService.find_by("this.name = 'David'")
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

Student student = studentsService.find_by("this.name = 'Lifo'");
```

### Conditions
```java
List<Client> clients = studentsService.where("this.ordersCount = ?", 10).fetch();
List<Client> clients = studentsService.where("this.ordersCount = ? AND this.locked = ?", 10, false).fetch();
```

### Ordering
```java
List<Client> clients = studentsService.order("this.createdAt").fetch();
List<Client> clients = studentsService.order("this.createdAt DESC").fetch();
List<Client> clients = studentsService.order("this.createdAt ASC").fetch();
```

### Selecting Specific Fields
```java
List<Client> clients = studentsService.select("this.viewableBy", "this.locked").fetch();
List<Client> clients = studentsService.select("this.name").distinct().fetch();
```

### Limit and Offset
```java
List<Client> clients = studentsService.limit(5).fetch();
List<Client> clients = studentsService.limit(5).offset(30).fetch();
```

### Group
```java
List<Client> clients = studentsService.select("date(this.createdAt), sum(price)").group("date(this.createdAt)").fetch();
```

### Having
```java
List<Client> clients = studentsService.select("date(this.createdAt), sum(this.price)").group("date(this.createdAt)").having("sum(this.price) > ?", 100).fetch();
```

### Reselect
```java
List<Client> clients = studentsService.select("this.title", "this.body").reselect("this.createdAt").fetch();
```

### Reorder
```java
List<Client> clients = studentsService.order("this.title", "this.body").reorder("this.createdAt").fetch();
```

### Rewhere
```java
List<Client> clients = studentsService.where("this.trashed = true").rewhere("this.trashed = false").fetch();
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
List<Client> clients = studentsService.includes("this.address").limit(10).fetch();
List<Client> clients = studentsService.eagerLoads("this.address").limit(10).fetch();
```

### Existence of Objects
```java
boolean exists = studentsService.exists("this.name = 'Lifo'");
boolean exists = studentsService.where("this.name = 'Lifo'").exists();
```

### Calculations
```java
long   count   = studentsService.count();
long   count1  = studentsService.count("this.id");
long   sum     = studentsService.sum("this.id", Long.class);
double average = studentsService.average("this.id", Double.class);
int    minimum = studentsService.minimum("this.id", Integer.class);
int    maximum = studentsService.maximum("this.id", Integer.class);
```

## Requirements
* JakartaEE 8

## More info
* https://guides.rubyonrails.org/active_record_querying.html
