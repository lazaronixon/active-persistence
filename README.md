# Getting Started

models/Student.java
```java
@Entity
public class Student extends Base<Integer> implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public abstract T find(String id);

    private final Class<T> entityClass;

    public ApplicationService(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

}
```

services/StudentService.java
```java
@ApplicationScoped
public class StudentsService extends ApplicationService<Student> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "jsfcrud")
    private EntityManager em;

    public StudentsService() {
        super(Student.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Student find(String id) {
        return getEntityManager().find(Student.class, Integer.parseInt(id));
    }

}
```

## Retrieving Objects from the Database
```java
Student student = studentFacade.find(10);
Student student = studentFacade.take();

Student student = studentFacade.first()
List<Student> students = studentFacade.first(3);

Student student = studentFacade.last()
List<Student> students = studentFacade.last(3);

Student student = studentFacade.find_by("this.name = 'Lifo'");
```

## Conditions
```java
List<Client> clients = clientFacade.where("this.ordersCount = ?", 10).fetch();
List<Client> clients = clientFacade.where("this.ordersCount = ? AND this.locked = ?", 10, false).fetch();
```

## Ordering
```java
List<Client> clients = clientFacade.order("this.createdAt").fetch();
List<Client> clients = clientFacade.order("this.createdAt DESC").fetch();
List<Client> clients = clientFacade.order("this.createdAt ASC").fetch();
```

## Selecting Specific Fields
```java
List<Client> clients = clientFacade.select("this.viewableBy", "this.locked").fetch();
List<Client> clients = clientFacade.select("this.name").distinct().fetch();
```

## Limit and Offset
```java
List<Client> clients = clientFacade.limit(5).fetch();
List<Client> clients = clientFacade.limit(5).offset(30).fetch();
```

## Group
```java
List<Client> clients = clientFacade.select("date(this.createdAt), sum(price)").group("date(this.createdAt)").fetch();
```

## Having
```java
List<Client> clients = clientFacade.select("date(this.createdAt), sum(this.price)").group("date(this.createdAt)").having("sum(this.price) > ?", 100).fetch();
```

## Reselect
```java
List<Client> clients = clientFacade.select("this.title", "this.body").reselect("this.createdAt").fetch();
```

## Reorder
```java
List<Client> clients = clientFacade.order("this.title", "this.body").reorder("this.createdAt").fetch();
```

## Rewhere
```java
List<Client> clients = clientFacade.where("this.trashed = true").rewhere("this.trashed = false").fetch();
```

## Null Relation
```java
List<Client> clients = clientFacade.none();
```

## Locking Records for Update
```java
Client client = clientFacade.lock().first();
```

## Joining Tables
```java
List<Author> authors = authorFacade.joins("INNER JOIN posts ON posts.author_id = this.id AND posts.published = 't'").fetch();
```

## Eager Loading Associations
```java
List<Client> clients = clientFacade.includes("this.address").limit(10).fetch();
List<Client> clients = clientFacade.eagerLoads("this.address").limit(10).fetch();
```

## Existence of Objects
```java
boolean exists = clientFacade.exists("this.name = 'Lifo'");
boolean exists = clientFacade.where("this.name = 'Lifo'").exists();
```

## Calculations
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
