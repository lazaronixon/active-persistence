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
User david = usersService.bind(1, "David").findBy("this.name = ?1");

// find all users named David who are Code Artists and sort by createdAt in reverse chronological order
List<User> users = usersService.where("this.name = ?1 AND this.occupation = ?2").order("this.createdAt DESC").bind(1, "David").bind(2, "Code Artist").fetch();
```

### Update
```java
User user = userService.bind(1, "David").findBy("this.name = ?1");
user.name = "Dave";
usersService.save(user);
//OR
usersService.updateAll("this.maxLoginAttempts = 3, mustChangePassword = 'true'");
```

### Delete
```java
User user = usersService.bind(1, "David").findBy("this.name = ?1");
usersService.destroy(user);
//OR
usersService.destroyBy("this.name = 'David'");
//OR
usersService.destroyAll();
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
Client client = clientsService.order("this.firstName").first();
List<Client> clients = clientsService.first(3);

// The last method finds the last record ordered by primary key (default)
Client client = clientsService.last();
Client client = clientsService.order("this.firstName").last();
List<Client> clients = clientsService.last(3);

// The findBy method finds the first record matching some conditions
Client client = clientsService.bind(1, "Lifo").findBy("this.firstName = ?1"); // #<Client id: 1, firstName: "Lifo">
Client client = clientsService.bind(1, "Jon").findBy("this.firstName = ?1"); // null

Client client = clientsService.bind(1, "does not exist").findByOrFail("this.firstName = ?1"); // NoResultException
```

### Conditions
```java
//Ordinal Conditions
clientsService.where("this.ordersCount = ?1").bind(1, 10).fetch();
clientsService.where("this.ordersCount = ?1 AND this.locked = ?2").bind(1, 10).bind(2, false).fetch();

//Placeholder Conditions
clientsService.where("this.ordersCount = :count").bind("count", 10).fetch();
clientsService.where("this.ordersCount = :count AND this.locked = :locked").bind("count", 10).bind("locked", false).fetch();

```

### Ordering
```java
clientsService.order("this.createdAt").fetch();
clientsService.order("this.createdAt DESC").fetch();
clientsService.order("this.createdAt ASC").fetch();
```

### Selecting Specific Fields
```java
Client client = clientsService.select("this.viewableBy", "this.locked").fetch();
Client client = clientsService.select("this.name").distinct().fetch();
```

### Limit and Offset
```java
clientsService.limit(5).fetch();
clientsService.limit(5).offset(30).fetch();
```

### Group
```java
ordersService.select("date(this.createdAt), sum(this.price)").group("date(this.createdAt)").fetch();
```

### Total of grouped items
```java
HashMap<String, Long> result = ordersService.group("this.status").count // => { 'awaiting_approval' => 7, 'paid' => 12 }
```

### Having
```java
ordersService.select("date(this.createdAt), sum(this.price)").group("date(this.createdAt)").having("sum(this.price) > 100").fetch();
```

## Overriding Conditions

### Unscope
```java
ordersService.where("this.id > 10").limit(20).order("this.id asc").unscope(ORDER).fetch();
```

### Reselect
```java
postsService.select("this.title", "this.body").reselect("this.createdAt").fetch();
```

### Reorder
```java
postsService.order("this.title").reorder("this.createdAt").fetch();
```

### Rewhere
```java
articlesService.where("this.trashed = true").rewhere("this.trashed = false").fetch();
```

## Null Relation
```java
studentsService.none(); // returns an empty Relation and fires where 1=0.
```

## Locking Records for Update
```java
Client client = clientsService.lock().first();
```

## Joining Tables
```java
authorsService.joins("INNER JOIN posts").fetch();
```

## Eager Loading Associations
```java
clientsService.includes("client.address").limit(10).fetch();
clientsService.eagerLoads("client.address").limit(10).fetch();
```

## Applying a default scope
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

clientsService.all(); // SELECT this FROM Client this WHERE this.name = 'nixon'
clientsService.unscoped().all(); // SELECT this FROM Client this
```

### Merging of scopes
```java
usersService.scoping(usersService.active()).fetch();
```

### Removing All Scoping
```java
clientsService.unscoped().all();
clientsService.where("this.published = false").unscoped.all
```

### Find or Build a New Object
```java
Post createdPost = postsService.findOrCreateBy("this.title = 'awesome title'",() -> new Post("awesome title", "body", 0));
Post newPost     = postsService.findOrGetBy("this.title = 'awesome title'",() -> new Post("awesome title", "body", 0));
```

### Existence of Objects
```java
boolean exists = studentsService.exists("this.name = 'Lifo'");
boolean exists = studentsService.where("this.name = 'Lifo'").exists();
```

### Pluck
```java
clientsService.where("this.active = true").pluck("this.id"); //[1, 2, 3]
clientsService.where("this.active = true").ids; //[1, 2, 3]

```

### Calculations
```java
long   count   = (long)   clientsService.count();
long   count   = (long)   clientsService.count("this.age");
int    minimum = (int)    clientsService.minimum("this.age");
int    maximum = (int)    clientsService.maximum("this.age");
long   total   = (long)   clientsService.sum("this.ordersCount");
double average = (double) clientsService.average("this.ordersCount");
```

## Requirements
* Java 9
* JakartaEE 8
* Payara Server

## Testing
* Install Payara Server >= 5.201
* Add Resources - ./asadmin add-resources path_to/payara-resources.xml

## More info
* https://guides.rubyonrails.org/active_record_querying.html
