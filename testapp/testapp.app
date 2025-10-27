application TestApp

entity Person {
  age : Int
  firstName : String
  lastName : String
  derive fullName : String = firstName + " " + lastName
  validate(age >= 18, "Must be 18 or older")
}

extend entity Person {
  function greet() : String {
    return "Hello, " + fullName;
  }
}

derive CRUD Person

page root() {
  title { "TestApp" }
  header { "Welcome" }
  par { navigate managePerson() { "Manage Persons" } }
}
