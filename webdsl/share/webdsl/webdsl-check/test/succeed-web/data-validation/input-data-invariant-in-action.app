application inputint

  entity Y {
    i -> Set<Y>
    
    validate(i.length >= 2, "select at least 2")
  }

  var y0  := Y{}
  var y1  := Y{}
  var y2  := Y{}
  var y3  := Y{}

  define page root(){
    form{
      select(y0.i,(from Y as qy where qy <> ~y0))
      submit action{} {"save"}
    }
 
    for(bla: Y){
      output(bla.i)
    } separated-by{ <br/> }
  }  
  
  test datavalidation {
    var d : WebDriver := getFirefoxDriver();
    
    d.get(navigate(root()));
    
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("option"));
    assert(elist.length == 3, "expected 3 <option> elements");
    
    elist[1].setSelected();

    var ilist : List<WebElement> := d.findElements(SelectBy.tagName("input"));
    assert(ilist.length == 2, "expected 2 <input> elements");

    d.getSubmit().click();
    
    var pagesource := d.getPageSource();

    assert(pagesource.contains("select at least 2"));
  }
