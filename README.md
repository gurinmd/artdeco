# artdeco

## About Artdeco
Art Deco  is a style of visual arts, architecture and design that first appeared in France just before World War I.[1] It became popular in the 1920s and 1930s, and influenced the design of buildings, furniture, jewellery, fashion, cars, movie theatres, trains, ocean liners, and everyday objects such as radios and vacuum cleaners.

Artdeco is a framework, which provides a new way of using Decorator Pattern in Java. To implement Decorator, we usually have to override all methods of the decorating object. Today, we'd like to provide you a new, lightweight approach. 

## Exmaple to understand

Let's imagine having a JMS-based application. And we'd like to test it reliability using the following testcase:
-Application runs as usual
-Method javax.jms.ConnectionFactory#createConnection throws JMSException with 0.05% probability. 

### Old-fashioned solution
We can create a decorator for ConnectionFactory, overriding createConnection method:
```java
  public class TestConnectionFactory implements ConnectionFactory {
      private ConnectionFactory factory;
      private double failProbability = 0.05;
      ...
      private Connection createConnection() throws JMSException {
          if (Math.random() <= failProbability){
              throw new JMSException();
          } else {
              return factory.createConnection();
          }
      }
  }
 ```
The main disadvantage is that you must override ALL methods of ConnectionFactory interface in TestConnectionFactory. It can be a little bit tiring:
 ```java
  public XXX doStuff(){
    return factory.doStuff();
  }
   ```
### Artdeco solution
```java
// creating builder for decoractor for ConnectionFactory class
DecoratorBuilder builder = DecoratorBuilder.forInterface(ConnectionFactory.class);

//object to redirect method calls from decorator
builder.setTargetObject(factory);

builder.addDecoratingMethodInstance(new Object(){
  
  private double failProbability = 0.05;
  
  @TargetObject
  private ConnectionFactory factory; // <---- factory object will be injected there, using @TargetobjectAnnotation
  
  @Decorates // <---- this method will be called instead of factory.createConnection(). And its signature IS IDENTICAL to javax.jms.ConnectionFactory#createConnection
  public Connection createConnection() throws JMSException{  
     if (Math.random() <= failProbability){
              throw new JMSException();
          } else {
              return factory.createConnection();
          }
  }
}
);

ConnectionFactory decoratedConnectionFactory = builder.buildDecorator(); // <--- TA DAAM! Connection Factory, which will throw        JMSException on createConnection with 0.05% probability/

 ```

   
  
