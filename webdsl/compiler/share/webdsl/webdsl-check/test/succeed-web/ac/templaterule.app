application test

section principal

  entity User{
    name :: String
  }

  principal is User with credentials name
  
  access control rules
    rule template bla(){
      1==1
    }
    rule page root() {
      true
    }
    rule template tab(*){
      55==55
    }
    rule template hide(){
      fls()
    }
  
    predicate fls(){
      "sfdfds" == "34erfg"
    }
section somesection  
  
  define bla(){
    "tester"
  }
  
  define hide(){
    "this should not be visible"
  }
  
  define page root(){
    bla()
    tab("1","2","3")
    hide
    action save()
    {
      1==1;
    }
    usedefaultrulefortemplates()
  }
  
  define tab(s:String,s1:String,s2:String){
    "tab template: " 
    output(s)
    output(s1)
    output(s2)
  }
  
  define usedefaultrulefortemplates(){
    "default rule opens this template"
  }
  
  test {
    var d : WebDriver := getHtmlUnitDriver();
    
    d.get(navigate(root()));
    assert(d.getPageSource().contains("tab template: 123"), "\"tab template: 123\" should be visible");
    assert(!d.getPageSource().contains("this should not be visible"), "\"this should not be visible\" is visible");
    assert(d.getPageSource().contains("default rule opens this template"));
  }