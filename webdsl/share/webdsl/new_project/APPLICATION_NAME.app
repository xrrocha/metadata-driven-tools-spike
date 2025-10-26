application exampleapp

description {
  A simple app that uses CRUD page generation for managing a Person entity
}

imports templates
imports search/searchconfiguration
section pages

define page root() {
  main()
  define body() {
    "Hello world!"
  }
}

entity Person {
  fullname    :: String (name)
  email       :: Email
  username    :: String (id, iderror="Username is taken"
                           , idemptyerror="Username may not be empty")
  bio         :: WikiText
  dateOfBirth :: Date
  parents     -> Set<Person>
  children    -> Set<Person> (inverse = Person.parents)
  photo       :: Image  
  admin       :: Bool
  favoriteColor -> Color
}

derive CRUD Person

entity Color {
  name :: String (id, iderror="Color exists already"
                    , idemptyerror="Color name may not be empty")
}

derive CRUD Color

init {
  var color : Color;
  color := Color { name := "blue" };
  color.save();
  color := Color { name := "yellow" };
  color.save();
  color := Color { name := "red" };
  color.save();
  color := Color { name := "green" };
  color.save();
}

test colorsInitialized {
  assert((from Color).length == 4);
}