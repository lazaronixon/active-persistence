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

repositories/UserRepository.java
```java
@RequestScoped
public class UserRepository extends Base<User> {

}
```

## CRUD: Reading and Writing Data

### Create
```java
User user = new User();
user.name = "David";
user.occupation = "Code Artist";

userRepository.save(user);
```

### Read
```java
// return a collection with all users
List<User> users = userRepository.all();

// return the first user
User user = userRepository.first();

// return the first user named David
User david = userRepository.findBy("user.name = ?", "David");

// find all users named David who are Code Artists and sort by createdAt in reverse chronological order
List<User> users = userRepository.where("user.name = 'David' AND user.occupation = 'Code Artist'").order("user.createdAt DESC");
```

### Update
```java
User user = userRepository.findBy("user.name = ?", "David");
user.name = "Dave";
userRepository.save(user);
//OR
userRepository.updateAll("user.maxLoginAttempts = 3, user.mustChangePassword = 'true'");
```

### Delete
```java
User user = userRepository.findBy("user.name = ?", "David");
userRepository.destroy(user);
//OR
userRepository.destroyBy("user.name = ?", "David");
userRepository.destroyAll();
```

### Callbacks

```java
public class ClientRepository extends Base<Client> {

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
Client client = clientRepository.find(10);

// The take method retrieves a record without any implicit ordering
Client client = clientRepository.take();
List<Client> clients = clientRepository.take(2);

// The first method finds the first record ordered by primary key (default)
Client client = clientRepository.first();
Client client = clientRepository.order("client.firstName").first();
List<Client> clients = clientRepository.first(3);

// The last method finds the last record ordered by primary key (default)
Client client = clientRepository.last();
Client client = clientRepository.order("client.firstName").last();
List<Client> clients = clientRepository.last(3);

// The findBy method finds the first record matching some conditions
Client client = clientRepository.findBy("client.firstName = ?", "Lifo"); // #<Client id: 1, firstName: "Lifo">
Client client = clientRepository.findBy("client.firstName = ?", "Jon");  // null

Client client = clientRepository.findBy$("client.firstName = ?", "does not exist"); // EntityNotFoundException
```

### Conditions
```java
//Ordinal Conditions
clientRepository.where("client.ordersCount = ?", 10);
clientRepository.where("client.ordersCount = ? AND client.locked = ?", 10, false);

//Placeholder Conditions
clientRepository.where("client.ordersCount = :count", Map.of("count", 10));
clientRepository.where("client.ordersCount = :count AND client.locked = :locked", Map.of("count", 10, "locked", false));

//SubQuery Conditions
var subquery = orderRepository.select("order.client.id");
clientRepository.where("client.id IN (?), subquery);
```

### Ordering
```java
clientRepository.order("client.createdAt");
clientRepository.order("client.createdAt DESC");
clientRepository.order("client.createdAt ASC");
```

### Selecting Specific Fields
```java
List<Client> client = clientRepository.select("client.viewableBy", "client.locked");
List<Client> client = clientRepository.select("client.name").distinct();
```

### Limit and Offset
```java
clientRepository.limit(5);
clientRepository.limit(5).offset(30);
```

### Group
```java
List<Order> orders = orderRepository.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)");
```

### Total of grouped items
```java
Map<String, Long> result = (Map) orderRepository.group("order.status").count(); // => { 'awaiting_approval' => 7, 'paid' => 12 }
```

### Having
```java
List<Order> orders = orderRepository.select("date(order.createdAt)", "sum(order.price)").group("date(order.createdAt)").having("sum(order.price) > 100");
```

## Overriding Conditions

### Unscope
```java
orderRepository.where("order.id > 10").limit(20).order("order.id asc").unscope(ORDER);
```

### Only
```java
orderRepository.where("order.id > 10").limit(20).order("order.id asc").only(ORDER);
```

### Reselect
```java
postRepository.select("post.title", "post.body").reselect("post.createdAt");
```

### Reorder
```java
postRepository.order("post.title").reorder("post.createdAt");
```

### Rewhere
```java
articleRepository.where("article.trashed = true").rewhere("article.trashed = false");
```

## Null Relation
```java
studentRepository.none(); // returns an empty Relation and fires where 1=0.
```

## Locking Records for Update
```java
Client client = clientRepository.lock().first();
// OR
Client client = clientRepository.lock(true).first();
// OR
Client client = clientRepository.lock(PESSIMISTIC_WRITE).first();
```

## Readonly Objects
```java
Client client = clientRepository.readonly().first();
client.visits = 1;

clientRepository.save(); // ReadOnlyRecord Exception
```

## Joining Tables
```java
authorRepository.joins("JOIN author.post post");
```

## Eager Loading Associations (EclipseLink only)
```java
clientRepository.includes("client.address").limit(10);
clientRepository.eagerLoads("client.address").limit(10);
```

## Scopes

### Applying a default scope
```java
public class ClientRepository extends Base<Client> {

    @Override
    public Relation<Client> defaultScope() {
        return where("client.name = 'nixon'");
    }

}

clientRepository.all(); // SELECT client FROM Client client WHERE client.name = 'nixon'
clientRepository.unscoped().all(); // SELECT client FROM Client client
```

### Merging of scopes
```java
userRepository.merge(userRepository.active());
```

### Removing All Scoping
```java
clientRepository.unscoped();
clientRepository.where("client.published = false").unscoped();
```

## Dynamic Finders
```java
Client client = clientRepository.findByExpression("Name", "Nixon");
Client client = clientRepository.findByExpression("NameAndLocked", "Nixon", true);
// OR
Client client = clientRepository.findByExpression$("Name", "not found"); // EntityNotFoundException
```

## Finding by SQL/JPQL
```java
List<Post> posts = postRepository.findBySql("SELECT id, title FROM Post WHERE id = 5").getResultList();
List<Post> posts = postRepository.findByJpql("SELECT p FROM Post p WHERE p.id = 5").getResultList();
// OR
List posts = postRepository.getConnection().selectAll("SELECT id, title FROM Post WHERE id = 5", QueryType.SQL).getResultList();
List posts = postRepository.getConnection().selectAll("SELECT p FROM Post p WHERE p.id = 5", QueryType.JPQL).getResultList();
```

## Existence of Objects
```java
boolean exists = studentRepository.exists("student.name = 'Lifo'");
boolean exists = studentRepository.where("student.name = 'Lifo'").exists();
```

## Pluck
```java
List<Integer> ids = clientRepository.where("client.active = true").pluck("client.id"); //[1, 2, 3]
List<Integer> ids = clientRepository.where("client.active = true").ids; //[1, 2, 3]

```

## Calculations
```java
long   count   = (long)   clientRepository.count();
long   count   = (long)   clientRepository.count("client.age");
int    minimum = (int)    clientRepository.minimum("client.age");
int    maximum = (int)    clientRepository.maximum("client.age");
long   total   = (long)   clientRepository.sum("client.ordersCount");
double average = (double) clientRepository.average("client.ordersCount");
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
