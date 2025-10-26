application canceltest

  entity SomeEnt {
    t :: Text
  }

  var globalent := SomeEnt{ t := "testvalue" }

  define page root() {
    var s := globalent
    output(s.t)
    form {
      submit("Save", action{ s.t := "123456"; flush(); validate(false,""); })
    }
  }

  test cancelfunc {
    var d : WebDriver := getHtmlUnitDriver();
    d.get(navigate(root()));
    assert(d.getPageSource().contains("testvalue"), "'testvalue' on initial page");
    d.getSubmit().click();

    assert(d.getPageSource().contains("testvalue"), "'testvalue' not shown on next page, flush should have been reverted the output value due to validate fail");  
  }