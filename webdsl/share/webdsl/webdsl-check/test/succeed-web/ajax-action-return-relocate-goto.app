application test

  entity User {
    password :: Secret
  }

  define main(){
    elements
  }

  define page testinit1(){
    init{
      goto red();
    }
  }
  define page testinit2(){
    init{
      return red();
    }
  }

  define page root() {
    main {
      submit action{replace(reg,reg());} [ajax] { "replace form with ajax" }
      header { "Form:" }
      placeholder reg{ reg() }
    }
    " -> "
    navigate testinit1() {"testinit1"}
    " -> "
    navigate testinit2() {"testinit2"}
  }
  
  define page red(){
    "redirectedpage"
  }

  define ajax reg(){
      action a1() {
        return red();
      }
      action a2() {
        relocate(red());
      }
      action a3() {
        goto red();
      }
      form {
        action("a1", a1())
        action("a2", a2())
        action("a3", a3())
      }
  }
  
  test redirecting {
    var d : WebDriver := getFirefoxDriver();
    
    for(i:Int from 0 to 1){
      checkInit(d,i);
    }
    
    for(i:Int from 2 to 4){
      checkButton(d,i);    	
      checkButtonAfterReplace(d,i);    	
    }
  }
  
  function checkInit(d:WebDriver, number :Int){
    d.get(navigate(root()));
    var alist : List<WebElement> := d.findElements(SelectBy.tagName("a"));
    assert(alist.length == 2, "expected 2 <a> elements");
    
    alist[number].click();
    
    assert(d.getPageSource().contains("redirectedpage"), "should have been redirected from navigation link "+(number+1));
  }
  
  function checkButton(d:WebDriver, number :Int){
    d.get(navigate(root()));
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("button"));
    assert(elist.length == 4, "expected 4 <button> elements");
    
    elist[number].click();
    
    assert(d.getPageSource().contains("redirectedpage"), "should have been redirected from button "+(number-1));
  }
  
  function checkButtonAfterReplace(d:WebDriver, number :Int){
    d.get(navigate(root()));
        
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("button"));
    assert(elist.length == 4, "expected 4 <button> elements");
    elist[0].click();

    var elist1 : List<WebElement> := d.findElements(SelectBy.tagName("button"));
    assert(elist1.length == 4, "expected 4 <button> elements");
    
    elist1[number].click();
    
    assert(d.getPageSource().contains("redirectedpage"), "should have been redirected from button "+(number-1)+" after replace");
  }