application scott

entity Dept {
  deptno : Int
  dname : String
  assignment : {Emp} (inverse = department)
}

entity Emp {
  empno : Int
  ename : String
  department -> Dept
}

derive CRUD Dept
derive CRUD Emp

page root() {
  title { "scott" }
  header { "Welcome" }
  par { navigate manageDept() { "Manage Depts" } }
  par { navigate manageEmp() { "Manage Emps" } }
}
