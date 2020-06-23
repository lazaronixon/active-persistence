## Getting Started

pom.xml
```xml
<dependency>
  <groupId>com.github.lazaronixon</groupId>
  <artifactId>active-persistence</artifactId>
  <version>0.0.6</version>
</dependency>
```

models/User.java
```java
@Entity
public class User extends Base<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String occupation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User() {
    }

    // Get/Set omitted by brevity
}
```

users/UsersService.java
```java
@ApplicationScoped
public class UsersService extends Base<User> {

    @PersistenceContext
    private EntityManager entityManager;

    public UsersService() {
        super(User.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
```

## CRUD: Reading and Writing Data

### Create
```java
usersService.create(new User("David", "Code Artist"));

// Using the new method, an object can be instantiated without being saved

User user = new User();
user.name = "David";
user.occupation = "Code Artist";
// A call to usersService.save(user) will commit the record to the database
```

### Read
```java
// return a collection with all users
List<User> users = usersService.all().fetch();

// return the first user
User user = usersService.first();

// return the first user named David
User david = usersService.findBy("user.name = ?1", "David");

// find all users named David who are Code Artists and sort by created_at in reverse chronological order
List<User> users = usersService.where("user.name = ?1 AND user.occupation = ?2", "David", "Code Artist").order("user.createdAt DESC").fetch();
```

### Update
```java
User user = userService.findBy("user.name = ?1", "David");
user.name = 'Dave'
usersService.save(user);
```

### Delete
```java
User user = usersService.findBy("user.name = ?1", "David");
usersService.destroy(user);
```

## Retrieving Objects from the Database

### Retrieving a Single Object
```java
// Find the client with primary key (id) 10.
Client client = clientsService.find(10);

// The take method retrieves a record without any implicit ordering
Client client = clientsService.take();
List<Client> clients = clientsService.take(2);

// The first method finds the first record ordered by primary key (default)
Client client = clientsService.first();
Client client = clientsService.order("client.firstName").first();
List<Client> clients = clientsService.first(3);

// The last method finds the last record ordered by primary key (default)
Client client = clientsService.last();
Client client = clientsService.order("client.firstName").last();
List<Client> clients = clientsService.last(3);

// The find_by method finds the first record matching some conditions
Client client = clientsService.findBy("client.firstName = ?1", "Lifo"); // #<Client id: 1, first_name: "Lifo">
Client client = clientsService.findBy("client.firstName = ?1", "Jon"); // null

Client client = clientsService.findByOrFail("client.firstName = ?1", "does not exist"); // NoResultException
```

### Conditions
```java
clientsService.where("client.orders_count = ?1", 10).fetch();
clientsService.where("client.orders_count = ?1 AND clients.locked = ?2", 10, false).fetch();
```

### Ordering
```java
clientsService.order("client.createdAt").fetch();
clientsService.order("client.createdAt DESC").fetch();
clientsService.order("client.createdAt ASC").fetch();
```

### Selecting Specific Fields
```java
clientsService.select("student.viewableBy", "student.locked").fetch();
clientsService.select("student.name").distinct().fetch();
```

### Limit and Offset
```java
clientsService.limit(5).fetch();
clientsService.limit(5).offset(30).fetch();
```

### Group
```java
ordersService.select("date(order.createdAt), sum(order.price)").group("date(order.createdAt)").fetch();
```

### Having
```java
ordersService.select("date(order.createdAt), sum(order.price)").group("date(order.createdAt)").having("sum(order.price) > 100").fetch();
```

### Unscope
```java
ordersService.where('order.id > 10').limit(20).order('order.id asc').unscope(ORDER).fetch();
```

### Reselect
```java
postsService.select("post.title", "post.body").reselect("post.createdAt").fetch();
```

### Reorder
```java
postsService.order("post.title").reorder("post.createdAt").fetch();
```

### Rewhere
```java
articlesService.where("article.trashed = true").rewhere("article.trashed = false").fetch();
```

### Null Relation
```java
studentsService.none(); // returns an empty Relation and fires where 1=0.
```

### Locking Records for Update
```java
Client client = clientsService.lock().first();
```

### Joining Tables
```java
authorsService.joins("INNER JOIN posts").fetch();
```

### Eager Loading Associations
```java
clientsService.includes("client.address").limit(10).fetch();
clientsService.eagerLoads("client.address").limit(10).fetch();
```

### Applying a default scope
```java
public class StudentsService extends ApplicationService<Student> {
    @Override
    public boolean useDefaultScope() {
        return true;
    }

    @Override
    public Relation<Student> defaultScope() {
        return where("student.name = 'nixon'");
    }
}

clientsService.all(); // SELECT student FROM Student student WHERE student.name = 'nixon'
clientsService.unscoped().all(); // SELECT student FROM Student student
```

### Merging of scopes
```java
usersService.scoping(usersService.active()).fetch();
```

### Existence of Objects
```java
boolean exists = studentsService.exists("student.name = 'Lifo'");
boolean exists = studentsService.where("student.name = 'Lifo'").exists();
```

### Pluck
```java
clientsService.where("client.active = true").pluck("client.id"); //[1, 2, 3]
clientsService.where("client.active = true").ids; //[1, 2, 3]

```

### Calculations
```java
long   count   = clientsService.count();
long   count   = clientsService.count("client.age");
double average = (double) clientsService.average("client.orders_count");
int    minimum = (int) clientsService.minimum("client.age");
int    maximum = (int) clientsService.maximum("client.age");
long   total   = (long) clientsService.sum("client.orders_count");
```

## Requirements
* JakartaEE 8
* Java 9

## More info
* https://guides.rubyonrails.org/active_record_querying.html
