application metamodel


entity DerivedProperty {
  expression : String
  name : String
  propertyType : String
  entity -> DomainEntity
}

entity DomainApp {
  name : String
  derive entityCount : Int = entities.length
  entities : {DomainEntity} (inverse = application)
  pages : {Page} (inverse = application)
  validate(name != "", "Application name is required")
}

extend entity DomainApp {
  function getEntityByName() : DomainEntity {
    for(e in entities) { if(e.name == name) { return e; } } return null;
  }
}

entity DomainEntity {
  name : String
  derive propertyCount : Int = properties.length
  derive relationshipCount : Int = relationships.length
  derive validationRuleCount : Int = validationRules.length
  application -> DomainApp
  derivedProperties : {DerivedProperty} (inverse = entity)
  functions : {EntityFunction} (inverse = entity)
  properties : {EntityProperty} (inverse = entity)
  relationships : {Relationship} (inverse = sourceEntity)
  validationRules : {ValidationRule} (inverse = entity)
  validate(name != "", "Entity name is required")
}

extend entity DomainEntity {
  function displayName() : String {
    return "Entity: " + name;
  }
}

extend entity DomainEntity {
  function hasProperties() : Bool {
    return properties.length > 0;
  }
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
  validate(name != "", "Property name is required")
  validate(propertyType != "", "Property type is required")
}

entity Page {
  name : String
  derive elementCount : Int = elements.length
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
  validate(name != "", "Relationship name is required")
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
  title { "Ouroboros Metamodel" }
  header { "WebDSL Modeling Itself" }
  par { "Model WebDSL applications as data, then generate code." }
  header { "Phase 1: Structural" }
  par { navigate manageDomainApp() { "Manage Applications" } }
  par { navigate manageDomainEntity() { "Manage Entities" } }
  par { navigate manageEntityProperty() { "Manage Properties" } }
  par { navigate manageRelationship() { "Manage Relationships" } }
  header { "Phase 2: Behavioral" }
  par { navigate manageValidationRule() { "Manage ValidationRules" } }
  par { navigate manageDerivedProperty() { "Manage DerivedPropertys" } }
  par { navigate manageEntityFunction() { "Manage EntityFunctions" } }
  header { "Phase 3: UI/Pages" }
  par { navigate managePage() { "Manage Pages" } }
  par { navigate managePageElement() { "Manage PageElements" } }
  header { "Actions" }
  par { navigate codeGenerator() { "Generate Code" } }
  par { navigate bootstrapPage() { "Bootstrap Metamodel (Self-Description)" } }
}

</body></html>
