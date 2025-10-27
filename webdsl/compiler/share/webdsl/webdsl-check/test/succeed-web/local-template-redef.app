application test

  entity Ent {
    int :: Int
  }

  define page root() {
    var i := 0 	
    form{
      b()
      submit action{ Ent{  int := i }.save(); } {"save"}
    }
    for(e:Ent){
      output(e.int)
    }
    
    define span b(){
      "correct" 
      input(i)
    }
    c
    wrapping{ "" }
    template c(){
    	"OK"
    }
    
    //define b() = root_b(*,i)
  }

  define span b(){ "error" }
  
  template c(){ div[class="error"]{ }/*inlined*/ }
  // template c(){ var now := now(); output( getTemplate().getUniqueId() + now) /*not inlined*/ }
  
  template wrapping(){
  	c
  }
  // template wrapping2(){
  // 	c
  // }
  test var {
    var d : WebDriver := getFirefoxDriver();
    d.get(navigate(root()));
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("input"));
    assert(elist.length == 2, "expected 2 <input> elements did not match");
    assert(d.findElements(SelectBy.className("b")).length > 0, "span class of local redefined template b should be 'b'");
    elist[1].sendKeys("23456789");
    d.getSubmit().click();
    assert(d.getPageSource().contains("23456789"), "entered data not found");
    assert(d.getPageSource().contains("OKOK"), "Global template def used instead of local redefined one, regression test for http://yellowgrass.org/issue/WebDSL/804");
  }
  

  
  /**
   *  Following fragments are not actively tested but caused compilation errors in previous versions of WebDSL
   */
  
  //triggered compilation error
  entity Page {}
  define localBody(){"default localBody"}
  define page bla(p:Page){
    define localBody(){
        navigate(bla(p)){"Full page"}
    }
  }
  
  
  //triggered compilation error
  define page editMenu(){
    var s := 34
    define localBody(){
     var e := Ent{int := s}
    }
  }
  
  
  //triggered compilation error
  define page communications(tab : String) {
    localBody()
    define localBody() {
      case(tab) {
        "inbox"   { "inbox" }
        "drafts"  { "drafts" }
      }
    }
  }
  
  
  //triggered compilation error
  entity Publication{
    cites -> Set<Publication>
  }
  define page referencesfrombibliography(pub : Publication) {
    define localBody() {
      action add(r:Publication) {
        pub.cites.add(r);
      }
      form{ action("Add as reference", add(pub)) }
    }
  }

  
  define body(){}


  //was not being inlined
  define page editinvitationtemplate() {
    define body() {
      form{
        submit action { } {"Save"}
      }
    }
  }
  
  
  //triggered compilation error
  define page confirmRegistration(/*conf : RegistrationEmailConfirmation*/) {
    var password : Secret
    define body() {
      form{ 
        input(password)
        action("Confirm", action{ 
        })
      }
    }
  }

