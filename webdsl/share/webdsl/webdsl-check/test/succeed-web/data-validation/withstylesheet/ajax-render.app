application registerexample

  entity Competition{
  }
  function createCompetition():Competition{ validate(false, "error"); return Competition{}; }
  
  define applicationmenu() {
    <div id="navbar">
    <ul>
      <li>form{ actionLink("Randomize", randomCompetition()) }</li>
    </ul>
    </div>
    action randomCompetition() {
      var c: Competition := createCompetition();
      return root();
    }
  }
  
  define page root(){
    csscontent
    applicationmenu()
  }

  test datavalidation {
    var d : WebDriver := getFirefoxDriver();
    
    d.get(navigate(root()));
    
    var elist : List<WebElement> := d.findElements(SelectBy.tagName("a"));
    assert(elist.length == 1, "expected 1 <a> element");
    
    elist[0].click();
    
    var b : List<WebElement> := d.findElements(SelectBy.tagName("body"));
    assert(b.length == 1, "<body> element gone");
  }
  
  template csscontent(){
<style>

body {
    margin: 0;
    padding: 0;
    font-family: Arial, Helvetica, sans-serif;
}
img {
  border-width   : 0px;
  background-color:white;
}
#pagewrapper {
    width: 720px;
    margin: 10px auto;
    padding: 0;
    border: 1px solid #999;
    font-family: Arial, Helvetica, sans-serif;
}
#content {
    padding: 20px 10px;
}
#header {
    background-color: white;
    margin: 0;
    padding: 0;
}
#header p {
    padding: 20px 10px;
    font-size: 24px;
    margin: 0;
    color: white;
    font-weight: bold;
  font-family: Arial, Helvetica, sans-serif;
}
#header img {
	width: 100%;
}
#footer {
    background-color: #999;
    padding-top: 6px;
    padding-bottom: 6px;
    clear:both;
    border-top: 2px solid #333;
    text-align: center;
}
#footer p, #footer a {
    text-align: center;
    font-size: 12px;
    color: white;
    font-weight:bold;
    margin: 0;
    padding: 2px;
}

/* Navigation Bar */

#navbar ul {
    padding: 0;
    margin: 0;
    background-color: #999;
    color: White;
    float: left;
    width: 100%;
    list-style: none;
    font-weight: bold;
    font-size: 13px;
    border-bottom: 2px solid #333;
}
#navbar ul li { 
    display: inline;
    color: White;
    float: left;
    border-right: 1px solid #333;
    font-family: arial, helvetica, sans-serif;
}
#navbar ul li.loggedin { 
    display: inline;
    padding: 0.2em 0.7em;
    color: Black;
    float: right;
    border-left: 1px solid #333;
    border-right: none;
    font-family: arial, helvetica, sans-serif;
}
#navbar ul li a {
    padding: 0.2em 0.7em;
    background-color: #999; 
    color: White;
    text-decoration: none;
    float:left;
}
#navbar ul li a:hover {
    background-color: #333;
    color: #fff;
}

form { 
  display : inline;
}

#usertable td { 
  padding : 2px 5px 2px 0px;
}

#usertable tr { 
  border-bottom : 1px solid #333;
}
#tasktable td { 
  padding : 2px 5px 2px 0px;
}

#tasktable tr { 
  border-bottom : 1px solid #333;
}

textarea { 
  height : 10em;
  width : 25em;
}
select {
  width : 20em;
}
input[type="text"]{
  width : 20em;
}
input[type="file"]{
  width : 20em;
}
h1 {
	font-size: 18px;
}
tr.theader {
	font-weight: bold;
	background-color: #9AC4FF;
}
tr.scoreevent {
	background-color: #CFFFD1;
}
tr.penaltyevent {
	background-color: #FFCFD1;
}
tr.userrow ul {
	margin: 0;
	padding: 0;
	padding-left: 18px;
}
tr.userrow li {
	margin: 0;
	padding: 0;
}

</style>
  }