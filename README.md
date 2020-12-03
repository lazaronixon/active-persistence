## Getting Started

pom.xml
```xml
<dependency>
  <groupId>com.github.lazaronixon</groupId>
  <artifactId>active-persistence</artifactId>
  <version>0.0.27</version>
</dependency>
```

models/User.java
```java
@Entity
public class User extends BaseIdentity {

    private String name;

    private String occupation;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Get/Set omitted by brevity
}
```

users/UsersService.java
```java
public class UsersService extends Base<User> {

}
```

## CRUD: Reading and Writing Data

### Create
```java
User user = new User();
user.name = "David";
user.occupation = "Code Artist";

usersService.save(user);
```

### Read
```java
// return a collection with all users
List<User> users = usersService.all();

// return the first user
User user = usersService.first();

// return the first user named David
User david = usersService.findBy("user.name = ?", "David");

// find all users named David who are Code Artists and sort by createdAt in reverse chronological order
List<User> users = usersService.where("user.name = 'David' AND user.occupation = 'Code Artist'").order("user.createdAt DESC");
```

### Update
```java
User user = usersService.findBy("user.name = ?", "David");
user.name = "Dave";
usersService.save(user);
//OR
usersService.updateAll("user.maxLoginAttempts = 3, user.mustChangePassword = 'true'");
```

### Delete
```java
User user = usersService.findBy("user.name = ?", "David");
usersService.destroy(user);
//OR
usersService.destroyBy("user.name = ?", "David");
usersService.destroyAll();
```

### Callbacks

```java
public class ClientsService extends Base<Client> {

    @Override
    public void beforeSave(Client client) {
        // implementation here
    }

    @Override
    public void afterSave(Client client) {
        // implementation here
    }

    @Override
    public void beforeCreate(Client client) {
        // implementation here
    }

    @Override
    public void afterCreate(Client client) {
        // implementation here
    }

    @Override
    public void beforeUpdate(Client client) {
        // implementation here
    }

    @Override
    public void afterUpdate(Client client) {
        // implementation here
    }

    @Override
    public void beforeDestroy(Client client) {
        // implementation here
    }

    @Override
    public void afterDestroy(Client client) {
        // implementation here
    }

}
```

### TimeStamps
```java
public class Post extends BaseIdentity {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
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
Client client = clientsService.findBy("client.firstName = ?", "Jon");  // null

Client client = clientsService.findBy$("client.firstName = ?", "does not exist"); // EntityNotFoundException
```

### Conditions
```java
//Ordinal Conditions
clientsService.where("client.ordersCount = ?", 10);
clientsService.where("client.ordersCount = ? AND client.locked = ?", 10, false);

//Placeholder Conditions
clientsService.where("client.ordersCount = :count", Map.of("count", 10));
clientsService.where("client.ordersCount = :count AND client.locked = :locked", Map.of("count", 10, "locked", false));

//SubQuery Conditions
var subquery = ordersService.selectScalar("order.client.id");
clientsService.where("client.id IN (?), subquery);
```

### Ordering
```java
clientsService.order("client.createdAt");
clientsService.order("client.createdAt DESC");
clientsService.order("client.createdAt ASC");
```

### Selecting Specific Fields
```java
List<Client> client = clientsService.select("client.viewableBy", "client.locked");
List<Client> client = clientsService.select("client.name").distinct();

List values = clientsService.selectScalar("client.viewableBy", "client.locked");
List values = clientsService.selectScalar("client.name").distinct();
```

### Limit and Offset
```java
clientsService.limit(5);
clientsService.limit(5).offset(30);
```

### Group
```java
List<Order> orders = ordersService.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)");
```

### Total of grouped items
```java
Map<String, Long> result = (Map) ordersService.group("order.status").count(); // => { 'awaiting_approval' => 7, 'paid' => 12 }
```

### Having
```java
List<Order> orders = ordersService.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)").having("sum(order.price) > 100");
```

## Overriding Conditions

### Unscope
```java
ordersService.where("order.id > 10").limit(20).order("order.id asc").unscope(ORDER);
```

### Only
```java
ordersService.where("order.id > 10").limit(20).order("order.id asc").only(ORDER);
```

### Reselect
```java
postsService.select("post.title", "post.body").reselect("post.createdAt");
```

### Reorder
```java
postsService.order("post.title").reorder("post.createdAt");
```

### Rewhere
```java
articlesService.where("article.trashed = true").rewhere("article.trashed = false");
```

## Null Relation
```java
studentsService.none(); // returns an empty Relation and fires where 1=0.
```

## Locking Records for Update
```java
Client client = clientsService.lock().first();
// OR
Client client = clientsService.lock(true).first();
// OR
Client client = clientsService.lock(PESSIMISTIC_WRITE).first();
```

## Readonly Objects
```java
Client client = clientsService.readonly().first();
client.visits = 1;

clientsService.save(); // ReadOnlyRecord Exception
```

## Joining Tables
```java
authorsService.joins("JOIN author.post post");
```

## Eager Loading Associations (EclipseLink only)
```java
clientsService.includes("client.address").limit(10);
clientsService.eagerLoads("client.address").limit(10);
```

## Scopes

### Applying a default scope
```java
public class ClientsService extends Base<Client> {

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
usersService.merge(usersService.active());
```

### Removing All Scoping
```java
clientsService.unscoped();
clientsService.where("client.published = false").unscoped();
```

## Dynamic Finders
```java
Client client = clientsService.findByExpression("Name", "Nixon");
Client client = clientsService.findByExpression("NameAndLocked", "Nixon", true);
// OR
Client client = clientsService.findByExpression$("Name", "not found"); // EntityNotFoundException
```

## Finding by SQL/JPQL
```java
List<Post> posts = postsService.findBySql("SELECT id, title FROM Post WHERE id = 5").getResultList();
List<Post> posts = postsService.findByJpql("SELECT p FROM Post p WHERE p.id = 5").getResultList();
// OR
List posts = postsService.getConnection().selectAll("SELECT id, title FROM Post WHERE id = 5", QueryType.SQL).getResultList();
List posts = postsService.getConnection().selectAll("SELECT p FROM Post p WHERE p.id = 5", QueryType.JPQL).getResultList();
```

## Existence of Objects
```java
boolean exists = studentsService.exists("student.name = 'Lifo'");
boolean exists = studentsService.where("student.name = 'Lifo'").exists();
```

## Pluck
```java
List<Integer> ids = clientsService.where("client.active = true").pluck("client.id"); //[1, 2, 3]
List<Integer> ids = clientsService.where("client.active = true").ids; //[1, 2, 3]

```

## Calculations
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
* https://github.com/lazaronixon/jsf-perfect-crud/tree/active-persistence
* https://www.youtube.com/watch?v=Hw8iKrZQk4o
* https://guides.rubyonrails.org/active_record_querying.html
