# Custom Annotations in Pure Java (End-to-End)

## Overview

Custom annotations in pure Java are **metadata**. By themselves, they do
nothing---you must use **reflection** (or annotation processing) to read
them and implement behavior.

``` text
Annotation
    │
    ▼
Compiler stores metadata
    │
    ▼
Reflection reads metadata
    │
    ▼
Your code decides what to do
```

## 1. Create an Annotation

``` java
public @interface MyAnnotation {
}
```

Usage:

``` java
@MyAnnotation
class Test {
}
```

## 2. Add Elements

``` java
public @interface Author {
    String name();
    int age();
}
```

Usage:

``` java
@Author(name = "John", age = 35)
class Employee {
}
```

## 3. Default Values

``` java
public @interface Author {
    String name();
    int age() default 20;
}
```

Usage:

``` java
@Author(name = "Alex")
class Test {
}
```

## 4. Special `value()` Element

``` java
public @interface Table {
    String value();
}
```

Usage:

``` java
@Table("users")
class User {
}
```

## 5. Multiple Elements

``` java
public @interface Column {
    String name();
    boolean nullable() default true;
    int length() default 255;
}
```

Usage:

``` java
@Column(name = "username", nullable = false, length = 50)
private String username;
```

# Meta-Annotations

## `@Target`

Controls where an annotation can be used.

Common targets:

-   TYPE
-   FIELD
-   METHOD
-   PARAMETER
-   CONSTRUCTOR
-   LOCAL_VARIABLE
-   ANNOTATION_TYPE
-   PACKAGE
-   TYPE_PARAMETER
-   TYPE_USE

Example:

``` java
@Target(ElementType.FIELD)
public @interface Column {}
```

## `@Retention`

-   `SOURCE` -- removed after compilation.
-   `CLASS` -- stored in `.class`, unavailable at runtime.
-   `RUNTIME` -- available through reflection.

``` java
@Retention(RetentionPolicy.RUNTIME)
```

## `@Inherited`

Allows subclasses to inherit a class annotation.

## `@Documented`

Includes the annotation in generated Javadocs.

## `@Repeatable`

Allows multiple instances of the same annotation.

## Complete Annotation

``` java
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    String name();

    boolean nullable() default true;

    int length() default 255;
}
```

## Reading Annotations with Reflection

Class:

``` java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Author {
    String name();
}
```

``` java
@Author(name = "John")
class Book {}
```

``` java
Author author = Book.class.getAnnotation(Author.class);
System.out.println(author.name());
```

Output:

    John

### Reading a Field Annotation

``` java
Field field = User.class.getDeclaredField("username");
Column column = field.getAnnotation(Column.class);
System.out.println(column.name());
```

### Reading All Fields

``` java
for (Field field : User.class.getDeclaredFields()) {
    if (field.isAnnotationPresent(Column.class)) {
        Column c = field.getAnnotation(Column.class);
        System.out.println(c.name());
    }
}
```

### Reading Method Annotations

``` java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Execute {}
```

``` java
class Test {

    @Execute
    public void hello() {
        System.out.println("Hello");
    }
}
```

``` java
Method[] methods = Test.class.getDeclaredMethods();

Test obj = new Test();

for (Method method : methods) {
    if (method.isAnnotationPresent(Execute.class)) {
        method.invoke(obj);
    }
}
```

## End-to-End Example: Tiny DI Container

### `@Inject`

``` java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {}
```

### Service

``` java
class EmailService {

    public void send() {
        System.out.println("Email Sent");
    }
}
```

### Client

``` java
class UserService {

    @Inject
    private EmailService emailService;

    public void register() {
        emailService.send();
    }
}
```

### Container

``` java
import java.lang.reflect.Field;

public class Container {

    public static <T> T create(Class<T> clazz) throws Exception {

        T object = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {

            if (field.isAnnotationPresent(Inject.class)) {

                Object dependency =
                        field.getType()
                             .getDeclaredConstructor()
                             .newInstance();

                field.setAccessible(true);
                field.set(object, dependency);
            }
        }

        return object;
    }
}
```

### Main

``` java
public class Main {

    public static void main(String[] args) throws Exception {

        UserService service = Container.create(UserService.class);

        service.register();
    }
}
```

Output:

    Email Sent

## Common Interview Questions

1.  Why is `RetentionPolicy.RUNTIME` required for reflection?
2.  Difference between `getAnnotation()`, `getAnnotations()`, and
    `isAnnotationPresent()`?
3.  Why are annotations passive metadata?
4.  Why use `@Target`?
5.  How does `@Repeatable` work?
6.  How does Spring discover annotated classes?
7.  Why do features like `@Transactional` require proxies in addition to
    reflection?