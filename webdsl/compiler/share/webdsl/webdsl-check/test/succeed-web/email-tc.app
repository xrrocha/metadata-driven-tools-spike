application test

section datamodel

  define email testemail(us : User) {
    to(us.mail)
    from("webdslorg@gmail.com")
    subject("Email confirmation")
    par{ output(us) }
    par{
     "showpar2"
    }
  }

  entity User {
    name :: String
    mail :: Email
    emailstring :: String
  }

  define output(u:User){
    "outputuser1"
  }

  var global_u : User := User {
    name := "bob"  
    mail := "webdslorg@gmail.com"
  };
  
  define page root() {
    output(global_u.name)
    output(global_u.mail)
    
    form {
      action("email",send())[class="savebutton"]
    }
    action send() {
      global_u.emailstring := renderemail(testemail(global_u)).body;
      email(testemail(global_u));
    }
    output(global_u.emailstring)
  }
  
  test {
    var d : WebDriver := getFirefoxDriver();
    d.get(navigate(root()));
    var button := d.findElements(SelectBy.className("savebutton"))[0];
    button.click();
    assert(d.getPageSource().contains("outputuser1"));
    assert(d.getPageSource().contains("showpar2"));
  }
