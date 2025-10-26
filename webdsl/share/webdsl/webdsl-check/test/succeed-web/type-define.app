application test

  entity Test {
    prop :: String
    prop1 :: Text
  }
  
  var t_1 := Test{ prop := "werwrwergdgdgrgrg" }  
  
  define page root(){
    output(t_1.prop)
    output(t_1.prop1)
    form{
      input(t_1.prop)
      input(t_1.prop1)
      action("save",action{})
    }
  }

  type String { 
    validate(this.length() >= 10 , "input too short (minimum is 10 characters)") 
  }

  test {
    var d : WebDriver := getFirefoxDriver();
    d.get(navigate(root()));
    
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("input"));
    assert(elist.length == 2, "expected <input> elements did not match");
    d.getSubmit().click();
    assert(!d.getPageSource().contains("input too short (minimum is 10 characters)"), "should not show error message for text input");

    var elist1 : List<WebElement> := d.findElements(SelectBy.tagName("input"));
    assert(elist1.length == 2, "expected <input> elements did not match");
    elist1[1].clear();
    elist1[1].sendKeys("123");
    d.getSubmit().click();
    assert(d.getPageSource().contains("input too short (minimum is 10 characters)"), "should show error message for string input");
  }
  

  