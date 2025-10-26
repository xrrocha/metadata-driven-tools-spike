application hello

entity Person {
  name : String
  email : Email
  age : Int
}

derive CRUD Person

page root() {
  title { "Hello WebDSL" }

  header { "Welcome" }

  par { "This is a minimal WebDSL application." }
  par { navigate managePerson() { "Manage People" } }
  par { navigate codeGen() { "Code Generation Demo" } }
}

// Code generation test page
page codeGen() {
  title { "Code Generation Demo" }

  header { "Generated Entity Code" }

  var generatedCode := generateEntityCode("Book", ["title", "author", "year"])

  pre {
    output(generatedCode)
  }
}

// Generator function - THIS IS THE KEY FOR OUROBOROS!
function generateEntityCode(entityName : String, properties : [String]) : String {
  var code := "entity " + entityName + " {\n";
  var i := 0;
  while(i < properties.length) {
    code := code + "  " + properties.get(i) + " : String\n";
    i := i + 1;
  }
  code := code + "}\n\nderive CRUD " + entityName;
  return code;
}
