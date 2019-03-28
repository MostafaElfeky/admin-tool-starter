# admin-tolol-starter



# Start Up project Modules

 1. [Authentication ](#authentication )
 2. [Exceptions ](#exceptions)
 3. [Logging ](#logging)
 4. [Action Log ](#action-log)
 5. [File Uploader ](#file-uploader)

# Authentication

 - As an admin tool we provide basic authentication with a method based authentication system based on spring security 
 - please check https://www.baeldung.com/spring-security-method-security
 - Spring security configuration include

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   ...
}
```

 - `WebSecurityConfig` in `com.gn4me.app.config` Is responsible for 
    
- Define password encryption algorithm. 
    `@Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(12);
   }`
- Ignore static resources 
    `web.ignoring().antMatchers("/resources/**", "/file/**");`
- Define login, logout resources
- Handle remember me functionality
- Register User details service  

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
   http
      .authorizeRequests()
      .anyRequest().authenticated()
   .and()
      .formLogin()
      .loginPage("/login")
      .permitAll()
   .and()
      .logout().logoutUrl("/logout")
      .deleteCookies("JSESSIONID").logoutSuccessUrl("/")
      .invalidateHttpSession(true)
   .and()
      .rememberMe()
      .key("uniqueAndSecret")
      .userDetailsService(appDetailsService)
   .and()
      .csrf().disable();

}
```

- `SpringSecurityInitializer` in `com.gn4me.app.config` this simple class just responsible for register spring security filter chain.
- `AppUserPrincipal implements UserDetails` in `com.gn4me.app.config.security` this check user status and refactor privilege  to `GrantedAuthority`
- `AppDetailsService implements UserDetailsService` his define how user loaded from database,
 - Basic security  include these functionality `login` `logout` `add user` `edit user roles` `edit role privilege` `delete user` `view user` `delete user` `find user by id`


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

> We used builder design pattern for create action log to simplify the usability of logging represented in 
> `ActionLogBuilder` `DBActionLogBuilder`
> Listing done on the original way and try to identify entity type to build message text based on message pattern from operation and list of required parameters.


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

# File Uploader

### Define system props.
 - You have to define your uploader  as local or remote file system
`file.uploader = SYSTEM`
Expected Values [SYSTEM, AMAZON, AZURE]
- Define upload and download paths as
`file.root-path.upload = E:/New Environment/files`
`file.root-path.download = http://localhost:8080/files`
- Supported types
 `file.supported-types = [jpeg][jpg][png][ico][svg][doc][xml]`
- Max overall file size  [NUMBER] in KB
 `file.size.max = 5024`

### Design database
![enter image description here](docs!images!actionLog.png)
 - Init your database by creating file_module table which categorize your files into modules and build custom configuration for each module including sizes and compression level
 - create table per module with name file_[module code] 
 - check database script file which include tables creation ddl.

### Upload file 

 - File uploader provide two basic end points which make you upload single and multiple files.
    - `URI/file/` which take parameters `file` `module` `tags`
    - `URI/file/multiple` which take parameters `files` `module` `tags`
    - check Rest documentation for further details.
- depend on your configuration files will be saved on `config path` /  `module name` / `file type` / `size type if Image`
- You will received response including all required info 

> {
       "info": {
           "id": 247,
           "name": "Tulips",
           "extension": "jpg",
           "tags": "",
           "type": "IMAGE",
           "fileModuleId": 1,
           "contentId": 0,
           "thumbnail": "https://demo.gn4me.net/funny/content/image/thumbnail/8c6a659beb0e44e89a75ba8d34d7baf7.jpg",
           "files": {
               "default": "https://demo.gn4me.net/funny/content/image/l/8c6a659beb0e44e89a75ba8d34d7baf7.jpg",
               "400_300": "https://demo.gn4me.net/funny/content/image/400_300/8c6a659beb0e44e89a75ba8d34d7baf7.jpg"
           },
           "userId": 0
       },
       "status": {
           "code": 200,
           "message": "Success"
       }
    }

 - single file module you have to save file ID into your current table.
 - multi file module call API `updateFilesOwner` method which present in `FileUtilS` to update your owner id for all your uploaded files.

### Retrieve File info
 - As you successfully upload your file and finally you want to retrieve your file info depend on ID for single module or retrieve files depend on entity ID for multi file module.
 - Single file just call API `getDownloadFileInfo` to retrieve all file info
 - Multi file join to your module file table, build `AppFile` from result set response, call API overloaded `getDownloadFileInfo` to refactor your `AppFile` to File Info.
