# Getting Started

## Basic Usage

- run application
- go to [Swagger](http://localhost:8080/swagger-ui.html)
- try the api /publishAddPerson and /publishAddOrder

## Basic Problem

- Any new model and event will be created a set of model/event, event dispatching function and event listener function
- This will go to too large number of classes when project grows

## Improvement 1

- use generic type to reduce number of classes
- See BaseEvent.java
- See NewTestController.java
- See new method in EventListenerService.java
- ```java
    @EventListener
    public void handleEvent(BaseEvent<?> baseEvent) {
        System.out.println("Get BaseEvent: " + baseEvent);
        Object data = baseEvent.data();
        if (data instanceof Person) {
            System.out.println("Extracted PersonEvent: " + data);
        } else if (data instanceof Order) {
            System.out.println("Extracted OrderEvent: " + data);
        }

    }

   ```

## Improvement 2
- this is not enough as all logic of events handling are in one method
- trying to seperate with
- ```java

    @EventListener
    public void handlePersonEvent(BaseEvent<Person> personEvent) {
        System.out.println("Get PersonEvent: " + personEvent);
    }

    @EventListener
    public void handleOrderEvent(BaseEvent<Order> orderEvent) {
        System.out.println("Get OrderEvent: " + orderEvent);
    }
   ```

- but this will not work as spring will only know the event is BaseEvent<?> and not able to dispatch to the correct
  method

## Improvement 3
- will need to use interface `ResolvableTypeProvider` to provide the type information
- change the `BaseEvent.java` to
- ```java
    public record BaseEvent<T>(T data, String addOrUpdate) implements ResolvableTypeProvider {
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(data));
    }
   }
   ```
- this will provide the type information to spring


## Reference and Reading
 - https://zhuanlan.zhihu.com/p/677880235
 - https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events-generics