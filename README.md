# admin-tolol-starter


# Exceptions

### Throwing Exception: 

 - Throw exception only on dao layer

 - Throw AppException with operation, status and actual exception.
`throw new AppException("opration will be here ", ResponseCode.GENERAL_FAILURE, exp);`

 -   Exception can be handled on service layer specifically on logic classes not all service layer
   


### Handling Exceptions: 

 - System have Two Exception handler (`AppExceptionHandler`,
   `GeneralExceptionHandler`).
 - AppExceptionHandler handle  AppException and will known exceptions and has HIGHEST_PRECEDENCE 
 - GeneralExceptionHandler handle only "Exception" and has LOWEST_PRECEDENCE
 - Every method should provide response including httpStatus which will be taken from ResponseCode or per Exception Type.
     

# Logging

 - System have two way action logging [manual logging, auto logging]
 - Auto logging done by LoggingAspect class which will work by Spring
   AOP Only you should annotated your class with

`@Loggable(Type = Type.SERVICE)`
 
 - Manual logging include exceptions and any critical behavior which
   should be logged
 
 - Manual logging should done by defining the logging level and use our
   helper format to follow the standard format.

` 
Logging.format("Logging info will be here and data will appended to it like user " + user, transition));
`
 - Exceptions will be logged only on Exception handler.

# Action Log
### Design and consideration:

 1. **Database design**

 2. **Code design**

> We used builder design pattern for create action log to simplify the usability of logging .
> listing done on the original way and try to identify entity type to build message text based on message pattern from operation and list of required parameters.


### How to use:

 - First you have to define your own entity type in `EntityType` and your operation in  `LogOpration` with alternatives into database lookup tables.
 - Builder has a log() method which build an `ActionLog` Object and automatically log it into database.

> Example

```java 
ActionLogBuilder.builder()
   .withUser(LoggedInUser.getLoggedInUser())
   .withEntityId(sponsor.getId())
   .withEntityType(EntityType.SPONSOR)
   .withLogOpration(LogOpration.SPONSOR_ADD)
   .withTransition(transition)
   .log();
```

