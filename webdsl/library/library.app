application library

entity Book {
  author : String
  title : String
}

derive CRUD Book

page root() {
  title { "library" }
  header { "Welcome" }
  par { navigate manageBook() { "Manage Books" } }
}
