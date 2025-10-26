
entity DerivedProperty {
  expression : String
  name : String
  propertyType : String
  entity -> DomainEntity
}

entity DomainApp {
  name : String
  entities : {DomainEntity} (inverse = application)
  pages : {Page} (inverse = application)
}

entity DomainEntity {
  name : String
  application -> DomainApp
  derivedProperties : {DerivedProperty} (inverse = entity)
  functions : {EntityFunction} (inverse = entity)
  properties : {EntityProperty} (inverse = entity)
  relationships : {Relationship} (inverse = sourceEntity)
  validationRules : {ValidationRule} (inverse = entity)
}

entity EntityFunction {
  body : String
  name : String
  returnType : String
  entity -> DomainEntity
}

entity EntityProperty {
  name : String
  propertyType : String
  entity -> DomainEntity
}

entity Page {
  name : String
  application -> DomainApp
  elements : {PageElement} (inverse = page)
}

entity PageElement {
  content : String
  elementType : String
  navigateLabel : String
  navigateTarget : String
  orderIndex : Int
  page -> Page
}

entity Relationship {
  inverseName : String
  name : String
  relationshipType : String
  sourceEntity -> DomainEntity
  targetEntity -> DomainEntity
}

entity ValidationRule {
  expression : String
  message : String
  name : String
  entity -> DomainEntity
}

derive CRUD DerivedProperty
derive CRUD DomainApp
derive CRUD DomainEntity
derive CRUD EntityFunction
derive CRUD EntityProperty
derive CRUD Page
derive CRUD PageElement
derive CRUD Relationship
derive CRUD ValidationRule

page root() {
  title { "metamodel" }
  header { "Welcome" }
  par { navigate manageDerivedProperty() { "Manage DerivedPropertys" } }
  par { navigate manageDomainApp() { "Manage DomainApps" } }
  par { navigate manageDomainEntity() { "Manage DomainEntitys" } }
  par { navigate manageEntityFunction() { "Manage EntityFunctions" } }
  par { navigate manageEntityProperty() { "Manage EntityPropertys" } }
  par { navigate managePage() { "Manage Pages" } }
  par { navigate managePageElement() { "Manage PageElements" } }
  par { navigate manageRelationship() { "Manage Relationships" } }
  par { navigate manageValidationRule() { "Manage ValidationRules" } }
}
