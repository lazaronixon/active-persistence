## Getting Started

pom.xml
```xml
<dependency>
  <groupId>com.github.lazaronixon</groupId>
  <artifactId>active-persistence</artifactId>
  <version>0.0.12</version>
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
User david = usersService.findBy("user.name = ?", "David");

// find all users named David who are Code Artists and sort by createdAt in reverse chronological order
List<User> users = usersService.where("user.name = ? AND user.occupation = ?", "David", "Code Artist").order("user.createdAt DESC").fetch();
```

### Update
```java
User user = userService.findBy("user.name = ?", "David");
user.name = "Dave";
usersService.save(user);
//OR
usersService.update(user);
//OR
usersService.updateAll("user.maxLoginAttempts = 3, user.mustChangePassword = 'true'");
```

### Delete
```java
User user = usersService.findBy("user.name = ?", "David");
usersService.destroy(user);
//OR
usersService.destroyBy("user.name = 'David'");
usersService.destroyAll()
//OR
usersService.deleteBy("user.name = 'David'");
usersService.deleteAll()
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

// The findBy method finds the first record matching some conditions
Client client = clientsService.findBy("client.firstName = ?", "Lifo"); // #<Client id: 1, firstName: "Lifo">
Client client = clientsService.findBy("client.firstName = ?", "Jon"); // null

Client client = clientsService.findByOrFail("client.firstName = ?", "does not exist"); // NoResultException
```

### Conditions
```java
//Ordinal Conditions
clientsService.where("client.ordersCount = ?", 10).fetch();
clientsService.where("client.ordersCount = ? AND client.locked = ?", 10, false).fetch();

//Placeholder Conditions
clientsService.where("client.ordersCount = :count", Map.of("count", 10)).fetch();
clientsService.where("client.ordersCount = :count AND client.locked = :locked", Map.of("count", 10, "locked", false)).fetch();

```

### Ordering
```java
clientsService.order("client.createdAt").fetch();
clientsService.order("client.createdAt DESC").fetch();
clientsService.order("client.createdAt ASC").fetch();
```

### Selecting Specific Fields
```java
Client client = clientsService.select("client.viewableBy", "client.locked").fetch();
Client client = clientsService.select("client.name").distinct().fetch();
```

### Limit and Offset
```java
clientsService.limit(5).fetch();
clientsService.limit(5).offset(30).fetch();
```

### Group
```java
List<Order> orders = ordersService.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)").fetch();
```

### Total of grouped items
```java
HashMap<String, Long> result = (HashMap) ordersService.group("order.status").count
// => { 'awaiting_approval' => 7, 'paid' => 12 }
```

### Having
```java
List<Order> orders = ordersService.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)").having("sum(order.price) > 100").fetch();
```

## Overriding Conditions

### Unscope
```java
ordersService.where("order.id > 10").limit(20).order("order.id asc").unscope(ORDER).fetch();
```

### Only
```java
ordersService.where("order.id > 10").limit(20).order("order.id asc").only(ORDER).fetch();
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
authorsService.joins("JOIN Post p").fetch();
```

## Eager Loading Associations
```java
clientsService.includes("client.address").limit(10).fetch();
clientsService.eagerLoads("client.address").limit(10).fetch();
```

## Applying a default scope
```java
public class ClientsService extends ApplicationService<Client> {
    @Override
    public boolean useDefaultScope() {
        return true;
    }

    @Override
    public Relation<Client> defaultScope() {
        return where("client.name = 'nixon'");
    }
}

clientsService.all(); // SELECT client FROM Client client WHERE client.name = 'nixon'
clientsService.unscoped().all(); // SELECT client FROM Client client
```

### Merging of scopes
```java
usersService.merge(usersService.active()).fetch();
```

### Removing All Scoping
```java
clientsService.unscoped().fetch();
clientsService.where("client.published = false").unscoped().fetch();
```

### Find or Build a New Object
```java
Post createdPost = postsService.findOrCreateBy(new Post("awesome title", "body", 0), "post.title = 'awesome title'");
Post newPost     = postsService.findOrInitializeBy(new Post("awesome title", "body", 0), "post.title = 'awesome title'");
```

## Finding by SQL
```java
List<Post> posts = postsService.findBySql("SELECT id, title FROM Post WHERE id = 5");
List<Post> posts = postsService.findBySql("SELECT id, title FROM Post WHERE id = ?1", Map.of(1, 5));
// OR
List<Object[]> posts = postsService.selectAll("SELECT id, title FROM Post WHERE id = 5");
List<Object[]> posts = postsService.selectAll("SELECT id, title FROM Post WHERE id = ?1", Map.of(1, 5));
```

### Existence of Objects
```java
boolean exists = studentsService.exists("student.name = 'Lifo'");
boolean exists = studentsService.where("student.name = 'Lifo'").exists();
```

### Pluck
```java
List<Integer> ids = clientsService.where("client.active = true").pluck("client.id"); //[1, 2, 3]
List<Integer> ids = clientsService.where("client.active = true").ids; //[1, 2, 3]

```

### Calculations
```java
long   count   = (long)   clientsService.count();
long   count   = (long)   clientsService.count("client.age");
int    minimum = (int)    clientsService.minimum("client.age");
int    maximum = (int)    clientsService.maximum("client.age");
long   total   = (long)   clientsService.sum("client.ordersCount");
double average = (double) clientsService.average("client.ordersCount");
```

## Recommended Environment
* Java 9
* JakartaEE 8
* Payara Server

## Testing
* Install Payara Server >= 5.201
* Add Resources - ./asadmin add-resources path_to/payara-resources.xml

## More info
* https://guides.rubyonrails.org/active_record_querying.html
