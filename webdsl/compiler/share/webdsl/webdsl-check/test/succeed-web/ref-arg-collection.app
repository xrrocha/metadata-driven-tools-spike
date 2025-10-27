application test

  entity Str{
    name::String 
    set->Set<Str>
  }
  
  var globalstr1 := Str{ name := "1" }
  var globalstr2 := Str{ name := "2" }

  define page root(){

    var a : Set<Str> := Set<Str>()
    form{
      bla(a)
      submit action{ var n : Str := Str{ set := a }; n.save();} { "save" }
    }
    for(str:Str){
      output(str) ": " output(str.set)
    } separated-by{ <br /> }
  }
  
  define bla(a:Ref<Set<Str>>){ 
    select(a,from Str)
  }

  define page root2(){
    var a : Set<Str> := Set<Str>()
    form{
      test(a)
      submit action{ var n : Str := Str{ set := a }; n.save();} { "save" }
    }
    for(str:Str){
      output(str) ": " output(str.set)
    } separated-by{ <br /> }
  }
  define test(t:Ref<Set<Str>>){ 
    bla(t)
  }
  
  test {
    var d : WebDriver := getFirefoxDriver();

    d.get(navigate(root()));
    log(d.getPageSource());
    checkpage(d);    

    d.get(navigate(root2()));
    checkpage(d);    
  }
  
  function checkpage(d:WebDriver){
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("option"));
    elist[0].setSelected();
    d.getSubmit().click();
    //log(d.getPageSource());
    //log(d.getPageSource());
    assert(d.getPageSource().contains("<li class=\"block\">2</li>"), "reference arguments not working as expected");
  }
  