application test

  entity Bla {
    val :: String
  }
  
  var b1 := Bla { val := "oldvalue" }

  define page root(){
    "root page"
    action red(){
      return success();
    }    
    form{
      input(b1.val)
    }
    submit("save",red())
  }
  
  define page success(){
    output(b1.val)
    "redirected to success page"
  }
  
  test {
    var d : WebDriver := getFirefoxDriver();
    
    //root first submit button
    d.get(navigate(root()));
    assert(d.getPageSource().contains("root page"), "expected to be on root page");
    
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("input"));
    assert(elist.length == 2, "expected <input> elements did not match");
    elist[1].sendKeys("new stuff");
    d.getSubmit().click();
    
    assert(d.getPageSource().contains("redirected to success page"), "should have been redirected");
    assert(!d.getPageSource().contains("new stuff"), "input shouldnt have been included in action data binding");
  }
  

  