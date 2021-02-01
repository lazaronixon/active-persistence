## Getting Started

pom.xml
```xml
<dependency>
  <groupId>com.github.lazaronixon</groupId>
  <artifactId>active-persistence</artifactId>
  <version>0.0.28</version>
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

services/UserService.java
```java
@RequestScoped
public class UserService extends Base<User> {

}
```

## CRUD: Reading and Writing Data

### Create
```java
User user = new User();
user.name = "David";
user.occupation = "Code Artist";

userService.save(user);
```

### Read
```java
// return a collection with all users
List<User> users = userService.all();

// return the first user
User user = userService.first();

// return the first user named David
User david = userService.findBy("user.name = ?", "David");

// find all users named David who are Code Artists and sort by createdAt in reverse chronological order
List<User> users = userService.where("user.name = 'David' AND user.occupation = 'Code Artist'").order("user.createdAt DESC");
```

### Update
```java
User user = userService.findBy("user.name = ?", "David");
user.name = "Dave";
userService.save(user);
//OR
userService.updateAll("user.maxLoginAttempts = 3, user.mustChangePassword = 'true'");
```

### Delete
```java
User user = userService.findBy("user.name = ?", "David");
userService.destroy(user);
//OR
userService.destroyBy("user.name = ?", "David");
userService.destroyAll();
```

### Callbacks

```java
public class ClientService extends Base<Client> {

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
Client client = clientService.find(10);

// The take method retrieves a record without any implicit ordering
Client client = clientService.take();
List<Client> clients = clientService.take(2);

// The first method finds the first record ordered by primary key (default)
Client client = clientService.first();
Client client = clientService.order("client.firstName").first();
List<Client> clients = clientService.first(3);

// The last method finds the last record ordered by primary key (default)
Client client = clientService.last();
Client client = clientService.order("client.firstName").last();
List<Client> clients = clientService.last(3);

// The findBy method finds the first record matching some conditions
Client client = clientService.findBy("client.firstName = ?", "Lifo"); // #<Client id: 1, firstName: "Lifo">
Client client = clientService.findBy("client.firstName = ?", "Jon");  // null

Client client = clientService.findBy$("client.firstName = ?", "does not exist"); // EntityNotFoundException
```

### Conditions
```java
//Ordinal Conditions
clientService.where("client.ordersCount = ?", 10);
clientService.where("client.ordersCount = ? AND client.locked = ?", 10, false);

//Placeholder Conditions
clientService.where("client.ordersCount = :count", Map.of("count", 10));
clientService.where("client.ordersCount = :count AND client.locked = :locked", Map.of("count", 10, "locked", false));

//SubQuery Conditions
var subquery = clientService.select("order.client.id");
clientService.where("client.id IN (?), subquery);
```

### Ordering
```java
clientService.order("client.createdAt");
clientService.order("client.createdAt DESC");
clientService.order("client.createdAt ASC");
```

### Selecting Specific Fields
```java
List<Client> client = clientService.select("client.viewableBy", "client.locked");
List<Client> client = clientService.select("client.name").distinct();
```

### Limit and Offset
```java
clientService.limit(5);
clientService.limit(5).offset(30);
```

### Group
```java
List<Order> orders = orderService.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)");
```

### Total of grouped items
```java
Map<String, Long> result = (Map) orderService.group("order.status").count(); // => { 'awaiting_approval' => 7, 'paid' => 12 }
```

### Having
```java
List<Order> orders = orderService.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)").having("sum(order.price) > 100");
```

## Overriding Conditions

### Unscope
```java
orderService.where("order.id > 10").limit(20).order("order.id asc").unscope(ORDER);
```

### Only
```java
orderService.where("order.id > 10").limit(20).order("order.id asc").only(ORDER);
```

### Reselect
```java
postService.select("post.title", "post.body").reselect("post.createdAt");
```

### Reorder
```java
postService.order("post.title").reorder("post.createdAt");
```

### Rewhere
```java
articleService.where("article.trashed = true").rewhere("article.trashed = false");
```

## Null Relation
```java
studentService.none(); // returns an empty Relation and fires where 1=0.
```

## Locking Records for Update
```java
Client client = clientService.lock().first();
// OR
Client client = clientService.lock(true).first();
// OR
Client client = clientService.lock(PESSIMISTIC_WRITE).first();
```

## Readonly Objects
```java
Client client = clientService.readonly().first();
client.visits = 1;

clientService.save(); // ReadOnlyRecord Exception
```

## Joining Tables
```java
authorService.joins("JOIN author.post post");
```

## Eager Loading Associations (EclipseLink only)
```java
clientService.includes("client.address").limit(10);
clientService.eagerLoads("client.address").limit(10);
```

## Scopes

### Applying a default scope
```java
public class ClientService extends Base<Client> {

    @Override
    public Relation<Client> defaultScope() {
        return where("client.name = 'nixon'");
    }

}

clientService.all(); // SELECT client FROM Client client WHERE client.name = 'nixon'
clientService.unscoped().all(); // SELECT client FROM Client client
```

### Merging of scopes
```java
userService.merge(active());
```

### Removing All Scoping
```java
clientService.unscoped();
clientService.where("client.published = false").unscoped();
```

## Dynamic Finders
```java
Client client = clientService.findByExpression("Name", "Nixon");
Client client = clientService.findByExpression("NameAndLocked", "Nixon", true);
// OR
Client client = clientService.findByExpression$("Name", "not found"); // EntityNotFoundException
```

## Finding by SQL/JPQL
```java
List<Post> posts = postService.findBySql("SELECT id, title FROM Post WHERE id = 5").getResultList();
List<Post> posts = postService.findByJpql("SELECT p FROM Post p WHERE p.id = 5").getResultList();
// OR
List posts = postService.getConnection().selectAll("SELECT id, title FROM Post WHERE id = 5", QueryType.SQL).getResultList();
List posts = postService.getConnection().selectAll("SELECT p FROM Post p WHERE p.id = 5", QueryType.JPQL).getResultList();
```

## Existence of Objects
```java
boolean exists = studentService.exists("student.name = 'Lifo'");
boolean exists = studentService.where("student.name = 'Lifo'").exists();
```

## Pluck
```java
List<Integer> ids = clientService.where("client.active = true").pluck("client.id"); //[1, 2, 3]
List<Integer> ids = clientService.where("client.active = true").ids; //[1, 2, 3]
```

## Calculations
```java
long   count   = (long)   clientService.count();
long   count   = (long)   clientService.count("client.age");
int    minimum = (int)    clientService.minimum("client.age");
int    maximum = (int)    clientService.maximum("client.age");
long   total   = (long)   clientService.sum("client.ordersCount");
double average = (double) clientService.average("client.ordersCount");
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
