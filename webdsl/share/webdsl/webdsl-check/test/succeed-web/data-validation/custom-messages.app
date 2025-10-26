application messages

  entity User{
    name :: String
  }
  var u0 : User := User{ name := "bob" };
  var u1 : User := User{ name := "alice" };
  
  define page root(){
    for(u:User) {
      output(u.name)
    }
    
    navigate(somepage()){"link"}
    
    form {
      input(u0.name)
      action("save",save())
    }
    action save() {
      u0.save();
      message("1: username: "+u0.name);
      message("2: username: "+u0.name);

      return somepage();
    }
  }
  
  define page somepage(){
    "somepagefragment"  
    
    messages()
  }
  
  define override templateSuccess(messages : List<String>){
    a(messages)
  }
  
  define a(m : List<String>){
    b{
      for(v: String in m){
        output(v)   
      } separated-by {", "}	
    }
  }
  
  define b(){
    elements()
  }

  
  test {
    var d : WebDriver := getHtmlUnitDriver();
    
    d.get(navigate(root()));
    
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("input"));
    assert(elist.length == 2, "expected 2 <input> element");
    
    elist[1].sendKeys("blabla");
    
    d.getSubmit().click();
    
    //check that messages are produced below "somepage fragment"  
    
    var pagesource := d.getPageSource();

    var list := /somepagefragment/.split(pagesource);
    
    assert(list.length == 2, "expected one occurence of \"somepagefragment\"");
    
    assert(list[1].contains("1: username: bobblabla"), "cannot find first message");
    assert(list[1].contains("2: username: bobblabla"), "cannot find second message");
  }
  