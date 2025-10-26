application registerexample

  entity User {
    password :: Secret
  }

  define main(){
    elements
  }

  define page root() {
    var u : User := User{};
    var p : Secret;
    action register() {
      validate(p==u.password,"passwords don't match action");
      u.password := u.password.digest();
      u.save();
      message("You have successfully registered. Sign in now.");
    }

    main {
   
      header { "Register" }
      form {
        label("Password:") { input(u.password) }
        label("Verify password:") { 
          input(p){validate(p==u.password,"passwords don't match inputcheck")}
          <hr/> 
        }
        <hr/>
        validate(p==u.password,"passwords don't match formcheck")
        action("Register", register())

      }
    }
  }

  test datavalidation {
    var d : WebDriver := getFirefoxDriver();
    
    d.get(navigate(root()));
    
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("input"));
    assert(elist.length == 3, "expected 3 <input> elements");
    
    elist[1].sendKeys("123");
    elist[2].sendKeys("111");
    
    d.getSubmit().click();
    
    var pagesource := d.getPageSource();
    var list := pagesource.split("<hr");
    
    assert(list.length == 3, "expected two occurences of \"<hr\"");
 
    assert(list[1].contains("inputcheck"), "cannot find inputcheck message");
    assert(list[2].contains("formcheck"), "cannot find formcheck message");
  }
